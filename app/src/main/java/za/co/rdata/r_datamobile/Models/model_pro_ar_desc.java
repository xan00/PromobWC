package za.co.rdata.r_datamobile.Models;

/**
 * Created by James de Scande on 31/10/2017 at 03:33.
 */

public class model_pro_ar_desc {

    private String InstNode_id;
    private String mobnode_id;
    private String desc_code;
    private String desc_desc;

    public model_pro_ar_desc(String instNode_id, String mobnode_id, String desc_code, String desc_desc) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.desc_code = desc_code;
        this.desc_desc = desc_desc;
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

    public String getDesc_code() {
        return desc_code;
    }

    public void setDesc_code(String desc_code) {
        this.desc_code = desc_code;
    }

    public String getDesc_desc() {
        return desc_desc;
    }

    public void setDesc_desc(String desc_desc) {
        this.desc_desc = desc_desc;
    }


}
