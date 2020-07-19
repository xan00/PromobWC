package za.co.rdata.r_datamobile.Models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class model_pro_qr_response implements Parcelable {

    public static final Creator<model_pro_qr_response> CREATOR = new Creator<model_pro_qr_response>() {
        @Override
        public model_pro_qr_response createFromParcel(Parcel in) {
            return new model_pro_qr_response(in);
        }

        @Override
        public model_pro_qr_response[] newArray(int size) {
            return new model_pro_qr_response[size];
        }
    };

    public model_pro_qr_response(String instNode_id, String mobnode_id, int qr_id, int response_code, String creation_date) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.qr_id = qr_id;
        this.response_code = response_code;
        this.creation_date = creation_date;
    }

    private String InstNode_id;
    private String mobnode_id;
    private int qr_id;
    private int response_code;
    private String creation_date;

    private model_pro_qr_response(Parcel in) {
        InstNode_id = in.readString();
        mobnode_id = in.readString();
        qr_id = in.readInt();
        response_code = in.readInt();
        creation_date = in.readString();

    }

    public int getQr_id() {
        return qr_id;
    }

    public int getResponse_code() {
        return response_code;
    }

    public String getCreation_date() {
        return creation_date;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(InstNode_id);
        parcel.writeString(mobnode_id);
        parcel.writeInt(qr_id);
        parcel.writeInt(response_code);
        parcel.writeString(creation_date);

}

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(qr_id);
        objectArrayList.add(response_code);
        objectArrayList.add(creation_date);
        return objectArrayList;
    }
}
