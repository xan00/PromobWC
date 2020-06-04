package za.co.rdata.r_datamobile.Models;

/**
 * Created by Dev on 02/02/2016.
 */
public class model_pro_sys_users {
    private String InstNode_id;
    private String mobnode_id;
    private String username;
    private String password;
    private String FullName;
    private String status;
    private String LastLogin;
    private int LoginTimes;

    public model_pro_sys_users(String instNode_id, String mobnode_id, String username, String password, String fullName,
                               String status, String lastLogin, int loginTimes) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.username = username;
        this.password = password;
        FullName = fullName;
        this.status = status;
        LastLogin = lastLogin;
        LoginTimes = loginTimes;
    }

    public String getFullName() {

        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getInstNode_id() {
        return InstNode_id;
    }

    public void setInstNode_id(String instNode_id) {
        InstNode_id = instNode_id;
    }

    public String getMobnode_id() {
        return mobnode_id;
    }

    public void setMobnode_id(String mobnode_id) {
        this.mobnode_id = mobnode_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastLogin() {
        return LastLogin;
    }

    public void setLastLogin(String lastLogin) {
        LastLogin = lastLogin;
    }

    public int getLoginTimes() {
        return LoginTimes;
    }

    public void setLoginTimes(int loginTimes) {
        LoginTimes = loginTimes;
    }
}
