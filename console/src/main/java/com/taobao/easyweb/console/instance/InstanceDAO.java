package com.taobao.easyweb.console.instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.console.instance.domain.Instance;
import com.taobao.easyweb.core.PaginationResult;

/**
 * 
 * @author jimmey
 * 
 */
@Component("ewInstanceDAO")
public class InstanceDAO extends SqlMapClientDaoSupport {

	public void insert(Instance instance) {
		getSqlMapClientTemplate().insert("instanceDAO.insert", instance);
	}

	public void update(Instance instance) {
		getSqlMapClientTemplate().update("instanceDAO.update", instance);
	}

	@SuppressWarnings("unchecked")
	public PaginationResult<Instance> query(int start, int limit) {
		int count = (Integer) getSqlMapClientTemplate().queryForObject("instanceDAO.queryCount");
		PaginationResult<Instance> result = new PaginationResult<Instance>();
		result.setTotalSize(count);
		if (count > 0) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("start", start);
			map.put("limit", limit);
			result.setModule(getSqlMapClientTemplate().queryForList("instanceDAO.queryPage", map));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Instance> query(Instance instance) {
		return getSqlMapClientTemplate().queryForList("instanceDAO.query", instance);
	}

}
