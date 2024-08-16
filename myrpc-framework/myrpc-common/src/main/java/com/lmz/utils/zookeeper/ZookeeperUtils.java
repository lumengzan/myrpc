package com.lmz.utils.zookeeper;

import com.lmz.Constant;
import com.lmz.exception.ZookeeperException;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author 老疯狗
 * @Title ZookeeperUtils
 * @date 2024/8/12
 */
@Slf4j
public class ZookeeperUtils {
    public static ZooKeeper createZookeeper() throws ZookeeperException {
        // 定义连接参数
        String connectString = Constant.DEFAULT_ZK_CONNECT;
        // 定义超时时间
        int timeout = Constant.TIME_OUT;
        return createZookeeper(connectString,timeout);
    }

    public static ZooKeeper createZookeeper(String connectString, int timeOut) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            final ZooKeeper zooKeeper = new ZooKeeper(connectString, timeOut, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    System.out.println("客户端已经连接成功。");
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
            return zooKeeper;
        } catch (IOException | InterruptedException e) {
            log.error("创建zookeeper实例时发生异常", e);
            throw new ZookeeperException(e);
        }
    }

    public static boolean exists(ZooKeeper zooKeeper,String node,Watcher watcher){
        try {
            return zooKeeper.exists(node,watcher)!=null;
        } catch (KeeperException | InterruptedException e) {
            log.error("判断节点是否存在时发生异常",e);
            throw new ZookeeperException(e);
        }
    }
    public static Boolean createNode(ZooKeeper zooKeeper, ZookeeperNode node, Watcher watcher, CreateMode createMode){
        try {
            if (zooKeeper.exists(node.getNodePath(), watcher) == null) {
                String result = zooKeeper.create(node.getNodePath(), node.getData(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                log.info("节点【{}】，成功创建。",result);
                return true;
            } else {
                if(log.isDebugEnabled()){
                    log.info("节点【{}】已经存在，无需创建。",node.getNodePath());
                }
                return false;
            }
        } catch (KeeperException| InterruptedException e) {
            log.error("创建基础目录时发生异常：",e);
            throw new ZookeeperException(e);
        }
    }
    public static void close(ZooKeeper zooKeeper) {
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            log.error("关闭zookeeper实例时发生异常", e);
            throw new ZookeeperException(e);
        }
    }

    public static List<String> getChildren(ZooKeeper zooKeeper, String serviceNode,Watcher watcher) {
        try {
             return zooKeeper.getChildren(serviceNode, watcher);

        } catch (KeeperException | InterruptedException e) {
            log.error("h获取节点【{}】的子元素时发生异常",serviceNode,e);
            throw new ZookeeperException(e);
        }
    }
}
