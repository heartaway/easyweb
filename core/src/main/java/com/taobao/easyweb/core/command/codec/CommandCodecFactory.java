package com.taobao.easyweb.core.command.codec;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CommandCodecFactory implements ProtocolCodecFactory {

	private CommandDecoder decoder;
	private CommandEncoder encoder;

	public CommandCodecFactory() {
		this.decoder = new CommandDecoder();
		this.encoder = new CommandEncoder();
	}

	@Override
	public ProtocolEncoder getEncoder() throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder() throws Exception {
		return decoder;
	}

}
