package com.bert.jpa;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(ChannelStockEntity.class)
public abstract class ChannelStockEntity_ {

    public static volatile SingularAttribute<ChannelStockEntity, Integer> accessPlatform;
    public static volatile SingularAttribute<ChannelStockEntity, Date> lastSyncTime;
    public static volatile SingularAttribute<ChannelStockEntity, String> lastSyncResult;
    public static volatile SingularAttribute<ChannelStockEntity, String> operatorName;
    public static volatile SingularAttribute<ChannelStockEntity, String> operator;
    public static volatile SingularAttribute<ChannelStockEntity, Integer> reviewFlag;
    public static volatile SingularAttribute<ChannelStockEntity, Integer> stockNum;
    public static volatile SingularAttribute<ChannelStockEntity, Long> id;
    public static volatile SingularAttribute<ChannelStockEntity, Integer> adjustNum;
    public static volatile SingularAttribute<ChannelStockEntity, String> channelCode;
    public static volatile SingularAttribute<ChannelStockEntity, Integer> physicsStockNum;
    public static volatile SingularAttribute<ChannelStockEntity, String> pmsSysCode;
    public static volatile SingularAttribute<ChannelStockEntity, Integer> syncFlag;
    public static volatile SingularAttribute<ChannelStockEntity, Integer> closedFlag;
    public static volatile SingularAttribute<ChannelStockEntity, Date> updateTime;
    public static volatile SingularAttribute<ChannelStockEntity, Integer> keepRoomNum;
    public static volatile SingularAttribute<ChannelStockEntity, String> pmsRoomTypeCode;
    public static volatile SingularAttribute<ChannelStockEntity, Integer> deleted;
    public static volatile SingularAttribute<ChannelStockEntity, Date> createTime;
    public static volatile SingularAttribute<ChannelStockEntity, Integer> freeSales;
    public static volatile SingularAttribute<ChannelStockEntity, String> sellDate;
    public static volatile SingularAttribute<ChannelStockEntity, String> pmsHotelCode;
    public static volatile SingularAttribute<ChannelStockEntity, String> pmsRatePlanCode;
    public static volatile SingularAttribute<ChannelStockEntity, String> uniqueId;
    public static volatile SingularAttribute<ChannelStockEntity, Integer> syncStatus;

}

