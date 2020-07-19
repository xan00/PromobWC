package za.co.rdata.r_datamobile.Models;

import java.util.ArrayList;

/**
 * Project: Promun
 * Created by wcrous on 03/08/2016.
 */
public class model_pro_stk_basket {
    private String InstNode_id;
    private String mobnode_id;
    private int whse_code;
    private String whse_description;

    public model_pro_stk_basket(String instNode_id, String mobnode_id, int whse_code, String whse_description) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.whse_code = whse_code;
        this.whse_description = whse_description;
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

    public int getWhse_code() {
        return whse_code;
    }

    public void setWhse_code(int whse_code) {
        this.whse_code = whse_code;
    }

    public String getWhse_description() {
        return whse_description;
    }

    public void setWhse_description(String whse_description) {
        this.whse_description = whse_description;
    }

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(whse_code);
        objectArrayList.add(whse_description);
        return objectArrayList;
    }
}
