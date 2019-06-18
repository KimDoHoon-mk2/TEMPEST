<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---getIoT_byUser");

   ConnectDB conn = ConnectDB.getInstance();   
   
   String ID = request.getParameter("ID");
   System.out.println("ID="+ID);

   out.print(conn.getIoT(ID));
%>