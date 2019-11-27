package com.wma.framework.driver;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.wma.framework.api.model.API_Model;

public class API_Executor {
	public static boolean execute(API_Model model) throws ParseException {
		System.out.println("Execution Started...");
		Response res = null;
		RestAssured.baseURI = model.getUrl();
		RequestSpecification rs = given().auth().ntlm("<userName>", "<Pwd>", "<WorkStation>", "<Domain>");
		
		switch(model.getMethod().toUpperCase()) {
		case "GET":
			res = rs.get();
			break;
		case "POST":
			res = rs.contentType(ContentType.JSON).header("Accept", ContentType.JSON.getAcceptHeader())
					.body(model.getRequest()).post();
			break;
		default:
			System.err.println("Invalid Request Method");
		}
		System.out.println("Response validation started...");
		Object obj = new JSONParser().parse(res.body().asString());
		try {
			if(obj instanceof JSONObject) {
				JSONObject actualResponse = (JSONObject) new JSONParser().parse(res.body().asString());
				JSONObject expResponse = (JSONObject) new JSONParser().parse(model.getResponse());
				
				if(actualResponse.toJSONString().toString().equalsIgnoreCase(expResponse.toJSONString().toString()))
					System.out.println("PASSED");
				else {
					System.err.println("FAILED : \n" + model);
					System.err.println("Actual Response : \n" + actualResponse.toJSONString());
				}
			} else if(obj instanceof JSONArray) {
				JSONArray actualResponse = (JSONArray) new JSONParser().parse(res.body().asString());
				JSONArray expResponse = (JSONArray) new JSONParser().parse(model.getResponse());
				
				if(actualResponse.toJSONString().toString().equalsIgnoreCase(expResponse.toJSONString().toString()))
					System.out.println("PASSED");
				else {
					System.err.println("FAILED : \n" + model);
					System.err.println("Actual Response : \n" + actualResponse.toJSONString());
				}
			}
		} catch(Exception e) {
			System.err.println(res.body().asString());
			e.printStackTrace();
		}
		return true;
	}
}