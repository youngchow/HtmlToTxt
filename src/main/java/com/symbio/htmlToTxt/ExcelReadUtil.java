package com.symbio.htmlToTxt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读取excle公共方法 兼容2003-2007，.xls\.xlsx格式
 * 
 */
public class ExcelReadUtil {

	/** 总行数 */
	private int totalRows = 0;

	/** 总列数 */
	private int totalCells = 0;

	/** 错误信息 */
	private String errorInfo;

	/** 构造方法 */
	public ExcelReadUtil() {

	}

	/**
	 * excel头信息
	 */
	private Map<Integer, String> heads = new HashMap<Integer, String>();;

	/**
	 * @param filePath
	 * @return
	 */
	public boolean validateExcel(String filePath) {
		/** 检查文件名是否为空或者是否是Excel格式的文件 */
		if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
			errorInfo = "文件名不是excel格式";
			return false;
		}

		/** 检查文件是否存在 */

		File file = new File(filePath);

		if (file == null || !file.exists()) {
			errorInfo = "文件不存在";
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @描述：是否是2003的excel，返回true是2003
	 * 
	 * @参数：@param filePath 文件完整路径
	 * 
	 * @参数：@return
	 * 
	 * @返回值：boolean
	 */

	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	/**
	 * 
	 * @描述：是否是2007的excel，返回true是2007
	 * 
	 * @参数：@param filePath 文件完整路径
	 * 
	 * @参数：@return
	 * 
	 * @返回值：boolean
	 */
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	/**
	 * 
	 * @描述：得到总行数
	 * 
	 * @参数：@return
	 * 
	 * @返回值：int
	 */
	public int getTotalRows() {
		return totalRows;
	}

	/**
	 * 
	 * @描述：得到总列数
	 * 
	 * @参数：@return
	 * 
	 * @返回值：int
	 */

	public int getTotalCells() {

		return totalCells;

	}

	/**
	 * 
	 * @描述：得到错误信息
	 * 
	 * @参数：@return
	 * 
	 * @返回值：String
	 */

	public String getErrorInfo() {
		return errorInfo;
	}

	public Map<Integer, String> getHeads() {
		return heads;
	}

	public void setHeads(Map<Integer, String> heads) {
		this.heads = heads;
	}

	/*
	 * public static void main(String[] args) throws Exception {
	 * 
	 * List<String> urList = new ExcelReadUtil().read(
	 * "C:\\Users\\yang5.zhou\\Desktop\\fileCompare\\E2E P4 to Production Test 29-Sep-2016.xlsx"
	 * ); System.out.println(urList.size()); }
	 */

	public List<String> read(String filePath) {
		List<String> urlLists = new ArrayList<String>();
		InputStream is = null;
		try {
			/** 验证文件是否合法 */
			if (!validateExcel(filePath)) {
				return urlLists;
			}

			/** 判断文件的类型，是2003还是2007 */
			boolean isExcel2003 = true;

			if (isExcel2007(filePath)) {
				isExcel2003 = false;
			}

			/** 调用本类提供的根据流读取的方法 */
			File file = new File(filePath);
			is = new FileInputStream(file);
			urlLists = read(is, isExcel2003);
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
			}
		}
		/** 返回最后读取的结果 */
		return urlLists;
	}

	/**
	 * 
	 * @描述：根据流读取Excel文件
	 * 
	 * @参数：@param inputStream
	 * 
	 * @参数：@param isExcel2003
	 * 
	 * @参数：@return
	 * 
	 * @返回值：List
	 */

	public List<String> read(InputStream inputStream, boolean isExcel2003) {
		List<String> urList = null;
		try {
			urList = new ArrayList<String>();
			/** 根据版本选择创建Workbook的方式 */

			Workbook wb = null;

			if (isExcel2003) {
				wb = new HSSFWorkbook(inputStream);
			} else {
				wb = new XSSFWorkbook(inputStream);
			}
			urList = read(wb, false);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return urList;
	}

	private List<String> read(Workbook wb, boolean f) {

		List<String> dataLst = new ArrayList<String>();

		/** 得到第一个sheet */
		Sheet sheet = wb.getSheetAt(0);

		/** 得到Excel的行数 */
		this.totalRows = sheet.getPhysicalNumberOfRows();

		for (int i = 0; i < this.totalRows - 1; i++) {
			Row row = sheet.getRow(i + 1);
			Cell cell = row.getCell(6);
			String cellValue = cell.getStringCellValue();
			dataLst.add(cellValue);
		}
		return dataLst;
	}
}
