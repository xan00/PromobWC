package za.co.rdata.r_datamobile.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class model_pro_ar_asset_headers implements Parcelable {

    private String roomnumber;
    private String locationid;
    private String department;
    private String reg_asset_barcode;
    private String serial_number;
    private String reg_asset_desc;
    private String condition;
    private String comments;

    private int usefullife;
    private int usefulremaining;


    public model_pro_ar_asset_headers(String roomnumber, String locationid, String department, String reg_asset_barcode, String serial_number, String reg_asset_desc, String condition, String comments, int usefullife, int usefulremaining) {
        this.roomnumber = roomnumber;
        this.locationid = locationid;
        this.department = department;
        this.reg_asset_barcode = reg_asset_barcode;
        this.serial_number = serial_number;
        this.reg_asset_desc = reg_asset_desc;
        this.condition = condition;
        this.comments = comments;
        this.usefullife = usefullife;
        this.usefulremaining = usefulremaining;
    }

    private model_pro_ar_asset_headers(Parcel in) {
        roomnumber = in.readString();
        locationid = in.readString();
        department = in.readString();
        reg_asset_barcode = in.readString();
        serial_number = in.readString();
        reg_asset_desc = in.readString();
        condition = in.readString();
        comments = in.readString();
        usefullife = in.readInt();
        usefulremaining = in.readInt();
    }

    public static final Creator<model_pro_ar_asset_headers> CREATOR = new Creator<model_pro_ar_asset_headers>() {
        @Override
        public model_pro_ar_asset_headers createFromParcel(Parcel in) {
            return new model_pro_ar_asset_headers(in);
        }

        @Override
        public model_pro_ar_asset_headers[] newArray(int size) {
            return new model_pro_ar_asset_headers[size];
        }
    };

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getLocationid() {
        return locationid;
    }

    public void setLocationid(String locationid) {
        this.locationid = locationid;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getReg_asset_barcode() {
        return reg_asset_barcode;
    }

    public void setReg_asset_barcode(String reg_asset_barcode) {
        this.reg_asset_barcode = reg_asset_barcode;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getReg_asset_desc() {
        return reg_asset_desc;
    }

    public void setReg_asset_desc(String reg_asset_desc) {
        this.reg_asset_desc = reg_asset_desc;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getUsefullife() {
        return usefullife;
    }

    public void setUsefullife(int usefullife) {
        this.usefullife = usefullife;
    }

    public double getUsefulremaining() {
        return usefulremaining;
    }

    public void setUsefulremaining(int usefulremaining) {
        this.usefulremaining = usefulremaining;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(roomnumber);
        parcel.writeString(locationid);
        parcel.writeString(department);
        parcel.writeString(reg_asset_barcode);
        parcel.writeString(serial_number);
        parcel.writeString(reg_asset_desc);
        parcel.writeString(condition);
        parcel.writeString(comments);
        parcel.writeInt(usefullife);
        parcel.writeInt(usefulremaining);
    }
}

