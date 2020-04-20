package com.bert.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author yangbo
 * @date 2018/12/27
 */
public interface ChannelPriceDao extends JpaRepository<ChannelPriceEntity, Long>, JpaSpecificationExecutor<ChannelPriceEntity> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM t_switch_channel_stock WHERE 1 = 1 "
                    + "AND IFNULL(id IN (:ids), 1 = 1) "
                    + "AND IFNULL(deleted = :deleted, 1 = 1) ")
    List<ChannelPriceEntity> findAllByIdInAndDeletedNative(@Param("ids") List<Long> ids,
                                                           @Param("deleted") Integer deleted);

}
