package com.fzu.chenly.commentspider.controller;

import com.fzu.chenly.commentspider.service.CommentService;
import com.fzu.chenly.commentspider.service.CommentSpiderService;
import com.fzu.chenly.commentspider.service.ProxyPoolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenly
 * @create 2022-05-29 13:26
 */
@RestController
public class CommentSpiderController {

    private final CommentSpiderService commentSpiderService;
    private final CommentService commentService;
    private final ProxyPoolService proxyPoolService;

    public CommentSpiderController(CommentSpiderService commentSpiderService, CommentService commentService, ProxyPoolService proxyPoolService) {
        this.commentSpiderService = commentSpiderService;
        this.commentService = commentService;
        this.proxyPoolService = proxyPoolService;
    }

    @GetMapping("/find-proxy-ip")
    public void startFindProxyIp() {
        proxyPoolService.proxyPool();
    }

    @GetMapping("/fetch-comment")
    public void fetchComment() {
        commentSpiderService.spider(299172232L);
    }

    @GetMapping("/find")
    public void find() {
        commentService.find();
    }
}
