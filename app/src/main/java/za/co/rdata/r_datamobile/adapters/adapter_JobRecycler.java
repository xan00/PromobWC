package za.co.rdata.r_datamobile.adapters;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.co.rdata.r_datamobile.Models.model_pro_jb_jobcard;
import za.co.rdata.r_datamobile.assetModule.PopulateRoomActivity;
import za.co.rdata.r_datamobile.jobModule.activity_job_card_holder;


public class adapter_JobRecycler extends RecyclerView.Adapter<adapter_JobRecycler.GenericViewHolder>

    {

    private List<model_pro_jb_jobcard> arrayList;
    private int recyclerViewID;

        public adapter_JobRecycler(View itemView, int retjobnumber, int retjobdesc, int retjobdepartment, int retjobaddress) {
        }

        public List<model_pro_jb_jobcard> getArrayList() {
            return arrayList;
        }

        public void setArrayList(List<model_pro_jb_jobcard> arrayList) {
            this.arrayList = arrayList;
        }

        public int getRecyclerViewID() {
            return recyclerViewID;
        }

        public void setRecyclerViewID(int recyclerViewID) {
            this.recyclerViewID = recyclerViewID;
        }

        public int getRetjobnumber() {
            return retjobnumber;
        }

        public void setRetjobnumber(int retjobnumber) {
            this.retjobnumber = retjobnumber;
        }

        public int getRetjobdesc() {
            return retjobdesc;
        }

        public void setRetjobdesc(int retjobdesc) {
            this.retjobdesc = retjobdesc;
        }

        public int getRetjobdepartment() {
            return retjobdepartment;
        }

        public void setRetjobdepartment(int retjobdepartment) {
            this.retjobdepartment = retjobdepartment;
        }

        public int getRetjobaddress() {
            return retjobaddress;
        }

        public void setRetjobaddress(int retjobaddress) {
            this.retjobaddress = retjobaddress;
        }

        public int getRetjobparts() {
            return retjobparts;
        }

        public void setRetjobparts(int retjobparts) {
            this.retjobparts = retjobparts;
        }

        private int retjobnumber;
    private int retjobdesc;
    private int retjobdepartment;
    private int retjobaddress;
    private int retjobparts;

        public int getRetframe() {
            return retframe;
        }

        public void setRetframe(int retframe) {
            this.retframe = retframe;
        }

        private int retframe;

    private Context mContext;



    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView txtjobnum, txtjobdesc, txtjobtype, txtJobAddress;
        ConstraintLayout conJobframe;
        //TextView textView_building;

        GenericViewHolder(View view, int jobnumber, int jobdesc, int jobdepartment, int jobaddress, int jobconframe) {
            super(view);
            retjobnumber = jobnumber;
            retjobdesc = jobdesc;
            retjobdepartment = jobdepartment;
            retjobaddress = jobaddress;
            txtjobnum = view.findViewById(jobnumber);
            txtjobdesc = view.findViewById(jobdesc);
            txtjobtype = view.findViewById(jobdepartment);
            txtJobAddress = view.findViewById(jobaddress);
            conJobframe = view.findViewById(jobconframe);
        }

    }

    public adapter_JobRecycler(List<model_pro_jb_jobcard> arrayList, int recyclerViewID) {
        this.arrayList = arrayList;
        this.recyclerViewID = recyclerViewID;
    }

    @Override
    public adapter_JobRecycler.GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(recyclerViewID, parent, false);
        itemView.setOnClickListener(gotojob);
        return new GenericViewHolder(itemView, retjobnumber, retjobdesc, retjobdepartment, retjobaddress, retframe);
    }

    @Override
    public void onBindViewHolder(adapter_JobRecycler.GenericViewHolder holder, int position) {
        model_pro_jb_jobcard modelProJbJobCard = arrayList.get(position);
        try {
            holder.txtjobnum.setText(String.valueOf(modelProJbJobCard.getJobnumber()));
            holder.txtjobdesc.setText(String.valueOf(modelProJbJobCard.getJobdesc()));
            holder.txtjobtype.setText(String.valueOf(modelProJbJobCard.getJobparts()));

            holder.conJobframe.getViewById(retframe);
        } catch (NullPointerException ignore) {}
    }

        @Override
        public int getItemCount() {
            return 0;
        }

        private View.OnClickListener gotojob = view -> {
            Intent jobintent = new Intent(mContext, activity_job_card_holder.class);

            mContext.startActivity(jobintent);
        };
}
