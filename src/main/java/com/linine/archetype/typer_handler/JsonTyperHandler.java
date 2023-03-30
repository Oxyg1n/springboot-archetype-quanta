package com.linine.archetype.typer_handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mybatis中varchar到json类处理器
 *
 * @author Linine
 * @since 2022/11/13 15:56
 */
public class JsonTyperHandler extends BaseTypeHandler<JSONObject> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, JSONObject jsonObject, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public JSONObject getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String jsonString = resultSet.getString(s);
        if (jsonString == null) return null;
        return JSON.parseObject(jsonString);
    }

    @Override
    public JSONObject getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String jsonString = resultSet.getString(i);
        if (jsonString == null) return null;
        return JSON.parseObject(jsonString);
    }

    @Override
    public JSONObject getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String jsonString = callableStatement.getString(i);
        if (jsonString == null) return null;
        return JSON.parseObject(jsonString);
    }
}
