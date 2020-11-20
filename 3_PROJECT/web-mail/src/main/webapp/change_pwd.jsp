<%-- 
    Document   : change_pwd
    Created on : 2020. 5. 27., 오후 4:28:19
    Author     : miso
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>비밀번호를 변경해 주세요</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
     
    <body>
        <jsp:include page="header.jsp" />
        <h1>마지막으로 비밀번호를 변경한지 3개월이 경과하였습니다.<br>
            비밀번호를 변경해 주세요<br>
        </h1>
            <div id="main">
            새 비밀번호를 입력해 주세요<br><br>
            <% String userName = (String) session.getAttribute("userid");
                out.print("유저 이름 :" + userName );
            %>

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
                            //비밀번호 변경하지 않기 눌러서 main_menu가는 버튼 생성
                        </td>
                    </tr>
                </table>

            </form>
        </div>
        
        <jsp:include page="footer.jsp" />
    </body>
</html>
