package za.co.rdata.r_datamobile.Models;

/**
 * Project: Promun
 * Created by wcrous on 06/02/2016.
 */
public class model_pro_sys_menu {
    private String InstNode_id;
    private String mobnode_id;
    private String module;
    private String user;
    private String mod_desc;

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

    public String getMod_desc() {
        return mod_desc;
    }

    public void setMod_desc(String mod_desc) {
        this.mod_desc = mod_desc;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public model_pro_sys_menu(String instNode_id, String mobnode_id, String mod_desc, String module, String user) {

        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.mod_desc = mod_desc;
        this.module = module;
        this.user = user;
    }
}
