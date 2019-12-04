package za.co.rdata.r_datamobile.Models;

/**
 * Project: Promun
 * Created by wcrous on 03/08/2016.
 */
public class model_pro_stk_notes {
    private String InstNode_id;
    private String mobnode_id;
    private String pro_stk_no_code;
    private String pro_stk_no_description;

    public model_pro_stk_notes(String instNode_id, String mobnode_id, String pro_stk_no_code, String pro_stk_no_description) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.pro_stk_no_code = pro_stk_no_code;
        this.pro_stk_no_description = pro_stk_no_description;
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

    public String getPro_stk_no_code() {
        return pro_stk_no_code;
    }

    public void setPro_stk_no_code(String pro_stk_no_code) {
        this.pro_stk_no_code = pro_stk_no_code;
    }

    public String getPro_stk_no_description() {
        return pro_stk_no_description;
    }

    public void setPro_stk_no_description(String pro_stk_no_description) {
        this.pro_stk_no_description = pro_stk_no_description;
    }
}
