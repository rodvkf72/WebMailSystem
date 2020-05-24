<%-- 
    Document   : test2
    Created on : 2020. 5. 24., 오후 4:28:24
    Author     : rodvk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>

<%
    //단순 DB연결과 값 전달만을 하는 페이지. 일단은 jsp파일로 생성해두었음
    request.setCharacterEncoding("UTF-8");
    
    String t_to = request.getParameter("to")==null?"":request.getParameter("to");
    String t_cc = request.getParameter("cc")==null?"":request.getParameter("cc");
    String t_subj = request.getParameter("subj")==null?"":request.getParameter("subj");
    String t_text = request.getParameter("body")==null?"":request.getParameter("body");
    
    Connection conn = null;
    Statement stmt = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mail?serverTimezone=UTC", "root", "1463");
        if (conn == null) {
            throw new Exception("DB Connect Fail");
        }
        stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO test (test_to, test_cc, test_subj, test_text) values('" + t_to + "','" + t_cc + "','" + t_subj + "','" + t_text + "');");
    } finally {
        try {
            stmt.close();
        } catch (Exception ignored) {

        }
        try {
            conn.close();
        } catch (Exception ignored) {

        }
    }
%>
