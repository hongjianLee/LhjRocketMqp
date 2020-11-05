package com.lhj.lhjRocketmqp.IService;

import java.util.Map;

public interface IMqProducerService {
    /**
     * 添加一个消息到MQ
     * 使用该方法 请注意  topic  tags  参数
     *
     * @param topic 队列名称
     * @param tags  标签名称
     * @param body  实际数据
     * @return String
     */
    String sendMessageToMQ(String topic, String tags, String body);

    /**
     * 添加一个消息到MQ
     * 使用该方法 请注意  topic  tags  参数
     *
     * @param topic 队列名称
     * @param tags  标签名称
     * @param body  实际数据
     * @param delayLevel  延迟级别
     * @return String
     */
    String sendDelayMessageToMQ(String topic, String tags, String body, int delayLevel);

    /**
     * 添加一个同步反馈消息到MQ
     * 使用该方法 请注意  topic  tags  参数
     *
     * @param topic     队列名称
     * @param tags      标签名称
     * @param payload   实际数据
     * @return Map<String, Object>
     */
    Map<String, Object> sendSyncMessage(String topic, String tags, String payload);
}
