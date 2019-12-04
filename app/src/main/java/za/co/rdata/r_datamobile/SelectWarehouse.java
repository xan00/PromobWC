package za.co.rdata.r_datamobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.locationTools.GetLocation;
import za.co.rdata.r_datamobile.Models.model_pro_stk_scan;
import za.co.rdata.r_datamobile.Models.model_pro_stk_stock;
import za.co.rdata.r_datamobile.Models.model_pro_stk_warehouse;
import za.co.rdata.r_datamobile.stockModule.WareHouseSummary;

/**
 * Created by James de Scande on 22/08/2017 at 12:50.
 */

public class SelectWarehouse extends AppCompatActivity {

    public ArrayList<model_pro_stk_warehouse> listArray = new ArrayList<>();
    public View itemView;
    public String warehousecode;
    public String warehousedesc;
    static public String displaywarehousecode;
    static public String displaywarehousedesc;
    public model_pro_stk_scan scannedstock;
    public model_pro_stk_stock stockinfo;
    static public String mob;
    public String scanBin;
    public int intentcode;
    public double locaX;
    public double locaY;
    public String input_value;
    public String scantype;
    public int GET_NOTE_CODE;
    public String stocknotes;
    public int currentlayout;
    public String TAG = "STOCK: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mob = sharedPref.getString("node id", "");


        SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();
        Cursor warehouses = db.rawQuery("SELECT * FROM pro_stk_warehouse ORDER BY "+meta.pro_stk_warehouse.whse_code, null);
        warehouses.moveToFirst();

        listArray.clear();

        while (!warehouses.isAfterLast()) {
            listArray.add(new model_pro_stk_warehouse(null, null, warehouses.getInt(warehouses.getColumnIndex(meta.pro_stk_warehouse.whse_code)),
                    warehouses.getString(warehouses.getColumnIndex(meta.pro_stk_warehouse.whse_description))));
            warehouses.moveToNext();
        }
        warehouses.close();

        GetLocationHere();
        ChooseWarehouse();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void ChooseWarehouse() {

        currentlayout = 0;

        setContentView(R.layout.activity_select_warehouse);
        final ListView listWarehouseView = findViewById(R.id.activity_warehouse_list_listView);
        ArrayAdapter<model_pro_stk_warehouse> adapter;
        adapter = new SelectWarehouse.warehousedetails_ListAdapter();
        listWarehouseView.setAdapter(adapter);
    }

    private class warehousedetails_ListAdapter extends ArrayAdapter<model_pro_stk_warehouse> {

        warehousedetails_ListAdapter() {
            super(SelectWarehouse.this, R.layout.select_warehouse_name, listArray);
        }

        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            itemView = convertView;

            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.select_warehouse_name, parent, false);
            model_pro_stk_warehouse Warehouse = listArray.get(position);

            final int pos = position;

            TextView textview_warehouse = itemView.findViewById(R.id.txtWarehouse);
            TextView textview_waredescription = itemView.findViewById(R.id.txtWarehouseDes);

            textview_warehouse.setText(String.valueOf(Warehouse.getWhse_code()));
            textview_waredescription.setText(Warehouse.getWhse_description());

            /*
            Collections.sort(listArray, new Comparator<model_pro_stk_warehouse>() {
                public int compare(model_pro_stk_warehouse p1, model_pro_stk_warehouse p2) {
                    return String.valueOf(p1.getWhse_code()).compareTo(p2.getWhse_code());
                }
            });
*/

