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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.beans.User;
import com.model.Account;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String jsonString = "";
		String username = null;
		String password = null;
		String name = null;
		String type = null;
		if (br != null) 
		{
			jsonString = br.readLine();
		}

		System.out.println("Received JSON " + jsonString);
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(jsonString);
			username = (String) obj.get("email");
			password = (String) obj.get("password");
			name = (String) obj.get("name");
			type = (String) obj.get("type");
			User user = new User();
			
			user.setEmail(username);
			user.setName(name);
			user.setType(type);
			user.setPassword(password);
			boolean validate = user.validate();
			JSONObject obj1 = new JSONObject();
			if(validate){
				Account account = new Account();
				boolean status = account.doRegister(username, password,name,type);
				
				if(status == true){
					obj1.put("msg", account.getMsg());
				}
				else{
					obj1.put("msg", account.getMsg());
				}
			}
			else{
				obj1.put("msg", user.getMsg());
			}	
			StringWriter out = new StringWriter();
			obj1.writeJSONString(out);
			String jsonText = out.toString();
			System.out.println(jsonText);
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
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
