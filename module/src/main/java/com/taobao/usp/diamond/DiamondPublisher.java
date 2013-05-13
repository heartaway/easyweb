package com.taobao.usp.diamond;

import com.taobao.diamond.manager.impl.DefaultBaseStonePubManager;
import org.springframework.stereotype.Component;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-10 ÏÂÎç9:17
 */
@Component
public class DiamondPublisher {

    private DefaultBaseStonePubManager baseStonePubManager;

    public DiamondPublisher() {
        this.baseStonePubManager = new DefaultBaseStonePubManager();
    }

    public static void main(String[] v) {
        DiamondPublisher diamondPublisher = new DiamondPublisher();
        diamondPublisher.publish("usp-site-66", "test datafdsa");
    }

    public void publish(String dataId, String content) {
        baseStonePubManager.publishAll(dataId, content);
    }

}
