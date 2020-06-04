package za.co.rdata.r_datamobile.Models;

import java.util.ArrayList;

public class model_pro_sys_devices {
    private String InstNode_id;

    public model_pro_sys_devices(String instNode_id, String mobnode_id, String device_id, String description,
                                 String device_type, String serial_number, String status, String device_guid,
                                 String device_ip, String soft_version, String device_enable, double device_current_lat,
                                 double device_current_long, String device_loc_last_update) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.device_id = device_id;
        this.description = description;
        this.device_type = device_type;
        this.serial_number = serial_number;
        this.status = status;
        this.device_guid = device_guid;
        this.device_ip = device_ip;
        this.soft_version = soft_version;
        this.device_enable = device_enable;
        this.device_current_lat = device_current_lat;
        this.device_current_long = device_current_long;
        this.device_loc_last_update = device_loc_last_update;
    }

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(device_id);
        objectArrayList.add(description);
        objectArrayList.add(device_type);
        objectArrayList.add(serial_number);
        objectArrayList.add(status);
        objectArrayList.add(device_guid);
        objectArrayList.add(device_ip);
        objectArrayList.add(soft_version);
        objectArrayList.add(device_enable);
        objectArrayList.add(device_current_lat);
        objectArrayList.add(device_current_long);
        objectArrayList.add(device_loc_last_update);

        return objectArrayList;
    }

    private String mobnode_id;
    private String device_id;

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

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDevice_guid() {
        return device_guid;
    }

    public void setDevice_guid(String device_guid) {
        this.device_guid = device_guid;
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public String getSoft_version() {
        return soft_version;
    }

    public void setSoft_version(String soft_version) {
        this.soft_version = soft_version;
    }

    public String getDevice_enable() {
        return device_enable;
    }

    public void setDevice_enable(String device_enable) {
        this.device_enable = device_enable;
    }

    public double getDevice_current_lat() {
        return device_current_lat;
    }

    public void setDevice_current_lat(double device_current_lat) {
        this.device_current_lat = device_current_lat;
    }

    public double getDevice_current_long() {
        return device_current_long;
    }

    public void setDevice_current_long(double device_current_long) {
        this.device_current_long = device_current_long;
    }

    public String getDevice_loc_last_update() {
        return device_loc_last_update;
    }

    public void setDevice_loc_last_update(String device_loc_last_update) {
        this.device_loc_last_update = device_loc_last_update;
    }

    private String description;
    private String device_type;
    private String serial_number;
    private String status;
    private String device_guid;
    private String device_ip;
    private String soft_version;
    private String device_enable;
    private double device_current_lat;
    private double device_current_long;
    private String device_loc_last_update;
}
