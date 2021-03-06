<%-- 
    Document   : delete_user.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<%@page import="cse.maven_webmail.model.UserAdminAgent" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="menuagent" scope="page" class="cse.maven_webmail.model.AdminListBean"/>

<!DOCTYPE html>
<%-- 자바 코드 대신 Beans와 jstl을 이용하는 코드로 수정하였습니다.  
    43~45라인도 수정하고 싶었으나 변수에 함수 리턴값을 저장하는 법을 몰라서 수정하지 못하였습니다. --%>

<script type="text/javascript">
    <!--
    function getConfirmResult(){
        var result = confirm("사용자를 정말로 삭제하시겠습니까?");
        return result;
    }
    -->
</script>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>사용자 제거 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <%-- 사용자 추가때와 동일하므로 같은 메뉴 사용함. --%>
            <jsp:include page="sidebar_admin_previous_menu.jsp" />
        </div>

        <div id="main">
            <h2> 삭제할 사용자를 선택해 주세요. </h2> <br>

            <%
                String cwd =  this.getServletContext().getRealPath(".");
            %>
            <jsp:setProperty name="menuagent" property="cwd" value="<%=cwd%>"/>
            <jsp:setProperty name="menuagent" property="server" value="localhost"/>
            <jsp:setProperty name="menuagent" property="port" value="4555"/>
        
            <form name="DeleteUser" action="UserAdmin.do?menu=<%=CommandType.DELETE_USER_COMMAND%>"
                  method="POST">
                <%menuagent.setUserListinAgent();%>
                
                <c:forEach var="userId" items="${menuagent.userList}">
                    <input type=checkbox name="selectedUsers" value="${userId}"/>
                    <c:out value="${userId}"/><br/>
                </c:forEach>
                
                <br>
                <input type="submit" value="제거" name="delete_command" onClick ="return getConfirmResult()"/>
                <input type="reset" value="선택 전부 취소" />
            </form>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
