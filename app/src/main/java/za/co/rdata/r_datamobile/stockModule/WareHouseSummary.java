package za.co.rdata.r_datamobile.stockModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.AppConfig;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.DBScripts;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.DBMeta.sharedprefcodes;
import za.co.rdata.r_datamobile.Models.model_pro_stk_stock;
import za.co.rdata.r_datamobile.Models.model_pro_stk_warehouse;
import za.co.rdata.r_datamobile.SelectJob;
import za.co.rdata.r_datamobile.adapters.adapter_JobRecycler;
import za.co.rdata.r_datamobile.adapters.adapter_WarehouseSummaryRecycler;
import za.co.rdata.r_datamobile.locationTools.GetLocation;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_stk_scan;
import za.co.rdata.r_datamobile.R;

/**
 * Created by James de Scande on 20/11/2017 at 09:15.
 */

public class WareHouseSummary extends AppCompatActivity {

    String strWarehousecode;
    String strWarehousedesc;
    public sqliteDBHelper sqliteDbHelper;
    String mob;

    @Override
    protected void onResume() {
        super.onResume();
        getstock();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent warehousesummintent = getIntent();
        strWarehousecode  = warehousesummintent.getStringExtra("WAREHOUSE NAME");
        strWarehousedesc  = warehousesummintent.getStringExtra("WAREHOUSE DESC");
        setContentView(R.layout.activity_warehouse_summary);
        sqliteDbHelper = new sqliteDBHelper(getBaseContext());

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mob = sharedPref.getString(sharedprefcodes.activity_startup.node_id, "");

        //getstock();

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

   /* public class adapter_WarehouseSummary extends ArrayAdapter<model_pro_stk_scan> {

        Context mContext;
        ArrayList<model_pro_stk_scan> arrStockValues;
        View itemview;

        class ViewHolderItem {
            TextView txtStockName;
            TextView txtStockCount;
            ConstraintLayout constraintLayout;
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
                viewHolder.constraintLayout = itemview.findViewById(R.id.clwarehousesummary);
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

            //if (!stockscan.getStk_take_qty().equals(null)) {viewHolder.constraintLayout.setBackgroundResource(R.drawable.stock_item_scanned);}

            curStockDesc.close();

            return itemview;
        }
    }
*/
    @Override
    public void onBackPressed() {
        Intent intentwarehouse = new Intent(WareHouseSummary.this, SelectWarehouse.class);
        intentwarehouse.putExtra("WAREHOUSE NAME", strWarehousecode);
        intentwarehouse.putExtra("WAREHOUSE DESC", strWarehousedesc);
        startActivity(intentwarehouse);
    }

    private void getstock() {

        String TAG = "req_leave";
        RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.setMessage("Logging in ...");
        //showDialog();
        String instnode = "";
        if (MainActivity.NODE_ID.length() == 4) {
            instnode = MainActivity.NODE_ID.substring(0, 2);
        } else {
            instnode = MainActivity.NODE_ID.substring(0, 1);
        }
        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        try {
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_stk_stock.ddl);
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_stk_scan.ddl);
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_stk_notes.ddl);
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_stk_no_access.ddl);

            String combinedurl = AppConfig.URL_STK + "?instnode=" + instnode + "" +
                    "&whse=" + strWarehousecode;
            StringRequest strReqMenu = new StringRequest(Request.Method.GET,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        // Check for error node in json
                        if (!error) {
                            JSONArray stockarray = jObj.getJSONArray("stock");
                            JSONArray stockitem = new JSONArray();//array = menu.getJSONArray("menu");

                            int arrSize = stockarray.length();
                            model_pro_stk_stock model_pro_stk_stock = null;
                            for (int i = 0; i < arrSize; ++i) {

                                stockitem = stockarray.getJSONArray(i);
                                //JSONObject leave = leaveitem.getJSONObject(0);

                                model_pro_stk_stock = new model_pro_stk_stock(
                                        stockitem.get(0).toString(),
                                        stockitem.get(1).toString(),
                                        stockitem.getString(2),
                                        stockitem.getString(3),
                                        stockitem.getString(4),
                                        stockitem.getString(5),
                                        stockitem.getInt(6),
                                        stockitem.getInt(7),
                                        stockitem.getInt(8),
                                        stockitem.getDouble(9),
                                        stockitem.getString(10),
                                        stockitem.getString(11),
                                        stockitem.getString(12),
                                        stockitem.getInt(13)
                                );
                                MainActivity.sqliteDbHelper.addStk(model_pro_stk_stock);
                            }
                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("error_msg");
                            //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (SQLiteConstraintException e) {
                        e.printStackTrace();
                    }// finally {
                        populatestocklist();
                    //}

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    //hideDialog();
                }
            });
            queue.add(strReqMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populatestocklist() {
        Cursor curStock = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_stk_stock WHERE whse_code = '"+strWarehousecode+"' ORDER BY stk_bin",null);
        curStock.moveToFirst();

        SelectWarehouse selectWarehouse = new SelectWarehouse();

        ArrayList<model_pro_stk_scan> arrStockScans = new ArrayList<>();

        if (curStock.getCount() != 0) {
            do {

                Cursor curStockScan = null;
                try {
                    curStockScan = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_stk_scan WHERE stk_code = '" + curStock.getString(curStock.getColumnIndex(meta.pro_stk_stock.stk_code)) + "'", null);
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

                Double[] coord = new Double[3]; //GetLocationHere(getBaseContext());
                try {
                    coord[0] = curStockScan.getDouble(curStockScan.getColumnIndex(meta.pro_stk_scan.stk_gps_master_lat));
                    coord[1] = curStockScan.getDouble(curStockScan.getColumnIndex(meta.pro_stk_scan.stk_gps_master_long));
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }

                int stockqty = -999;
                try {
                    stockqty = curStockScan.getInt(curStockScan.getColumnIndex(meta.pro_stk_scan.stk_take_qty));
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }

                try {
                    arrStockScans.add(new model_pro_stk_scan(curStock.getString(curStock.getColumnIndex(
                            meta.pro_stk_stock.InstNode_id)),
                            "",
                            0,
                            strWarehousecode,
                            curStock.getString(curStock.getColumnIndex(meta.pro_stk_stock.stk_bin)),
                            curStock.getString(curStock.getColumnIndex(meta.pro_stk_stock.stk_code)),
                            "",
                            stockqty,
                            "",
                            nacode,
                            note_code,
                            diffreasons,
                            comments,
                            coord[0],
                            coord[1],
                            0d,
                            0d,
                            MainActivity.NODE_ID,
                            0));
                    curStockScan.close();
                    curStock.moveToNext();
                } catch (NullPointerException | CursorIndexOutOfBoundsException ignore) {
                }
            }
            while (!curStock.isLast());
        }
        curStock.close();

        adapter_WarehouseSummaryRecycler adapter_warehouseSummaryRecycler = new adapter_WarehouseSummaryRecycler(arrStockScans, R.layout.select_warehouse_summary);
        adapter_warehouseSummaryRecycler.setRetStockcode(R.id.txtStockCount);
        adapter_warehouseSummaryRecycler.setRetstockdesc(R.id.txtStockDesc);
        adapter_warehouseSummaryRecycler.setRetframe(R.id.clwarehousesummary);
        adapter_warehouseSummaryRecycler.setmContext(WareHouseSummary.this);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(WareHouseSummary.this, DividerItemDecoration.VERTICAL);
        RecyclerView recyclerView = findViewById(R.id.lstWarehosueSumm);
        recyclerView.removeItemDecoration(itemDecorator);
        itemDecorator.setDrawable(ContextCompat.getDrawable(WareHouseSummary.this, R.drawable.recycler_divider_large));
        recyclerView.addItemDecoration(itemDecorator);
        ///recyclerView.setDrawable(new DividerItemDecoration(this,R.drawable.recycler_divider));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter_warehouseSummaryRecycler);

        TextView lblWareSumm = findViewById(R.id.lblWarehouseSummTitle);
        lblWareSumm.setText("WAREHOUSE: "+strWarehousedesc);

        FloatingActionButton fltScanStock = findViewById(R.id.flbScan);
        fltScanStock.setOnClickListener(view -> {
            Intent stockscan = new Intent(WareHouseSummary.this, StockScanActivity.class);
            stockscan.putExtra(sharedprefcodes.activity_stores.whcode, strWarehousecode);
            stockscan.putExtra(sharedprefcodes.activity_stores.whdesc, strWarehousedesc);
            startActivity(stockscan);
        });
    }

}
