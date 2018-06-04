package com.bert.jetcache.dao;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.Date;

public final class TUserDynamicSqlSupport {

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    public static final TUser TUser = new TUser();

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: t_user.id")
    public static final SqlColumn<Long> id = TUser.id;

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: t_user.name")
    public static final SqlColumn<String> name = TUser.name;

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: t_user.age")
    public static final SqlColumn<Integer> age = TUser.age;

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: t_user.money")
    public static final SqlColumn<BigDecimal> money = TUser.money;

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: t_user.create_at")
    public static final SqlColumn<Date> createAt = TUser.createAt;

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: t_user.update_at")
    public static final SqlColumn<Date> updateAt = TUser.updateAt;

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    public static final class TUser extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<Integer> age = column("age", JDBCType.INTEGER);

        public final SqlColumn<BigDecimal> money = column("money", JDBCType.DECIMAL);

        public final SqlColumn<Date> createAt = column("create_at", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> updateAt = column("update_at", JDBCType.TIMESTAMP);

        public TUser() {
            super("t_user");
        }
    }
}