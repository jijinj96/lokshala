
package com.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.beans.Events;
import com.beans.Facilities;

public class Account {
	private final String dbName = "lokshala";
	private final String driver = "com.mysql.jdbc.Driver";
	private final String url = "jdbc:mysql://localhost:3306/";
	private final String user="root";
	private final String password = "";
	private final String TABLE_EVENT = "event";
	private final String TABLE_USERS = "user";
	private final String TABLE_facility = "facility";
	private final String TABLE_facilityfeedback = "facility_feedback";
	private final String TABLE_School = "school";
	private final String TABLE_EventFeedback = "event_feedback";
	
	
	Connection con;
	private String msg = "";
	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	private void dbConnect() throws ClassNotFoundException, SQLException{
		Class.forName(driver);
		
		con = DriverManager.getConnection(url+dbName, user, password);
	}
	
	
	
	public boolean doLogin(String email , String password) throws ClassNotFoundException, SQLException{
		dbConnect();
		String query = "Select count(*) as count from "+TABLE_USERS+" where email = ? and password = ?";
		PreparedStatement pstmt = con.prepareStatement(query);
		
		pstmt.setString(1, email);
		pstmt.setString(2, password);
		
		ResultSet rs = pstmt.executeQuery();
		
		int count = 0;
		while(rs.next()){
			count = rs.getInt("count");
		}
		rs.close();
		dbClose();
		if(count > 0){
			return true;
		}
		return false;
	}
	
	public boolean doRegister(String email,String password ,String name,String type) throws ClassNotFoundException, SQLException{
		dbConnect();
		
		String sql = "select count(*) as count from "+TABLE_USERS+" where email = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, email);
		ResultSet rsCount = pstmt.executeQuery();
		int count = 0;
		while(rsCount.next()){
			count = rsCount.getInt("count");
		}
		System.out.println(count);
		if(count > 0){
			dbClose();
			pstmt.close();
			rsCount.close();
			setMsg("Email id already exists!!");
			return false;
		}
		String query = "insert into "+TABLE_USERS+" (email,password,name,type) values (?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, email);
		ps.setString(2, password);
		ps.setString(3, name);
		ps.setString(4, type);
		System.out.println(ps);
		int i = ps.executeUpdate();
		dbClose();
		if(i > 0){
			setMsg("Sucessfully Registered");
			return true;
		}
		setMsg("Error Registring");
		return false;
	}
	
	
	public ArrayList<Events> getEvents() throws ClassNotFoundException, SQLException{
		dbConnect();
		String sql = "select * from "+TABLE_EVENT;
		PreparedStatement stmt = con.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<Events> events = new ArrayList<Events>();
 		while( rs.next() ){
			Events event = new Events();
			event.setEvent_id(rs.getInt("event_id"));
			event.setEvent_title(rs.getString("event_title"));
			event.setEvent_description(rs.getString("event_description"));
			event.setEvent_image(rs.getString("image"));
			event.setSchool_id(rs.getInt("school_id"));
			events.add(event);
		}	
		dbClose();
		return events;
	}
	
	
	public ArrayList<Facilities> getFacility() throws ClassNotFoundException, SQLException{
		dbConnect();
		String sql = "select * from "+TABLE_facility;
		PreparedStatement stmt = con.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<Facilities> facilities = new ArrayList<Facilities>();
 		while( rs.next() ){
			Facilities f = new Facilities();
			f.setFacility_id(rs.getInt("facility_id"));
			f.setFacility_name(rs.getString("facility_name"));
			f.setImage(rs.getString("image"));
			facilities.add(f);
		}	
		dbClose();
		return facilities;
	}
	
	
public void putEventFeedback(int event_id , String event_feedback , int School_id, String username) throws ClassNotFoundException, SQLException{
		
		dbConnect();
		
		String sql = "Select user_id from " + TABLE_USERS + " where email = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, username);
		ResultSet rs = stmt.executeQuery();
		int user_id = -1;
		while( rs.next()){
			user_id = rs.getInt("user_id");
		}
		
		if(user_id > 0){
			sql = "select count(*) as count from "+TABLE_EventFeedback +" where user_id = ? and event_id = ?";
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, user_id);
			stmt.setInt(2, event_id);
			rs = stmt.executeQuery();
			int noOfFeed = -1;
			while(rs.next()){
				noOfFeed = rs.getInt("count");
			}
			if(noOfFeed <= 0 ){
				sql = "insert into "+TABLE_EventFeedback+" (user_id,event_id,e_feedback,school_id) values(?,?,?,?)";
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, user_id);
				stmt.setInt(2, event_id);
				stmt.setInt(4, School_id);
				stmt.setString(3, event_feedback);
				int i = stmt.executeUpdate();
				if(i > 0){
					setMsg("Feedback inserted");
				}
				else{
					setMsg("Failed to insert feedback");
				}
			}
			else{
				setMsg("Feedback already given");
			}
		}
		
		else{
			setMsg("Error putting feedback");
			dbClose();
			
		}
	}
	
	
	public boolean putFacilityFeedback(int facility_id , String facility_feedback , int School_id, String username) throws ClassNotFoundException, SQLException{
		
		dbConnect();
		
		String sql = "Select user_id from " + TABLE_USERS + " where email = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, username);
		ResultSet rs = stmt.executeQuery();
		int user_id = -1;
		while( rs.next()){
			user_id = rs.getInt("user_id");
		}
		
		if(user_id > 0){
			sql = "select count(*) as count from "+TABLE_facilityfeedback + " where user_id = ? and facility_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.setInt(2, facility_id);
			ResultSet rs2 = ps.executeQuery();
			int noOfFeed = -1;
			while( rs2.next() ){
				noOfFeed = rs2.getInt("count");
			}
			if( noOfFeed <= 0){
				String sql1 = "insert into "+ TABLE_facilityfeedback +" (school_id,user_id,facility_id,f_feedback) values(?,?,?,?)";
				stmt = con.prepareStatement(sql1);
				stmt.setInt(1, School_id);
				stmt.setInt(2, user_id);
				stmt.setInt(3, facility_id);
				stmt.setString(4, facility_feedback);
				int i = stmt.executeUpdate();
				dbClose();
				if(i > 0){
					setMsg("Succeesss");
					return true;
				}
				else{
					setMsg("Error inserting");
					return true;
				}
			}
			else{
				setMsg("Already Given Feedback");
				return false;
			}
		}
		
		else{
			setMsg("Error putting feedback");
			dbClose();
			return false;
		}
	}
	
	public int getSchoolId(String schoolName) throws ClassNotFoundException, SQLException{
		dbConnect();
		
		String sql = "Select school_id from "+TABLE_School+" where school_name = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, schoolName);
		ResultSet rs = stmt.executeQuery();
		int school_id = -1;
		while(rs.next()){
			school_id = rs.getInt("school_id");
		}
		
		
		dbClose();
		return school_id;
	}
	
	
	
	
	
	private void dbClose() throws SQLException{
		con.close();
	}
}