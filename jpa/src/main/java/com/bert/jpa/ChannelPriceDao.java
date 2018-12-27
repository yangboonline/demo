package com.bert.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yangbo
 * @date 2018/12/27
 */
public interface ChannelPriceDao extends JpaRepository<ChannelPriceEntity, Long>, JpaSpecificationExecutor<ChannelPriceEntity> {


}
