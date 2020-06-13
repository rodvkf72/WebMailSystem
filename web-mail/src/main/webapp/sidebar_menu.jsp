<%-- 
    Document   : sidebar_menu.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>웹메일 시스템 메뉴</title>
    </head>
    <body>
        <br> <br>
        <span style="color: indigo"> <strong>사용자: <%= session.getAttribute("userid") %> </strong> </span> <br>

        <p> <a href="temporary.jsp"> 임시 보관함 </a> </p>
        <p> <a href="sentmail.jsp">보낸 메일함</a>
        <p> <a href="main_menu.jsp"> 메일 읽기 </a> </p>
        <p> <a href="write_mail.jsp"> 메일 쓰기 </a> </p>
        <p> <a href="mypage.jsp">마이 페이지</a></p>
        <p> <a href="write_mail.jsp?to=<%=session.getAttribute("userid")%>"> 내게 쓰기 </a> </p>
        <p> <a href="Login.do?menu=<%= CommandType.LOGOUT %>">로그아웃</a></p>
    </body>
</html>
