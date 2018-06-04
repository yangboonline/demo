package com.bert.jetcache.dao;

import com.bert.jetcache.model.TUser;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.DeleteDSL;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

import javax.annotation.Generated;

import static com.bert.jetcache.dao.TUserDynamicSqlSupport.age;
import static com.bert.jetcache.dao.TUserDynamicSqlSupport.createAt;
import static com.bert.jetcache.dao.TUserDynamicSqlSupport.id;
import static com.bert.jetcache.dao.TUserDynamicSqlSupport.money;
import static com.bert.jetcache.dao.TUserDynamicSqlSupport.name;
import static com.bert.jetcache.dao.TUserDynamicSqlSupport.updateAt;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface TUserMapper {

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    @DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    int insert(InsertStatementProvider<TUser> insertStatement);

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "TUserResult", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "age", property = "age", jdbcType = JdbcType.INTEGER),
            @Result(column = "money", property = "money", jdbcType = JdbcType.DECIMAL),
            @Result(column = "create_at", property = "createAt", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_at", property = "updateAt", jdbcType = JdbcType.TIMESTAMP)
    })
    TUser selectOne(SelectStatementProvider selectStatement);

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, TUserDynamicSqlSupport.TUser)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    default int insert(TUser record) {
        return insert(SqlBuilder.insert(record)
                .into(TUserDynamicSqlSupport.TUser)
                .map(id).toProperty("id")
                .map(name).toProperty("name")
                .map(age).toProperty("age")
                .map(money).toProperty("money")
                .map(createAt).toProperty("createAt")
                .map(updateAt).toProperty("updateAt")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    default int insertSelective(TUser record) {
        return insert(SqlBuilder.insert(record)
                .into(TUserDynamicSqlSupport.TUser)
                .map(id).toPropertyWhenPresent("id", record::getId)
                .map(name).toPropertyWhenPresent("name", record::getName)
                .map(age).toPropertyWhenPresent("age", record::getAge)
                .map(money).toPropertyWhenPresent("money", record::getMoney)
                .map(createAt).toPropertyWhenPresent("createAt", record::getCreateAt)
                .map(updateAt).toPropertyWhenPresent("updateAt", record::getUpdateAt)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    default TUser selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, name, age, money, createAt, updateAt)
                .from(TUserDynamicSqlSupport.TUser)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    default int updateByPrimaryKey(TUser record) {
        return UpdateDSL.updateWithMapper(this::update, TUserDynamicSqlSupport.TUser)
                .set(name).equalTo(record::getName)
                .set(age).equalTo(record::getAge)
                .set(money).equalTo(record::getMoney)
                .set(createAt).equalTo(record::getCreateAt)
                .set(updateAt).equalTo(record::getUpdateAt)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source Table: t_user")
    default int updateByPrimaryKeySelective(TUser record) {
        return UpdateDSL.updateWithMapper(this::update, TUserDynamicSqlSupport.TUser)
                .set(name).equalToWhenPresent(record::getName)
                .set(age).equalToWhenPresent(record::getAge)
                .set(money).equalToWhenPresent(record::getMoney)
                .set(createAt).equalToWhenPresent(record::getCreateAt)
                .set(updateAt).equalToWhenPresent(record::getUpdateAt)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

}