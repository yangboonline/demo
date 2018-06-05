package com.bert.jetcache.controller;

import com.bert.jetcache.common.Message;
import com.bert.jetcache.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping("add")
    public Message add() {
        return Message.success(userService.addTUser());
    }

    @GetMapping("get")
    public Message get() {
        return Message.success(userService.selectByPrimaryKey(1L));
    }

    @PutMapping("update")
    public Message update() {
        return Message.success(userService.updateByPrimaryKey(1L));
    }

    @DeleteMapping("delete")
    public Message delete() {
        return Message.success(userService.delete(1L));
    }

}
