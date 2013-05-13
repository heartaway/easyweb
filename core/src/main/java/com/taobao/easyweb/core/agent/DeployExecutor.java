package com.taobao.easyweb.core.agent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.command.Command;
import com.taobao.easyweb.core.command.CommandExecutor;
import com.taobao.easyweb.core.command.server.CommandExecutorFactory;

@Component
public class DeployExecutor extends CommandExecutor {

	public DeployExecutor() {
		CommandExecutorFactory.regist(1, this);
	}

	@Override
	public void execute(Command command) {
		byte[] data = command.getData();
		String fileName = "/home/admin/easyweb/tmp/" + command.getAppKey() + ".zip";
		File receiveFile = new File(fileName);
		File rename = new File("/home/admin/easyweb/tmp/" + command.getAppKey() + ".zip." + new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date()));
		try {
			if (receiveFile.exists()) {
				rename.createNewFile();
				receiveFile.renameTo(rename);
			}
			receiveFile.createNewFile();
			OutputStream out = new FileOutputStream(receiveFile);
			IOUtils.copy(new ByteArrayInputStream(data), out);
			IOUtils.closeQuietly(out);
			ZipUtil.upzip(fileName, "/home/admin/easyweb/deploy/apps/");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
