package com.taobao.easyweb;

import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;


@SpringApplicationContext({"classpath:project_persistence.xml","classpath:project_bean.xml"})
public class BaseTest  extends UnitilsJUnit4{
	
}
