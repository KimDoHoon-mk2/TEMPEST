<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---getUser_byIoT");

   ConnectDB conn = ConnectDB.getInstance();   
   
   String lock_id = request.getParameter("lock_id");
   
   System.out.println("lock_id="+lock_id);

   out.print(conn.getUser(lock_id));
%>