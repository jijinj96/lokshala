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

import com.beans.Facilities;
import com.model.Account;

@WebServlet("/FacilityFeedbackServlet")
public class FacilityFeedbackServlet extends HttpServlet {
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
			String facility_id = (String) jsonobj.get("facility-id");
			int fid = Integer.parseInt(facility_id);
			String feedback = (String) jsonobj.get("facility-feedback");
			String school_name = (String) jsonobj.get("school-name");
			HttpSession session = request.getSession(false);
			JSONObject obj = new JSONObject();
			if(session == null){
				obj.put("msg", "Unauthorized login");
			}
			else{
				String email = (String) session.getAttribute("username");
				Account account = new Account();
				int school_id = account.getSchoolId(school_name);
				
				boolean status = account.putFacilityFeedback(fid, feedback,school_id, email);
				obj.put("msg", account.getMsg());
				
			}
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
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
 		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		
		
		
	}

}
