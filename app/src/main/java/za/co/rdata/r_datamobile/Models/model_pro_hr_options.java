package za.co.rdata.r_datamobile.Models;

import java.util.ArrayList;

public class model_pro_hr_options {
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

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    private String InstNode_id;
    private String mobnode_id;
    private String menu;
    private String desc;
    private String mod;

    public model_pro_hr_options(String instNode_id, String mobnode_id, String menu, String desc, String mod) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.menu = menu;
        this.desc = desc;
        this.mod = mod;
    }

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(menu);
        objectArrayList.add(desc);
        objectArrayList.add(mod);
        return objectArrayList;
    }

}
