package com.taobao.easyweb.web;

import com.alibaba.common.lang.SystemUtil;
import org.apache.commons.lang.SystemUtils;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taobao.easyweb.web.filter.CharsetHandler;
import com.taobao.easyweb.web.filter.EasywebHandler;
import com.taobao.hsf.hsfunit.HSFEasyStarter;

public class Main {
	public static void main(String[] args) throws Exception {
		try {
//			HSFEasyStarter.startFromPath("/opt/hsf/");
//			new ClassPathXmlApplicationContext("easyweb_spring.xml");
//			Server server = new Server(8080);
//			server.addHandler(new CharsetHandler());
//			server.addHandler(new EasywebHandler());
//			SocketConnector connector = new SocketConnector();
//			connector.setPort(8080);
//			server.setConnectors(new Connector[] { connector });
//			server.start();
//
//			System.out.println("start");
            System.out.print(SystemUtils.FILE_ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
