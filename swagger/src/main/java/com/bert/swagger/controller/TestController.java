package com.bert.swagger.controller;

import com.bert.swagger.common.Message;
import com.bert.swagger.model.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangbo
 * @date 2018/5/30
 */
@RestController
public class TestController {

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

}
