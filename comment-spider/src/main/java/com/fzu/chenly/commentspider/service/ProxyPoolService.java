package com.fzu.chenly.commentspider.service;

import com.fzu.chenly.commentspider.mapper.ProxyIpMapper;
import com.fzu.chenly.commentspider.model.dao.ProxyIp;
import com.fzu.chenly.proxypool.ProxyManager;
import com.fzu.chenly.proxypool.ProxyPool;
import com.fzu.chenly.proxypool.domain.Proxy;
import com.fzu.chenly.proxypool.site.Ip3366ProxyListPageParser;
import com.fzu.chenly.proxypool.site.KxdailiProxyListPageParser;
import com.fzu.chenly.proxypool.site.M66ipProxyListPageParser;
import com.fzu.chenly.proxypool.site.MimvpProxyListPageParser;
import com.fzu.chenly.proxypool.site.ProxyDbProxyListPageParser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author chenly
 * @create 2022-05-30 0:49
 */
@Service
public class ProxyPoolService {
    private final ProxyIpMapper proxyIpMapper;

    public ProxyPoolService(ProxyIpMapper proxyIpMapper) {
        this.proxyIpMapper = proxyIpMapper;
    }

    public void proxyPool() {
        Map<String, Class> proxyMap = new HashMap<>();
        proxyMap.put("https://proxy.mimvp.com/", MimvpProxyListPageParser.class);
        proxyMap.put("http://m.66ip.cn/2.html", M66ipProxyListPageParser.class);
        //proxyMap.put("http://www.data5u.com/", Data4uProxyListPageParser.class);
        //proxyMap.put("https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-1", ProxyListPlusProxyListPageParser.class);
        //proxyMap.put("http://www.feilongip.com/", FeilongipProxyListPageParser.class);
        proxyMap.put("http://proxydb.net/", ProxyDbProxyListPageParser.class);
        // proxyMap.put("http://www.xiaohexia.cn/", XiaoHeXiaProxyListPageParser.class);
        proxyMap.put("http://www.ip3366.net/", Ip3366ProxyListPageParser.class);
        proxyMap.put("http://www.kxdaili.com/dailiip/1/1.html", KxdailiProxyListPageParser.class);

        ProxyPool.proxyMap = proxyMap;
        ProxyManager proxyManager = ProxyManager.get();
        proxyManager.start();

        if (CollectionUtils.isNotEmpty(ProxyPool.proxyList)) {
            //清库
            proxyIpMapper.truncate();

            //TODO 数据库存在就不插入
            ProxyPool.proxyList.stream()
                    .distinct()
                    .filter(proxy -> proxy.getType().equals("https"))
                    .map(proxy -> {
                        final ProxyIp proxyIp = new ProxyIp();
                        proxyIp.setIp(proxy.getIp());
                        proxyIp.setType(proxy.getType());
                        proxyIp.setPort(proxy.getPort());
                        return proxyIp;
                    })
                    .forEach(proxyIpMapper::insert);
        }

    }

}
