package za.co.rdata.r_datamobile.Models;

/**
 * Created by James de Scande on 31/05/2017.
 */

public class model_pro_ar_asset_rows {

    private String InstNode_id;                     //1
    private String mobnode_id;                      //2
    private String reg_barcode;                     //3
    private String reg_asset_code;                  //4
    private String reg_asset_desc;                  //5
    private String reg_location_code;               //6
    private String reg_dept_code;                   //7
    private String reg_condition_code;              //8
    private String reg_route_nr;                    //9

    /*
    public model_pro_ar_asset_rows(String instNode_id, String mobnode_id, String reg_barcode, String reg_asset_code, String reg_asset_desc, String reg_location_code, String reg_dept_code, String reg_condition_code, String reg_route_nr, String reg_asset_serial_nr, String reg_useful_life, String reg_useful_remainder, String reg_comments1, String reg_comments2, String reg_comments3, Boolean isActive, Boolean isManual, int i, int reg_seq) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.reg_barcode = reg_barcode;
        this.reg_asset_code = reg_asset_code;
        this.reg_asset_desc = reg_asset_desc;
        this.reg_location_code = reg_location_code;
        this.reg_dept_code = reg_dept_code;
        this.reg_condition_code = reg_condition_code;
        this.reg_route_nr = reg_route_nr;
        this.reg_asset_serial_nr = reg_asset_serial_nr;
        this.reg_useful_life = reg_useful_life;
        this.reg_useful_remainder = reg_useful_remainder;
        this.reg_comments1 = reg_comments1;
        this.reg_comments2 = reg_comments2;
        this.reg_comments3 = reg_comments3;
        this.isActive = isActive;
        this.isManual = isManual;
        this.reg_seq = reg_seq;
    }
*/

    private String reg_asset_serial_nr;             //10
    private String reg_useful_life;                 //11
    private String reg_useful_remainder;            //12
    private String reg_comments1;                   //13
    private String reg_comments2;                   //14
    private String reg_comments3;                   //15
    private Boolean isActive;                       //16
    private Boolean isManual;                       //17
    private Boolean reg_investigate;                //18
    private int reg_qtyformanual;                   //19
    private int reg_seq;                            //20

    public model_pro_ar_asset_rows(String instNode_id, String mobnode_id, String reg_barcode, String reg_asset_code, String reg_asset_desc, String reg_location_code, String reg_dept_code, String reg_condition_code, String reg_route_nr, String reg_asset_serial_nr, String reg_useful_life, String reg_useful_remainder, String reg_comments1, String reg_comments2, String reg_comments3, Boolean isActive, int reg_qtyformanual, int reg_seq) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.reg_barcode = reg_barcode;
        this.reg_asset_code = reg_asset_code;
        this.reg_asset_desc = reg_asset_desc;
        this.reg_location_code = reg_location_code;
        this.reg_dept_code = reg_dept_code;
        this.reg_condition_code = reg_condition_code;
        this.reg_route_nr = reg_route_nr;
        this.reg_asset_serial_nr = reg_asset_serial_nr;
        this.reg_useful_life = reg_useful_life;
        this.reg_useful_remainder = reg_useful_remainder;
        this.reg_comments1 = reg_comments1;
        this.reg_comments2 = reg_comments2;
        this.reg_comments3 = reg_comments3;
        this.isActive = isActive;
        //this.isManual = isManual;
        //this.reg_investigate = reg_investigate;
        this.reg_qtyformanual = reg_qtyformanual;
        this.reg_seq = reg_seq;
    }

    public int getReg_qtyformanual() {
        return reg_qtyformanual;
    }

    public void setReg_qtyformanual(int reg_qtyformanual) {
        this.reg_qtyformanual = reg_qtyformanual;
    }

    public Boolean getReg_investigate() {
        return reg_investigate;
    }

    public void setReg_investigate(Boolean reg_investigate) {
        this.reg_investigate = reg_investigate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getManual() {
        return isManual;
    }

    public void setManual(Boolean manual) {
        isManual = manual;
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

    public String getReg_barcode() {
        return reg_barcode;
    }

    public void setReg_barcode(String reg_barcode) {
        this.reg_barcode = reg_barcode;
    }

    public String getReg_asset_code() {
        return reg_asset_code;
    }

    public void setReg_asset_code(String reg_asset_code) {
        this.reg_asset_code = reg_asset_code;
    }

    public String getReg_asset_desc() {
        return reg_asset_desc;
    }

    public void setReg_asset_desc(String reg_asset_desc) {
        this.reg_asset_desc = reg_asset_desc;
    }

    public String getReg_location_code() {
        return reg_location_code;
    }

    public void setReg_location_code(String reg_location_code) {
        this.reg_location_code = reg_location_code;
    }

    public String getReg_dept_code() {
        return reg_dept_code;
    }

    public void setReg_dept_code(String reg_dept_code) {
        this.reg_dept_code = reg_dept_code;
    }

    public String getReg_condition_code() {
        return reg_condition_code;
    }

    public void setReg_condition_code(String reg_condition_code) {
        this.reg_condition_code = reg_condition_code;
    }

    public String getReg_route_nr() {
        return reg_route_nr;
    }

    public void setReg_route_nr(String reg_route_nr) {
        this.reg_route_nr = reg_route_nr;
    }

    public String getReg_asset_serial_nr() {
        return reg_asset_serial_nr;
    }

    public void setReg_asset_serial_nr(String reg_asset_serial_nr) {
        this.reg_asset_serial_nr = reg_asset_serial_nr;
    }

    public String getReg_useful_life() {
        return reg_useful_life;
    }

    public void setReg_useful_life(String reg_useful_life) {
        this.reg_useful_life = reg_useful_life;
    }

    public String getReg_useful_remainder() {
        return reg_useful_remainder;
    }

    public void setReg_useful_remainder(String reg_useful_remainder) {
        this.reg_useful_remainder = reg_useful_remainder;
    }

    public String getReg_comments1() {
        return reg_comments1;
    }

    public void setReg_comments1(String reg_comments1) {
        this.reg_comments1 = reg_comments1;
    }

    public String getReg_comments2() {
        return reg_comments2;
    }

    public void setReg_comments2(String reg_comments2) {
        this.reg_comments2 = reg_comments2;
    }

    public String getReg_comments3() {
        return reg_comments3;
    }

    public void setReg_comments3(String reg_comments3) {
        this.reg_comments3 = reg_comments3;
    }

    public int getReg_seq() {
        return reg_seq;
    }

    public void setReg_seq(int reg_seq) {
        this.reg_seq = reg_seq;
    }
}
