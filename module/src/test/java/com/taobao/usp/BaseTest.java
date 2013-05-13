package com.taobao.usp;

import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;


@SpringApplicationContext({"classpath:persistence.xml","classpath:bean.xml"})
public class BaseTest extends UnitilsJUnit4{
	
}
