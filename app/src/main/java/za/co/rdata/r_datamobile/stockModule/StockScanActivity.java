package za.co.rdata.r_datamobile.stockModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.PermissionChecker;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import za.co.rdata.r_datamobile.DBHelpers.DBHelperStock;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.fileTools.EmailCreator;
import za.co.rdata.r_datamobile.locationTools.GetLocation;
import za.co.rdata.r_datamobile.scanTools.IntentIntegrator;
import za.co.rdata.r_datamobile.scanTools.IntentResult;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_stk_scan;
import za.co.rdata.r_datamobile.Models.model_pro_stk_stock;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectAsset;
import za.co.rdata.r_datamobile.SelectWarehouse;
import za.co.rdata.r_datamobile.stringTools.SelectNoteActivity;

/**
 * Created by James de Scande on 20/11/2017 at 15:34.
 */

public class StockScanActivity extends AppCompatActivity {

    private static final String TAG = "StockScanActivity: ";
    int intentcode = -1;
    int currentlayout = -1;
    int GET_NOTE_CODE;

    String scanBin = null;
    String stocknotes = "";
    String scantype = null;
    String input_value;
    private String displaywarehousecode;
    private String displaywarehousedesc;
    String mob;

    model_pro_stk_scan scannedstock;
    model_pro_stk_stock stockinfo;

    Context mContext = getBaseContext();

    Double[] coord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mob = sharedPref.getString("node id", "");

        displaywarehousecode  = getIntent().getStringExtra("WAREHOUSE NAME");
        displaywarehousedesc  = getIntent().getStringExtra("WAREHOUSE DESC");

        ScanBin();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (intentcode == 1) {

            super.onActivityResult(requestCode, resultCode, intent);
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            if (scanningResult.getContents()!=null) {
                scanBin = scanningResult.getContents();
                scantype = "S";
                MakeBin();
            } else {
                scantype = "K";
                Inputbox();
            }
        }

        if (intentcode == 2) {

            TextView txtNotes = findViewById(R.id.txtStockNotes);
            String tempnotes = stocknotes;

            try {
                stocknotes = intent.getStringExtra("note_description");
            } catch (NullPointerException e) {
                stocknotes = tempnotes;
            } finally {
                txtNotes.setText(stocknotes);
                DBHelperStock.updateStockNotes(stocknotes, scanBin);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        if (intentcode == 0) {
            if (scanBin == null) {
                Inputbox();
            } else {
                scantype = "s";
                MakeBin();
            }
        }
        */
    }

    public void ScanBin() {

            intentcode = 1;

            coord = GetLocationHere(getBaseContext());
            IntentIntegrator scanBinintent = new IntentIntegrator(this);
            scanBinintent.setBeepEnabled(false);
            scanBinintent.setOrientationLocked(false);
            scanBinintent.initiateScan();
    }

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

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void MakeBin() {

        currentlayout = 1;

        GetStockBinInfo();
        GetScanBinInfo();
        Cursor notecursor = null;
        setContentView(R.layout.activity_stock_capture);

        try {
            notecursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                    meta.pro_stk_notes.TableName,
                    null, meta.pro_stk_notes.pro_stk_no_code + " = ?", new String[]{scannedstock.getStk_note_code()}, null, null, null, null);
            notecursor.moveToLast();
        } catch (IllegalArgumentException ignore) {}

        TextView txtNotes = findViewById(R.id.txtStockNotes);

        CommentChanger();

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

    public void GetScanBinInfo() {

        /////////////Bin Info Query
        Cursor bincursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                meta.pro_stk_stock.TableName,
                null, meta.pro_stk_stock.stk_bin + " = ?", new String[]{scanBin}, null, null, null, null);
        bincursor.moveToLast();

        /////////////Username Info Query
        Cursor usernamecursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                meta.pro_sys_users.TableName,
                null, meta.pro_sys_users.mobnode_id + " = ?", new String[]{mob}, null, null, null, null);
        usernamecursor.moveToLast();

        /////////////Company Info Query

        Log.d(TAG, "mobinst id is " + mob.substring(0, 1));

