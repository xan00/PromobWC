package za.co.rdata.r_datamobile;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import za.co.rdata.r_datamobile.DBHelpers.DBHelperStock;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.DBScripts;
import za.co.rdata.r_datamobile.Models.model_pro_stk_options;

public class SelectStores extends AppCompatActivity {

    private String TAG = null;
    private List<model_pro_stk_options> menuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stk_options);
        getstkmenus();

        menuItems = DBHelperStock.pro_stk_options.GetStockMenuByUser(MainActivity.NODE_ID);
        ArrayAdapter<model_pro_stk_options> adapter = new SelectStores.stkItems_ListAdapter();
        ListView listView = findViewById(R.id.selectemployee_LVmenuItems);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            TextView TVmodule = view.findViewById(R.id.menu_list_item_program);
            try {
                Intent intent = new Intent(SelectStores.this, Class.forName(TVmodule.getText().toString()));
                finish();
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent gotomain = new Intent(SelectStores.this, MainActivity.class);
        startActivity(gotomain);
    }

    public SelectStores() {
    }

    private void getstkmenus() {

        String tag_string_req = "req_login";
        RequestQueue queue = Volley.newRequestQueue(this);
        //pDialog.setMessage("Logging in ...");
        //showDialog();
        MainActivity.sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());
        try {
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(DBScripts.pro_stk_options.ddl);


        String combinedurl = AppConfig.URL_STKOPTIONS + "?mobnode_id=" + MainActivity.NODE_ID + "";
        StringRequest strReqMenu = new StringRequest(Request.Method.GET,
                combinedurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        JSONArray menuarray = jObj.getJSONArray("menu");
                        JSONArray menuitem = new JSONArray();//array = menu.getJSONArray("menu");

                        int arrSize = menuarray.length();
                        model_pro_stk_options model_pro_stk_options = null;
                        for (int i = 0; i < arrSize; ++i) {

                            menuitem = menuarray.getJSONArray(i);
                            //JSONObject menu = menuitem.getJSONObject(0);

                            model_pro_stk_options = new model_pro_stk_options(
                                    menuitem.get(0).toString(),
                                    menuitem.get(1).toString(),
                                    menuitem.get(2).toString(),
                                    menuitem.get(3).toString(),
                                    menuitem.get(4).toString()
                            );
                            MainActivity.sqliteDbHelper.addStockMenu(model_pro_stk_options);
                        }
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        });
        queue.add(strReqMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class stkItems_ListAdapter extends ArrayAdapter<model_pro_stk_options> {

        stkItems_ListAdapter() {
            super(SelectStores.this, R.layout.select_menu_list_item, menuItems);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.select_menu_list_item, parent, false);

            model_pro_stk_options menuItem = menuItems.get(position);

            try {
                TextView textview_menu_description = itemView.findViewById(R.id.menu_list_item_description);
                TextView textview_menu_program = itemView.findViewById(R.id.menu_list_item_program);

                textview_menu_description.setText(menuItem.getDesc());
                textview_menu_program.setText(menuItem.getMod());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return itemView;
        }
    }
}
