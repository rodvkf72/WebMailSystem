<%-- 
    Document   : show_sentmessage
    Created on : 2020. 6. 9., 오전 1:51:46
    Author     : miso
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="sentmessage" scope="page" class="cse.maven_webmail.model.getSentMessage" />

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메일 보기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <jsp:include page="sidebar_previous_menu.jsp" />
        </div>
        <div id="msgBody">
            
            <% String userid = (String)session.getAttribute("userid"); %>
            <%= sentmessage.getSentMessagedetail(Integer.parseInt((String) request.getParameter("msgid"))) %>
           
        </div>
    </body>
</html>
