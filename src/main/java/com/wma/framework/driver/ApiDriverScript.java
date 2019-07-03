package com.wma.framework.driver;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wma.framework.api.ApiTestCaseReader;
import com.wma.framework.api.RESTOperations;
import com.wma.framework.api.model.ApiTestCase;
import com.wma.framework.api.model.ApiTestCase.ApiTestSteps;
import com.wma.framework.api.model.ResourceParameter;
import com.wma.framework.common.ConfigProvider;
import com.wma.framework.report.ReportCustomiser;
import com.wma.framework.report.TestRailAndExtentReporter;
import com.wma.framework.util.DatabaseReader;
import com.aventstack.extentreports.Status;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import io.restassured.response.Response;

public class ApiDriverScript {
	
	private static Logger log = Logger.getLogger(ApiDriverScript.class);
	private TestRailAndExtentReporter tcLogger;
	private Map<String, String> globalVariables;
	private Map<ApiTestCase.ApiTestSteps, Response> responseOfSteps;
	private Map<ApiTestCase.ApiTestSteps, ResultSet> dbQueryResultOfSteps;
	private Map<String, String> paramsWithValues;
	private List<ApiTestCase> testCases;
	private boolean STOP_EXECUTION_ON_STEP_FAILURE = false;
	
	public void start() throws Exception {
		log.info("Executing API Driver Script");
		
		// Initialize the config setting from the Test Controller file 
		ConfigProvider config = ConfigProvider.getInstance();
		
		File myFile = new File(config.getResultFolder());
		// Create results folder if doesn't exist already
		if(!myFile.exists())
			myFile.mkdirs();
		
		// Fetch the API end point url
		String endPoint = config.getAppUrl();
		
		//Initialize the API Test Case reader object which will read the testCases
		ApiTestCaseReader tcReader = new ApiTestCaseReader(config);
		
		// Read the test cases from the excel
		testCases = tcReader.readApiTestCases();
		
		// Read global variables 
		globalVariables = tcReader.getGlobalVariables();
		
		for (int iteration = 1; iteration <= config.getIterations(); iteration++) {
			
			// iterate through all the test cases
			for (ApiTestCase testCase : testCase) {
				// if the test case is already passed, do not execute it 
				if (testCase.getExecutionStatus())
					continue;
				
				log.info("Started execution of " + testCase.getTest_ID() + " " + testCase.getTestCase_Name());
				//Create Test Case in the report
				tcLogger = TestRailAndExtentReporter.getInstance(config, testCase.getTestCase_Name(), "");
				
				// To store their parameter values which can be used in the Resource path 
				paramsWithValues = new HashMap<>();
				
				// Store responses of each step and use it next steps if required 
				responsesOfSteps = new HashMap<>();
				
				// Store database query Result of each step and use it in next steps if required
				dbQueryResultOfSteps = new HashMap<>();
				try { 
					// Execute each test step
					for (ApiTestCase.ApiTestSteps testStep : testCase.getTestSteps()) {
						// Do not step which is marked as NO in the filed Execute Step
				 		if (testStep.getExecute_Step().equalsIgnoreCase("No"))
				            continue;
				 		
                		// Log the step which is currently executed in the report
				 		tcLogger.log(Status.INFO, testStep.getStep_Number() + " : " + testStep.getStep_Description());
				        
				 		// Store the query result in this object for each step and use it whenever 
				 		// required
				 		ResultSet dbQueryResult = null;
				 		
				 		// Execute database query if there is query provided in the Database_Query field 
				 		if (!testStep.getDatabase_Query().equals("")) {
				 			// Execute database query
				 			dbQueryResult = getDatabaseQueryResult(config, testStep);
				 			dbQueryResultOdSteps.put(testStep, dbQueryResult);
				 	    }
				 		
				 		// Fetch the resource, json, method values provided for the step 
				 		String resource = testStep.getResource();
				 		String json = testStep.getJSON_Content();
				        String method = testStep.getRequest_Type();
				        
				        // Get parameter names from the resource
				        List<String> params = new ArrayList<>();
				        if (resource.contains("{"))
				        	params = getParametersFromResource(resource);
				        
				        if (!params.isEmpty()) {
				        	
				        	List<String> paramsFoundInGlobal = new ArrayList<>();
				        	for (String param : params) {
				        		if (globalVariables.containsKey(param)) {
				        			resource.replaceAll(param, globalVariables.get(param));
				        			paramsFoundInGlobal.add(param);
				        		}
				        	}
				        	
				        	params.removeAll(paramsFoundInGlobal);
				        	if (!param.isEmpty())
				        		resource = formatResourceParameters(testStep, dbQueryResult, resource, params);
				        } // End of if(!param.isEmpty())
				        
				        // Format JSON, by replacing the variables with proper value from DB Query 
				        // or Response of previous step 
				        List<String> jsonParams = getParameterFromJson(json);
				        if (!jsonParams.isEmpty())
				        	json = formatJsonValues(json, jsonParams);
				        
				        // Store the response object 
				        Response res;
				        
				        // Set the end point for the APIs
				        RESTOperations rest = new RESTOperations(endPoint);
				        
				        // Remove braces { } from the resource
				        resource = resource.replaceAll(Pattern.quote("{"), "").replaceAll(Pattern.quote("}"), "");
				        
				        // Excute specific HTTP method based on the method in the step
				        switch (method.toUpperCase())
				        case "GET":
				        	tcLogger.log(Status.INFO, "Executing GET method with resource : " + resource);
				        	res = rest.get(resource);
				        	break;
				        
				        case "POST":
				        	tcLogger.log(Status.INFO,
				        			"Executing POST method with resource : " + resource + " \nand JSON : " + json);
				        	res = rest.post(resource, json);
				        	break;
				        	
				        case "PUT"
				            tcLogger.log(Status.INFO,
				            		"Executing PUT method with resource : " + resource + " \nand JSON : " + json);
				            res = rest.put(resource, json);
				            break;
				            
				        case "DELETE":
				        	tcLogger.log(Status.INFO,
				        			"EXECUTING DELETE "
				        			
				        	