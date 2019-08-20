package com.wma.framework.common;

import org.openqa.selenium.WebElement;

import io.appium.java_client.windows.WindowsDriver;

public class WinAppActions {

	public static WebElement getElementByAccessibilityID(@SuppressWarnings("rawtypes") WindowsDriver driver, String AutomationID) {
		
		return driver.findElementByAccessibilityId(AutomationID);	
	}
	
	
//	public WebElement elementClick(@SuppressWarnings("rawtypes") WindowsDriver driver,WebElement element)
//	{
//	//	return element.click();
//	}
	

}
