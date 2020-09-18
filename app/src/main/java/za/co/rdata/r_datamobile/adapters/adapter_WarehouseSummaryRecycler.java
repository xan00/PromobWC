package za.co.rdata.r_datamobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.co.rdata.r_datamobile.DBMeta.intentcodes;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_room;
import za.co.rdata.r_datamobile.Models.model_pro_stk_scan;
import za.co.rdata.r_datamobile.Models.model_pro_stk_stock;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.assetModule.RoomMainSummary;
import za.co.rdata.r_datamobile.stockModule.WareHouseSummary;

import static za.co.rdata.r_datamobile.MainActivity.sqliteDbHelper;

/**
 * Created by James de Scande on 21/09/2018 at 09:34.
 */

public class adapter_WarehouseSummaryRecycler extends RecyclerView.Adapter<adapter_WarehouseSummaryRecycler.GenericViewHolder> {

    private List<model_pro_stk_scan> arrayListStockScan;
    private int recyclerViewID;
    private int retbincode;
    private int retstockdesc;
    private Context mContext;
    private String strSelectedWarehouse;
    private int retStockcode;
    private int retframe;

    public adapter_WarehouseSummaryRecycler(List<model_pro_stk_scan> arrayListStockScan, int recyclerViewID) {
        this.arrayListStockScan = arrayListStockScan;
        this.recyclerViewID = recyclerViewID;
    }

    public List<model_pro_stk_scan> getArrayListStockScan() {
        return arrayListStockScan;
    }

    public void setArrayListStockScan(List<model_pro_stk_scan> arrayListStockScan) {
        this.arrayListStockScan = arrayListStockScan;
    }

    public int getRetbincode() {
        return retbincode;
    }

    public void setRetbincode(int retbincode) {
        this.retbincode = retbincode;
    }

    public int getRetstockdesc() {
        return retstockdesc;
    }

    public void setRetstockdesc(int retstockdesc) {
        this.retstockdesc = retstockdesc;
    }

    public String getStrSelectedWarehouse() {
        return strSelectedWarehouse;
    }

    public void setStrSelectedWarehouse(String strSelectedWarehouse) {
        this.strSelectedWarehouse = strSelectedWarehouse;
    }

    public int getRetStockcode() {
        return retStockcode;
    }

    public void setRetStockcode(int retStockcode) {
        this.retStockcode = retStockcode;
    }

    public int getRetframe() {
        return retframe;
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
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(recyclerViewID, parent, false);
        return new GenericViewHolder(itemView, retstockdesc, retStockcode, retframe);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        model_pro_stk_scan model_pro_stk_scan = arrayListStockScan.get(position);

        try {
            holder.textDepartment.setText(String.valueOf( 0));

            Cursor curStockDesc = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT stk_descrip FROM pro_stk_stock WHERE "+ meta.pro_stk_stock.stk_code+" = '"+model_pro_stk_scan.getStk_code()+"'",null);
            curStockDesc.moveToFirst();
            holder.textview_room_description.setText(String.valueOf(curStockDesc.getString(0)));
            curStockDesc.close();

            if (!model_pro_stk_scan.getStk_take_qty().equals(-999)) {
                holder.conframe.setBackgroundResource(R.drawable.stock_item_scanned);
                holder.textDepartment.setText(String.valueOf(model_pro_stk_scan.getStk_take_qty()));
            }

            //holder.conframe.getViewById(retframe);
        } catch (NullPointerException ignore) {
        }
    }

    private boolean isRoomCompleted(String expression) {

        String delim = "/";
        String[] stringTokens = expression.split(delim);
        return stringTokens[0].equals(stringTokens[1]);

    }

    @Override
    public int getItemCount() {
        return arrayListStockScan.size();
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView  textview_room_description, textDepartment;
        ConstraintLayout conframe;
        //TextView textView_building;

        GenericViewHolder(View view, int desc, int dept, int frame) {
            super(view);
            retstockdesc = desc;
            retStockcode = dept;
            retframe = frame;
            ///textview_room_barcode = view.findViewById(barcode);
            textview_room_description = view.findViewById(desc);
            textDepartment = view.findViewById(dept);
            conframe = view.findViewById(frame);
        }

    }

}
