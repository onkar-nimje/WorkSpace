package Demo;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class all_Method {
	
	static WebDriver driver = new ChromeDriver();
	static JavascriptExecutor js = (JavascriptExecutor) driver;
	static FileInputStream finput;
	static File src;
	static XSSFWorkbook workbook;
	static Sheet sheet;
	
	
	public static void initialize_WebDriver() 
	{
		System.setProperty("webdriver.chrome.driver", "C:\\Python27\\chromedriver.exe");
		driver.manage().window().maximize();
	}
	
	public static void login_To_Jira() throws Exception {
		// TODO Auto-generated method stub
		initialize_WebDriver();
		driver.navigate().to("https://jira.rexel.com");
		waitForElement("//input[@id='userNameInput']");
		
		src = new File("D:\\Rexel\\WorkSpace\\Test_Automation\\Test_Data.xlsx");
		finput = new FileInputStream(src);
		workbook = new XSSFWorkbook(finput);
		sheet = workbook.getSheet("Credentials");
		
		//login
		driver.findElement(By.name("UserName")).sendKeys(sheet.getRow(0).getCell(1).getStringCellValue());
		driver.findElement(By.name("Password")).sendKeys(sheet.getRow(1).getCell(1).getStringCellValue());
		driver.findElement(By.id("submitButton")).click();
		
//		Alert alert = driver.switchTo().alert();
		waitForElement("//a[@id='create_link']");  
	}
	
	public static void click_On_TestCycle_Button() throws Exception
	{
		waitForElement("//a[@id='create_link']");  
		//click on plan test cycle		
		Thread.sleep(5000);
		driver.findElement(By.id("zephyr_je.topnav.tests")).click();
		driver.findElement(By.id("zephyr-je.topnav.tests.plan.cycle")).click();
		waitForElement("//a[@id='create_link']");  	
	}
	
	public static boolean search_TestCycle() throws Exception
	{
		click_On_TestCycle_Button();
		
		//select version
		driver.findElement(By.id("select-version2-field")).clear();
		driver.findElement(By.id("select-version2-field")).sendKeys("REL 17.4");
		driver.findElement(By.id("select-version2-field")).sendKeys(Keys.ENTER);
		System.out.println("entered release");
		Thread.sleep(5000);

		sheet = workbook.getSheet("Credentials");
		String cycleName = sheet.getRow(2).getCell(1).getStringCellValue();
		System.out.println(cycleName);
		boolean status = false;
		Thread.sleep(5000);
		
		List<WebElement> lst = driver.findElements(By.xpath("//ul[@id='project-panel-cycle-list-summary']/li/div/div[1]/a"));
		//boolean cyclePresent=false;
		for(int i=0;i<lst.size();i++)
		{
			String fromJiralist = lst.get(i).getText();
			if(fromJiralist.contains(cycleName))
			{
				status=true;
				WebElement Element = driver.findElement(By.linkText(cycleName));
		        js.executeScript("arguments[0].scrollIntoView();", Element);
				break;
			}
		}
		if(status)
        {
      		JavascriptExecutor js = (JavascriptExecutor) driver;
    		WebElement Element = driver.findElement(By.linkText(cycleName));
            js.executeScript("arguments[0].scrollIntoView();", Element);
            
            WebElement web_Element_To_Be_Hovered = driver.findElement(By.linkText(cycleName));
	        Actions builder = new Actions(driver);
	        builder.moveToElement(web_Element_To_Be_Hovered).build().perform();
	        Thread.sleep(5000);
	        
	        String cycle_Settings_XPath = "//a[@title='" + cycleName + "']/following::div[1]/div/a" ;
	        driver.findElement(By.xpath(cycle_Settings_XPath)).click();
	        
	        Thread.sleep(5000);
	        //driver.findElement(By.xpath("//a[contains(text(),'Add Tests')]")).click();
	        List<WebElement> list = driver.findElements(By.xpath("//a[@class='aui-list-item-link cycle-operations-add-tests']"));
	        int a = list.size();
	        String xpath1 = "(//a[@class='aui-list-item-link cycle-operations-add-tests'])[" + a + "]";
	        driver.findElement(By.xpath(xpath1)).click();
        }
    	else 
    	{
    		status = false; 
    		createNewTestCycle();
    		
    	}
        return status;        
	}
	
	public static void createNewTestCycle() throws Exception
	{
		driver.findElement(By.id("pdb-create-cycle-dialog")).click();
		driver.findElement(By.id("cycle_name")).sendKeys(sheet.getRow(2).getCell(1).getStringCellValue());
		driver.findElement(By.id("cycle-create-form-submit-10820")).click(); 
	}
	
	
	public static List collect_TestCase_Ids_From_Story() throws Exception
	{
		
		List<WebElement> list = driver.findElements(By.xpath("//*[contains(@title,'is related to')]//parent::dl//img[contains(@title,'Test')]/ancestor::p//span[1]//a"));
	    Iterator<WebElement> iterator = list.iterator();

	    List<String> values = new ArrayList<String>();
	    while (iterator.hasNext()){
	        WebElement element = iterator.next();
	        values.add(element.getText());
	    }

	    System.out.println("Test Ids from Story : " + values.toString());
		return values;
	}
	
	
	public static void waitForElement(String item) 
	{
	    WebDriverWait wait = new WebDriverWait(driver,30);
	    WebElement element =  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(item)));
	}
	
	
	public static List get_All_TestIds() throws Exception
	{
		sheet = workbook.getSheet("Story_ids");
		List<String> testCaseList = new ArrayList<String>();
		int rowCount = sheet.getLastRowNum()+1;	
		System.out.println("rowCount : " + rowCount);
		
        for(int i=0; i<rowCount ; i++)
        {         
        	String story_ID = sheet.getRow(i).getCell(0).getStringCellValue();
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
    	    Iterator<WebElement> iterator = list.iterator();

    	    List<String> values = new ArrayList<String>();
    	    while (iterator.hasNext()){
    	        WebElement element = iterator.next();
    	        values.add(element.getText());
    	    }

    	    System.out.println("Test Ids from Story : " + values.toString());
           //testCaseList =  all_Method.collect_TestCase_Ids_From_Story();      		
    	}
        System.out.println("test case list : " +testCaseList.size());
		return testCaseList;
	}
	
	
	public static void addTestToTestCycle() throws Exception
	{
//		sheet = workbook.getSheet("Credentials");
//		String cycleName = sheet.getRow(2).getCell(1).getStringCellValue();
//		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.id("zephyr_je.topnav.tests")));
		List lst = get_All_TestIds();
		System.out.println("list size : " +lst.size());
		search_TestCycle();
        driver.findElement(By.id("zephyr-je-testkey-textarea")).click();
        
        for(int i=0; i<lst.size(); i++)
        {
        	System.out.println("test ids to be added : " + lst.get(i).toString());
        	driver.findElement(By.id("zephyr-je-testkey-textarea")).sendKeys(lst.get(i).toString());
        }
        
		

        Thread.sleep(3000);
    	driver.findElement(By.xpath("//button[contains(text(),'Add')]")).click();
    	Thread.sleep(2000);
    	driver.findElement(By.xpath("//button[contains(text(),'Close')]")).click();
		
	}
	
}
