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

import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_room;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.assetModule.RoomMainSummary;
import za.co.rdata.r_datamobile.jobModule.activity_job_card_holder;

public class adapter_JobRecycler extends RecyclerView.Adapter<adapter_JobRecycler.GenericViewHolder> {

    private List<model_pro_ar_asset_room> arrayList;
    private int recyclerViewID;
    private int retbarcode;
    private int retdesc;
    private Context mContext;
    private String strSelectedRoom;
    private int retdept;
    private int retframe;
    private View.OnClickListener gotoroom = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent gotojob = new Intent(mContext, activity_job_card_holder.class);
            TextView textView = view.findViewById(retbarcode);
            String strJobNumber = textView.getText().toString();
            TextView textViewasset = view.findViewById(retdesc);
            String strSelectedDesc = textViewasset.getText().toString();
            gotojob.putExtra("job_number",strJobNumber);
            gotojob.putExtra("meter_number",strSelectedDesc);
            mContext.startActivity(gotojob);
        }
    };

    public adapter_JobRecycler(List<model_pro_ar_asset_room> arrayList, int recyclerViewID) {
        this.arrayList = arrayList;
        this.recyclerViewID = recyclerViewID;
    }

    public String getStrSelectedRoom() {
        return strSelectedRoom;
    }

    public void setStrSelectedRoom(String strSelectedRoom) {
        this.strSelectedRoom = strSelectedRoom;
    }

    public void setRetbarcode(int retbarcode) {
        this.retbarcode = retbarcode;
    }

    public void setRetdesc(int retdesc) {
        this.retdesc = retdesc;
    }

    public void setRetdept(int retdept) {
        this.retdept = retdept;
    }

    public void setRetframe(int retframe) {
        this.retframe = retframe;
    }

    public int getRecyclerViewID() {
        return recyclerViewID;
    }

    public void setRecyclerViewID(int recyclerViewID) {
        this.recyclerViewID = recyclerViewID;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public adapter_JobRecycler.GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(recyclerViewID, parent, false);
        itemView.setOnClickListener(gotoroom);
        return new adapter_JobRecycler.GenericViewHolder(itemView, retbarcode, retdesc, retdept, retframe);
    }

    @Override
    public void onBindViewHolder(adapter_JobRecycler.GenericViewHolder holder, int position) {
        model_pro_ar_asset_room modelProArAssetRoom = arrayList.get(position);
        try {
            holder.textview_room_barcode.setText(String.valueOf(modelProArAssetRoom.getRoomnumber()));
            holder.textview_room_description.setText(String.valueOf(modelProArAssetRoom.getRoomname()));
            holder.textDepartment.setText(String.valueOf(modelProArAssetRoom.getRoomcounter()));


            switch (Integer.parseInt(modelProArAssetRoom.getRoomcounter())) {
                case 7: holder.textDepartment.setText("Water");
                    holder.conframe.setBackgroundResource(R.drawable.room_item_newly_scanned);
                break;
                case 12: holder.textDepartment.setText("Electricity");
                    holder.conframe.setBackgroundResource(R.drawable.room_item_manually_added);
                break;

            }

           // if (isRoomCompleted(String.valueOf(modelProArAssetRoom.getRoomcounter()))) {
            //    holder.conframe.setBackgroundResource(R.drawable.room_complete);
            //} else {
                //holder.conframe.setBackgroundResource(R.drawable.room_actual_item);
            //}
            ///holder.conframe.getViewById(retframe);
        } catch (NullPointerException ignore) {}
    }

    private boolean isRoomCompleted(String expression){

        String delim = "/";
        String[] stringTokens = expression.split(delim);
        return stringTokens[0].equals(stringTokens[1]);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView textview_room_barcode, textview_room_description, textDepartment;
        ConstraintLayout conframe;
        //TextView textView_building;

        GenericViewHolder(View view, int barcode, int desc, int dept, int frame) {
            super(view);
            retbarcode = barcode;
            retdesc = desc;
            retdept = dept;
            retframe = frame;
            textview_room_barcode = view.findViewById(barcode);
            textview_room_description = view.findViewById(desc);
            textDepartment = view.findViewById(dept);
            conframe = view.findViewById(frame);
        }

    }

}