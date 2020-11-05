package com.lhj.serviceImpl;

import com.lhj.lhjRocketmqp.IService.IMqProducerService;
import com.lhj.lhjRocketmqp.constant.SymbolConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service(version = "${dubbo.provider.version}", timeout = 30000)
public class MqProducerService implements IMqProducerService {

    @Value(" ${rocketmq.tag.prefix:}")
    private String tagPrefix;

    private final RocketMQTemplate rocketMQTemplate;

    @Autowired
    public MqProducerService(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    /**
     * 添加一个消息到MQ
     * 使用该方法 请注意  topic  tags  参数
     *
     * @param topic 队列名称
     * @param tags  标签名称
     * @param body  实际数据
     * @return String
     */
    @Override
    public String sendMessageToMQ(String topic, String tags, String body) {
        String result = "";
        try {
            String getRequestData = "接收到的消息是   Topic=" + topic + " Tags= " + tags + " body= " + body;
            log.debug(getRequestData);
//            String destination = topic.concat(SymbolConstants.COLON).concat(tagPrefix).concat(tags);
            String destination = topic.concat(SymbolConstants.COLON).concat(tags);
            SendResult sendResult = rocketMQTemplate.syncSend(destination, body);
            log.debug("响应的数据是：" + sendResult);
            if (null != sendResult) {
                result = "SendStatus = " + sendResult.getSendStatus() + " msgId =" + sendResult.getMsgId();
            } else {
                result = " 添加到MQ 失败 ！";
            }
        } catch (Exception e) {
            log.error("MQProducerService 发送异常", e);
            result = e.getMessage();
        }
        return result;
    }

    /**
     * 添加一个消息到MQ
     * 使用该方法 请注意  topic  tags  参数
     *
     * @param topic 队列名称
     * @param tags  标签名称
     * @param body  实际数据
     * @return String
     */
    @Override
    public String sendDelayMessageToMQ(String topic, String tags, String body, int delayLevel) {
        String result = "";
        try {
            String getRequestData = "接收到的消息是   Topic=" + topic + " Tags= " + tags + " body= " + body;
            log.debug(getRequestData);
            String destination = topic.concat(SymbolConstants.COLON).concat(tagPrefix).concat(tags);
            SendResult sendResult = rocketMQTemplate.syncSend(destination,
                    MessageBuilder.withPayload(body).build(), Long.valueOf("60000"), delayLevel);
            log.debug("响应的数据是：" + sendResult);
            if (null != sendResult) {
                result = "SendStatus = " + sendResult.getSendStatus() + " msgId =" + sendResult.getMsgId();
            } else {
                result = " 添加到MQ 失败 ！";
            }
        } catch (Exception e) {
            log.error("MQProducerService 发送异常", e);
            result = e.getMessage();
        }
        return result;
    }

    /**
     * 添加一个同步反馈消息到MQ
     * 使用该方法 请注意  topic  tags  参数
     *
     * @param topic   队列名称
     * @param tags    标签名称
     * @param payload 实际数据
     * @return ResultData
     */
    @Override
    public Map<String, Object> sendSyncMessage(String topic, String tags, String payload) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", false);
        String result = "";
        try {
            String getRequestData = "接收到的消息是   Topic=" + topic + " Tags= " + tags + " payload= " + payload;
            log.debug(getRequestData);
            String destination = topic.concat(SymbolConstants.COLON).concat(tagPrefix).concat(tags);
            SendResult sendResult = rocketMQTemplate.syncSend(destination, payload);
            log.debug("响应的数据是：" + sendResult);
            if (null != sendResult) {
                // 发送成功，返回成功标识
                if (SendStatus.SEND_OK == sendResult.getSendStatus()) {
                    map.put("state", true);
                }
                result = "SendStatus = " + sendResult.getSendStatus() + " msgId =" + sendResult.getMsgId();
            } else {
                result = " 添加到MQ 失败 ！";
            }
        } catch (Exception e) {
            log.error("MQProducerService 发送异常", e);
            result = e.getMessage();
        }
        map.put("data", result);
        return map;
    }
}
