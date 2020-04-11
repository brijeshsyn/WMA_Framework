package com.wma.framework.api;

import static com.wma.framework.api.model.AuthMode.BASIC;
import static com.wma.framework.api.model.AuthMode.NTLM;
import static com.wma.framework.api.model.AuthMode.SSL;
import static io.restassured.RestAssured.given;

import java.net.URI;

import com.wma.framework.api.model.AuthMode;
import com.wma.framework.common.ConfigProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * This class is responsible to consume the REST Web Services based on the HTTP method
 *  
 *  Please set the mode of authentication e.g. ntlm, ssl, basic
 *  <code>
 *  RESTOperations.AUTH_MODE = AuthMode.SSL;
 *  </code>
 *  Please note: In case of SSL authentication, please add the respective in the jre/.../security/cacerts
 *  @author singhb
 *  
 */
public class RESTOperations {
	private RequestSpecification req;
	public static AuthMode AUTH_MODE = null;
	
	public RESTOperations(String endPoint) {
		ConfigProvider config = ConfigProvider.getInstance();
		
		if(null == endPoint)
			RestAssured.baseURI = config.getAppUrl();
		else
			RestAssured.baseURI = endPoint;
		
		if(null == AUTH_MODE)
				req = given();
		else if(AUTH_MODE.equals(NTLM))
			req = given().auth().basic(config.getUserName(), config.getPassword());
		else if(AUTH_MODE.equals(SSL) || AUTH_MODE.equals(BASIC))
			req = given().auth().basic(config.getUserName(), config.getPassword());
	}
	
	//To handle HTTP GEt requests
	public Response get(String resource) {
		return req.get(resource);
	}
    //To handle HTTP POST requests
	public Response post(String resource, String json) {
		return req.header("Content-Type", "application/json").body(json).when().post(resource);
	}
	//To handle HTTP PUT requests 
	public Response put(String resource, String json) {
		return req.header("Content-Type", "application/json").body(json).when().put(resource);
	}
	//To handle HTTP DELETE requests 
	public Response delete(String resource, String json) {
		return req.header("Content-Type", "application/json").body(json).when().delete(resource);
	}
 }
	
