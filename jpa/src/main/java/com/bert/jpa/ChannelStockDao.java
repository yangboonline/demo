package com.bert.jpa;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yangbo
 * @date 2018/12/27
 */
@Repository
public interface ChannelStockDao extends JpaRepository<ChannelStockEntity, Long>, JpaSpecificationExecutor<ChannelStockEntity> {

    @Override
    List<ChannelStockEntity> findAll(Specification<ChannelStockEntity> specification);
}
