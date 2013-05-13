package com.taobao.easyweb.core.command;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;
import org.apache.mina.transport.socket.nio.SocketConnector;

/**
 * √¸¡Ó∑¢ÀÕ
 * 
 * @author jimmey
 * 
 */
public class CommandSenderFactory {

	private final ConcurrentHashMap<String, FutureTask<CommandSender>> senders = new ConcurrentHashMap<String, FutureTask<CommandSender>>();

	private static final Logger LOGGER = Logger.getLogger(CommandSenderFactory.class);

	private static final String CONNECTOR_THREADNAME = "CommandSender";

	// daemon thread
	private static final ThreadFactory CONNECTOR_TFACTORY = new NamedThreadFactory(CONNECTOR_THREADNAME, true);

	private static final CommandSenderFactory factory = new CommandSenderFactory(Runtime.getRuntime().availableProcessors() + 1);

	private static final int MIN_CONN_TIMEOUT = 1000;

	private final SocketConnector ioConnector;

	public CommandSenderFactory(int processorCount) {
		ioConnector = new SocketConnector();
	}

	public static CommandSenderFactory getSingleInstance() {
		return factory;
	}

}
