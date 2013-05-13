package com.taobao.easyweb.console.deploy;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class DeployTaskDAO extends SqlMapClientDaoSupport {

	public void insert(DeployTask deployTask){
		
	}
	
	public void update(DeployTask task){
		
	}
	
	public List<DeployTask> queryByStatus(Integer status) {
		return null;
	}
	
	public List<DeployTask> queryMyTask(){
		return null;
	}

}
