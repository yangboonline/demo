package com.bert.jetcache.controller;

import com.bert.jetcache.common.Message;
import com.bert.jetcache.model.TUser;
import com.bert.jetcache.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yangbo
 * @date 2018/6/4
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("add")
    public Message add() {
        Integer insert = userService.addTUser();
        return Message.success(insert);
    }

    @GetMapping("queryById")
    public Message queryById() {
        TUser tUser = userService.selectByPrimaryKey(1L);
        return Message.success(tUser);
    }

}
