package com.lmz;

import com.lmz.exception.ZookeeperException;
import com.lmz.utils.zookeeper.ZookeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author 老疯狗
 * @Title Application
 * @date 2024/7/15
 */
@Slf4j
public class Application {
    public static void main(String[] args) throws ZookeeperException {

        try {
            ZooKeeper zooKeeper= ZookeeperUtils.createZookeeper();
            CountDownLatch countDownLatch = new CountDownLatch(1);

            countDownLatch.await();

            String basePath = "/myrpc-metadata";
            String providerPath = basePath + "/providers";
            String consumerPath = basePath + "/consumers";

            if (zooKeeper.exists(basePath, null) == null) {
                String result = zooKeeper.create(
                        basePath,
                        null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
                log.info("根节点{}已经成功创建",result);
            }
            if (zooKeeper.exists(providerPath, null) == null) {
                String result = zooKeeper.create(
                        providerPath,
                        null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
                log.info("节点{}已经成功创建",result);
            }
            if (zooKeeper.exists(consumerPath, null) == null) {
                String result = zooKeeper.create(
                        consumerPath,
                        null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
                log.info("节点{}已经成功创建",result);
            }
            zooKeeper.close();
        } catch (InterruptedException | KeeperException e) {
            log.error("创建基础目录时，产生异常，如下：",e);
        }
    }
}
