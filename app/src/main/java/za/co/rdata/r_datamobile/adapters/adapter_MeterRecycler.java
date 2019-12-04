package za.co.rdata.r_datamobile.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_rows;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.meterReadingModule.MeterReaderController;
import za.co.rdata.r_datamobile.meterReadingModule.MeterReadingActivity;

public class adapter_MeterRecycler extends RecyclerView.Adapter<adapter_MeterRecycler.GenericViewHolder> {

    private List<model_pro_mr_route_rows> arrayList;
    private String InstNode;
    private String mobnode;
    private int recyclerViewID;
    private int recMetertype;
    private int recAddressName;
    private int recMeterNumber;
    private String route_number;
    private Context mContext;
    private int recFrame;

    public int getRecFrame() {
        return recFrame;
    }

    public void setRecFrame(int recFrame) {
        this.recFrame = recFrame;
    }

    public List<model_pro_mr_route_rows> getArrayList() {
        return arrayList;
    }

    public void setArrayList(List<model_pro_mr_route_rows> arrayList) {
        this.arrayList = arrayList;
    }

    public int getRecyclerViewID() {
        return recyclerViewID;
    }

    public void setRecyclerViewID(int recyclerViewID) {
        this.recyclerViewID = recyclerViewID;
    }

    public int getRecMetertype() {
        return recMetertype;
    }

    public void setRecMetertype(int recMetertype) {
        this.recMetertype = recMetertype;
    }

    public int getRecAddressName() {
        return recAddressName;
    }

    public void setRecAddressName(int recAddressName) {
        this.recAddressName = recAddressName;
    }

    public int getRecMeterNumber() {
        return recMeterNumber;
    }

    public void setRecMeterNumber(int recMeterNumber) {
        this.recMeterNumber = recMeterNumber;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getRoute_number() {
        return route_number;
    }

    public void setRoute_number(String route_number) {
        this.route_number = route_number;
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView TVmeterType, TVaddress, TVmeterNumber;
        ConstraintLayout conframe;
        //TextView textView_building;

        GenericViewHolder(View view, int metertype, int addressname, int meternumber, int frame) {
            super(view);
            recMetertype = metertype;
            recAddressName = addressname;
            recMeterNumber = meternumber;
            recFrame = frame;
            TVmeterType = view.findViewById(metertype);
            TVaddress = view.findViewById(addressname);
            TVmeterNumber = view.findViewById(meternumber);
            conframe = view.findViewById(frame);
        }
    }

    public adapter_MeterRecycler(List<model_pro_mr_route_rows> arrayList, int recyclerViewID, String route_number, String instNode, String mobnode) {
        this.arrayList = arrayList;
        this.recyclerViewID = recyclerViewID;
        this.route_number = route_number;
        this.InstNode = instNode;
        this.mobnode = mobnode;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(recyclerViewID,parent,false);
        itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, MeterReadingActivity.class);

            TextView textViewMeterNumber = view.findViewById(recMeterNumber);
            String meter_number = textViewMeterNumber.getText().toString();
            TextView textViewAddress = view.findViewById(recAddressName);
            String address = textViewAddress.getText().toString();

            MeterReaderController.RouteNumber = route_number;

            ArrayList<model_pro_mr_route_rows> route_rows;
            route_rows = DBHelper.pro_mr_route_rows.getRouteRows(InstNode, mobnode, "1", route_number);

            MeterReaderController.route_row_keys.clear();
            for (model_pro_mr_route_rows r : route_rows) {
                MeterReaderController.route_row_keys.add(new MeterReaderController.Keys(
                        r.getCycle(), r.getInstNode_id(), r.getMeter_id(), r.getMobnode_id(), r.getRoute_number(), r.getWalk_sequence(), r.getMeter_reading(), r.getNa_code(), r.getNote_code())
                );
            }

            MeterReaderController.notes.clear();
            MeterReaderController.notes = DBHelper.pro_mr_notes.getNotes();

            MeterReaderController.no_access.clear();
            MeterReaderController.no_access = DBHelper.pro_mr_no_access.getNoAccess();

            MeterReaderController.route_header = DBHelper.pro_mr_route_headers.getRouteHeader(InstNode, mobnode, "1", route_number);


            intent.putExtra("meter_number",meter_number);
            intent.putExtra("address_name",address);

            intent.putExtra("route_number",route_number);
            intent.putExtra("came_from_adapter",true);
            mContext.startActivity(intent);
        });
        return new GenericViewHolder(itemView, recMetertype, recAddressName, recMeterNumber, recFrame);
    }

    private int CheckStatus(model_pro_mr_route_rows model_pro_mr_route_rows) {
        if (model_pro_mr_route_rows.getMeter_reading() == null || model_pro_mr_route_rows.getMeter_reading() == -999d) {
            if (model_pro_mr_route_rows.getNa_code() == null || model_pro_mr_route_rows.getNa_code().isEmpty())
                return (R.drawable.room_item);
            else {
                return (R.drawable.meter_red);
            }
        } else {
            if (model_pro_mr_route_rows.getMeter_reading() > model_pro_mr_route_rows.getHigh_reading() ||
                    model_pro_mr_route_rows.getMeter_reading() < model_pro_mr_route_rows.getLow_reading())
                return (R.drawable.meter_yellow);
            else {
                return (R.drawable.meter_green);
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        model_pro_mr_route_rows modelProMrRouteRows = arrayList.get(position);
        try {
            holder.TVaddress.setText(String.format("%s %s", modelProMrRouteRows.getStreet_number(), modelProMrRouteRows.getAddress_name()));
            holder.TVmeterNumber.setText(String.format("%s",modelProMrRouteRows.getMeter_number()));
            holder.TVmeterType.setText(String.format("%s",modelProMrRouteRows.getMeter_type()));
            holder.conframe.setBackgroundResource(CheckStatus(modelProMrRouteRows));

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try {
            return arrayList.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

}
