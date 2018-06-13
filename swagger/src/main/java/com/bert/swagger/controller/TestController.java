package com.bert.swagger.controller;

import com.bert.swagger.common.Message;
import com.bert.swagger.model.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author yangbo
 * @date 2018/5/30
 */
@RestController
public class TestController {

    @Resource
    private RestTemplate restTemplate;

    @ApiOperation(value = "测试方法1", notes = "我的第一个Swagger2方法")
    @GetMapping("test1")
    public Message test1() {
        return Message.EMPTY_SUCCESS;
    }

    @ApiOperation(value = "测试方法2", notes = "我的第二个Swagger2方法")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @PostMapping("test2")
    public Message test2(@RequestBody User user) {
        return Message.success(user);
    }

    @RequestMapping("test3")
    public Message test3() {
        String url = "https://services.expediapartnercentral.com/products/properties";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encodeBase64String("EQCtest12933870:ew67nk33".getBytes()));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> object = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return Message.success(object);
    }

}
