package com.taobao.easyweb.core.command;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;

public class CommandSender {
	private IoSession ioSession;
	public Object invoke(String ip, int port, Command command) {
		WriteFuture writeFuture = ioSession.write(command);
		writeFuture.join();
		return null;
	}

	class A extends IoHandlerAdapter {
		Command command;

		public A(Command command) {
			this.command = command;
		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {
			session.write(command);
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			System.out.println("sessionClosed");
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
			cause.printStackTrace();
		}

	}

}
