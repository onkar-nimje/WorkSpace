package Demo;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class a extends all_Method  {
//	static FileInputStream finput;
//	static File src;
//	static XSSFWorkbook workbook;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		all_Method.initialize_WebDriver();
		all_Method.login_To_Jira();
		//all_Method.addTestToTestCycle();
	    
		src = new File("D:\\Rexel\\WorkSpace\\Test_Automation\\Test_Data.xlsx");
		finput = new FileInputStream(src);
		workbook = new XSSFWorkbook(finput);
	    Sheet sheet1 = workbook.getSheet("Story_ids");
		int rowCount = sheet1.getLastRowNum()+1;	
		System.out.println("rowCount : " + rowCount);
		System.out.println("Hello Omkar");
		
		
        for(int i=0; i<rowCount ; i++)
        {         
        	String story_ID = sheet1.getRow(i).getCell(0).getStringCellValue();
        	driver.navigate().to("https://jira.rexel.com/browse/" + story_ID);
    		waitForElement("//a[@id='create_link']");  
    		WebElement Element = driver.findElement(By.xpath("//div[@class='mod-content issue-drop-zone']"));
            js.executeScript("arguments[0].scrollIntoView();", Element);
            
            try
            {
        	   driver.findElement(By.id("show-more-links-link")).click();        	   
            }
            
            catch(Exception e)
            {
            	System.out.println("its a catch block");
            }
            
            List<WebElement> list = driver.findElements(By.xpath("//*[contains(@title,'is related to')]//parent::dl//img[contains(@title,'Test')]/ancestor::p//span[1]//a"));
    	    Iterator<WebElement> iterator= list.iterator();
    	    search_TestCycle();
    	    Thread.sleep(5000);
    	    driver.findElement(By.id("zephyr-je-testkey-textarea")).click();
    	    List<String> values = new ArrayList<String>();
    	    while (iterator.hasNext())
    	    {
    	        WebElement element = iterator.next();
    	        values.add(element.getText());
    	            	        
    	        for(int a=0; a<list.size(); a++)
    	        {
    	        	System.out.println("test ids to be added : " + values.get(i).toString());
    	        	driver.findElement(By.id("zephyr-je-testkey-textarea")).sendKeys(values.get(i).toString());
    	        }   			

    	        Thread.sleep(3000);
    	    	driver.findElement(By.xpath("//button[contains(text(),'Add')]")).click();
    	    	Thread.sleep(2000);
    	    	driver.findElement(By.xpath("//button[contains(text(),'Close')]")).click();
    	    }

    	    System.out.println("Test Ids from Story : " + values.toString());

        }
	}

}
