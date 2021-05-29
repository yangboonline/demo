package com.bert.jpa;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(ChannelPriceEntity.class)
public abstract class ChannelPriceEntity_ {

    public static volatile SingularAttribute<ChannelPriceEntity, Integer> accessPlatform;
    public static volatile SingularAttribute<ChannelPriceEntity, Date> lastSyncTime;
    public static volatile SingularAttribute<ChannelPriceEntity, String> pmsSysCode;
    public static volatile SingularAttribute<ChannelPriceEntity, Integer> syncFlag;
    public static volatile SingularAttribute<ChannelPriceEntity, String> lastSyncResult;
    public static volatile SingularAttribute<ChannelPriceEntity, Long> channelSellPrice;
    public static volatile SingularAttribute<ChannelPriceEntity, Date> updateTime;
    public static volatile SingularAttribute<ChannelPriceEntity, String> operatorName;
    public static volatile SingularAttribute<ChannelPriceEntity, String> operator;
    public static volatile SingularAttribute<ChannelPriceEntity, String> pmsRoomTypeCode;
    public static volatile SingularAttribute<ChannelPriceEntity, Integer> reviewFlag;
    public static volatile SingularAttribute<ChannelPriceEntity, Integer> relationModifyFlag;
    public static volatile SingularAttribute<ChannelPriceEntity, Long> channelBasePrice;
    public static volatile SingularAttribute<ChannelPriceEntity, Integer> deleted;
    public static volatile SingularAttribute<ChannelPriceEntity, Long> channelCommissionPrice;
    public static volatile SingularAttribute<ChannelPriceEntity, Date> createTime;
    public static volatile SingularAttribute<ChannelPriceEntity, String> sellDate;
    public static volatile SingularAttribute<ChannelPriceEntity, Long> id;
    public static volatile SingularAttribute<ChannelPriceEntity, Integer> breakfast;
    public static volatile SingularAttribute<ChannelPriceEntity, String> pmsHotelCode;
    public static volatile SingularAttribute<ChannelPriceEntity, String> pmsRatePlanCode;
    public static volatile SingularAttribute<ChannelPriceEntity, String> uniqueId;
    public static volatile SingularAttribute<ChannelPriceEntity, Integer> syncStatus;
    public static volatile SingularAttribute<ChannelPriceEntity, String> channelCode;

}

