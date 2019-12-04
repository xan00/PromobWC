package za.co.rdata.r_datamobile.stockModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.PermissionChecker;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.locationTools.GetLocation;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_stk_scan;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectAsset;
import za.co.rdata.r_datamobile.SelectWarehouse;

/**
 * Created by James de Scande on 20/11/2017 at 09:15.
 */

public class WareHouseSummary extends AppCompatActivity {

    String strWarehousecode;
    String strWarehousedesc;
    public sqliteDBHelper sqliteDbHelper;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent warehousesummintent = getIntent();
        strWarehousecode  = warehousesummintent.getStringExtra("WAREHOUSE NAME");
        strWarehousedesc  = warehousesummintent.getStringExtra("WAREHOUSE DESC");
        setContentView(R.layout.activity_warehouse_summary);
        sqliteDbHelper = new sqliteDBHelper(getBaseContext());

        Cursor curStock = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_stk_stock WHERE whse_code = '"+strWarehousecode+"' ORDER BY stk_bin",null);
        curStock.moveToFirst();

        SelectWarehouse selectWarehouse = new SelectWarehouse();

        ArrayList<model_pro_stk_scan> arrStockScans = new ArrayList<>();

        if (curStock.getCount() != 0) {
            do {

                Cursor curStockScan = null;
                try {
                    curStockScan = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_stk_scan WHERE stk_bin = '" + curStock.getString(curStock.getColumnIndex(meta.pro_stk_stock.stk_bin)) + "' ORDER BY stk_bin", null);
                    curStockScan.moveToFirst();
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }

                String comments = "";
                try {
                    comments = curStockScan.getString(curStockScan.getColumnIndex(meta.pro_stk_scan.stk_comments));
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }

                String diffreasons = "";
                try {
                    diffreasons = curStockScan.getString(curStockScan.getColumnIndex(meta.pro_stk_scan.stk_diff_reason));
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }

                String nacode = "";
                try {
                    nacode = curStockScan.getString(curStockScan.getColumnIndex(meta.pro_stk_scan.stk_na_code));
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }

                String note_code = "";
                try {
                    note_code = curStockScan.getString(curStockScan.getColumnIndex(meta.pro_stk_scan.stk_note_code));
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }

                Double[] coord = GetLocationHere(getBaseContext());
                try {
                    coord[0] = curStockScan.getDouble(curStockScan.getColumnIndex(meta.pro_stk_scan.stk_gps_master_lat));
                    coord[1] = curStockScan.getDouble(curStockScan.getColumnIndex(meta.pro_stk_scan.stk_gps_master_long));
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }

                int stockqty = 0;
                try {
                    stockqty = curStockScan.getInt(curStockScan.getColumnIndex(meta.pro_stk_scan.stk_take_qty));
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }

                try {
                    arrStockScans.add(new model_pro_stk_scan(curStock.getString(curStock.getColumnIndex(meta.pro_stk_stock.InstNode_id)),
                            "",
                            curStock.getString(curStock.getColumnIndex(meta.pro_stk_stock.stk_bin)),
                            "",
                            curStock.getString(curStock.getColumnIndex(meta.pro_stk_stock.stk_code)),
                            comments,
                            diffreasons,
                            coord[0],
                            coord[1],
                            coord[2],
                            coord[3],
                            nacode,
                            note_code,
                            selectWarehouse.MakeDate(),
                            1,
                            0,
                            stockqty,
                            MainActivity.NODE_ID,
                            strWarehousecode));
                    curStockScan.close();
                    curStock.moveToNext();
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }
            }
            while (!curStock.isLast());
        }
        curStock.close();

        ArrayAdapter<model_pro_stk_scan> adapter = new adapter_WarehouseSummary(arrStockScans);

        final ListView listView = findViewById(R.id.lstWarehosueSumm);
        listView.setAdapter(adapter);

        TextView lblWareSumm = findViewById(R.id.lblWarehouseSummTitle);
        lblWareSumm.setText("WAREHOUSE: "+strWarehousedesc);

        FloatingActionButton fltScanStock = findViewById(R.id.flbScan);
        fltScanStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stockscan = new Intent(WareHouseSummary.this, StockScanActivity.class);
                stockscan.putExtra("WAREHOUSE NAME", strWarehousecode);
                stockscan.putExtra("WAREHOUSE DESC", strWarehousedesc);
                startActivity(stockscan);
            }
        });

    }

    protected Double[] GetLocationHere(Context mContext) {

        Double[] coordinates =  new Double[]{0d, 0d, 0d, 0d};

        if (PermissionChecker.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {

            GetLocation gps = new GetLocation(this);
            if (gps.canGetLocation()) {
                coordinates[2] = gps.getLatitude();
                coordinates[3] = gps.getLongitude();
                // \n is for new line
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showAlertDialog();
            }
            gps.stopUsingGPS();
        }

        //SelectAsset selectAsset = new SelectAsset();
        //coordinates = selectAsset.GetLocationHere();
        return coordinates;
    }

    public class adapter_WarehouseSummary extends ArrayAdapter<model_pro_stk_scan> {

        Context mContext;
        ArrayList<model_pro_stk_scan> arrStockValues;
        View itemview;

        class ViewHolderItem {
            TextView txtStockName;
            TextView txtStockCount;
        }

        adapter_WarehouseSummary(ArrayList<model_pro_stk_scan> arrStockValues) {
            super(WareHouseSummary.this,R.layout.select_warehouse_summary, arrStockValues);
            this.arrStockValues = arrStockValues;
            this.mContext = WareHouseSummary.this;
        }

        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            itemview = convertView;
            ViewHolderItem viewHolder;
            final int pos = position;
            viewHolder = new ViewHolderItem();

            if (itemview == null) {
                itemview = getLayoutInflater().inflate(R.layout.select_warehouse_summary, parent, false);
                viewHolder.txtStockName = itemview.findViewById(R.id.txtStockName);
                viewHolder.txtStockCount = itemview.findViewById(R.id.txtStockCount);
                itemview.setTag(viewHolder);
            } else {
                try {
                    viewHolder = (ViewHolderItem) convertView.getTag();
                } catch (ClassCastException ignore) {}
            }

            model_pro_stk_scan stockscan = arrStockValues.get(pos);

            Cursor curStockDesc = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT stk_descrip FROM pro_stk_stock WHERE stk_bin = '"+stockscan.getStk_bin()+"'",null);
            curStockDesc.moveToFirst();

            viewHolder.txtStockName.setText(curStockDesc.getString(0));
            viewHolder.txtStockName.setTag(curStockDesc.getString(0));

            viewHolder.txtStockCount.setText(String.valueOf(stockscan.getStk_take_qty()));
            viewHolder.txtStockCount.setTag(String.valueOf(stockscan.getStk_take_qty()));

            curStockDesc.close();

            return itemview;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intentwarehouse = new Intent(WareHouseSummary.this, SelectWarehouse.class);
        intentwarehouse.putExtra("WAREHOUSE NAME", strWarehousecode);
        intentwarehouse.putExtra("WAREHOUSE DESC", strWarehousedesc);
        startActivity(intentwarehouse);
    }
}
