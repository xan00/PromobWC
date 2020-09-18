package za.co.rdata.r_datamobile.Models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class model_pro_hr_docreq implements Parcelable {

    public static final Creator<model_pro_hr_docreq> CREATOR = new Creator<model_pro_hr_docreq>() {
        @Override
        public model_pro_hr_docreq createFromParcel(Parcel in) {
            return new model_pro_hr_docreq(in);
        }

        @Override
        public model_pro_hr_docreq[] newArray(int size) {
            return new model_pro_hr_docreq[size];
        }
    };

    private String InstNode_id;

    public model_pro_hr_docreq(String instNode_id, String mobnode_id, int docreq_id, String employee_id, String docreq_date_from, String docreq_date_to, int docreq_type, String date_created) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.docreq_id = docreq_id;
        this.employee_id = employee_id;
        this.docreq_date_from = docreq_date_from;
        this.docreq_date_to = docreq_date_to;
        this.docreq_type = docreq_type;
        this.date_created = date_created;
    }

    private String mobnode_id;
    private int docreq_id;
    private String employee_id;
    private String docreq_date_from;
    private String docreq_date_to;
    private int docreq_type;
    private String date_created;

    private model_pro_hr_docreq(Parcel in) {
        InstNode_id = in.readString();
        mobnode_id = in.readString();
        docreq_id = in.readInt();
        employee_id = in.readString();
        docreq_date_from = in.readString();
        docreq_date_to = in.readString();
        docreq_type = in.readInt();
        date_created = in.readString();
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

    public int getDocreq_id() {
        return docreq_id;
    }

    public void setDocreq_id(int docreq_id) {
        this.docreq_id = docreq_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getDocreq_date_from() {
        return docreq_date_from;
    }

    public void setDocreq_date_from(String docreq_date_from) {
        this.docreq_date_from = docreq_date_from;
    }

    public String getDocreq_date_to() {
        return docreq_date_to;
    }

    public void setDocreq_date_to(String docreq_date_to) {
        this.docreq_date_to = docreq_date_to;
    }

    public int getDocreq_type() {
        return docreq_type;
    }

    public void setDocreq_type(int docreq_type) {
        this.docreq_type = docreq_type;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date c = Calendar.getInstance().getTime();
        String formattedDate = format.format(c);
        setDate_created(formattedDate);
        this.date_created = date_created;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(InstNode_id);
        parcel.writeString(mobnode_id);
        parcel.writeInt(docreq_id);
        parcel.writeString(employee_id);
        parcel.writeString(docreq_date_from);
        parcel.writeString(docreq_date_to);
        parcel.writeInt(docreq_type);
        parcel.writeString(date_created);
    }

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(docreq_id);
        objectArrayList.add(employee_id);
        objectArrayList.add(docreq_date_from);
        objectArrayList.add(docreq_date_to);
        objectArrayList.add(docreq_type);
        objectArrayList.add(date_created);
        return objectArrayList;
    }
}
