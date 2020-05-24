<%-- 
    Document   : main_menu.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>임시 보관함</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
    </head>

    <body>
        <jsp:include page="header.jsp" />

        <div id="sidebar">
            <jsp:include page="sidebar_menu.jsp" />
        </div>

        <div id="main">
            <%
                Connection conn = null;
                Statement stmt = null;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mail?serverTimezone=UTC", "root", "1463");

                    if (conn == null) {
                        throw new Exception("DB Connect Fail");
                    }
                    stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from test");
                    while (rs.next()) {
                        String to = rs.getString("test_to");
                        String cc = rs.getString("test_cc");
                        String subj = rs.getString("test_subj");
                        String text = rs.getString("test_text");
                        out.println("<br> 수신자 : " + to + "<br> 참조 : " + cc + "<br> 메일 제목 : " + subj + "<br> 본문 : " + text);
                    }
                } finally {
                    try {
                        stmt.close();
                    } catch (Exception ignored) {

                    }
                    try {
                        conn.close();
                    } catch (Exception ignored) {

                    }
                }
            %>
        </div>

        <jsp:include page="footer.jsp" />

    </body>
</html>
