package com.taobao.easyweb.remark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;


@Component("ewRemarkDAO")
public class RemarkDAO extends SqlMapClientDaoSupport {

	public void insert(Remark remark) {
		getSqlMapClientTemplate().insert("remarkDAO.insert", remark);
	}

	public void update(Remark remark) {
		getSqlMapClientTemplate().update("remarkDAO.update", remark);
	}

	public void delete(Integer id) {
		getSqlMapClientTemplate().delete("remarkDAO.delete", id);
	}

	public Remark queryById(Integer id) {
		return (Remark) getSqlMapClientTemplate().queryForObject("remarkDAO.queryById", id);
	}

	@SuppressWarnings("unchecked")
	public List<Remark> queryRootRemark(Integer type, int start, int limit) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("start", start);
		map.put("limit", limit);
		map.put("type", type);// ∞¥’’¿‡–Õ
		return getSqlMapClientTemplate().queryForList("remarkDAO.queryRootRemark", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Remark> queryRemarks(Remark remark) {
		return getSqlMapClientTemplate().queryForList("remarkDAO.queryRemarks", remark);
	}

	public int queryRootCount(Integer type) {
		return (Integer) getSqlMapClientTemplate().queryForObject("remarkDAO.queryRootCount", type);
	}

	@SuppressWarnings("unchecked")
	public List<Remark> queryTitleLike(String titleLike) {
		return getSqlMapClientTemplate().queryForList("remarkDAO.queryTitleLike", titleLike);
	}

	@SuppressWarnings("unchecked")
	public List<Remark> queryChildrenRemarks(int parentId) {
		return getSqlMapClientTemplate().queryForList("remarkDAO.queryChildrenRemarks", parentId);
	}
}