            View.OnClickListener warehouselistener = new View.OnClickListener() {
                public void onClick(View v) {
                    model_pro_stk_warehouse templist = listArray.get(pos);

                    displaywarehousecode = String.valueOf(templist.getWhse_code());
                    displaywarehousedesc = templist.getWhse_description();

                    Intent newwarehousesum = new Intent(SelectWarehouse.this, WareHouseSummary.class);
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

    /*
    public void ScanBin() {

        intentcode = 1;

        GetLocationHere();

        IntentIntegrator scanBinintent = new IntentIntegrator(this);
        scanBinintent.setBeepEnabled(false);
        scanBinintent.setOrientationLocked(false);
        scanBinintent.initiateScan();
    }
*/
/*
    public void Inputbox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Bin Barcode");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()

        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input_value = input.getText().toString().toUpperCase();
                if (input_value.length() == 5) {
                    intentcode = 0;
                    scanBin = input_value;
                    scantype = "k";
                    MakeBin();
                } else {
                    Toast.makeText(getBaseContext(), "Input value was the incorrect length, please try again", Toast.LENGTH_SHORT).show();
                    Inputbox();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()

        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ScanBin();
            }
        });
        builder.show();
    }
*/
/*
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void MakeBin() {

        currentlayout = 1;

        GetStockBinInfo();
        GetScanBinInfo();
        Cursor notecursor = null;

        try {
            notecursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                    meta.pro_stk_notes.TableName,
                    null, meta.pro_stk_notes.pro_stk_no_code + " = ?", new String[]{scannedstock.getStk_note_code()}, null, null, null, null);
            notecursor.moveToLast();
        } catch (IllegalArgumentException ignore) {}

        TextView txtNotes = findViewById(R.id.txtStockNotes);

        CommentChanger();

        setContentView(R.layout.activity_stock_capture);

        TextView txtBin = findViewById(R.id.txtBin);
        TextView txtStockCode = findViewById(R.id.txtStockCode);
        TextView txtDesc = findViewById(R.id.txtDescription);
        TextView txtCategory = findViewById(R.id.txtCategory);
        TextView txtUnitsofMeasure = findViewById(R.id.txtUnitofMeasure);
        TextView txtReorder = findViewById(R.id.txtReorder);
        TextView txtMaxLevel = findViewById(R.id.txtMaximum);
        TextView txtExpectedQty = findViewById(R.id.txtExpectedQty);

        TextView txtWarehouseHeader = findViewById(R.id.txtWarehouseHeader);
        txtWarehouseHeader.setText(displaywarehousecode + ": " + displaywarehousedesc);

        txtBin.setText(stockinfo.getStk_bin());
        txtStockCode.setText(stockinfo.getStk_code());
        txtDesc.setText(stockinfo.getStk_descrip());
        txtCategory.setText(stockinfo.getStk_category());
        txtUnitsofMeasure.setText(stockinfo.getStk_unit_desc());

        txtReorder.setText(String.valueOf(stockinfo.getStk_reorder()));
        txtMaxLevel.setText(String.valueOf(stockinfo.getStk_max_level()));
        txtExpectedQty.setText(String.valueOf(stockinfo.getStk_qty()));

        try {
            txtNotes.setText(notecursor.getString(notecursor.getColumnIndex(meta.pro_stk_notes.pro_stk_no_description)));
            notecursor.close();
        } catch (CursorIndexOutOfBoundsException | NullPointerException e) {
            txtNotes.setText("");
        }
        txtNotes.setOnLongClickListener(listen_noteTextView);



        Cursor scanstockcursor = MainActivity.sqliteDbHelper.getWritableDatabase().query(
                meta.pro_stk_scan.TableName,
                null, meta.pro_stk_scan.stk_bin + " = ?", new String[]{scanBin}, null, null, null, null);
        scanstockcursor.moveToLast();

        if (scanstockcursor.getCount() == 0) {
            Log.d(TAG, "Insert attempt made");
            SaveData saveData = new SaveData();
            saveData.execute(scannedstock);
        }

        scanstockcursor.close();

        Button btnnewbin = findViewById(R.id.btnScanNewBin);
        btnnewbin.setOnClickListener(listenernewbin);

        Button btnnewwarehouse = findViewById(R.id.btnNewWarehouse);
        btnnewwarehouse.setOnClickListener(listenernewwarehouse);

        final EditText txtBinQty = findViewById(R.id.txtBinQty);

        Cursor scanqtycursor = MainActivity.sqliteDbHelper.getWritableDatabase().query(
                meta.pro_stk_scan.TableName,
                null, meta.pro_stk_scan.stk_bin + " = ?", new String[]{scanBin}, null, null, null, null);
        scanqtycursor.moveToFirst();

        try {
            txtBinQty.setText(String.valueOf(scanqtycursor.getInt(scanqtycursor.getColumnIndex(meta.pro_stk_scan.stk_take_qty))));
        } catch (CursorIndexOutOfBoundsException e) {
            txtBinQty.setText(String.valueOf(0));
        }

        scanqtycursor.close();

        final TextView txtComments = findViewById(R.id.txtStockComments);
        txtComments.setText(scannedstock.getStk_comments());

        txtBinQty.setOnTouchListener(listen_binQtyTextView);
        txtBinQty.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                s = txtBinQty.getText();                ///////Extracts bin qty to String

                String templife = s.toString().trim();

                try {
                    scannedstock.setStk_take_qty(Integer.valueOf(templife));
                } catch (NullPointerException | NumberFormatException e) {
                    scannedstock.setStk_take_qty(0);
                } finally {
                    Log.d("SQL", "quantity change   "+scannedstock.getStk_take_qty());
                    DBHelperStock.updateStockQty(scannedstock.getStk_take_qty(), scanBin);
                }
            }
        });

        txtComments.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                s = (Editable) txtComments.getText();                ///////Extracts bin qty to String

                String templife = s.toString().trim();

                try {
                    scannedstock.setStk_comments(templife);
                } catch (NullPointerException e) {
                    scannedstock.setStk_comments("");
                } finally {
                    DBHelperStock.updateStockComments(scannedstock.getStk_comments(), scanBin);
                }
            }
        });

        FloatingActionButton fltMoreOptions = findViewById(R.id.fltStockOptions);
        registerForContextMenu(fltMoreOptions);

        fltMoreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanBin();
            }
        });
    }
*/
/*
    private class SaveData extends AsyncTask<model_pro_stk_scan, String, Boolean> {

        @Override
        protected Boolean doInBackground(model_pro_stk_scan... rows) {    ///////Async Task Handler For SQL Handling
            DBHelperStock.setBinScan(scannedstock);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean b) {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
*/
    @SuppressLint("SetTextI18n")
    private void MakeWarehouse() {

        currentlayout = 1;

        setContentView(R.layout.activity_stock_capture);
        TextView txtWarehouseHeader = findViewById(R.id.txtWarehouseHeader);
        txtWarehouseHeader.setText(displaywarehousecode + ": " + displaywarehousedesc);

    }

    public void GetLocationHere() {

        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(SelectWarehouse.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

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
        Intent mainmenu = new Intent(this, MainActivity.class);
        switch (currentlayout) {
            case 0:
                startActivity(mainmenu);
            case 1:
                ChooseWarehouse();
        }
    }

/*
    private void CommentChanger() {

        final TextView txtComments = findViewById(R.id.txtStockComments);

        try {
            txtComments.setText(scannedstock.getStk_comments());
        } catch (NullPointerException | CursorIndexOutOfBoundsException e) {
            txtComments.setText("");
        }

        txtComments.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                s = (Editable) txtComments.getText();                ///////Extracts bin qty to String

                String templife = s.toString().trim();

                try {
                    scannedstock.setStk_comments(templife);
                } catch (NullPointerException e) {
                    scannedstock.setStk_comments("");
                } finally {
                    DBHelperStock.updateStockComments(scannedstock.getStk_comments(), scanBin);
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Send Stock Report");
        menu.add(1, v.getId(), 0, "Go To Warehouses");
        //menu.add(6, v.getId(), 0, "Edit Asset");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        SelectAsset selectAsset = new SelectAsset();
        if (item.getTitle() == "Send Stock Report") {
            EmailCreator emailCreator = new EmailCreator();
            emailCreator.SendEmailforStock(scanBin, stockinfo.getStk_reorder(), stockinfo.getStk_max_level(), stockinfo.getStk_qty());
        } else if (item.getTitle() == "Go To Warehouses") {
           ChooseWarehouse();
        }
        return true;
    }
    */

    View.OnClickListener listenernewwarehouse = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ChooseWarehouse();

        }
    };

}

