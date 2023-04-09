package com.fzu.chenly.commentspider.service;

import com.fzu.chenly.commentspider.exception.BusinessException;
import com.fzu.chenly.commentspider.mapper.CommentMapper;
import com.fzu.chenly.commentspider.mapper.ProxyIpMapper;
import com.fzu.chenly.commentspider.model.CommentData;
import com.fzu.chenly.commentspider.model.Member;
import com.fzu.chenly.commentspider.model.dao.Comment;
import com.fzu.chenly.commentspider.model.dao.ProxyIp;
import com.fzu.chenly.commentspider.util.JsonUtils;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenly
 * @create 2022-05-29 11:39
 */
@Service
@Slf4j
public class CommentSpiderService {
    private static final ThreadPoolExecutor threadPool = getThreadPool();

    private final BilibiliHttpHelper bilibiliHttpHelper;
    private final CommentMapper commentMapper;
    private final ProxyIpMapper proxyIpMapper;

    public CommentSpiderService(BilibiliHttpHelper bilibiliHttpHelper, CommentMapper commentMapper, ProxyIpMapper proxyIpMapper) {
        this.bilibiliHttpHelper = bilibiliHttpHelper;
        this.commentMapper = commentMapper;
        this.proxyIpMapper = proxyIpMapper;
    }

    @SneakyThrows
    public void spider(Long oid) {
        //final List<ProxyIp> proxyIps = proxyIpMapper.selectList(null);
        fetchDataAsync(oid, addLocalIp(null));
    }

    private List<ProxyIp> addLocalIp(List<ProxyIp> proxyIps) {
        if (proxyIps == null) {
            proxyIps = new ArrayList<>();
        }
        final ProxyIp proxyIp = new ProxyIp();
        proxyIp.setIp("127.0.0.1");
        proxyIps.add(proxyIp);
        return proxyIps;
    }

    @SneakyThrows
    private void fetchDataAsync(Long oid, List<ProxyIp> proxySet) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        final int size = proxySet.size();
        for (int i = 0; i < size; i++) {
            final ProxyIp proxy = proxySet.get(i);
            final Integer mod = size == 1 ? null : i;
            final CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> fetchDataAndThenExtractInsert(proxy, oid, mod, size), threadPool);
            futureList.add(completableFuture);
        }

        try {
            for (CompletableFuture<Void> commentDataCompletableFuture : futureList) {
                commentDataCompletableFuture.get();
            }
        } catch (Exception e) {
            //结束查询
            if (e instanceof BusinessException && ((BusinessException) e).getCode() == 10002) {
                return;
            }
            throw e;
        }

        final long elapsed = stopwatch.elapsed(TimeUnit.SECONDS);
        log.info("time cost:{}", elapsed);
    }

    // @formatter:off

    private List<Comment> transform(List<CommentData> commentDataList) {
        return commentDataList.parallelStream()
                .map(CommentData::getReplies)
                .flatMap(Collection::parallelStream)
                .map(replyData -> {
                    Comment comment = new Comment();
                    comment.setCtime(ZonedDateTime.ofInstant(Instant.ofEpochSecond(replyData.getCtime()), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    final Member member = replyData.getMember();
                    comment.setUname(member.getUname());
                    comment.setSex(member.getSex());
                    comment.setMid(member.getMid());
                    comment.setCurrentLevel(member.getLevelInfo().getCurrentLevel());
                    comment.setMessage(replyData.getContent().getMessage());
                    comment.setSubReplyEntryText(replyData.getReplyControl().getSubReplyEntryText());
                    comment.setRpid(replyData.getRpid());
                    return comment;
                })
                .collect(Collectors.toList());
    }
    // @formatter:on

    @SneakyThrows
    public void fetchDataAndThenExtractInsert(ProxyIp proxyIp, Long oid, Integer mod, int size) {
        log.info("thread:{},ip:{}，mod:{},size:{}", Thread.currentThread(), proxyIp.getIp(), mod, size);
        final RestTemplate restTemplate = new RestTemplate();
        //FIXME 循环次数看评论量级
        final int total = 1000;
        for (int i = 226; i < total; i++) {
            if (mod != null && i % size != mod) {
                continue;
            }
            final int page = i;
            CommentData commentData = fetchDataBy(proxyIp, oid, page, restTemplate);
            if (commentData == null) {
                log.info("proxyIp:{}, index:{},请求失败，继续 ", proxyIp.getIp(), i);
                //TODO 记录查询失败的index
                continue;
            }
            if (CollectionUtils.isEmpty(commentData.getReplies())) {
                log.info("评论结束");
                //抛出异常停止请求
                throw new BusinessException(10002, "评论结束");
            }
            final List<Comment> collect = transform(Lists.newArrayList(commentData));
            //log.info("index:{},data:{}", 26 + i, collect);
            int insert = 0;
            try {
                insert = commentMapper.batchInsert(collect);
            } catch (Exception e) {
                log.error("异常数据:{},index:{}", JsonUtils.toJson(collect), page);
            }
            log.info("proxyIp:{},index:{},total:{},insert num:{}",proxyIp.getIp(), page, collect.size(), insert);
            Thread.sleep(4000L + RandomUtils.nextLong(1000, 5000));
        }
    }

    private CommentData fetchDataBy(ProxyIp proxyIp, Long oid, int page, RestTemplate restTemplate) {
        CommentData commentData = null;
        if (proxyIp == null || StringUtils.equals(proxyIp.getIp(), "127.0.0.1")) {
            commentData = bilibiliHttpHelper.fetchComment(oid, page, restTemplate);
        } else {
            commentData = bilibiliHttpHelper.fetchCommentWithProxy(proxyIp, oid, page, restTemplate);
        }
        return commentData;
    }

    private static ThreadPoolExecutor getThreadPool() {
        return new ThreadPoolExecutor(16, 100, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>(50000));
    }

}
