package com.ljf.po.hamdler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWord {
    @Value("${server.port}")
    private int port;
    @RequestMapping("/index")
    public String index(){
        return "HELLO WORD ！！！！！！端口是："+port;
    }

}
