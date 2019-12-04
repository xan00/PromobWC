package za.co.rdata.r_datamobile.Models;

/**
 * Created by Dev on 24/01/2016.
 */
public class model_pro_mr_route_rows {
    private String InstNode_id;
    private String mobnode_id;
    private String cycle;
    private String route_number;
    private int walk_sequence;
    private int meter_id;
    private String meter_number;
    private String meter_type;
    private String address_name;
    private String street_number;
    private String town;
    private String suburb;
    private String account_number;
    private String erf_number;
    private String description;
    private String previous_date;
    private Double previous_reading;
    private Double low_reading;
    private Double high_reading;
    private Double estimate_consumption;
    private Double gps_master_long;
    private Double gps_master_lat;
    private Double gps_read_long;
    private Double gps_read_lat;
    private Double meter_reading;
    private String reading_date;
    private String na_code;
    private String note_code;
    private int retries;
    private float distance_to_meter_read;
    private int status;
    private String name;

    public model_pro_mr_route_rows(String account_number, String address_name, String cycle, String description, float distance_to_meter_read, String erf_number, Double estimate_consumption, Double gps_master_lat, Double gps_master_long, Double gps_read_lat, Double gps_read_long, Double high_reading, String instNode_id, Double low_reading, int meter_id, String meter_number, Double meter_reading, String meter_type, String mobnode_id, String na_code, String name, String note_code, String previous_date, Double previous_reading, String reading_date, int retries, String route_number, int status, String street_number, String suburb, String town, int walk_sequence) {
        this.account_number = account_number;
        this.address_name = address_name;
        this.cycle = cycle;
        this.description = description;
        this.distance_to_meter_read = distance_to_meter_read;
        this.erf_number = erf_number;
        this.estimate_consumption = estimate_consumption;
        this.gps_master_lat = gps_master_lat;
        this.gps_master_long = gps_master_long;
        this.gps_read_lat = gps_read_lat;
        this.gps_read_long = gps_read_long;
        this.high_reading = high_reading;
        InstNode_id = instNode_id;
        this.low_reading = low_reading;
        this.meter_id = meter_id;
        this.meter_number = meter_number;
        this.meter_reading = meter_reading;
        this.meter_type = meter_type;
        this.mobnode_id = mobnode_id;
        this.na_code = na_code;
        this.name = name;
        this.note_code = note_code;
        this.previous_date = previous_date;
        this.previous_reading = previous_reading;
        this.reading_date = reading_date;
        this.retries = retries;
        this.route_number = route_number;
        this.status = status;
        this.street_number = street_number;
        this.suburb = suburb;
        this.town = town;
        this.walk_sequence = walk_sequence;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getDistance_to_meter_read() {
        return distance_to_meter_read;
    }

    public void setDistance_to_meter_read(float distance_to_meter_read) {
        this.distance_to_meter_read = distance_to_meter_read;
    }

    public String getErf_number() {
        return erf_number;
    }

    public void setErf_number(String erf_number) {
        this.erf_number = erf_number;
    }

    public Double getEstimate_consumption() {
        return estimate_consumption;
    }

    public void setEstimate_consumption(Double estimate_consumption) {
        this.estimate_consumption = estimate_consumption;
    }

    public Double getGps_master_lat() {
        return gps_master_lat;
    }

    public void setGps_master_lat(Double gps_master_lat) {
        this.gps_master_lat = gps_master_lat;
    }

    public Double getGps_master_long() {
        return gps_master_long;
    }

    public void setGps_master_long(Double gps_master_long) {
        this.gps_master_long = gps_master_long;
    }

    public Double getGps_read_lat() {
        return gps_read_lat;
    }

    public void setGps_read_lat(Double gps_read_lat) {
        this.gps_read_lat = gps_read_lat;
    }

    public Double getGps_read_long() {
        return gps_read_long;
    }

    public void setGps_read_long(Double gps_read_long) {
        this.gps_read_long = gps_read_long;
    }

    public Double getHigh_reading() {
        return high_reading;
    }

    public void setHigh_reading(Double high_reading) {
        this.high_reading = high_reading;
    }

    public String getInstNode_id() {
        return InstNode_id;
    }

    public void setInstNode_id(String instNode_id) {
        InstNode_id = instNode_id;
    }

    public Double getLow_reading() {
        return low_reading;
    }

    public void setLow_reading(Double low_reading) {
        this.low_reading = low_reading;
    }

    public int getMeter_id() {
        return meter_id;
    }

    public void setMeter_id(int meter_id) {
        this.meter_id = meter_id;
    }

    public String getMeter_number() {
        return meter_number;
    }

    public void setMeter_number(String meter_number) {
        this.meter_number = meter_number;
    }

    public Double getMeter_reading() {
        return meter_reading;
    }

    public void setMeter_reading(Double meter_reading) {
        this.meter_reading = meter_reading;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    public String getMobnode_id() {
        return mobnode_id;
    }

    public void setMobnode_id(String mobnode_id) {
        this.mobnode_id = mobnode_id;
    }

    public String getNa_code() {
        return na_code;
    }

    public void setNa_code(String na_code) {
        this.na_code = na_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote_code() {
        return note_code;
    }

    public void setNote_code(String note_code) {
        this.note_code = note_code;
    }

    public String getPrevious_date() {
        return previous_date;
    }

    public void setPrevious_date(String previous_date) {
        this.previous_date = previous_date;
    }

    public Double getPrevious_reading() {
        return previous_reading;
    }

    public void setPrevious_reading(Double previous_reading) {
        this.previous_reading = previous_reading;
    }

    public String getReading_date() {
        return reading_date;
    }

    public void setReading_date(String reading_date) {
        this.reading_date = reading_date;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getRoute_number() {
        return route_number;
    }

    public void setRoute_number(String route_number) {
        this.route_number = route_number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public int getWalk_sequence() {
        return walk_sequence;
    }

    public void setWalk_sequence(int walk_sequence) {
        this.walk_sequence = walk_sequence;
    }
}
