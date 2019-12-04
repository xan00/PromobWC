package za.co.rdata.r_datamobile.Models;

/**
 * Project: Promun
 * Created by wcrous on 05/02/2016.
 */
public class model_pro_sys_images {
    private String InstNode_id;
    private String mobnode_id;
    private String img_id;
    private String img_desc;
    private String img_date;
    private Byte[] img_blob;

    public model_pro_sys_images(Byte[] img_blob, String img_date, String img_desc, String img_id, String instNode_id, String mobnode_id) {
        this.img_blob = img_blob;
        this.img_date = img_date;
        this.img_desc = img_desc;
        this.img_id = img_id;
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
    }

    public Byte[] getImg_blob() {
        return img_blob;
    }

    public void setImg_blob(Byte[] img_blob) {
        this.img_blob = img_blob;
    }

    public String getImg_date() {
        return img_date;
    }

    public void setImg_date(String img_date) {
        this.img_date = img_date;
    }

    public String getImg_desc() {
        return img_desc;
    }

    public void setImg_desc(String img_desc) {
        this.img_desc = img_desc;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
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
}
