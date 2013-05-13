package com.taobao.easyweb.core.agent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	static Set<String> ignores = new HashSet<String>();
	static {
		ignores.add("target");
	}

	public static void main(String[] args) throws Exception {
//		zip("C:/Users/jimmey/workspace/ew/groovy/ide", "/home/admin/test.zip");
		upzip("/home/admin/test.zip", "/home/admin/ide");
	}

	static int BUFFER_SIZE = 1024;

	public static void upzip(String inputZip, String outputFile) {
		//ʹ��zip����Ϊ��ѹ�ļ���
		File unzipdir = new File(outputFile+"/"+new File(inputZip).getName().replace(".zip", ""));
		unzipdir.mkdirs();
		try {
			ZipFile zipFile = new ZipFile(inputZip);
			Enumeration<? extends ZipEntry> emu = zipFile.entries();
			while (emu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) emu.nextElement();
				if (zipEntry.isDirectory()) {
					// ���Ŀ¼��Ϊһ��file����һ�Σ�����ֻ����Ŀ¼�Ϳ��ԣ�֮�µ��ļ����ᱻ������
					new File(unzipdir, zipEntry.getName()).mkdirs();
					continue;
				}
				File subfile = new File(unzipdir, zipEntry.getName());
				File parent = subfile.getParentFile();
				if (parent != null && (!parent.exists())) {
					// ���������ԭ����zipfile��ȡ�ļ��������ȡ�ģ������ɿ����ȶ�ȡһ���ļ�
					// ������ļ����ڵ�Ŀ¼��û�г��ֹ�������Ҫ����Ŀ¼����
					parent.mkdirs();
				}

				BufferedInputStream bin = new BufferedInputStream(zipFile.getInputStream(zipEntry));
				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(subfile), BUFFER_SIZE);
				int count;
				byte data[] = new byte[BUFFER_SIZE];
				while ((count = bin.read(data, 0, BUFFER_SIZE)) != -1) {
					bout.write(data, 0, count);
				}
				bout.close();
				bin.close();
			}
			zipFile.close();
		} catch (Exception e) {
		}
	}

	public static void zip(String inputFile, String outputFile) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
		zip(out, new File(inputFile), "");
		System.out.println("zip done");
		out.close();
	}

	private static void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.getName().startsWith(".") || ignores.contains(f.getName())) {
			return;
		}
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			System.out.println(base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}

}
