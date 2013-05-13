package com.taobao.easyweb.core.command.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.command.codec.CommandCodecFactory;

/**
 * 
 * @author jimmey
 * 
 */
public class CommandReciver {

//	private Logger logger = Logger.getLogger(CommandReciver.class);

	public void init(){
		try {
			SocketAcceptor acceptor = new SocketAcceptor();
//			acceptor.getDefaultConfig().setReadBufferSize(1024 * 1024 * 10);
//			acceptor.getDefaultConfig().setReceiveBufferSize(1024 * 1024 * 10);
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new CommandCodecFactory()));
//			acceptor.bind(new InetSocketAddress(9099),new CommandHandler());
//			System.out.println("listening 9099");
//			logger.info("listening 9099");
		} catch (Exception e) {
//			logger.error("listening 9099 error", e);
		}
	}

	public static void main(String[] args) throws IOException {
//		new DeployExecutor();
//		new CommandReciver().init();
//		Command command = new Command();
//		command.setType(1);
//		command.setAppKey("test-1");
//		InputStream in = new FileInputStream("/home/admin/test.zip");
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		IOUtils.copy(in, out);
//		command.setData(out.toByteArray());
//		CommandSender.send("127.0.0.1", 9099, command);
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		command.setAppKey("test-2");
//		CommandSender.send("127.0.0.1", 9099, command);
	}
}
