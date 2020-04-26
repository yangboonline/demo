package com.bert.jpa;

import org.springframework.beans.factory.annotation.Value;
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

    /**
     * 原生查询
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM t_switch_channel_stock WHERE 1 = 1 "
                    + "AND (:ids IS NULL OR id IN (:ids)) "
                    + "AND (:deleted IS NULL OR deleted = :deleted) ")
    List<ChannelPriceEntity> findAllByIdInAndDeletedNative(@Param("ids") List<Long> ids,
                                                           @Param("deleted") Integer deleted);

    /**
     * 投影查询
     */
    @Query(nativeQuery = true,
            value = "SELECT id, pms_sys_code AS pmsSysCode,access_platform FROM t_switch_channel_stock WHERE 1 = 1 "
                    + "AND (:ids IS NULL OR id IN (:ids)) "
                    + "AND (:deleted IS NULL OR deleted = :deleted) ")
    List<ChannelPriceEntityProjection> findAllByIdInAndDeletedProjection(@Param("ids") List<Long> ids,
                                                                         @Param("deleted") Integer deleted);

    /**
     * 投影
     */
    interface ChannelPriceEntityProjection {
        @Value("#{target.id}")
        Long getId();

        @Value("#{target.pmsSysCode}")
        String getPmsSysCode();

        @Value("#{target.access_platform}")
        Integer getAccessPlatform();
    }

}
