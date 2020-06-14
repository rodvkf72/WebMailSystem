<%-- 
    Document   : nullError
    Created on : 2020 Jun 13, 17:39:58
    Author     : 김희정
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true"%>
<meta http-equiv="refresh" content="5; url=index.jsp">
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>로그인 시간 초과</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>

    <body>
        서버에 접속할 수 없습니다. <br/>
        로그인 사용 기간이 경과하였거나, 서버에 문제가 있습니다.<br/>
        메인 메뉴에서 다시 로그인을 해주세요. <br/>
        계속해서 문제가 발생한다면 관리자에게 연락해주세요. <br/>
        이 페이지는 5초 뒤에 메인 메뉴로 이동합니다. <br/>
        <br/>
        <%session.invalidate();%>

        <a href="index.jsp">메인메뉴 가기</a>
    </body>
</html>
