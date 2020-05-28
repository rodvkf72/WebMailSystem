<%-- 
    Document   : show_message.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<jsp:useBean id="pop3" scope="page" class="cse.maven_webmail.model.Pop3Agent" />
<jsp:useBean id="reply" scope="page" class="cse.maven_webmail.model.ReplyBean"/>
<%
            pop3.setHost((String) session.getAttribute("host"));
            pop3.setUserid((String) session.getAttribute("userid"));
            pop3.setPassword((String) session.getAttribute("password"));
            
%>


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
            <%= pop3.getMessage(Integer.parseInt((String) request.getParameter("msgid")))%>
            <% String tmp = pop3.getReply(Integer.parseInt((String) request.getParameter("msgid"))); 
               reply.parseUrl(tmp);
            %>
            <br/><br/>
            <input type="button" onclick="location.href='<%=reply.getUrl()%>';" value="답장하기"/>
        </div>
            
    </body>
</html>
