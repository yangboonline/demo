package com.bert.jetcache.service;

import com.alicp.jetcache.anno.Cached;
import com.bert.jetcache.dao.TUserMapper;
import com.bert.jetcache.model.TUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author yangbo
 * @date 2018/6/4
 */
@Service
public class UserService {

    @Resource
    private TUserMapper tUserMapper;

    @Cached(name = "userCache.", key = "#id", expire = 10)
    public TUser selectByPrimaryKey(Long id) {
        TUser tUser = tUserMapper.selectByPrimaryKey(id);
        return tUser;
    }

    public Integer addTUser() {
        TUser user = TUser.builder().age(1).money(BigDecimal.ONE).name("test").build();
        int insert = tUserMapper.insert(user);
        return insert;
    }

}
