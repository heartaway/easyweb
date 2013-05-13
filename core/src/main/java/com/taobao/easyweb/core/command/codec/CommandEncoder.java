package com.taobao.easyweb.core.command.codec;


import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.taobao.easyweb.core.command.Command;

public class CommandEncoder extends ProtocolEncoderAdapter {

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		Command command = (Command) message;
		byte[] b1 = command.getAppKey().getBytes("utf-8");
		byte[] b2 = command.getData() == null ? new byte[0] : command.getData();
//		byte[] b2 = command.getZipFile();
		int capacity = b1.length + b2.length + 8;
		ByteBuffer buffer = ByteBuffer.allocate(capacity, false);
		buffer.setAutoExpand(true);
		buffer.putInt(command.getType());
		buffer.putInt(b1.length);
		buffer.put(b1);
		buffer.putInt(b2.length);
		buffer.put(b2);
		buffer.flip();
		out.write(buffer);
	}

}
