package za.co.rdata.r_datamobile.Models;

/**
 * Created by James de Scande on 01/08/2017.
 */

public class model_location_result {

    public String lat;
    public String lng;
    public String accuracy;

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public model_location_result(String lat, String lng, String accuracy) {
        this.lat = lat;
        this.lng = lng;
        this.accuracy = accuracy;
    }
}
