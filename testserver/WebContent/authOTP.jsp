<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.db.ConnectDB" %>

<%  
	System.out.println("---authOTP");

   ConnectDB conn = ConnectDB.getInstance();   
   
   String id = request.getParameter("id");
   String otp_num = request.getParameter("otp");
   String lock_id = request.getParameter("lock_id");
   String num = request.getParameter("num");
   String type = request.getParameter("type");
   
   System.out.println("id="+id);
   System.out.println("otp_num="+otp_num);
   System.out.println("lock_id="+lock_id);
   System.out.println("num="+num);
   System.out.println("type="+type);

   out.print(conn.authOTP(otp_num,id,lock_id,num,type));  //  otp인증시  0 리턴
%>