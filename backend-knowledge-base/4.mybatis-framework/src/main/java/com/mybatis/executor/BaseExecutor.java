package com.mybatis.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mybatis.config.Configuration;
import com.mybatis.config.MappedStatement;

/**
 * 基本执行器，主要处理一级缓存
 * 
 * @author liu
 *
 */
public abstract class BaseExecutor implements Executor {

	private Map<String, List<Object>> oneLevelCache = new HashMap<String, List<Object>>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> query(MappedStatement mappedStatement, Configuration configuration, Object param) {
		// 获取带有值的sql语句
		String sql = mappedStatement.getSqlSource().getBoundSql(param).getSql();
		// 从一级缓存去根据sql语句获取查询结果
		List<Object> result = oneLevelCache.get(sql);
		if (result != null) {
			return (List<T>) result;
		}
		// 如果没有结果，则调用相应的处理器去处理
		result = queryFromDataBase(mappedStatement, configuration, param);
		oneLevelCache.put(sql, result);
		return (List<T>) result;
	}

	public abstract List<Object> queryFromDataBase(MappedStatement mappedStatement, Configuration configuration,
			Object param);

}
