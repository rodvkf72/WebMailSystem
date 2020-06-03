/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.maven_webmail.model;

import java.util.List;

/**
 *
 * @author 김희정
 */
public class AdminListBean {
    String server;
    int port;
    String cwd;
    //UserAdminAgent agent;
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
/*
    public UserAdminAgent getAgent() {
        return agent;
    }

    public void setAgent(UserAdminAgent agent) {
        this.agent = agent;
    }
*/
    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
    
    public void setUserListinAgent() throws Exception{
        UserAdminAgent agent = new UserAdminAgent(server, port, cwd);
        userList = agent.getUserList();
    }
    
}
