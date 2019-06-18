<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---getAddress");

   ConnectDB conn = ConnectDB.getInstance();   
   
   String address = request.getParameter("address");
   System.out.println("address="+address);

   out.print(conn.getAddress(address));
%>