<%-- 
    Document   : sentmail
    Created on : 2020. 6. 8., 오후 10:50:00
    Author     : miso
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>

<!DOCTYPE html>

<jsp:useBean id="sentmail" scope="page" class="cse.maven_webmail.model.getSentMail" />
<%    
            String status = "";
            status = request.getParameter("status");
           
%>
<html>
     <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>보낸 메일함</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
     </head>
     <body>
        <jsp:include page="header.jsp" />
        
        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>
        <div id="main">

            <script>
 
                    
                    history.replaceState({}, null, location.pathname);
            
            </script>
            
            <% if(status != null){
                    if(status.equals("1")){
                        out.println("<script>confirm('메일 삭제 성공');</script>");
                        status = null;
                    }
                    else if(status.equals("0")){
                        out.println("<script>alert('메일 삭제 성공');</script>");
                        status = null;
                    }
                   
            }
                
                                %>

            <% String userid = (String)session.getAttribute("userid"); %>
            <%= sentmail.getSentMessageList(userid) %>
            
        </div>
         <jsp:include page="footer.jsp" />
     </body>
        
       
</html>
