<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---openOTP");

   ConnectDB conn = ConnectDB.getInstance();   
   
   String id = request.getParameter("id");
   String otp_num = request.getParameter("otp");
   String lock_id = request.getParameter("lock_id");
   
   System.out.println("id="+id);
   System.out.println("otp_num="+otp_num);
   System.out.println("lock_id="+lock_id);
   out.print(conn.openOTP(otp_num,id,lock_id));  //  otp인증시  0 리턴
%>