        Cursor companycursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                meta.pro_sys_company.TableName,
                null, meta.pro_sys_company.InstNode_id + " = ?", new String[]{mob.substring(0, 1)}, null, null, null, null);
        companycursor.moveToLast();

        int stockcycle = companycursor.getInt(companycursor.getColumnIndex(meta.pro_sys_company.st_cycle));

        /////////////Scan Info Query
        Cursor scanstockcursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                meta.pro_stk_scan.TableName,
                null, meta.pro_stk_scan.stk_bin + " = ?", new String[]{scanBin}, null, null, null, null);
        scanstockcursor.moveToLast();

        scannedstock = new model_pro_stk_scan(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        scannedstock.setStk_take_cycle(stockcycle);

        scannedstock.setWhse_code(displaywarehousecode);
        scannedstock.setStk_bin(scanBin);
        scannedstock.setStk_code(bincursor.getString(bincursor.getColumnIndex(meta.pro_stk_stock.stk_code)));
        scannedstock.setStk_bin_scan_type(scantype);

        try {
            scannedstock.setStk_take_qty(scanstockcursor.getInt(scanstockcursor.getColumnIndex(meta.pro_stk_scan.stk_take_qty)));
        } catch (NullPointerException | CursorIndexOutOfBoundsException e) {
            scannedstock.setStk_take_qty(0);
        }

        SelectWarehouse selectWarehouse = new SelectWarehouse();
        scannedstock.setStk_scan_date(selectWarehouse.MakeDate());

        try {
            scannedstock.setStk_na_code(scanstockcursor.getString(scanstockcursor.getColumnIndex(meta.pro_stk_scan.stk_na_code)));
        } catch (NullPointerException | CursorIndexOutOfBoundsException e) {
            scannedstock.setStk_na_code("");
        }

        try {
            scannedstock.setStk_note_code(scanstockcursor.getString(scanstockcursor.getColumnIndex(meta.pro_stk_scan.stk_note_code)));
        } catch (NullPointerException | CursorIndexOutOfBoundsException e) {
            scannedstock.setStk_note_code("");
        }

        try {
            scannedstock.setStk_diff_reason(scanstockcursor.getString(scanstockcursor.getColumnIndex(meta.pro_stk_scan.stk_diff_reason)));
        } catch (NullPointerException | CursorIndexOutOfBoundsException e) {
            scannedstock.setStk_diff_reason("");
        }

        try {
            scannedstock.setStk_comments(scanstockcursor.getString(scanstockcursor.getColumnIndex(meta.pro_stk_scan.stk_comments)));
        } catch (NullPointerException | CursorIndexOutOfBoundsException e) {
            scannedstock.setStk_comments("");
        }

        scannedstock.setStk_gps_master_long(coord[0]);
        scannedstock.setStk_gps_master_lat(coord[1]);
        scannedstock.setStk_gps_read_long(coord[2]);
        scannedstock.setStk_gps_read_lat(coord[3]);
        scannedstock.setStk_user_code(usernamecursor.getString(usernamecursor.getColumnIndex(meta.pro_sys_users.username)));
        scannedstock.setStk_status(1);

        bincursor.close();
        usernamecursor.close();
        companycursor.close();
        scanstockcursor.close();
    }

    public void GetStockBinInfo() {

        Log.d(TAG, "scan id is " + scanBin);
        stockinfo = new model_pro_stk_stock(null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        Cursor bincursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                meta.pro_stk_stock.TableName,
                null, meta.pro_stk_stock.stk_bin+ " = ?", new String[]{scanBin}, null, null, null, null);

/*
        SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();
        Cursor bincursor = db.rawQuery("SELECT * FROM pro_stk_stock WHERE stk_bin ='" + scanBin + "'", null);
  */
        bincursor.moveToLast();

        //Log.d(TAG, bincursor.getString(bincursor.getColumnIndex(meta.pro_stk_stock.stk_code)));
        //Log.d(TAG, displaywarehousecode);

        stockinfo.setWhse_code(displaywarehousecode);
        try {
            stockinfo.setStk_code(bincursor.getString(bincursor.getColumnIndex(meta.pro_stk_stock.stk_code)));
            stockinfo.setStk_descrip(bincursor.getString(bincursor.getColumnIndex(meta.pro_stk_stock.stk_descrip)));
            stockinfo.setStk_bin(bincursor.getString(bincursor.getColumnIndex(meta.pro_stk_stock.stk_bin)));
            stockinfo.setStk_qty(bincursor.getInt(bincursor.getColumnIndex(meta.pro_stk_stock.stk_qty)));
            stockinfo.setStk_reorder(bincursor.getInt(bincursor.getColumnIndex(meta.pro_stk_stock.stk_reorder)));

            stockinfo.setStk_max_level(bincursor.getInt(bincursor.getColumnIndex(meta.pro_stk_stock.stk_max_level)));

            stockinfo.setStk_fuel(bincursor.getString(bincursor.getColumnIndex(meta.pro_stk_stock.stk_fuel)));
            stockinfo.setStk_category(bincursor.getString(bincursor.getColumnIndex(meta.pro_stk_stock.stk_category)));
            stockinfo.setStk_unit_desc(bincursor.getString(bincursor.getColumnIndex(meta.pro_stk_stock.stk_unit_desc)));
            stockinfo.setStk_display_qty(bincursor.getInt(bincursor.getColumnIndex(meta.pro_stk_stock.stk_display_qty)));

            bincursor.close();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            finish();
            Toast.makeText(this,"Bin not found",Toast.LENGTH_SHORT).show();
        }
    }

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
            Intent newwarehouse = new Intent(StockScanActivity.this, SelectWarehouse.class);
            startActivity(newwarehouse);
        }
        return true;
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

    class SaveData extends AsyncTask<model_pro_stk_scan, String, Boolean> {

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

    View.OnLongClickListener listen_noteTextView = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            intentcode = 2;
            SelectNoteActivity.tablename = meta.pro_stk_notes.TableName;
            Intent intent = new Intent(StockScanActivity.this, SelectNoteActivity.class);
            startActivityForResult(intent, GET_NOTE_CODE);      ///////Starts Instance Of Notes for Note Value in Registry
            return true;
        }
    };

    View.OnTouchListener listen_binQtyTextView = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            final EditText txtBinQty = findViewById(R.id.txtBinQty);
            txtBinQty.getText().clear();           ///////Clears AdjRemainder EditText

            return false;
        }
    };

}
