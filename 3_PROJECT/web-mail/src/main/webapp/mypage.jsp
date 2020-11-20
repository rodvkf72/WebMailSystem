<%-- 
    Document   : mypage
    Created on : 2020. 5. 28., 오후 3:17:09
    Author     : miso
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>

<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>마이 페이지</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <jsp:include page="sidebar_previous_menu.jsp" />
        </div>

        <div id="main">
            비밀번호 변경을 할 수 있습니다.  <br>
            새 비밀번호를 입력해 주세요<br><br>

            <form name="changeInfo" action="UserAdmin.do?menu=<%= CommandType.CHANGE_USER_PWD%>"
                  method="POST">
                
                <table border="0" align="left">
                    <tr>
                        <td>현재 비밀번호 </td>
                        <td> <input type="password" name="oldpassword" value="" /> </td>
                    </tr>
                    <tr>
                        <td>새 비밀번호 </td>
                        <td> <input type="password" name="newpassword" value="" /> </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="등록" name="register" />
                            <input type="reset" value="초기화" name="reset" />
                        </td>
                    </tr>
                </table>

            </form>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>