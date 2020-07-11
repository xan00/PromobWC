package za.co.rdata.r_datamobile.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Parcelable;
import android.os.Parcel;

public class model_pro_hr_leavereq implements Parcelable {

    public model_pro_hr_leavereq(String instNode_id, String mobnode_id, int leave_request_id, String employee_id,
                                 int leave_type, String leave_reason, String leave_date_from, int starthalf, String leave_date_to,
                                 int endhalf, double leave_count_requested, String date_created,
                                 int approved, String reject_reason, String date_of_approval, int isdeleted, String date_of_delete) {
        this.InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.leave_request_id = leave_request_id;
        this.employee_id = employee_id;
        this.leave_type = leave_type;
        this.leave_reason = leave_reason;
        this.leave_date_from = leave_date_from;
        this.starthalf = starthalf;
        this.leave_date_to = leave_date_to;
        this.endhalf = endhalf;
        this.leave_count_requested = leave_count_requested;
        this.date_created = date_created;
        this.approved = approved;
        this.reject_reason = reject_reason;
        this.date_of_approval = date_of_approval;
        this.isdeleted = isdeleted;
        this.date_of_delete = date_of_delete;
    }

    private String InstNode_id;
    private String mobnode_id;

    public static final Creator<model_pro_hr_leavereq> CREATOR = new Creator<model_pro_hr_leavereq>() {
        @Override
        public model_pro_hr_leavereq createFromParcel(Parcel in) {
            return new model_pro_hr_leavereq(in);
        }

        @Override
        public model_pro_hr_leavereq[] newArray(int size) {
            return new model_pro_hr_leavereq[size];
        }
    };

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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date c = Calendar.getInstance().getTime();
        String formattedDate = format.format(c);
        setDate_of_approval(formattedDate);
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

    private model_pro_hr_leavereq(Parcel in) {
        InstNode_id = in.readString();
        mobnode_id = in.readString();
        leave_request_id = in.readInt();
        employee_id = in.readString();
        leave_type = in.readInt();
        this.leave_reason = in.readString();
        this.leave_date_from = in.readString();
        starthalf = in.readInt();
        leave_date_to = in.readString();
        endhalf = in.readInt();
        this.leave_count_requested = in.readDouble();
        this.date_created = in.readString();
        this.approved = in.readInt();
        this.reject_reason = in.readString();
        this.date_of_approval = in.readString();
        this.isdeleted = in.readInt();
        date_of_delete = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(InstNode_id);
        parcel.writeString(mobnode_id);
        parcel.writeInt(leave_request_id);
        parcel.writeString(employee_id);
        parcel.writeInt(leave_type);
        parcel.writeString(leave_reason);
        parcel.writeString(leave_date_from);
        parcel.writeInt(starthalf);
        parcel.writeString(leave_date_to);
        parcel.writeInt(endhalf);
        parcel.writeDouble(leave_count_requested);
        parcel.writeString(date_created);
        parcel.writeInt(approved);
        parcel.writeString(reject_reason);
        parcel.writeString(date_of_approval);
        parcel.writeInt(isdeleted);
        parcel.writeString(date_of_delete);
    }

    public int getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(int isdeleted) {
        this.isdeleted = isdeleted;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date c = Calendar.getInstance().getTime();
        String formattedDate = format.format(c);
        setDate_of_delete(formattedDate);
    }

    private int isdeleted;

    public String getDate_of_delete() {
        return date_of_delete;
    }

    public void setDate_of_delete(String date_of_delete) {
        this.date_of_delete = date_of_delete;
    }

    public int getStarthalf() {
        return starthalf;
    }

    public void setStarthalf(int starthalf) {
        this.starthalf = starthalf;
    }

    public int getEndhalf() {
        return endhalf;
    }

    public void setEndhalf(int endhalf) {
        this.endhalf = endhalf;
    }

    public String getLeave_date_to() {
        return leave_date_to;
    }

    public void setLeave_date_to(String leave_date_to) {
        this.leave_date_to = leave_date_to;
    }

    private String date_of_delete;
    private int starthalf;
    private int endhalf;
    private String leave_date_to;

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(leave_request_id);
        objectArrayList.add(employee_id);
        objectArrayList.add(leave_type);
        objectArrayList.add(leave_reason);
        objectArrayList.add(leave_date_from);
        objectArrayList.add(starthalf);
        objectArrayList.add(leave_date_to);
        objectArrayList.add(endhalf);
        objectArrayList.add(leave_count_requested);
        objectArrayList.add(date_created);
        objectArrayList.add(approved);
        objectArrayList.add(reject_reason);
        objectArrayList.add(date_of_approval);
        objectArrayList.add(isdeleted);
        objectArrayList.add(date_of_delete);
        return objectArrayList;
    }
}
