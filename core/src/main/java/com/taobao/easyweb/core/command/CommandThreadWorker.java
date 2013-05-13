package com.taobao.easyweb.core.command;

import com.taobao.easyweb.core.command.server.CommandExecutorFactory;

public class CommandThreadWorker implements Runnable {

	private Command command;

	public CommandThreadWorker(Command command) {
		this.command = command;
	}

	@Override
	public void run() {
		CommandExecutor executor = CommandExecutorFactory.getExecutor(command.getType());
		if (executor == null) {
			System.out.println("no executor");
			return;
		}
		executor.execute(command);
	}

}
