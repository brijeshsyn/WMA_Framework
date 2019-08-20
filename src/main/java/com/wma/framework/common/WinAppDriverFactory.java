package com.wma.framework.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver; 
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import io.appium.java_client.windows.WindowsDriver;

 /**
 * The WebDriverFactory class is responsible for providing instance of a 
 * WebDriver.<br>
 * It has been made
 * <a href="https://en.wikipedia.org/wiki/Singleton_pattern> target=
 * "_blank">Singleton class</a> so that it cannot have more one object at any
 * given point of time. <br>
 * <br>
 * <br>
 * It can be used as following :-
 *
 * <pre>
 * WebDriver driver = WebDriverFactory.getWebDriver();
 * driver.get(url);
 * WebDriverFactory.quit();
 * </pre>
 *
 * <br>
 * It can not meant to be used outside the framework code, hence its methods 
 * access has been marked Default 
 *
 *
 */
public class WinAppDriverFactory {
	
	private WinAppDriverFactory() {
	}
	
	private static WindowsDriver winDriver = null;
	//private static WebDriver chromeDriver = null;
	private static ConfigProvider config = ConfigProvider.getInstance();
	private static boolean isTunnelStarted = false;
	
	/**
	* Create an instance of web driver based on the browser name provided in the 
	* parameter
	*
	* @param browserName
	* 
	* @return
	*/
 @SuppressWarnings("rawtypes")
private static WindowsDriver createWindowsInstance(String applicationType) {
	 WindowsDriver driver = null;
	 
	 String fileSeparator = System.getProperty("file.Separator");
	 try {
		 File file = new File(config.getFrameworkFolderPath() + fileSeparator + "Resources" + "Drivers");
		 
		 DesiredCapabilities capabilities;
		 if (applicationType.toLowerCase().contains("windows")) {
			 Properties prop = new Properties();
			 prop.load(new FileInputStream(new File(config.getWindowsConfigFilePath())));
			 
			 String appiumUrl = prop.getProperty("winAppURL");
			 prop.remove("winAppURL");
			 
			 capabilities = new DesiredCapabilities();
			 System.out.println("Windows driver Configuration as follows :\n" + prop);
			 
			 for (Object key : prop.keySet())
				 capabilities.setCapability(key.toString(), prop.get(key).toString());
				 
			// String filePath = capabilities.getCapability("chromedriverExecutable");
			 String driverLocation = file.getCanonicalPath() + fileSeparator
					 + capabilities.getCapability("winAppDriverPath");
			 launchWinAppDriver(driverLocation);
			// capabilities.setCapability("winAppDriverPath", driverLocation);
			 System.out.println("Windows Configurations as follows :\n" + capabilities.toJson());
			 driver = new WindowsDriver(new URL(appiumUrl), capabilities);
			 return driver;
		 }
		 else
		 {
			 System.out.println("Select a valid application type.");
		 }
	 } catch (Exception e) {
		 e.printStackTrace();
	 }
	 
	 // Set the implicit timeout
	 driver.manage().timeouts().implicitlyWait(config.getDefaultTimeOut(), TimeUnit.SECONDS);
	 
	 // maximize the browser window when it's not mobile 

	 
	 return driver;
  }
 
  // To mantain the driver instances for multiple threads
 private static ThreadLocal<WindowsDriver> windowsDriver = new ThreadLocal<>();
 
 /**
 * Get the instance of wbe driver 
 *
 * @return
 */
 @SuppressWarnings("rawtypes")
public static WindowsDriver getWindowsDriver() {
	 if (winDriver == null) {
		 winDriver = createWindowsInstance(config.getBrowserName());
		 windowsDriver.set(winDriver);;
	 } else if (windowsDriver.get().toString().contains("null")) {
		 winDriver = createWindowsInstance(config.getBrowserName());
		 windowsDriver.remove();
		 windowsDriver.set(winDriver);
	 }
	 return windowsDriver.get();
 }
 
/**
 * Quite default web driver instance
 */
 public static void quitWindowsDriver() {
	 if (winDriver != null)
		 winDriver.quit();
	 winDriver = null;
  }
  

/**
 * Quit all open instances of web driver, including the explicit instances 
 */
 public static void quitAllInstancesOfWebDriver() {
	 quitWindowsDriver();
	 
	 if (!windowsDriver.get().toString().contains("null")) {
		 windowsDriver.get().quit();
		 windowsDriver.remove();
	 } 
  }
 

 public static void launchWinAppDriver(String driverpath)
	{
		try 
		{ 

			// create a new process 
			System.out.println("Creating Process"); 

			ProcessBuilder runProg  = new ProcessBuilder(driverpath); 
			Process pro = runProg.start(); 

			// wait 10 seconds 
			System.out.println("Waiting"); 
			Thread.sleep(10000); 
			System.out.println("Process start successfully"); 
			// kill the process 
			//            pro.destroy(); 
			//            System.out.println("Process destroyed"); 

		}  
		catch (Exception ex)  
		{ 
			ex.printStackTrace(); 
		} 
	} 

 

}
