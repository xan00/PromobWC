package za.co.rdata.r_datamobile.Models;

/**
 * Created by James de Scande on 23/06/2017.
 */

public class model_pro_ar_scanasset {

    public int getRoute_nr() {
        return route_nr;
    }

    public void setRoute_nr(int route_nr) {
        this.route_nr = route_nr;
    }

    public model_pro_ar_scanasset(String instaNode, String mobnode_id, long scan_id, int scan_cycle, String scan_reader, String scan_barcode, String scan_type_location, String scan_type_barcode, String scan_location, String scan_location_entry, String condition, String adjremainder, String notes, String date, double coordX, double coordY, String comments1, String comments2, int quantity, int retries, String user, int route_nr) {
        InstaNode = instaNode;
        this.mobnode_id = mobnode_id;
        this.scan_id = scan_id;
        this.scan_cycle = scan_cycle;
        this.scan_reader = scan_reader;
        this.scan_barcode = scan_barcode;
        this.scan_type_location = scan_type_location;
        this.scan_type_barcode = scan_type_barcode;
        this.scan_location = scan_location;
        this.scan_location_entry = scan_location_entry;
        this.condition = condition;
        this.adjremainder = adjremainder;
        this.notes = notes;
        this.date = date;
        this.coordX = coordX;
        this.coordY = coordY;
        this.comments1 = comments1;
        this.comments2 = comments2;
        this.comments3 = comments3;
        this.scan_quantity = quantity;
        this.scan_quan_retries = retries;
        this.user = user;
        this.route_nr = route_nr;
    }

    private int route_nr;
    private String InstaNode;
    private String mobnode_id;
    private Long scan_id;
    private int scan_cycle;
    private String scan_reader;
    private String scan_barcode;
    private String scan_type_location;
    private String scan_type_barcode;
    private String scan_location;
    private String scan_location_entry;
    private String condition;
    private String adjremainder;
    private String notes;
    private String date;
    private double coordX;
    private double coordY;
    private String comments1;
    private String comments2;

    public String getComments3() {
        return comments3;
    }

    public void setComments3(String comments3) {
        this.comments3 = comments3;
    }

    private String comments3;
    private int scan_quantity;
    private int scan_quan_retries;
    private String user;

    public String getComments2() {
        return comments2;
    }

    public void setComments2(String comments2) {
        this.comments2 = comments2;
    }

    public String getInstaNode() {
        return InstaNode;
    }


    public void setInstaNode(String instaNode) {
        this.InstaNode = instaNode;
    }

    public String getMobnode_id() {
        return mobnode_id;
    }

    public void setMobnode_id(String mobnode_id) {
        this.mobnode_id = mobnode_id;
    }

    public String getScan_barcode() {
        return scan_barcode;
    }

    public void setScan_barcode(String scan_barcode) {
        this.scan_barcode = scan_barcode;
    }

    public String getScan_location() {
        return scan_location;
    }

    public void setScan_location(String scan_location_entry) {
        this.scan_location_entry = scan_location_entry;
    }

    public String getScan_location_entry() {
        return scan_location_entry;
    }

    public void setScan_location_entry(String scan_location_entry) {
        this.scan_location_entry = scan_location_entry;
    }

    public String getScan_type_location() {
        return scan_type_location;
    }

    public void setScan_type_location(String scan_type_location) {
        this.scan_type_location = scan_type_location;
    }

    public String getScan_type_barcode() {
        return scan_type_barcode;
    }

    public void setScan_type_barcode(String scan_type_barcode) {
        this.scan_type_barcode = scan_type_barcode;
    }

    public String getScan_reader() {
        return scan_reader;
    }

    public void setScan_reader(String scan_reader) {
        this.scan_reader = scan_reader;
    }

    public Long getScan_id() {
        return scan_id;
    }

    public void setScan_id(Long scan_id) {
        this.scan_id = scan_id;
    }

    public int getScan_cycle() {
        return scan_cycle;
    }

    public void setScan_cycle(int scan_cycle) {
        this.scan_cycle = scan_cycle;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAdjremainder() {
        return adjremainder;
    }

    public void setAdjremainder(String adjremainder) {
        this.adjremainder = adjremainder;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCoordX(double coordX) {
        this.coordX = coordX;
    }

    public void setCoordY(double coordY) {
        this.coordY = coordY;
    }

    public double getCoordX() {
        return coordX;
    }

    public double getCoordY() {
        return coordY;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComments1() {
        return comments1;
    }

    public void setComments1(String comments1) {
        this.comments1 = comments1;
    }

    public int getScan_quantity() {
        return scan_quantity;
    }

    public void setScan_quantity(int scan_quantity) {
        this.scan_quantity = scan_quantity;
    }

    public int getScan_quan_retries() {
        return scan_quan_retries;
    }

    public void setScan_quan_retries(int scan_quan_retries) {
        this.scan_quan_retries = scan_quan_retries;
    }
}
