package com.symbio.htmlToTxt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LocalHtmlToTxt {

	public void start(){

		CommonFileUtils cFileUtils = new CommonFileUtils();
		String tips = "Please choose a file path";
		String type = "local";
		String path = cFileUtils.chooseFile(tips);
		try {
			if (path != null && !"".equals(path)) {
				int fileNameLength = path.substring(3, path.length()).length();
				String target = cFileUtils.chooseTarget();
				if (readfile(path, fileNameLength, target, type)) {
					System.out.println("^_^success^_^");
					JOptionPane.showMessageDialog(null, "File saved successfully!");
				} else {
					System.out.println("@_@Error@_@");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please choose an file path!", "Warning", JOptionPane.ERROR_MESSAGE);
				System.out.println("@_@Error@_@");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取某个文件夹下的所有文件
	 */
	public static boolean readfile(String filepath, int len, String target, String type) throws IOException {
		CommonFileUtils fu = new CommonFileUtils();
		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				String fileName = file.getPath().replaceAll("html", "txt").replaceAll("htm", "txt");
				Document doc = Jsoup.parse(file, "utf-8");
				Elements elements = doc.getElementsByTag("body");
				Element e = elements.get(0);
				fu.writeContentToTxt(e.text(), fileName, len, target, type);
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
						String readfileName = readfile.getPath().replaceAll("html", "txt").replaceAll("htm", "txt");
						Document doc = Jsoup.parse(readfile, "utf-8");
						Elements elements = doc.getElementsByTag("body");
						Element e = elements.get(0);
						fu.writeContentToTxt(e.text(), readfileName, len, target, type);
					} else if (readfile.isDirectory()) {
						readfile(filepath + "\\" + filelist[i], len, target, type);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
		return true;
	}
}
