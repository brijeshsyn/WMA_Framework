package com.wma.framework.report;

import org.apache.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.wma.framework.common.*;

/**
* ReportManager class is responsible for the initialization of the Extent Report and provide the access to the <pre>ExtentReports</pre> object
* @author singhb
*
*/
public abstract class ReportManager {
	private static Logger log = Logger.getLogger(ReportManager.class);
	private static ExtentReports tsmReports;
	private static ExtentReports tsmReport;
	
	protected static ExtentReports getExtent(ConfigProvider config) {
		if (tsmReport != null)
			return tsmReport;
		tsmReport = new ExtentReports();
		tsmReport.attachReporter(getHtmlReporter(config));
		return tsmReport;
	}
	
	/**
	 * To create and get the instance of ExtentHtmlReporter
	 * @param config
	 * @return Returns the instance of ExtentHtmlReporter
	 */
     protected static ExtentHtmlReporter getHtmlReporter(ConfigProvider config) {
    	 ExtentHtmlReporter html
     }

