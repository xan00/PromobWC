package za.co.rdata.r_datamobile.Models;

/**
 * Created by James de Scande on 28/06/2017.
 */

public class model_pro_ar_cond {

    private String InstNode_id;
    private String mobnode_id;
    private String cond_code;
    private String cond_desc;


    public model_pro_ar_cond(String instNode_id, String mobnode_id, String cond_code, String cond_desc) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.cond_code = cond_code;
        this.cond_desc = cond_desc;
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

    public String getCond_code() {
        return cond_code;
    }

    public void setCond_code(String cond_code) {
        this.cond_code = cond_code;
    }

    public String getCond_desc() {
        return cond_desc;
    }

    public void setCond_desc(String cond_desc) {
        this.cond_desc = cond_desc;
    }
}
