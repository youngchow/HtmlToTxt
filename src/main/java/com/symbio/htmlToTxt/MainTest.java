package com.symbio.htmlToTxt;

public class MainTest {

	public static void main(String[] args) {
		LocalHtmlToTxt localHtmlToTxt = new LocalHtmlToTxt();
		HttpHtmlToTxt httpHtmlToTxt = new HttpHtmlToTxt();
		localHtmlToTxt.start();
		httpHtmlToTxt.start();
	}
}
