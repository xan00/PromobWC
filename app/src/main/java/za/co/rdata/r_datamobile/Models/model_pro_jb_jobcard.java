package za.co.rdata.r_datamobile.Models;

public class model_pro_jb_jobcard {

    private final String jobnumber;
    private final String jobdesc;
    private final String jobdepartment;
    private final String jobaddress;
    private final String jobparts;

    public String getJobnumber() {
        return jobnumber;
    }

    public String getJobdesc() {
        return jobdesc;
    }

    public String getJobdepartment() {
        return jobdepartment;
    }

    public String getJobaddress() {
        return jobaddress;
    }

    public String getJobparts() {
        return jobparts;
    }

    public model_pro_jb_jobcard(String jobnumber, String jobdesc, String jobdepartment, String jobaddress, String jobparts) {
        this.jobnumber = jobnumber;
        this.jobdesc = jobdesc;
        this.jobdepartment = jobdepartment;
        this.jobaddress = jobaddress;
        this.jobparts = jobparts;
    }

}
