package za.co.rdata.r_datamobile.Models;

import java.util.ArrayList;
import java.util.Date;

public class model_pro_hr_leavereq {

    public model_pro_hr_leavereq(String instNode_id, String mobnode_id, int leave_request_id, String employee_id, int leave_type, String leave_date_from, double leave_count_requested, String leave_reason, String date_created, int approved, String reject_reason, String date_of_approval) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.leave_request_id = leave_request_id;
        this.employee_id = employee_id;
        this.leave_type = leave_type;
        this.leave_date_from = leave_date_from;
        this.leave_count_requested = leave_count_requested;
        this.leave_reason = leave_reason;
        this.date_created = date_created;
        this.approved = approved;
        this.reject_reason = reject_reason;
        this.date_of_approval = date_of_approval;
    }

    private String InstNode_id;
    private String mobnode_id;

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

    public int getLeave_request_id() {
        return leave_request_id;
    }

    public void setLeave_request_id(int leave_request_id) {
        this.leave_request_id = leave_request_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public int getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(int leave_type) {
        this.leave_type = leave_type;
    }

    public String getLeave_date_from() {
        return leave_date_from;
    }

    public void setLeave_date_from(String leave_date_from) {
        this.leave_date_from = leave_date_from;
    }

    public double getLeave_count_requested() {
        return leave_count_requested;
    }

    public void setLeave_count_requested(double leave_count_requested) {
        this.leave_count_requested = leave_count_requested;
    }

    public String getLeave_reason() {
        return leave_reason;
    }

    public void setLeave_reason(String leave_reason) {
        this.leave_reason = leave_reason;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public String getDate_of_approval() {
        return date_of_approval;
    }

    public void setDate_of_approval(String date_of_approval) {
        this.date_of_approval = date_of_approval;
    }

    private int leave_request_id;
    private String employee_id;
    private int leave_type;
    private String leave_date_from;
    private double leave_count_requested;
    private String leave_reason;
    private String date_created;
    private int approved;
    private String reject_reason;
    private String date_of_approval;

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(leave_request_id);
        objectArrayList.add(employee_id);
        objectArrayList.add(leave_type);
        objectArrayList.add(leave_date_from);
        objectArrayList.add(leave_count_requested);
        objectArrayList.add(leave_reason);
        objectArrayList.add(date_created);
        objectArrayList.add(approved);
        objectArrayList.add(reject_reason);
        objectArrayList.add(date_of_approval);
        return objectArrayList;
    }
}
