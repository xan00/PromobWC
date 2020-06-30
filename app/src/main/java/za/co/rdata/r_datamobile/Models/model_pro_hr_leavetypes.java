package za.co.rdata.r_datamobile.Models;

import java.util.ArrayList;

public class model_pro_hr_leavetypes {

    public model_pro_hr_leavetypes(String instNode_id, String mobnode_id, int leave_type_id, String leave_type_desc) {
        InstNode_id = instNode_id;
        this.mobnode_id = mobnode_id;
        this.leave_type_id = leave_type_id;
        this.leave_type_desc = leave_type_desc;
    }

    private String InstNode_id;
    private String mobnode_id;
    private int leave_type_id;
    private String leave_type_desc;


    public ArrayList<Object> getModelAsArrayList() {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        objectArrayList.add(InstNode_id);
        objectArrayList.add(mobnode_id);
        objectArrayList.add(leave_type_id);
        objectArrayList.add(leave_type_desc);
        return objectArrayList;
    }
}
