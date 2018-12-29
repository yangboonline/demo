package com.bert.jpa;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "t_switch_channel_stock")
public class ChannelStockEntity implements Serializable {

    private static final long serialVersionUID = -1;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pms_sys_code")
    private String pmsSysCode;
    @Column(name = "access_platform")
    private Integer accessPlatform;
    @Column(name = "channel_code")
    private String channelCode;
    @Column(name = "pms_hotel_code")
    private String pmsHotelCode;
    @Column(name = "pms_room_type_code")
    private String pmsRoomTypeCode;
    @Column(name = "pms_rate_plan_code")
    private String pmsRatePlanCode;
    @Column(name = "sell_date")
    private String sellDate;
    @Column(name = "stock_num")
    private Integer stockNum;
    @Column(name = "adjust_num")
    private Integer adjustNum;
    @Column(name = "physics_stock_num")
    private Integer physicsStockNum;
    @Column(name = "keep_room_num")
    private Integer keepRoomNum;
    @Column(name = "free_sales")
    private Integer freeSales;
    @Column(name = "closed_flag")
    private Integer closedFlag;
    @Column(name = "review_flag")
    private Integer reviewFlag;
    @Column(name = "unique_id")
    private String uniqueId;
    @Column(name = "sync_status")
    private Integer syncStatus;
    @Column(name = "last_sync_result")
    private String lastSyncResult;
    @Column(name = "last_sync_time")
    private Date lastSyncTime;
    @Column(name = "operator")
    private String operator;
    @Column(name = "operator_name")
    private String operatorName;
    @Column(name = "deleted")
    private Integer deleted;
    @Column(name = "sync_flag")
    private Integer syncFlag;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;

}
