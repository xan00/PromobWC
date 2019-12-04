package za.co.rdata.r_datamobile.Models;

/**
 * Project: R-DataMobile
 * Created by wcrous on 25/01/2016.
 */
public class model_pro_mr_no_access {
    private String InstNode_id;
    private String mobnode_id;
    private String na_code;
    private String na_description;

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

    public String getNa_code() {
        return na_code;
    }

    public void setNa_code(String na_code) {
        this.na_code = na_code;
    }

    public String getNa_description() {
        return na_description;
    }

    public void setNa_description(String na_description) {
        this.na_description = na_description;
    }

    public model_pro_mr_no_access(String instNode_id, String mobnode_id, String na_code, String na_description) {

        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.na_code = na_code;
        this.na_description = na_description;
    }
}
