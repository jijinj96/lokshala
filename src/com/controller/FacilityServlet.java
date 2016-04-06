package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.beans.Events;
import com.beans.Facilities;
import com.model.Account;

@WebServlet("/FacilityServlet")
public class FacilityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String jsonString = "";
		
		
		if (br != null) 
		{
			jsonString = br.readLine();
		}
		
		
		System.out.println("Received JSON " + jsonString);
		JSONParser parser = new JSONParser();
		String action="";
		try {
			JSONObject jsonobj = (JSONObject) parser.parse(jsonString);
			action = (String) jsonobj.get("action");
			if(action.equals("eventAfterLogin")){
				HttpSession session = request.getSession(false);
				if(session == null){
					System.exit(0);
				}
				else{
					System.out.println(session.getAttribute("username"));
				}
			}
			
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		Account account = new Account();
		
		try {
			ArrayList<Facilities> facilities = account.getFacility();
			
			JSONObject obj = new JSONObject();
			
			JSONArray facilityarray = new JSONArray();
			for(Facilities f : facilities){
				JSONObject facility = new JSONObject();
				facility.put("facility_id", f.getFacility_id());
				facility.put("facility_name", f.getFacility_name());
				facility.put("image", f.getImage());
				facilityarray.add(facility);
			}
			obj.put("facility", facilityarray);
			System.out.println(obj.toJSONString());
			StringWriter out = new StringWriter();
			obj.writeJSONString(out);
			String jsonText = out.toString();
			System.out.print(jsonText);
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
			// writer.write(URLDecoder.decode(new String(decryptedData),
			// "UTF-8"));
			writer.write(jsonText);
			writer.flush();
			writer.close();
			System.out.println("Done Sending Data");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
 		
 		
 	}
 	
 	
 	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
