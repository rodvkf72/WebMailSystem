<%-- 
    Document   : write_mail.jsp
    Author     : jongmin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cse.maven_webmail.control.CommandType" %>
<%@page import="java.sql.*"%>

<!DOCTYPE html>
<% request.setCharacterEncoding("UTF-8"); %>
<%-- @taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" --%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>메일 쓰기 화면</title>
        <link type="text/css" rel="stylesheet" href="css/main_style.css" />
        
    </head>
    <body>
        <jsp:include page="header.jsp" />
        
        <div id="sidebar">
            <jsp:include page="sidebar_previous_menu.jsp" />
        </div>

        <div id="main">
            <%-- <jsp:include page="mail_send_form.jsp" /> --%>

            <form enctype="multipart/form-data" method="POST" name="frm" onsubmit="return uploadFile()"
                  action="WriteMail.do?menu=<%= CommandType.SEND_MAIL_COMMAND%>">
                <table>
                    <tr>
                        <input type="text" name="number" value="<%=request.getParameter("number") == null ? "" : request.getParameter("number")%>" hidden>
                        <td> 수신 </td>
                    
                        <td> <input type="text" name="to" size="80" required
                                    value=<%=request.getParameter("to") == null ? "" : request.getParameter("to")%>>  </td>
                        <!--
                        -- 이거 userid로 하니까 임시보관함 수정 기능에 null값 들어가요..
                        -- 기존에 "to" 말고 "recv" 라고 적혀 있었는데 왜 있었는지 모르겠음
                        -- 요청값을 받는 부분이라 수정해도 전송하는데 이상 없을거라 생각하고 바꾸고 테스트 한 결과 이상 없음
                        -->
                    </tr>
                    <tr>
                        <td>참조</td>
                        <td> <input type="text" name="cc" size="80" value="<%=request.getParameter("cc") == null ? "" : request.getParameter("cc")%>">  </td>
                    </tr>
                    <tr>
                        <td> 메일 제목 </td>
                        <td> <input type="text" name="subj" size="80" value="<%=request.getParameter("subj") == null ? "" : request.getParameter("subj")%>">  </td>
                    </tr>
                    <tr>
                        <td colspan="2">본  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 문</td>
                    </tr>
                    <tr>  <%-- TextArea    --%>
                        <td colspan="2">  <textarea rows="15" name="body" cols="80"><%=request.getParameter("text") == null ? "" : request.getParameter("text")%></textarea> </td>
                    </tr>
                    
                    <tr>
                        <td>첨부 파일</td>
                        <td> <input type="file" id="file1" name="file1" size="40"> </br>
                             <input type="file" id="file2" name="file2" size="40"> </td>
                    </tr>
                    <input type="text" name="temp" id="temp" value="<%=request.getParameter("temporary") == null ? "" : request.getParameter("temporary")%>" hidden>
                    <tr>
                        <td colspan="2">
                            <input name="sbt" type="submit" value="메일 보내기" onclick="submitForm();">
                            <input type="reset" value="다시 입력">
                        </td>
                    </tr>
                </table>
            </form>

            <iframe name='ifrm' width='0' height='0' frameborder='0'></iframe>  
            <script type="text/javascript">
                function f_submit() {
                    /*
                     * form 에서의 multipart/form-data 때문에 test2.jsp 파일에서
                     * request.getParameter를 null로 받는 것을 방지
                     */
                    document.frm.encoding = "application/x-www-form-urlencoded";
                    //target을 iframe으로 하여 현재 폼의 submit을 페이지 변환 없이 값 전달
                    document.frm.target = 'ifrm';
                    //폼의 액션을 test2.jsp로 재지정
                    document.frm.action = 'TemporaryHandler.do';
                    //액션 위치가 변환된 폼 submit
                    document.frm.submit();
                }
                
                //전송 버튼을 눌렀을 때는 아래의 window.onbeforeunload가 사용되지 않도록 하기 위한 변수
                var submitted = true;

                //메일 보내기 버튼 클릭 시 submitted를 false로 전환하여 window.onbeforeunload가 실행되지 않도록 함
                function submitForm() {
                    submitted = false;
                }
                
                //페이지 전환 시 나갈 것인 지를 확인하는 문구를 출력
                window.onbeforeunload = function () {
                    if (submitted) {
                        f_submit();
                        /*
                         * Internet Explorer에서는 아래의 return에 작성한 값이 문구로 출력되지만
                         * Chrome 브라우저에서는 return에 작성한 값이 출력되지 않음.
                         * 찾아본 결과 브라우저의 차이라고 함.
                         */
                        return 'abc';
                    }
                }
                
                function uploadFile(){
                    var size_limit = 20 * 1000 * 1000;
                    //if(document.frm.file1.values < 1){
                    //    alert("선택된 이미지가 없습니다.");
                    //    return;
                    //}
                    var file = document.getElementById('file1').files[0];
                    var fileName = file.name;
                    console.log(fileName);     
                    
                    if(fileName.length != 0){
                        if(fileName.length > 40){
                            alert("파일 이름은 40자를 넘을 수 없습니다.");
                            //file.value = "";
                            return false;
                        }
                    }
                    
                    if(file.size() > size_limit){
                        alert('Cannot upload the file because of FILE SIZE > 20MB');
                        //file.value = "";
                        return false;
                    }
                    
                    return true;
                    
                }
            </script>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
