package com.lhj;

import com.alibaba.fastjson.JSON;
import com.lhj.lhjRocketmqp.IService.IMqProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class LhjRocketmqpServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LhjRocketmqpServiceApplication.class, args);
    }

    @Autowired
    private IMqProducerService mqProducerService;

    @GetMapping("/")
    public String index() {
        //发送到Rocketmq生产者
        log.warn("发送消息到RocketMQ");
        for (int i = 0; i < 5; i++) {
            String resultMsg = mqProducerService.sendMessageToMQ("test", "test",
                    JSON.toJSONString(i));
            log.warn("当前进程：" + i + resultMsg);
        }
        return "MQ生产端dubbo微服务启动完成！";
    }

}
