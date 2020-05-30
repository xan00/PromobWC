package za.co.rdata.r_datamobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.intentcodes;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_headers;
import za.co.rdata.r_datamobile.assetModule.PopulateRoomActivity;

/**
 * Created by James de Scande on 01/10/2018 at 10:26.
 */

public class adapter_AssetRecycler extends RecyclerView.Adapter<adapter_AssetRecycler.GenericViewHolder> {

    private List<model_pro_ar_asset_headers> arrayList;
    private int recyclerViewID;
    private int retbarcode;
    private int retdesc;
    private Context mContext;
    private int lightcolour;
    private String currentRoom;

    public void setRetbarcode(int retbarcode) {
        this.retbarcode = retbarcode;
    }

    public void setRetdesc(int retdesc) {
        this.retdesc = retdesc;
    }

    public int getRetroom() {
        return retroom;
    }

    public void setRetroom(int retroom) {
        this.retroom = retroom;
    }

    private int retroom;

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

    public void setLightcolour(int lightcolour) {
        this.lightcolour = lightcolour;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView textview_asset_barcode, textview_asset_description, textroombarcode;
        ConstraintLayout conframe;
        //TextView textView_building;

        GenericViewHolder(View view, int barcode, int desc, int room, int frame) {
            super(view);
            retbarcode = barcode;
            retdesc = desc;
            retroom = room;
            retframe = frame;
            textview_asset_barcode = view.findViewById(barcode);
            textview_asset_description = view.findViewById(desc);
            textroombarcode = view.findViewById(room);
            conframe = view.findViewById(frame);
        }
    }

    public adapter_AssetRecycler(List<model_pro_ar_asset_headers> arrayList, int recyclerViewID) {
        this.arrayList = arrayList;
        this.recyclerViewID = recyclerViewID;
    }

    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(recyclerViewID, parent, false);
        itemView.setOnClickListener(gotoasset);
        return new GenericViewHolder(itemView, retbarcode, retdesc, retroom, retframe);
    }

    public void onBindViewHolder(GenericViewHolder holder, int position) {
        model_pro_ar_asset_headers modelProArAsset = arrayList.get(position);
        try {
            holder.textview_asset_barcode.setText(String.valueOf(modelProArAsset.getReg_asset_barcode()));
            holder.textview_asset_description.setText(String.valueOf(modelProArAsset.getReg_asset_desc()));
            holder.textroombarcode.setText(String.valueOf(modelProArAsset.getRoomnumber()));
            holder.conframe.setBackgroundResource(lightcolour);
        } catch (NullPointerException ignore) {}
    }

    public int getItemCount() {
        try {
            return arrayList.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    private int GetCycle() {
        int cycle;
        try {
            Cursor getcycle = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT ar_cycle FROM pro_sys_company", null);
            getcycle.moveToLast();
            cycle = getcycle.getInt(getcycle.getColumnIndex(meta.pro_sys_company.ar_cycle));
            getcycle.close();
        } catch (CursorIndexOutOfBoundsException e) {
            sqliteDBHelper sqliteDbHelper = new sqliteDBHelper(this.mContext);
            Cursor getcycle = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT ar_cycle FROM pro_sys_company", null);
            getcycle.moveToLast();
            cycle = getcycle.getInt(getcycle.getColumnIndex(meta.pro_sys_company.ar_cycle));
            getcycle.close();
        }
        return cycle;
    }

    private String GetLocationName(String locationname) {

        String locationnameresult = currentRoom;
        Cursor roomcursor1 = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT loc_name FROM pro_ar_locations WHERE loc_code = '" + locationname + "'", null);
        roomcursor1.moveToFirst();

        try {
            locationnameresult = roomcursor1.getString(roomcursor1.getColumnIndex(meta.pro_ar_locations.loc_name));
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
            roomcursor1.close();
            return locationnameresult;

    }

    private String GetRespPerson(String locationname) {
        String responsibleperson = "";
        Cursor roomcursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT loc_person FROM pro_ar_locations WHERE loc_code = '" + locationname + "'", null);
        roomcursor.moveToLast();
        try {
            responsibleperson = roomcursor.getString(roomcursor.getColumnIndex(meta.pro_ar_locations.loc_person));
        } catch (Exception ignore) {
        }
        roomcursor.close();
        return responsibleperson;
    }

    private View.OnClickListener gotoasset = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //int itemPosition = recyclerView.getChildLayoutPosition(view);
            TextView textView = view.findViewById(retroom);
            String strSelectedRoom = textView.getText().toString();
            TextView textViewasset = view.findViewById(retbarcode);
            String strSelectedAsset = textViewasset.getText().toString();
            Intent roomintent = new Intent(mContext, PopulateRoomActivity.class);
            roomintent.putExtra("ROOM SCAN", strSelectedRoom);
            roomintent.putExtra(intentcodes.asset_activity.current_room, currentRoom);
            roomintent.putExtra("CYCLE", GetCycle());
            roomintent.putExtra("LOCATION SCAN TYPE", "s");
            roomintent.putExtra("LOCATION NAME", GetLocationName(strSelectedRoom));
            roomintent.putExtra("RESPONSIBLE PERSON", GetRespPerson(strSelectedRoom));
            roomintent.putExtra("BARCODE",strSelectedAsset);
            roomintent.putExtra("LIGHT COLOUR",lightcolour);
            //roomintent.putExtra("SUMMARY VALUE", tempviewid);
            mContext.startActivity(roomintent);
        }
    };


}
