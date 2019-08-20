package com.wma.framework.common;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;


import io.appium.java_client.windows.WindowsDriver;

public class WinAppDriver {

	//	static Process proc;

	public static void InvokeWinAppDriverProcess()
	{
		//		Runtime run = Runtime.getRuntime();
		//		proc = run.exec(WinAppDriverPath);
		//		String str = proc.getOutputStream().toString();
		//		System.out.println(str);
		//		Runtime.getRuntime().exec(winAppDriver);
	}
	@SuppressWarnings("rawtypes")
	public static WindowsDriver InitiateWindowsDriver(WindowsDriver driver, String AppId, String WinAppDriverPath, String AppiumServerURL ) throws IOException
	{

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("app", AppId);  
		capabilities.setCapability("platformName", "Windows");
		capabilities.setCapability("deviceName", "WindowsPC");
		driver = new WindowsDriver(new URL(AppiumServerURL), capabilities); //WindowsDriver
		System.out.println("Application Invoked");

		// implicit wait
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		return driver;
	}


	public static void destroyProcess()
	{
		//	proc.destroy();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	
	public static void main(String[] args) throws IOException {
		// Initiate win app driver and appium server
		WindowsDriver driver = null;
		String winAppDriverPath = "C:/Program Files (x86)/Windows Application Driver/WinAppDriver.exe";
		String AppID = "E:/Vidhi/ControlsAndLayout.exe";
		String AppiumServerURL ="http://127.0.0.1:4723";
		driver = InitiateWindowsDriver(driver,AppID,winAppDriverPath,AppiumServerURL);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		
		//Object of CommonWinAppActions
		CommonWinAppActions action = new CommonWinAppActions(driver);
		//CommonWebActions<WinAppElements> action = new CommonWebActions<>(driver);

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
		action.clickCommand(com.wma.framework.common.WinAppElements.LAYOUT);
		System.out.println("clicked Layout...");
		//using accessibility id
	
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElementByAccessibilityId("HeaderSite").click();
		System.out.println("Layout");
		System.out.println("closed Layout...");
	//	
		action.clickCommand(com.wma.framework.common.WinAppElements.CONTROLS);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		System.out.println("clicked Controls...");

		//using xpath and click function

		action.clickCommand(com.wma.framework.common.WinAppElements.BUTTON);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		System.out.println("clicked Button Menu...");	
		
		//using name and click function
		action.clickCommand(com.wma.framework.common.WinAppElements.CLICKME);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		System.out.println("clicked Click Me Button...");	

		action.clickCommand(com.wma.framework.common.WinAppElements.PASSWORDBOX);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		//using class name and sendkeys function
		action.sendkeys(com.wma.framework.common.WinAppElements.PASSWORDBOXTEXTBOX, "Testing");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		System.out.println("sendkeys used for entering password...");	

		
		System.out.println("Completed...");	

	}

}
