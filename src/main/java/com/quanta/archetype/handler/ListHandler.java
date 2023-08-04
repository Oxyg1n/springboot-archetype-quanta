package com.quanta.archetype.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Linine
 * @since 2023/5/31 21:36
 */
public class ListHandler extends BaseTypeHandler<List<Integer>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<Integer> integerList, JdbcType jdbcType) throws SQLException {
        if (integerList == null) return;
        String jsonString = JSON.toJSONString(integerList);
        preparedStatement.setString(i, jsonString);
    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String string = resultSet.getString(s);
        if (string == null) return null;
        return JSONObject.parseArray(string, Integer.class);
    }

    @Override
    public List<Integer> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String string = resultSet.getString(i);
        if (string == null) return null;
        return JSONObject.parseArray(string, Integer.class);
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String string = callableStatement.getString(i);
        if (string == null) return null;
        return JSONObject.parseArray(string, Integer.class);
    }
}
