package com.symbio.htmlToTxt;

import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HttpHtmlToTxt {

	public void start(){

		ExcelReadUtil eUtil = new ExcelReadUtil();
		CommonFileUtils cFileUtils = new CommonFileUtils();
		String tips = "Please choose an Excel file path";
		String type = "https";
		String path = cFileUtils.chooseFile(tips);
		try {
			if (path != null && !"".equals(path) && eUtil.validateExcel(path)) {
				int fileNameLength = path.substring(3, path.length()).length();
				String target = cFileUtils.chooseTarget();
				List<String> urList = eUtil.read(path);
				bolgBody(urList, fileNameLength, target, type);
				System.out.println("^_^success^_^");
				JOptionPane.showMessageDialog(null, "File saved successfully!");
			} else {
				JOptionPane.showMessageDialog(null, "Please choose an Excel file path!", "Warning",
						JOptionPane.ERROR_MESSAGE);
				System.out.println("@_@Error@_@");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定HTML 文档指定的body(针对URL)
	 * 
	 * @throws IOException
	 */
	private static void bolgBody(List<String> urList, int len, String target, String type) throws IOException {
		// 从 URL 直接加载 HTML 文档
		CommonFileUtils fu = new CommonFileUtils();
		for (int i = 0; i < urList.size(); i++) {
			String filepath = urList.get(i).substring(67, urList.get(i).length()).replaceAll("html", "txt")
					.replaceAll("htm", "txt");
			if (!new CommonFileUtils().isValidateUrl(urList.get(i))) {
				fu.writeContentToTxt("File not found.\"", filepath, len, target, type);
				continue;
			} else {
				Connection connection = Jsoup.connect(urList.get(i));
				connection.timeout(5000);
				Document doc2 = connection.get();
				fu.writeContentToTxt(doc2.body().text(), filepath, len, target, type);
			}
		}
	}
}
