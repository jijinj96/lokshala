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


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String jsonString = "";
			String username = null;
			String password = null;
			if (br != null) 
			{
				jsonString = br.readLine();
			}

			System.out.println("Received JSON " + jsonString);
			JSONParser parser = new JSONParser();

			try {
				JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
				// get a String from the JSON object
				username = (String) jsonObject.get("username");
				System.out.println("User name is: " + username);

				password = (String) jsonObject.get("password");
				System.out.println("Password is: " + password);
			} catch (ParseException pe) {
				System.out.println("position: " + pe.getPosition());
				System.out.println(pe);
			}
			// String jsonStr = "{\"auth\": \"Authenticated\"}";
			JSONObject obj = new JSONObject();

			Account account = new Account();
			boolean status = account.doLogin(username, password);
			if (status) {
					HttpSession session = request.getSession();
					session.setAttribute("username", username);
					obj.put("username", username);
					obj.put("msg", "authenticated");
			} 
			else {
				obj.put("msg", "Wrong Username");
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
			// e.printStackTrace();
			System.out.println(e);
		}
	
	
	
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
