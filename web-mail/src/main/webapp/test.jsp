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
                String to = null;
                String cc = null;
                String subj = null;
                String text = null;

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mail?serverTimezone=UTC", "root", "1463");

                    if (conn == null) {
                        throw new Exception("DB Connect Fail");
                    }
                    stmt = conn.createStatement();
                    //복호화
                    ResultSet decrypt_rs = stmt.executeQuery("SELECT "
                            + "CAST(AES_DECRYPT(UNHEX(test_to), 'to') AS CHAR), "
                            + "CAST(AES_DECRYPT(UNHEX(test_cc), 'cc') AS CHAR), "
                            + "CAST(AES_DECRYPT(UNHEX(test_subj), 'subj') AS CHAR), "
                            + "CAST(AES_DECRYPT(UNHEX(test_text), 'text') AS CHAR) FROM test;");

                    while (decrypt_rs.next()) {
                        to = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(test_to), 'to') as char)");
                        cc = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(test_cc), 'cc') as char)");
                        subj = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(test_subj), 'subj') as char)");
                        text = decrypt_rs.getString("CAST(AES_DECRYPT(UNHEX(test_text), 'text') as char)");%>
                        
            <form method="POST" action="write_mail.jsp">
                수신자 : <input type="text" name="to" value="<%=to == null ? "" : to%>" readonly style="background-color:transparent;border:0 solid black;text-align:center;width:100px;"> &nbsp;
                참조 : <input type="text" name="cc" value="<%=cc == null ? "" : cc%>" readonly style="background-color:transparent;border:0 solid black;text-align:center;width:100px;"> &nbsp;
                메일 제목 : <input type="text" name="subj" value="<%=subj == null ? "" : subj%>" readonly style="background-color:transparent;border:0 solid black;text-align:center;width:100px;"> &nbsp;
                본문 : <input type="text" name="text" value="<%=text == null ? "" : text%>" readonly style="background-color:transparent;border:0 solid black;text-align:center;width:100px;"> &nbsp;
                <input type="submit" value="수정">
            </form>
                
            <%}
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
