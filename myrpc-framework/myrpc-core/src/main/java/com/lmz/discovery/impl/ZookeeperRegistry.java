package com.lmz.discovery.impl;

import com.lmz.Constant;
import com.lmz.ServiceConfig;
import com.lmz.discovery.AbstractRegistry;
import com.lmz.exception.NetworkException;
import com.lmz.exception.RegistryCenterException;
import com.lmz.utils.NetUtil;
import com.lmz.utils.zookeeper.ZookeeperNode;
import com.lmz.utils.zookeeper.ZookeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 老疯狗
 * @Title ZookeeperRegistry
 * @date 2024/8/13
 */
@Slf4j
public class ZookeeperRegistry extends AbstractRegistry {

    private ZooKeeper zooKeeper;

    public ZookeeperRegistry() {
        zooKeeper=ZookeeperUtils.createZookeeper();
    }

    public ZookeeperRegistry(String connectString, int timeOut) {
        zooKeeper = ZookeeperUtils.createZookeeper(connectString, timeOut);
    }

    @Override
    public void register(ServiceConfig<?> service) {
        // 服务名称节点
        String parentNode = Constant.BASE_PROVIDERS_PATH + "/" + service.getInterfaceProvider().getName();
        // 这个节点应该是一个持久节点
        if (!ZookeeperUtils.exists(zooKeeper, parentNode, null)) {
            ZookeeperNode zookeeperNode = new ZookeeperNode(parentNode, null);
            ZookeeperUtils.createNode(zooKeeper, zookeeperNode, null, CreateMode.PERSISTENT);
        }

        //todo:后续输入
        String node=parentNode+"/"+ NetUtil.getIp()+":"+8088;
        if (!ZookeeperUtils.exists(zooKeeper, node, null)) {
            ZookeeperNode zookeeperNode = new ZookeeperNode(node, null);
            ZookeeperUtils.createNode(zooKeeper, zookeeperNode, null, CreateMode.EPHEMERAL);
        }

        if (log.isDebugEnabled()) {
            log.debug("服务{}已经被注册", service.getInterfaceProvider().getName());
        }
    }

    @Override
    public InetSocketAddress lookup(String serviceName) {
        String serviceNode = Constant.BASE_PROVIDERS_PATH + "/" + serviceName;
        List<String> children= ZookeeperUtils.getChildren(zooKeeper, serviceNode,null);
        List<InetSocketAddress> collect = children.stream().map(ipString -> {
            String[] ipAndPort = ipString.split(":");
            String ip = ipAndPort[0];
            int port = Integer.parseInt(ipAndPort[1]);
            return new InetSocketAddress(ip, port);
        }).collect(Collectors.toList());
        if (collect.isEmpty()) {
            throw new RegistryCenterException("未发现任何可用的服务主机。");
        }
        return collect.get(0);
    }
}
