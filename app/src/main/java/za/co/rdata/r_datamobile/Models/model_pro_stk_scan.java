package za.co.rdata.r_datamobile.Models;

import java.util.ArrayList;

/**
 * Project: Promun
 * Created by wcrous on 03/08/2016.
 */
public class model_pro_stk_scan {
    private String InstNode_id;
    private String mobnode_id;
    private Integer stk_take_cycle;
    private String whse_code;
    private String stk_bin;
    private String stk_code;
    private String stk_bin_scan_type;
    private Integer stk_take_qty;
    private String stk_scan_date;
    private String stk_na_code;
    private String stk_note_code;
    private String stk_diff_reason;
    private String stk_comments;
    private Double stk_gps_master_long;
    private Double stk_gps_master_lat;
    private Double stk_gps_read_long;
    private Double stk_gps_read_lat;
    private String stk_user_code;
    private Integer stk_status;

    public model_pro_stk_scan(String instNode_id, String mobnode_id, Integer stk_take_cycle, String whse_code,
                              String stk_bin, String stk_code, String stk_bin_scan_type,
                              Integer stk_take_qty, String stk_scan_date, String stk_na_code, String stk_note_code,
                              String stk_diff_reason, String stk_comments,
                              Double stk_gps_master_lat, Double stk_gps_master_long, Double stk_gps_read_lat,
                              Double stk_gps_read_long,
                              String stk_user_code, Integer stk_status
                               ) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.stk_bin = stk_bin;
        this.stk_bin_scan_type = stk_bin_scan_type;
        this.stk_code = stk_code;
        this.stk_comments = stk_comments;
        this.stk_diff_reason = stk_diff_reason;
        this.stk_gps_master_lat = stk_gps_master_lat;
        this.stk_gps_master_long = stk_gps_master_long;
        this.stk_gps_read_lat = stk_gps_read_lat;
        this.stk_gps_read_long = stk_gps_read_long;
        this.stk_na_code = stk_na_code;
        this.stk_note_code = stk_note_code;
        this.stk_scan_date = stk_scan_date;
        this.stk_status = stk_status;
        this.stk_take_cycle = stk_take_cycle;
        this.stk_take_qty = stk_take_qty;
        this.stk_user_code = stk_user_code;
        this.whse_code = whse_code;
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

    public String getStk_bin() {
        return stk_bin;
    }

    public void setStk_bin(String stk_bin) {
        this.stk_bin = stk_bin;
    }

    public String getStk_bin_scan_type() {
        return stk_bin_scan_type;
    }

    public void setStk_bin_scan_type(String stk_bin_scan_type) {
        this.stk_bin_scan_type = stk_bin_scan_type;
    }

    public String getStk_code() {
        return stk_code;
    }

    public void setStk_code(String stk_code) {
        this.stk_code = stk_code;
    }

    public String getStk_comments() {
        return stk_comments;
    }

    public void setStk_comments(String stk_comments) {
        this.stk_comments = stk_comments;
    }

    public String getStk_diff_reason() {
        return stk_diff_reason;
    }

    public void setStk_diff_reason(String stk_diff_reason) {
        this.stk_diff_reason = stk_diff_reason;
    }

    public Double getStk_gps_master_lat() {
        return stk_gps_master_lat;
    }

    public void setStk_gps_master_lat(Double stk_gps_master_lat) {
        this.stk_gps_master_lat = stk_gps_master_lat;
    }

    public Double getStk_gps_master_long() {
        return stk_gps_master_long;
    }

    public void setStk_gps_master_long(Double stk_gps_master_long) {
        this.stk_gps_master_long = stk_gps_master_long;
    }

    public Double getStk_gps_read_lat() {
        return stk_gps_read_lat;
    }

    public void setStk_gps_read_lat(Double stk_gps_read_lat) {
        this.stk_gps_read_lat = stk_gps_read_lat;
    }

    public Double getStk_gps_read_long() {
        return stk_gps_read_long;
    }

    public void setStk_gps_read_long(Double stk_gps_read_long) {
        this.stk_gps_read_long = stk_gps_read_long;
    }

    public String getStk_na_code() {
        return stk_na_code;
    }

    public void setStk_na_code(String stk_na_code) {
        this.stk_na_code = stk_na_code;
    }

    public String getStk_note_code() {
        return stk_note_code;
    }

    public void setStk_note_code(String stk_note_code) {
        this.stk_note_code = stk_note_code;
    }

    public String getStk_scan_date() {
        return stk_scan_date;
    }

    public void setStk_scan_date(String stk_scan_date) {
        this.stk_scan_date = stk_scan_date;
    }

    public Integer getStk_status() {
        return stk_status;
    }

    public void setStk_status(Integer stk_status) {
        this.stk_status = stk_status;
    }

    public Integer getStk_take_cycle() {
        return stk_take_cycle;
    }

    public void setStk_take_cycle(Integer stk_take_cycle) {
        this.stk_take_cycle = stk_take_cycle;
    }

    public Integer getStk_take_qty() {
        return stk_take_qty;
    }

    public void setStk_take_qty(Integer stk_take_qty) {
        this.stk_take_qty = stk_take_qty;
    }

    public String getStk_user_code() {
        return stk_user_code;
    }

    public void setStk_user_code(String stk_user_code) {
        this.stk_user_code = stk_user_code;
    }

    public String getWhse_code() {
        return whse_code;
    }

    public void setWhse_code(String whse_code) {
        this.whse_code = whse_code;
    }

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(stk_take_cycle);
        objectArrayList.add(whse_code);
        objectArrayList.add(stk_bin);
        objectArrayList.add(stk_code);
        objectArrayList.add(stk_bin_scan_type);
        objectArrayList.add(stk_take_qty);
        objectArrayList.add(stk_scan_date);
        objectArrayList.add(stk_na_code);
        objectArrayList.add(stk_note_code);
        objectArrayList.add(stk_diff_reason);
        objectArrayList.add(stk_comments);
        objectArrayList.add(stk_gps_master_lat);
        objectArrayList.add(stk_gps_master_long);
        objectArrayList.add(stk_gps_read_lat);
        objectArrayList.add(stk_gps_read_long);
        objectArrayList.add(stk_user_code);
        objectArrayList.add(stk_status);
        return objectArrayList;
    }
}
