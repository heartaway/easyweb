package com.taobao.easyweb.core.command.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import com.taobao.easyweb.core.command.Command;
import com.taobao.easyweb.core.command.CommandThreadWorker;

public class CommandHandler extends IoHandlerAdapter {

	ExecutorService executorService = Executors.newFixedThreadPool(4);

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		Command command = (Command) message;
		executorService.submit(new CommandThreadWorker(command));
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

}
