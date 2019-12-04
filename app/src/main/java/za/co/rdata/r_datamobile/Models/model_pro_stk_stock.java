package za.co.rdata.r_datamobile.Models;

/**
 * Project: Promun
 * Created by wcrous on 03/08/2016.
 */
public class model_pro_stk_stock {
    private String InstNode_id;
    private String mobnode_id;
    private String whse_code;
    private String stk_code;
    private String stk_descrip;
    private String stk_bin;
    private Integer stk_qty;
    private Integer stk_reorder;
    private Integer stk_max_level;
    private Double stk_cost;
    private String stk_fuel;
    private String stk_category;
    private String stk_unit_desc;
    private Integer stk_display_qty;

    public model_pro_stk_stock(String instNode_id, String mobnode_id, String stk_bin, String stk_category, String stk_code, Double stk_cost, String stk_descrip, Integer stk_display_qty, String stk_fuel, Integer stk_max_level, Integer stk_qty, Integer stk_reorder, String stk_unit_desc, String whse_code) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.stk_bin = stk_bin;
        this.stk_category = stk_category;
        this.stk_code = stk_code;
        this.stk_cost = stk_cost;
        this.stk_descrip = stk_descrip;
        this.stk_display_qty = stk_display_qty;
        this.stk_fuel = stk_fuel;
        this.stk_max_level = stk_max_level;
        this.stk_qty = stk_qty;
        this.stk_reorder = stk_reorder;
        this.stk_unit_desc = stk_unit_desc;
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

    public String getStk_category() {
        return stk_category;
    }

    public void setStk_category(String stk_category) {
        this.stk_category = stk_category;
    }

    public String getStk_code() {
        return stk_code;
    }

    public void setStk_code(String stk_code) {
        this.stk_code = stk_code;
    }

    public Double getStk_cost() {
        return stk_cost;
    }

    public void setStk_cost(Double stk_cost) {
        this.stk_cost = stk_cost;
    }

    public String getStk_descrip() {
        return stk_descrip;
    }

    public void setStk_descrip(String stk_descrip) {
        this.stk_descrip = stk_descrip;
    }

    public Integer getStk_display_qty() {
        return stk_display_qty;
    }

    public void setStk_display_qty(Integer stk_display_qty) {
        this.stk_display_qty = stk_display_qty;
    }

    public String getStk_fuel() {
        return stk_fuel;
    }

    public void setStk_fuel(String stk_fuel) {
        this.stk_fuel = stk_fuel;
    }

    public Integer getStk_max_level() {
        return stk_max_level;
    }

    public void setStk_max_level(Integer stk_max_level) {
        this.stk_max_level = stk_max_level;
    }

    public Integer getStk_qty() {
        return stk_qty;
    }

    public void setStk_qty(Integer stk_qty) {
        this.stk_qty = stk_qty;
    }

    public Integer getStk_reorder() {
        return stk_reorder;
    }

    public void setStk_reorder(Integer stk_reorder) {
        this.stk_reorder = stk_reorder;
    }

    public String getStk_unit_desc() {
        return stk_unit_desc;
    }

    public void setStk_unit_desc(String stk_unit_desc) {
        this.stk_unit_desc = stk_unit_desc;
    }

    public String getWhse_code() {
        return whse_code;
    }

    public void setWhse_code(String whse_code) {
        this.whse_code = whse_code;
    }
}
