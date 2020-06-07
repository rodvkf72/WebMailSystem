/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import java.util.List;

/**
 * admin_menu.jsp, delete_user.jsp에서
 * 자바 코드로 작성된 부분이 많은 문제가 있어서
 * 빈즈 밋 jstl로 구현을 변경하였습니다. 
 * 
 * @author 김희정
 */
public class AdminListBean {
    String server;
    int port;
    String cwd;
    
    List<String> userList; 

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCwd() {
        return cwd;
    }

    public void setCwd(String cwd) {
        this.cwd = cwd;
    }
    
    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
    
    /**
     * admin계정 로그인 시 확인할 수 있는 유저 리스트를 출력합니다. 
     * 
     * @throws Exception 
     */
    public void setUserListinAgent() throws Exception{
        UserAdminAgent agent = new UserAdminAgent(server, port, cwd);
        userList = agent.getUserList();
    }
    
}
