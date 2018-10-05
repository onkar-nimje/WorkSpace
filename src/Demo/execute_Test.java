package Demo;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class execute_Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FileInputStream finput;
		File src;
		XSSFWorkbook workbook;
		Sheet sheet;
		src = new File("D:\\Rexel\\WorkSpace\\Test_Automation\\Test_Data.xlsx");
		finput = new FileInputStream(src);
		workbook = new XSSFWorkbook(finput);
		sheet = workbook.getSheet("Credentials");
		String cycleName = sheet.getRow(2).getCell(1).getStringCellValue();
		System.out.println(cycleName);
	}

}
