package com.taobao.easyweb.core.command.codec;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.taobao.easyweb.core.command.Command;

public class CommandDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		int type = in.getInt();
		int b1 = in.getInt();
		byte[] appKeyBytes = new byte[b1];
		in.get(appKeyBytes);
		int zip = in.getInt();
		byte[] zipFile = new byte[zip];
		in.get(zipFile);
		Command command = new Command();
		command.setAppKey(new String(appKeyBytes, "utf-8"));
		command.setType(type);
		command.setData(zipFile);
		out.write(command);
		return true;
	}
}
