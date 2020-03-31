package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.ClientService;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class ClientServiceImpl implements ClientService {

    @Override
    public Result findById(String id) {
        return new Result(false, StatusCode.ERROR,"熔断器触发了！");
    }
}
