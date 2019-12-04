package za.co.rdata.r_datamobile.Models;

/**
 * Project: R-DataMobile
 * Created by wcrous on 25/01/2016.
 */
public class model_pro_mr_notes {
    private String InstNode_id;
    private String mobnode_id;
    private String note_code;
    private String note_description;

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

    public String getNote_code() {
        return note_code;
    }

    public void setNote_code(String note_code) {
        this.note_code = note_code;
    }

    public String getNote_description() {
        return note_description;
    }

    public void setNote_description(String note_description) {
        this.note_description = note_description;
    }

    public model_pro_mr_notes(String instNode_id, String mobnode_id, String note_code, String note_description) {

        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.note_code = note_code;
        this.note_description = note_description;
    }
}
