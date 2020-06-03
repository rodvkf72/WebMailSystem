<%-- 
    Document   : signup
    Created on : 2020. 5. 11., 오전 12:13:59
    Author     : rodvk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>회원가입 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>
    <body>
        <%@include file="header.jspf"%>
        <div id="login_form">
            <form name="AddUser" action="UserAdmin.do?menu=<%= CommandType.ADD_USER_COMMAND%>"
                  method="POST">
                <p>
                    <strong>사&nbsp;&nbsp;&nbsp;&nbsp;용&nbsp;&nbsp;&nbsp;&nbsp;자 : </strong>
                    <input type=""text" name="id" placeholder="사용자 입력">
                </p>
                <p>
                    <strong>비&nbsp;&nbsp;밀&nbsp;&nbsp;번&nbsp;&nbsp;호 : </strong>
                    <input type="password" name="password" placeholder="비밀번호 입력" id="pwd" onchange="SameCheck()">
                </p>
                <p>
                    <strong>비밀번호 확인 : </strong>
                    <input type="password" placeholder="비밀번호 재입력" id="re_pwd" onchange="SameCheck()">
                    <br>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="same"></span>
                </p>
                <br/>
                <input type="submit" value="회원가입" name="register">&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="reset" value="다시 입력" name="re_input">&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="취소" name="cancel" onclick="location = 'index.jsp'">
            </form>

            <script type="text/javascript">
                function SameCheck(){
                    var pwd = document.getElementById("pwd").value;
                    var re_pwd = document.getElementById("re_pwd").value;
                
                    if ((pwd != '') && (re_pwd != '')){
                        if (pwd == re_pwd){
                            document.getElementById("same").innerHTML="비밀번호가 일치합니다.";
                            document.getElementById("same").style.color="blue";
                            return true;
                        } else {
                            document.getElementById("same").innerHTML="비밀번호가 일치하지 않습니다.";
                            document.getElementById("same").style.color="red";
                            return false;
                        }
                    } else {
                        return false;
                    }                   
                }
            </script>
        </div>
        <%@include file="footer.jspf"%>
    </body>
</html>
