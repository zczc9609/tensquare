package com.tensquare.sms.listener;

import com.tensquare.sms.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @RabbitHandler
    public void getSms(Map<String,String> map){
        String mobile = map.get("mobile");
        String code = map.get("checkcode");
        SmsUtil.sendSms(mobile,code);
    }
}
