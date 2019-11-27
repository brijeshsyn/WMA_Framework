package com.wma.framework.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wma.framework.api.model.API_Model;

public class APIDetailsReader {
	private String[] keys = {"--url--", "--method--", "--request--", "--response--"};
	private String filePath;
	
	public APIDetailsReader(String filePath) {
		this.filePath = filePath;
	}
	
	public List<API_Model> readFile() {
		List<API_Model> apiDetails = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = "";
			String contents = "";
			
			System.out.println("Reading file using Buffered Reader");
			
			List<String> temp = new ArrayList<>();
			int lineCounter = 0;
			
			while ((line = br.readLine()) != null) {
				if(Arrays.asList(keys).contains(line.toLowerCase().trim())) {
					System.out.println("Reading " + line);
					temp.add(contents);
					contents = "";
				} else
					contents += line;
				
				//when Line Number is greater than 0 and the fetched line is equal to --URL--
				//Add the details in the list
				if(line.trim().equalsIgnoreCase(keys[0]) && lineCounter != 0) {
					if(temp.get(0).equals(""))
						temp.remove(0);
					
					//In case there is value for all the keys "--url--", "--method--", "--request--", "--response--"
					if(temp.size() == 4)
						apiDetails.add(new API_Model(temp.get(0), temp.get(1), temp.get(2), temp.get(3)));
					else if(temp.size() == 3)
						apiDetails.add(new API_Model(temp.get(0), temp.get(1), "", temp.get(2)));
					
					temp.clear();
				}
				lineCounter++;
			}
			//In case there is no more api details, the last line of the file will not have the key
			//Therefore need to add the list fetched contents in the temp list
			if(!contents.isEmpty()) {
				temp.add(contents);
				contents = "";
			}
			//Check whether the temp list has some data to be saved i nthe apiDetails list
			if(!temp.isEmpty()) {
				if(temp.get(0).equals(""))
					temp.remove(0);
				
				//In case there is value for all the keys "--url--", "--method--", "--request--", "--response--"
				if(temp.size() == 4)
					apiDetails.add(new API_Model(temp.get(0), temp.get(1), temp.get(2), temp.get(3)));
				else if(temp.size() == 3)
					apiDetails.add(new API_Model(temp.get(0), temp.get(1), "", temp.get(2)));
				
				temp.clear();
			}			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return apiDetails;
	}
}