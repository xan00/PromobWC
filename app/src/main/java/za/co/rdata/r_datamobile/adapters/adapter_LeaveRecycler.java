package za.co.rdata.r_datamobile.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.intentcodes;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.DBMeta.sharedprefcodes;
import za.co.rdata.r_datamobile.HRModule.SelectApplyForLeave;
import za.co.rdata.r_datamobile.HRModule.SelectLeave;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_headers;
import za.co.rdata.r_datamobile.Models.model_pro_hr_leavereq;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.assetModule.PopulateRoomActivity;

/**
 * Created by James de Scande on 01/10/2018 at 10:26.
 */

public class adapter_LeaveRecycler extends RecyclerView.Adapter<adapter_LeaveRecycler.GenericViewHolder> {

    private List<model_pro_hr_leavereq> arrayList;
    private int recyclerViewID;
    private Context mContext;
    private static RecyclerViewClickListener itemListener;
    private String[] menuitems;

    @Override
    public void onViewRecycled(@NonNull GenericViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public List<model_pro_hr_leavereq> getArrayList() {
        return arrayList;
    }

    public void setArrayList(List<model_pro_hr_leavereq> arrayList) {
        this.arrayList = arrayList;
    }

    public int getRecyclerViewID() {
        return recyclerViewID;
    }

    public void setRecyclerViewID(int recyclerViewID) {
        this.recyclerViewID = recyclerViewID;
    }

    public int getTxtleavereqdateid() {
        return txtleavereqdateid;
    }

    public void setTxtleavereqdateid(int txtleavereqdateid) {
        this.txtleavereqdateid = txtleavereqdateid;
    }

    public int getClconframeid() {
        return clconframeid;
    }

    public void setClconframeid(int clconframeid) {
        this.clconframeid = clconframeid;
    }

    private int txtleavereqdateid;
    private int clconframeid;

    public int getTxtleavereqstartid() {
        return txtleavereqstartid;
    }

    public void setTxtleavereqstartid(int txtleavereqstartid) {
        this.txtleavereqstartid = txtleavereqstartid;
    }

    public int getTxtleavereqendid() {
        return txtleavereqendid;
    }

    public void setTxtleavereqendid(int txtleavereqendid) {
        this.txtleavereqendid = txtleavereqendid;
    }

    public int getTxtleavereqtypeid() {
        return txtleavereqtypeid;
    }

    public void setTxtleavereqtypeid(int txtleavereqtypeid) {
        this.txtleavereqtypeid = txtleavereqtypeid;
    }

    public int getTxtleavereqdaysid() {
        return txtleavereqdaysid;
    }

    public void setTxtleavereqdaysid(int txtleavereqdaysid) {
        this.txtleavereqdaysid = txtleavereqdaysid;
    }

    private int txtleavereqstartid;
    private int txtleavereqendid;
    private int txtleavereqtypeid;
    private int txtleavereqdaysid;
    private int txtleavereqidid;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setTxtleavereqidid(int txtSelectLeaveReqID) {
        this.txtleavereqidid = txtSelectLeaveReqID;
    }

    public void setMenuitems(String[] menuitems) {
        this.menuitems = menuitems;
    }

    class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener,
            MenuItem.OnMenuItemClickListener {
        TextView txtLeaveReqDate, txtleavestart, txtleaveend, txtleavetype, txtleavedays, txtreqid;
        ConstraintLayout conframe;

        GenericViewHolder(View view, int reqdate, int frame, int reqstart, int reqend, int reqtype, int reqdays, int reqid) {
            super(view);
            txtLeaveReqDate = view.findViewById(reqdate);
            txtleavereqdateid = reqdate;

            txtleavedays = view.findViewById(reqdays);
            txtleavereqdaysid = reqdays;

            txtleavestart = view.findViewById(reqstart);
            txtleavereqstartid = reqstart;

            txtleaveend = view.findViewById(reqend);
            txtleavereqendid = reqend;

            txtleavetype = view.findViewById(reqtype);
            txtleavereqtypeid = reqtype;

            txtreqid = view.findViewById(reqid);
            txtleavereqidid = reqid;

            conframe = view.findViewById(frame);
            clconframeid = frame;

            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
           // if (v.getId() == R.id.leaveActivity_LVleaveItems) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt(sharedprefcodes.activity_hr.reqposition, this.getAdapterPosition());
            editor.apply();

                String[] StrmenuItems = menuitems;
                for (int i = 0; i < StrmenuItems.length; i++) {
                    menu.add(Menu.NONE, i, i, StrmenuItems[i]);
                }
           // }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return true;
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getAdapterPosition());
        }
    }

    public adapter_LeaveRecycler(List<model_pro_hr_leavereq> arrayList, int recyclerViewID, Context context, RecyclerViewClickListener itemListener) {
        this.arrayList = arrayList;
        this.recyclerViewID = recyclerViewID;
        this.mContext = context;
        this.itemListener = itemListener;
    }

    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(recyclerViewID, parent, false);
        return new GenericViewHolder(itemView, txtleavereqdateid, clconframeid, txtleavereqstartid,txtleavereqendid,txtleavereqtypeid,txtleavereqdaysid,txtleavereqidid);
    }

    public void onBindViewHolder(GenericViewHolder holder, int position) {
        int item = holder.getAdapterPosition();
        model_pro_hr_leavereq modelProArAsset = arrayList.get(position);
        try {
            holder.txtLeaveReqDate.setText(String.valueOf(modelProArAsset.getDate_created()));
            holder.txtleavetype.setText(String.valueOf(modelProArAsset.getLeave_type()));
            holder.txtleavedays.setText(String.valueOf(modelProArAsset.getLeave_count_requested()));
            holder.txtleavestart.setText(String.valueOf(modelProArAsset.getLeave_date_from()));
            holder.txtleaveend.setText(String.valueOf(modelProArAsset.getLeave_date_to()));
            holder.txtreqid.setText(String.valueOf(modelProArAsset.getLeave_request_id()));

            if (modelProArAsset.getApproved() == 1) {
                holder.conframe.setBackgroundResource(R.drawable.approved_leave);
            } else if (modelProArAsset.getApproved() == -1) {
                holder.conframe.setBackgroundResource(R.drawable.rejected_leave);
            }

        } catch (NullPointerException ignore) {}
    }

    public int getItemCount() {
        try {
            return arrayList.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

}
