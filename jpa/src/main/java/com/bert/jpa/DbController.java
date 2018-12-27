package com.bert.jpa;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangbo
 * @date 2018/12/27
 */
@RestController
public class DbController {

    @Resource
    private ChannelStockDao channelStockDao;

    @GetMapping("/db")
    public String db() {
        List<ChannelStockEntity> all = channelStockDao.findAll((root, query, cb) ->
                query.where(cb.and(cb.equal(root.get("deleted").as(Integer.class), 2))).getRestriction());
        System.out.println(all.get(0));
        return "";
    }

}
