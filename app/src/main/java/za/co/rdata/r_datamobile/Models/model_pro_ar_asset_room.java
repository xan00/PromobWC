package za.co.rdata.r_datamobile.Models;

/**
 * Created by James de Scande on 18/09/2017 at 10:35.
 */

public class model_pro_ar_asset_room {

    private String roomnumber;

    public model_pro_ar_asset_room(String roomnumber, String roomname, String department, String responsibleperson, String roomcounter) {
        this.roomnumber = roomnumber;
        this.roomname = roomname;
        this.department = department;
        this.responsibleperson = responsibleperson;
        this.roomcounter = roomcounter;
    }

    private String roomname;
    private String department;
    private String responsibleperson;
    private String roomcounter;

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getResponsibleperson() {
        return responsibleperson;
    }

    public void setResponsibleperson(String responsibleperson) {
        this.responsibleperson = responsibleperson;
    }

    public model_pro_ar_asset_room(String roomnumber, String roomname, String department, String responsibleperson) {
        this.roomnumber = roomnumber;
        this.roomname = roomname;
        this.department = department;
        this.responsibleperson = responsibleperson;
    }

    public model_pro_ar_asset_room() {
    }

    public String getRoomcounter() {
        return roomcounter;
    }

    public void setRoomcounter(String roomcounter) {
        this.roomcounter = roomcounter;
    }
}
