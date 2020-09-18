package za.co.rdata.r_datamobile.stockModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import za.co.rdata.r_datamobile.AppConfig;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.DBScripts;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.DBMeta.sharedprefcodes;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_fo_jobs;
import za.co.rdata.r_datamobile.Models.model_pro_stk_basket;
import za.co.rdata.r_datamobile.Models.model_pro_stk_scan;
import za.co.rdata.r_datamobile.Models.model_pro_stk_stock;
import za.co.rdata.r_datamobile.Models.model_pro_stk_warehouse;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectStores;
import za.co.rdata.r_datamobile.locationTools.GetLocation;

/**
 * Created by James de Scande on 22/08/2017 at 12:50.
 */

public class SelectBasket extends AppCompatActivity {

    static public String displaywarehousecode;
    static public String displaywarehousedesc;
    static public String mob;
    public ArrayList<model_pro_stk_basket> listArray = new ArrayList<>();
    public View itemView;
    public String warehousecode;
    public String warehousedesc;
    public model_pro_stk_scan scannedstock;
    public model_pro_stk_stock stockinfo;
    public String scanBin;
    public int intentcode;
    public double locaX;
    public double locaY;
    public String input_value;
    public String scantype;
    public int GET_NOTE_CODE;
    public String stocknotes;
    public int currentlayout;
    public String TAG = "BASKET: ";
    /*View.OnClickListener listenernewwarehouse = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ChooseWarehouse();

        }
    };
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mob = sharedPref.getString(sharedprefcodes.activity_startup.node_id, "");

        getbaskets();

    }

    private void populatebasketlist(String inst) {
        SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();

        try {
            Cursor baskets = db.rawQuery("SELECT InstNode_id, mobnode_id, basket_id, job_id FROM pro_stk_basket WHERE InstNode_id = "+inst, null);
            baskets.moveToFirst();

            listArray.clear();

            while (!baskets.isAfterLast()) {
                model_pro_stk_basket model_pro_stk_basket =
                        new model_pro_stk_basket(baskets.getString(0), baskets.getString(1), baskets.getInt(2),
                        baskets.getInt(3));

                Cursor jobs = db.rawQuery("SELECT pro_fo_job_type FROM pro_fo_jobs WHERE mobnode_id = '"+mob+"' and pro_fo_job_no = "+model_pro_stk_basket.getJob_id(), null);
                jobs.moveToFirst();

                model_pro_stk_basket.setBasket_job_type(jobs.getInt(0));
                jobs.close();

                listArray.add(model_pro_stk_basket);
                baskets.moveToNext();
            }
            baskets.close();

            //GetLocationHere();
            ChooseWarehouse();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Toast.makeText(this, meta.pro_stk_basket.TableName + " not found", Toast.LENGTH_SHORT).show();
            Intent backtomenu = new Intent(SelectBasket.this, MainActivity.class);
            startActivity(backtomenu);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getbaskets() {

        String tag_string_req = "req_baskets";
        RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.setMessage("Logging in ...");
        //showDialog();
        String instnode = "";
        if (MainActivity.NODE_ID.length() >= 4) {
            instnode = MainActivity.NODE_ID.substring(0, 2);
        } else {
            instnode = MainActivity.NODE_ID.substring(0, 1);
        }
        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        try {
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_stk_basket.ddl);
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_stk_basket_contents.ddl);

            String combinedurl = AppConfig.URL_STKGETBASKETS
                    + "?inst=" + instnode;
            String finalInstnode = instnode;
            StringRequest strReqMenu = new StringRequest(Request.Method.GET,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        // Check for error node in json
                        if (!error) {
                            JSONArray basketarray = jObj.getJSONArray("basket");
                            JSONArray basketitem = new JSONArray();

                            int arrSize = basketarray.length();
                            model_pro_stk_basket model_pro_stk_basket = null;
                            for (int i = 0; i < arrSize; ++i) {

                                basketitem = basketarray.getJSONArray(i);

                                model_pro_stk_basket = new model_pro_stk_basket(
                                        basketitem.get(0).toString(),
                                        basketitem.get(1).toString(),
                                        basketitem.getInt(2),
                                        basketitem.getInt(3)
                                );
                                    MainActivity.sqliteDbHelper.addStkBasket(model_pro_stk_basket);
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
                    } finally {
                        populatebasketlist(finalInstnode);
                    }

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

    private void getJobs() {

        String tag_string_req = "req_jobs";
        RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.setMessage("Logging in ...");
        //showDialog();
        String instnode = "";
        if (MainActivity.NODE_ID.length() >= 4) {
            instnode = MainActivity.NODE_ID.substring(0, 2);
        } else {
            instnode = MainActivity.NODE_ID.substring(0, 1);
        }
        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        try {
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_fo_jobs.ddl);

            String combinedurl = AppConfig.URL_FOGETJOBS
                    + "?inst=" + instnode;
            String finalInstnode = instnode;
            StringRequest strReqMenu = new StringRequest(Request.Method.GET,
                    combinedurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        // Check for error node in json
                        if (!error) {
                            JSONArray jobarray = jObj.getJSONArray("job");
                            JSONArray jobitem = new JSONArray();

                            int arrSize = jobarray.length();
                            model_pro_fo_jobs model_pro_fo_jobs = null;
                            for (int i = 0; i < arrSize; ++i) {

                                jobitem = jobarray.getJSONArray(i);

                                model_pro_fo_jobs = new model_pro_fo_jobs(
                                        jobitem.getString(0),
                                        jobitem.getString(1),
                                        jobitem.getInt(2),
                                        jobitem.getInt(3),
                                        jobitem.getString(4),
                                        String.valueOf(jobitem.get(5)),
                                        String.valueOf(jobitem.get(6)),
                                        jobitem.getDouble(7),
                                        jobitem.getDouble(8),
                                        jobitem.getInt(9),
                                        String.valueOf(jobitem.get(10))
                                );
                                MainActivity.sqliteDbHelper.addFOJob(model_pro_fo_jobs);
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
                    } finally {
                        populatebasketlist(finalInstnode);
                    }

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

    private void ChooseWarehouse() {

        currentlayout = 0;

        setContentView(R.layout.activity_select_basket);
        final ListView listWarehouseView = findViewById(R.id.activity_basket_list_listView);
        ArrayAdapter<model_pro_stk_basket> adapter;
        adapter = new SelectBasket.warehousedetails_ListAdapter();
        listWarehouseView.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    private void MakeWarehouse() {

        currentlayout = 1;

        setContentView(R.layout.activity_stock_capture);
        TextView txtWarehouseHeader = findViewById(R.id.txtWarehouseHeader);
        txtWarehouseHeader.setText(displaywarehousecode + ": " + displaywarehousedesc);

    }

    public void GetLocationHere() {

        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(SelectBasket.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            GetLocation gps = new GetLocation(this);
            if (gps.canGetLocation()) {
                locaX = (gps.getLatitude());
                locaY = (gps.getLongitude());
            } else {
                gps.showAlertDialog();
            }
        }
    }

    public String MakeDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss aa", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onBackPressed() {
        Intent mainmenu = new Intent(this, SelectStores.class);
        switch (currentlayout) {
            case 0:
                startActivity(mainmenu);
            case 1:
                ChooseWarehouse();
        }
    }

    private class warehousedetails_ListAdapter extends ArrayAdapter<model_pro_stk_basket> {

        warehousedetails_ListAdapter() {
            super(SelectBasket.this, R.layout.select_basket_item, listArray);
        }

        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            itemView = convertView;

            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.select_basket_item, parent, false);
            model_pro_stk_basket basket = listArray.get(position);

            final int pos = position;

            TextView textview_warehouse = itemView.findViewById(R.id.txtWarehouse);
            TextView textview_waredescription = itemView.findViewById(R.id.txtWarehouseDes);
            TextView txtBasketDesc = itemView.findViewById(R.id.txtJobDesc);

            textview_warehouse.setText(String.valueOf(basket.getBasket_id()));
            textview_waredescription.setText(String.valueOf(basket.getJob_id()));
            txtBasketDesc.setText(String.valueOf(basket.getBasket_job_type()));

            /*
            Collections.sort(listArray, new Comparator<model_pro_stk_warehouse>() {
                public int compare(model_pro_stk_warehouse p1, model_pro_stk_warehouse p2) {
                    return String.valueOf(p1.getWhse_code()).compareTo(p2.getWhse_code());
                }
            });
*/

            View.OnClickListener warehouselistener = new View.OnClickListener() {
                public void onClick(View v) {
                    model_pro_stk_basket templist = listArray.get(pos);

                    displaywarehousecode = String.valueOf(templist.getBasket_id());
                    displaywarehousedesc = String.valueOf(templist.getJob_id());

                    Intent newwarehousesum = new Intent(SelectBasket.this, SelectBasketContent.class);
                    newwarehousesum.putExtra("WAREHOUSE NAME", displaywarehousecode);
                    newwarehousesum.putExtra("WAREHOUSE DESC", displaywarehousedesc);
                    startActivity(newwarehousesum);
                }
            };

            itemView.setClickable(true);
            itemView.setOnClickListener(warehouselistener);

            return itemView;
        }
    }

}

