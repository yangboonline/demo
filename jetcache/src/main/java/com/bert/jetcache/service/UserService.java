package com.bert.jetcache.service;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.Cached;
import com.bert.jetcache.dao.TUserMapper;
import com.bert.jetcache.model.TUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yangbo
 * @date 2018/6/4
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private TUserMapper tUserMapper;

    @Cached(name = "userCache.", key = "#id", expire = 60, cacheNullValue = true)
    @CacheRefresh(refresh = 50)
    public TUser selectByPrimaryKey(Long id) {
        TUser tUser = tUserMapper.selectByPrimaryKey(id);
        return tUser;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer updateByPrimaryKey(Long id) {
        TUser tUser = tUserMapper.selectByPrimaryKey(id);
        TUser temp = new TUser();
        BeanUtils.copyProperties(tUser, temp);
        temp.setAge(2);
        temp.setUpdateAt(new Date());
        int update = tUserMapper.updateByPrimaryKey(temp);
        return update;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer addTUser() {
        TUser user = TUser.builder().age(1).money(BigDecimal.ONE).name("test").build();
        int insert = tUserMapper.insert(user);
        return insert;
    }

    @CacheInvalidate(name = "userCache.", key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(Long id) {
        int delete = tUserMapper.deleteByPrimaryKey(id);
        return delete;
    }

}
