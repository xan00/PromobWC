package za.co.rdata.r_datamobile.Models;

import java.util.ArrayList;

/**
 * Project: Promun
 * Created by wcrous on 06/02/2016.
 */
public class model_pro_sys_company {
    private String InstNode_id;
    private String mobnode_id;
    private String company_name;
    private String sup_email;
    private String status;
    private int ar_cycle;

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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getSup_email() {
        return sup_email;
    }

    public void setSup_email(String sup_email) {
        this.sup_email = sup_email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAr_cycle() {
        return ar_cycle;
    }

    public void setAr_cycle(int ar_cycle) {
        this.ar_cycle = ar_cycle;
    }

    public int getMr_cycle() {
        return mr_cycle;
    }

    public void setMr_cycle(int mr_cycle) {
        this.mr_cycle = mr_cycle;
    }

    public int getSt_cycle() {
        return st_cycle;
    }

    public void setSt_cycle(int st_cycle) {
        this.st_cycle = st_cycle;
    }

    private int mr_cycle;
    private int st_cycle;

    public model_pro_sys_company(String instNode_id, String mobnode_id, String company_name, String sup_email, String status, int ar_cycle, int mr_cycle, int st_cycle) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.company_name = company_name;
        this.sup_email = sup_email;
        this.status = status;
        this.ar_cycle = ar_cycle;
        this.mr_cycle = mr_cycle;
        this.st_cycle = st_cycle;
    }

    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(company_name);
        objectArrayList.add(sup_email);
        objectArrayList.add(status);
        objectArrayList.add(ar_cycle);
        objectArrayList.add(mr_cycle);
        objectArrayList.add(st_cycle);
        return objectArrayList;
    }

}
