package za.co.rdata.r_datamobile.Models;

/**
 * Project: Promun
 * Created by wcrous on 03/08/2016.
 */
public class model_pro_stk_no_access {
    private String InstNode_id;
    private String mobnode_id;
    private String stk_na_code;
    private String stk_na_descriptionl;
    private String pro_stk_no_accesscol;

    public model_pro_stk_no_access(String instNode_id, String mobnode_id, String pro_stk_no_accesscol, String stk_na_code, String stk_na_descriptionl) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.pro_stk_no_accesscol = pro_stk_no_accesscol;
        this.stk_na_code = stk_na_code;
        this.stk_na_descriptionl = stk_na_descriptionl;
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

    public String getPro_stk_no_accesscol() {
        return pro_stk_no_accesscol;
    }

    public void setPro_stk_no_accesscol(String pro_stk_no_accesscol) {
        this.pro_stk_no_accesscol = pro_stk_no_accesscol;
    }

    public String getStk_na_code() {
        return stk_na_code;
    }

    public void setStk_na_code(String stk_na_code) {
        this.stk_na_code = stk_na_code;
    }

    public String getStk_na_descriptionl() {
        return stk_na_descriptionl;
    }

    public void setStk_na_descriptionl(String stk_na_descriptionl) {
        this.stk_na_descriptionl = stk_na_descriptionl;
    }
}
