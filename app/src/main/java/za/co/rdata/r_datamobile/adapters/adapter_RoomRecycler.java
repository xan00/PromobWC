package za.co.rdata.r_datamobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_room;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.assetModule.RoomMainSummary;

/**
 * Created by James de Scande on 21/09/2018 at 09:34.
 */

public class adapter_RoomRecycler extends RecyclerView.Adapter<adapter_RoomRecycler.GenericViewHolder> {

    private List<model_pro_ar_asset_room> arrayList;
    private int recyclerViewID;
    private int retbarcode;
    private int retdesc;
    private Context mContext;

    public String getStrSelectedRoom() {
        return strSelectedRoom;
    }

    public void setStrSelectedRoom(String strSelectedRoom) {
        this.strSelectedRoom = strSelectedRoom;
    }

    private String strSelectedRoom;

    public void setRetbarcode(int retbarcode) {
        this.retbarcode = retbarcode;
    }

    public void setRetdesc(int retdesc) {
        this.retdesc = retdesc;
    }

    public void setRetdept(int retdept) {
        this.retdept = retdept;
    }

    private int retdept;

    public void setRetframe(int retframe) {
        this.retframe = retframe;
    }

    private int retframe;

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

    public adapter_RoomRecycler(List<model_pro_ar_asset_room> arrayList, int recyclerViewID) {
        this.arrayList = arrayList;
        this.recyclerViewID = recyclerViewID;
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(recyclerViewID, parent, false);
        itemView.setOnClickListener(gotoroom);
        return new GenericViewHolder(itemView, retbarcode, retdesc, retdept, retframe);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        model_pro_ar_asset_room modelProArAssetRoom = arrayList.get(position);
        try {
            holder.textview_room_barcode.setText(String.valueOf(modelProArAssetRoom.getRoomnumber()));
            holder.textview_room_description.setText(String.valueOf(modelProArAssetRoom.getRoomname()));
            holder.textDepartment.setText(String.valueOf(modelProArAssetRoom.getRoomcounter()));
            if (isRoomCompleted(String.valueOf(modelProArAssetRoom.getRoomcounter()))) {
                holder.conframe.setBackgroundResource(R.drawable.room_complete);
            } else {
                holder.conframe.setBackgroundResource(R.drawable.room_actual_item);
            }
            //holder.conframe.getViewById(retframe);
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

    private View.OnClickListener gotoroom = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //int itemPosition = recyclerView.getChildLayoutPosition(view);
            TextView textView = view.findViewById(retbarcode);
            strSelectedRoom = textView.getText().toString();
            //Toast.makeText(mContext, strSelectedRoom, Toast.LENGTH_LONG).show();
            //nextPass(getApplicationContext());
            Intent gotomainsummary = new Intent(mContext,RoomMainSummary.class);
            gotomainsummary.putExtra("ROOM SCAN",strSelectedRoom);
            mContext.startActivity(gotomainsummary);
        }
    };

}
