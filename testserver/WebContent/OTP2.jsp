<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "OTP.OTP" %>
<%@ page import = "com.db.ConnectDB" %>

<%  

	System.out.println("---OTP2");

   String mem_id = request.getParameter("mem_id");
   String lock_id = request.getParameter("lock_id");
   
   System.out.println("mem_id="+mem_id);
   System.out.println("lock_id="+lock_id);

   OTP conn = OTP.getInstance();   
   ConnectDB connect = ConnectDB.getInstance();
   
   String check = connect.check_list(mem_id, lock_id);
   
   if(check.equals("0")){
	   String otp = conn.create();
	   connect.storeOTP(otp,mem_id,lock_id);
	       
	   out.print(otp);
	   System.out.println(otp);
   }
   else{
	   out.print("1");
   }
%>