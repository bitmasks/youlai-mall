package com.youlai.common.zookeeper;


import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/24 8:50
 * @description: zk监听
 */
@Slf4j
public class SoutOutWatcher implements Watcher {

    public static final String FALSE = "false";

    @Autowired
    private ZkApiUtils zkApi;

    @Override
    public void process(WatchedEvent event) {
        /**
         * /product_sold_out/1/2
         * zk目录节点数据变化通知事件
         */
        if (event.getType() == org.apache.zookeeper.Watcher.Event.EventType.NodeDataChanged) {
            try {
                String path = event.getPath();
                String soldOutFlag = new String(zkApi.getData(path, null));
                log.info("zookeeper数据节点修改变动,path={},value={}", path, soldOutFlag);
                if (FALSE.equals(soldOutFlag)) {
                    String skuId = path.substring(path.lastIndexOf("/")+1, path.length());
                    ProductSoutOutMap.getProductSoldOutMap().remove(skuId);
                }
            } catch (Exception e) {
                log.error("zookeeper数据节点修改回调事件异常", e);
            }
        }
    }
}
