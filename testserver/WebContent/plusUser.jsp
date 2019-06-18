<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---plusUser");

    ConnectDB conn = ConnectDB.getInstance();   
   
	String ID = request.getParameter("id");
    String lock_id = request.getParameter("lock_id");
    String date = request.getParameter("date");
    
    System.out.println("ID="+ID);
    System.out.println("lock_id="+lock_id);
    System.out.println("date="+date);

    out.print(conn.plusUser(ID,lock_id,date));
%>