package za.co.rdata.r_datamobile.Models;

import java.util.ArrayList;

public class model_pro_stk_basket {

    private String InstNode_id;
    private String mobnode_id;
    private int basket_id;
    private int job_id;
    private String checkout_date;
    private int basket_job_type;

    public model_pro_stk_basket(String instNode_id, String mobnode_id, int basket_id, int job_id) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.basket_id = basket_id;
        this.job_id = job_id;
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

    public int getBasket_id() {
        return basket_id;
    }

    public void setBasket_id(int basket_id) {
        this.basket_id = basket_id;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public String getCheckout_date() {
        return checkout_date;
    }

    public void setCheckout_date(String checkout_date) {
        this.checkout_date = checkout_date;
    }

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(basket_id);
        objectArrayList.add(job_id);
        objectArrayList.add(checkout_date);
        return objectArrayList;
    }

    public int getBasket_job_type() {
        return basket_job_type;
    }

    public void setBasket_job_type(int basket_job_type) {
        this.basket_job_type = basket_job_type;
    }
}
