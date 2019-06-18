<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
   ConnectDB conn = ConnectDB.getInstance();   
   
   String id = request.getParameter("ID");

   out.println(conn.lockerState(id));
%>
