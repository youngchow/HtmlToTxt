package com.symbio.htmlToTxt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class CommonFileUtils {

	/**
	 * 选择html文件
	 * 
	 * @param tips
	 * @return
	 */
	public String chooseFile(String tips) {

		String path = null;
		try {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			jfc.showDialog(new JLabel(), tips);
			File file = jfc.getSelectedFile();
			if (file.isDirectory()) {
				path = file.getPath();
			} else if (file.isFile()) {
				path = file.getPath();
			}
		} catch (java.lang.NullPointerException e) {
			System.out.println(tips);
		}
		return path;
	}

	/**
	 * 选择保存文件路径
	 * 
	 * @return
	 */
	public String chooseTarget() {

		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.showDialog(new JLabel(), "Choose an file save path");
		File file = jfc.getSelectedFile();
		if (file.isDirectory()) {
			return file.getAbsolutePath();
		} else if (file.isFile()) {
			return file.getAbsolutePath();
		}
		System.out.println(jfc.getSelectedFile().getName());
		return jfc.getSelectedFile().getName();
	}

	/**
	 * 验证是否是合法的url
	 * 
	 * @param string
	 * @return
	 */
	public boolean isValidateUrl(String string) {
		URL url;
		try {
			url = new URL(string);
			url.openStream();
			return true;
		} catch (Exception e1) {
			url = null;
			return false;
		}
	}

	/**
	 * 获取https格式url
	 * 
	 * @param path
	 * @return
	 */
	public static String getFilePath(String path) {
		String[] paths = path.split("https:");
		if (paths != null && paths.length > 0) {
			return "https:" + paths[paths.length - 1];
		}
		return path;
	}

	/**
	 * 写入到txt文本文件
	 * 
	 * @param text
	 * @param name
	 * @param length
	 * @param target
	 * @param type
	 */
	public void writeContentToTxt(String text, String name, int length, String target, String type) {

		File file = new File("");
		if (type.equals("local")) {
			file = new File(target + File.separator + "local" + name.substring(length + 9, name.length()));
		} else if (type.equals("https")) {
			file = new File(target + File.separator + "html" + name);
		}
		System.out.println(file);
		if (!file.getParentFile().exists() && !file.getParentFile().isDirectory()) {
			file.getParentFile().mkdirs();
		}
		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			System.out.println("文件名、目录名或卷标语法不正确，请检查相关link是否正确");
		}
	}

}
