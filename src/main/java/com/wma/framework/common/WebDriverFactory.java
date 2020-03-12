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
public class WebDriverFactory {
	
	private WebDriverFactory() {
	}
	
	private static WebDriver chromeDriver = null;
	private static ConfigProvider config = ConfigProvider.getInstance();
	
	/**
	* Create an instance of web driver based on the browser name provided in the 
	* parameter
	*
	* @param browserName
	* 
	* @return
	*/
 private static WebDriver createInstance(String browserName) {
	 WebDriver driver = null;
	 
	 String fileSeparator = System.getProperty("file.separator");
	 try {
		 File file = new File(config.getFrameworkFolderPath() + fileSeparator + "Resources" + fileSeparator + "Drivers");
		 
		 DesiredCapabilities capabilities;
		 if (browserName.toLowerCase().contains("ie")) {
			 System.setProperty("webdriver.ie.driver", file.getCanonicalPath() + fileSeparator
					 + "ie_driver" + fileSeparator + "IEDriverServer.exe");
			InternetExplorerOptions ieOptions = new InternetExplorerOptions();
			ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			ieOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			ieOptions.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
			ieOptions.setCapability("ie.enableFullPageScreenshot", false);
			driver = new InternetExplorerDriver(ieOptions);
			
		 } else if (browserName.toLowerCase().contains("chrome")) {
			 System.setProperty("webdriver.chrome.driver", file.getCanonicalPath() + fileSeparator
			         + "chromedriver_win32" + fileSeparator + "chromedriver.exe");
			         
		     Map<String, Object> prefs = new HashMap<String, Object>();
		     prefs.put("download.promt_for_download", false);
		     prefs.put("download.default_directory", System.getProperty("user.dir") + fileSeparator + "Resources"
		    		 + fileSeparator + "DownloadedFiles");
		     
		     ChromeOptions options = new ChromeOptions();
		     options.setExperimentalOption("prefs" , prefs);
		     options.addArguments("--test-type");
		     options.setCapability(ChromeOptions.CAPABILITY, options);
		     driver = new ChromeDriver(options);
		    
		 } else if (browserName.toLowerCase().contains("remote")) {
			 driver = getRemoteWebDriver();
			 
		 } else if (browserName.toLowerCase().contains("safari")) {
			 driver = new SafariDriver();
			 
		 } else if (browserName.toLowerCase().contains("firefox")) {
			 System.setProperty("webdriver.gecko.driver",
					 file.getCanonicalPath() + fileSeparator + "geckoDriver" + fileSeparator + "geckodriver.exe");
			 driver = new FirefoxDriver();
			 
		 } else if (browserName.toLowerCase().contains("edge")) {
			 System.setProperty("webdriver.edge.driver", file.getCanonicalPath() + fileSeparator + "edgedriver"
					+ fileSeparator + "MicrosoftWebDriver.exe");
			 driver = new EdgeDriver();
			 
		 } else if (browserName.toLowerCase().contains("mobile")) {
			 Properties prop = new Properties();
			 prop.load(new FileInputStream(new File(config.getAppiumConfigFilePath())));
			 
			 String appiumUrl = prop.getProperty("appiumURL");
			 prop.remove("appiumURL");
			 
			 capabilities = new DesiredCapabilities();
			 System.out.println("Mobile Configuration as follows :\n" + prop);
			 for (Object key : prop.keySet())
				 capabilities.setCapability(key.toString(), prop.get(key).toString());
				 
			// String filePath = capabilities.getCapability("chromedriverExecutable");
			 String driverLocation = file.getCanonicalPath() + fileSeparator
					 + capabilities.getCapability("chromedriverExecutable");
			 capabilities.setCapability("chromedriverExecutable", driverLocation);
			 System.out.println("Mobile Configurations as follows :\n" + capabilities.toJson());
			 driver = new RemoteWebDriver(new URL(appiumUrl), capabilities);
		 }
	 } catch (Exception e) {
		 e.printStackTrace();
	 }
	 
	 // Set the implicit timeout
	 driver.manage().timeouts().implicitlyWait(config.getDefaultTimeOut(), TimeUnit.SECONDS);
	 
	 // maximize the browser window when it's not mobile 
	 if (!(browserName.toLowerCase().contains("mobile") || browserName.toLowerCase().contains("remote"))) {
		 driver.manage().window().maximize();
		 driver.get(config.getAppUrl());
	 }
	 
	 return driver;
  }
 
  // To mantain the driver instances for multiple threads
 private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
 
 /**
 * Get the instance of wbe driver 
 *
 * @return
 */
 public static WebDriver getWebDriver() {
	 if(webDriver.get() == null)
		 webDriver.set(createInstance(config.getBrowserName()));
	 return webDriver.get();
 }
 
 /**
 * Get an explicit instance of Chrome Driver 
 * 
 * @return
 */
 public static WebDriver getExplicitInstanceOfChromeDriver() {
	 if (chromeDriver == null )
		 chromeDriver = createInstance("chrome");
	 return chromeDriver;
  }
 
/**
 * Quite default web driver instance
 */
 public synchronized static void quitWebDriver() {
	 if (webDriver.get() != null) {
		webDriver.get().quit();
		webDriver.remove();
	 }
  }
  
/**
 * Quit the explicit instance of chrome driver 
 */
 public static void quitExplicitInstanceOfChromeDriver() {
	 if (chromeDriver != null) {
		 chromeDriver.quit();
		 chromeDriver = null;
	 }
  }
 
 
/**
 * Create an instance of remote web driver 
 */
 private static WebDriver getRemoteWebDriver() throws FileNotFoundException, IOException, InterruptedException {
	 WebDriver driver;
	 
	 Properties prop = new Properties();
	 prop.load(new FileInputStream(new File(config.getRemoteWebDriverConfigFilePath())));
	 
	 String remoteURL;
	 String urlKey="";
	 if(prop.containsKey("remoteURL"))
		 urlKey = "remoteURL";
	 else if (prop.containsKey("winAppURL"))
		 urlKey = "winAppURL";
	 else if (prop.containsKey("appiumURL"))
		 urlKey = "appiumURL";
	 else
		 System.err.println("Please provide Remote Driver's URL in the properties file");
		 
	 remoteURL = prop.getProperty(urlKey);
	 prop.remove(urlKey);
	 
	 DesiredCapabilities capabilities = new DesiredCapabilities();
	 System.out.println("Remote WebDriver Configurations as follows :\n" + prop);
	 
	 for (Object key : prop.keySet())
		 capabilities.setCapability(key.toString(), prop.get(key).toString());
	 
	 driver = new RemoteWebDriver(new URL(remoteURL), capabilities);
	 driver.manage().timeouts().implicitlyWait(config.getDefaultTimeOut(), TimeUnit.SECONDS);
	 driver.manage().window().maximize();
	 return driver;
   }

}
