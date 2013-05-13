package com.taobao.easyweb.console.instance;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.console.instance.domain.InstanceApp;

@Component("ewInstanceAppDAO")
public class InstanceAppDAO extends SqlMapClientDaoSupport {

	public void insert(InstanceApp instanceApp) {
		getSqlMapClientTemplate().insert("instanceAppDAO.insert", instanceApp);
	}

	public void update(InstanceApp instanceApp) {
		getSqlMapClientTemplate().update("instanceAppDAO.update", instanceApp);
	}
	
	public void deleteAppInstance(InstanceApp instanceApp) {
		getSqlMapClientTemplate().delete("instanceAppDAO.delete", instanceApp);
	}

	@SuppressWarnings("unchecked")
	public List<InstanceApp> query(InstanceApp instanceApp) {
		return getSqlMapClientTemplate().queryForList("instanceAppDAO.query", instanceApp);
	}

}
