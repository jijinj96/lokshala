package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.model.Account;

@WebServlet("/EventFeedbackServlet")
public class EventFeedbackServlet extends HttpServlet {
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
			String event_feedback = (String) jsonobj.get("event-feedback");
			String school_id = (String) jsonobj.get("school-id");
			int s_id = Integer.parseInt(school_id);
			String event_id = (String) jsonobj.get("event-id");
			int e_id = Integer.parseInt(event_id);
			HttpSession session = request.getSession(false);
			
			JSONObject obj = new JSONObject();
			if(session == null){
				obj.put("msg", "unauthorised Feedback");	
			}
			else{
				String email = (String) session.getAttribute("username");
				Account account = new Account();
				account.putEventFeedback(e_id, event_feedback, s_id, email);
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
			
		} 
		
		
		
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
