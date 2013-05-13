package com.taobao.easyweb.remark;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.unitils.spring.annotation.SpringBean;

import com.taobao.easyweb.BaseTest;

public class RemarkManagerTest extends BaseTest{

	@SpringBean("ewRemarkManager")
	private RemarkManager remarkManager;
	
	@Test
	public void testAddRemark() {
		Remark remark = new Remark();
		remark.setTitle("test");
		remark.setContent("fdsafds");
		remarkManager.addRemark(remark);
	}

	@Test
	public void testUpdateRemark() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryRootRemark() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryTitleLike() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryDetail() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryRemarks() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryChildrenRemarks() {
		fail("Not yet implemented");
	}

}
