package za.co.rdata.r_datamobile.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class model_pro_qr_code implements Parcelable {

    public static final Creator<model_pro_qr_code> CREATOR = new Creator<model_pro_qr_code>() {
        @Override
        public model_pro_qr_code createFromParcel(Parcel in) {
            return new model_pro_qr_code(in);
        }

        @Override
        public model_pro_qr_code[] newArray(int size) {
            return new model_pro_qr_code[size];
        }
    };

    public model_pro_qr_code(String instNode_id, String mobnode_id, int qr_id, String qr_value, String creation_date) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.qr_id = qr_id;
        this.qr_value = qr_value;
        this.creation_date = creation_date;
    }

    private String InstNode_id;
    private String mobnode_id;

    public model_pro_qr_code(int qr_id, String qr_value) {
        this.qr_id = qr_id;
        this.qr_value = qr_value;
    }

    private int qr_id;
    private String qr_value;
    private String creation_date;

    private model_pro_qr_code(Parcel in) {
        InstNode_id = in.readString();
        mobnode_id = in.readString();
        qr_id = in.readInt();
        qr_value = in.readString();
        creation_date = in.readString();

    }

    public int getQr_id() {
        return qr_id;
    }

    public String getqr_value() {
        return qr_value;
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
        parcel.writeString(qr_value);
        parcel.writeString(creation_date);

}

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(qr_id);
        objectArrayList.add(qr_value);
        objectArrayList.add(creation_date);
        return objectArrayList;
    }
}
