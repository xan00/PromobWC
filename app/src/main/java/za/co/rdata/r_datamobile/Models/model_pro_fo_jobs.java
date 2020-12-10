package za.co.rdata.r_datamobile.Models;

import java.util.ArrayList;
import java.util.Date;

public class model_pro_fo_jobs {

    private String instnode_id;
    private String mobnode_id;

    private int jobnumber;
    private int jobtype;

    private String jobstatus;
    private String action_date;
    private String prop_detail;
    private double job_lat;
    private double job_lng;
    private int stk_basket_id;
    private String job_close_msg;
    private String creation_date;

    public model_pro_fo_jobs(String instnode_id, String mobnode_id, int jobnumber, int jobtype, String jobstatus, String action_date, String prop_detail, double job_lat, double job_lng, int stk_basket_id, String job_close_msg, String creation_date) {
        this.instnode_id = instnode_id;
        this.mobnode_id = mobnode_id;
        this.jobnumber = jobnumber;
        this.jobtype = jobtype;
        this.jobstatus = jobstatus;
        this.action_date = action_date;
        this.prop_detail = prop_detail;
        this.job_lat = job_lat;
        this.job_lng = job_lng;
        this.stk_basket_id = stk_basket_id;
        this.job_close_msg = job_close_msg;
        this.creation_date = creation_date;
    }

    public String getProp_detail() {
        return prop_detail;
    }

    public void setProp_detail(String prop_detail) {
        this.prop_detail = prop_detail;
    }

    public String getInstnode_id() {
        return instnode_id;
    }

    public void setInstnode_id(String instnode_id) {
        this.instnode_id = instnode_id;
    }

    public String getMobnode_id() {
        return mobnode_id;
    }

    public void setMobnode_id(String mobnode_id) {
        this.mobnode_id = mobnode_id;
    }

    public int getJobnumber() {
        return jobnumber;
    }

    public void setJobnumber(int jobnumber) {
        this.jobnumber = jobnumber;
    }

    public int getJobtype() {
        return jobtype;
    }

    public void setJobtype(int jobtype) {
        this.jobtype = jobtype;
    }

    public String getJobstatus() {
        return jobstatus;
    }

    public void setJobstatus(String jobstatus) {
        this.jobstatus = jobstatus;
    }

    public String getAction_date() {
        return action_date;
    }

    public void setAction_date(String action_date) {
        this.action_date = action_date;
    }

    public double getJob_lat() {
        return job_lat;
    }

    public void setJob_lat(double job_lat) {
        this.job_lat = job_lat;
    }

    public double getJob_lng() {
        return job_lng;
    }

    public void setJob_lng(double job_lng) {
        job_lng = job_lng;
    }

    public int getStk_basket_id() {
        return stk_basket_id;
    }

    public void setStk_basket_id(int stk_basket_id) {
        this.stk_basket_id = stk_basket_id;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(instnode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(jobnumber);
        objectArrayList.add(jobtype);

        objectArrayList.add(jobstatus);
        objectArrayList.add(action_date);
        objectArrayList.add(prop_detail);
        objectArrayList.add(job_lat);

        objectArrayList.add(job_lng);
        objectArrayList.add(stk_basket_id);
        objectArrayList.add(job_close_msg);
        objectArrayList.add(creation_date);

        return objectArrayList;
    }


    public String getJob_close_msg() {
        return job_close_msg;
    }

    public void setJob_close_msg(String job_close_msg) {
        this.job_close_msg = job_close_msg;
    }
}
