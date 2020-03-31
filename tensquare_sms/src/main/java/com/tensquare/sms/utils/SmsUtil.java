package com.tensquare.sms.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.stereotype.Component;

/**
 * 短信工具类
 * @author Administrator
 *
 */
@Component
public class SmsUtil {
    //阿里云账户的AccessKey ID
    public static final String accessKeyId = "LTAI4FerMMM4VqwM1qiPZGwC";
    //阿里云账户的Access Key Secret
    public static final String accessKeySecret = "fDg7WHaCFliAAreS0SLvHLgoyzJHnq";
    //阿里云的签名名称
    public static final String signName = "十次方微服务";
    //阿里云的模板code
    public static final String templateCode = "SMS_186596021";

    /**
     * 发送短信
     * @param mobile 手机号
     * @param code 参数
     * @return
     */
    public static void sendSms(String mobile, String code){
        //参数，服务器名称
        DefaultProfile profile = DefaultProfile.getProfile("default",accessKeyId,accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //请求方式
        request.setMethod(MethodType.POST);
        //请求地址
        request.setDomain("dysmsapi.aliyuncs.com");
        //对应的版本号
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        //电话号码
        request.putQueryParameter("PhoneNumbers", mobile);
        //签名
        request.putQueryParameter("SignName",signName);
        //模板code
        request.putQueryParameter("TemplateCode",templateCode);
        //验证码
        request.putQueryParameter("TemplateParam","{\"code\":"+code+"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}