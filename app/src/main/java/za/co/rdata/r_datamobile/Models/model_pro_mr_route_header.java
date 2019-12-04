package za.co.rdata.r_datamobile.Models;

/**
 * Created by Dev on 24/01/2016.
 */
public class model_pro_mr_route_header {
    private String InstNode_id;
    private String mobnode_id;
    private String cycle;
    private String route_number;
    private String description;
    private Integer status;
    private String release_date;

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

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRoute_number() {
        return route_number;
    }

    public void setRoute_number(String route_number) {
        this.route_number = route_number;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public model_pro_mr_route_header(String cycle, String description, String instNode_id, String mobnode_id, String release_date, String route_number, Integer status) {
        this.cycle = cycle;
        this.description = description;
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.release_date = release_date;
        this.route_number = route_number;
        this.status = status;
    }
}

