<%-- 
    Document   : admin_menu.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.model.UserAdminAgent"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="menuagent" scope="page" class="cse.maven_webmail.model.AdminListBean"/>

<!DOCTYPE html>
<%-- 자바 코드 대신 Beans와 jstl을 이용하는 코드로 수정하였습니다. 
     28~30라인도 수정하고 싶었으나 변수에 함수 리턴값을 저장하는 법을 몰라서 수정하지 못하였습니다. --%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>사용자 관리 메뉴</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <jsp:include page="sidebar_admin_menu.jsp" />
        </div>
        <%
            String cwd =  this.getServletContext().getRealPath(".");
        %>
        <jsp:setProperty name="menuagent" property="cwd" value="<%=cwd%>"/>
        <jsp:setProperty name="menuagent" property="server" value="localhost"/>
        <jsp:setProperty name="menuagent" property="port" value="4555"/>
        
        <div id="main">
            <h2> 메일 사용자 목록 </h2>
            
            <%menuagent.setUserListinAgent();%>
            <ul>
                <c:forEach var="userId" items="${menuagent.userList}">
                    <li><c:out value="${userId}"/></li>
                </c:forEach>
            </ul>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
