package za.co.rdata.r_datamobile.assetModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.GalleryActivity;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_rows;
import za.co.rdata.r_datamobile.Models.model_pro_ar_scanasset;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectAsset;
import za.co.rdata.r_datamobile.adapters.adapter_RoomScanning;
import za.co.rdata.r_datamobile.fileTools.ActivityLogger;
import za.co.rdata.r_datamobile.fragments.fragment_MakeAssetViewContent;
import za.co.rdata.r_datamobile.scanTools.IntentIntegrator;
import za.co.rdata.r_datamobile.scanTools.IntentResult;
import za.co.rdata.r_datamobile.stringTools.SelectDescriptionActivity;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static za.co.rdata.r_datamobile.DBHelpers.DBHelperAssets.pro_ar_asset_rows.GetLocationEntry;
import static za.co.rdata.r_datamobile.stringTools.MakeDate.GetDate;

/**
 * Created by James de Scande on 27/10/2017 at 08:14.
 */

public class PopulateRoomActivity extends AppCompatActivity {

    private static final int GET_INPUT_CODE = 5001;

    Context mContext = PopulateRoomActivity.this;
    Activity mActivity = (Activity) mContext;

    public View itemView;

    public ViewPager viewPager;

    Cursor curAsset;
    Cursor curScanned;

    //sqliteDBHelper sqliteDb = sqliteDBHelper.getInstance(this.getApplicationContext());
    //SQLiteDatabase db = sqliteDb.getReadableDatabase();

    String strSelectedRoom;
    String strLocationscantype;
    String strLocationdesc;
    String strResponsibleperson;
    String barcode;

    public static int intentcode = -1;
    int GET_DESC_CODE = 4;
    int scancycle;
    int intPagePosition;
    int lightcolour;
    int itemviewid;
    int currentlayout;
    String strDetailName="ROOM CODE: ";
    String strDetailName2="BARCODE: ";

    TextView txtRoomNumber;
    TextView txtAssetTally;
    TextView txtCurrentItem;

    model_pro_ar_scanasset scan;
    model_pro_ar_asset_rows data;

    private static SQLiteDatabase db;

    ArrayList<fragment_MakeAssetViewContent> listAssetFragments;
    private String newbarcodevalue = "";

////////////////////////////////////////////Initialise////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_holder);
        viewPager = findViewById(R.id.pgRoomAssets);
        currentlayout = 0;
        db = MainActivity.sqliteDbHelper.getReadableDatabase();

        Intent iPoproom = getIntent();
        Bundle bSaved = iPoproom.getExtras();

        assert bSaved != null;
        strSelectedRoom = bSaved.getString("ROOM SCAN");
        scancycle = bSaved.getInt("SCAN CYCLE");
        strLocationscantype = bSaved.getString("LOCATION SCAN TYPE");
        strLocationdesc = bSaved.getString("LOCATION NAME");
        strResponsibleperson = bSaved.getString("RESPONSIBLE PERSON");
        itemviewid = bSaved.getInt("SUMMARY VALUE");
        barcode = bSaved.getString("BARCODE");
        lightcolour = bSaved.getInt("LIGHT COLOUR");

        FloatingActionButton fltMoreOptions = findViewById(R.id.fltMoreOptions);
        registerForContextMenu(fltMoreOptions);

        fltMoreOptions.setOnClickListener(listenerscan);

        mContext = getApplicationContext();

        curAsset = db.rawQuery("SELECT reg_barcode FROM pro_ar_register WHERE reg_barcode = '" + barcode + "'", null);
        curAsset.moveToFirst();

        curScanned = db.rawQuery("SELECT scan_barcode FROM pro_ar_scan WHERE scan_barcode = '" + barcode + "' and scan_location_entry not like '" +
                strSelectedRoom + "'", null);
        curScanned.moveToFirst();

        txtRoomNumber = findViewById(R.id.txtCurrentRoom);
        txtAssetTally = findViewById(R.id.lblAssetTally);
        txtCurrentItem = findViewById(R.id.txtCurrentItem);


        Cursor curAssetsinroom = db.rawQuery("SELECT reg_barcode FROM pro_ar_register WHERE reg_location_code = '" + strSelectedRoom + "'", null);
        curAssetsinroom.moveToFirst();

        String sqlstring = "SELECT scan_barcode FROM pro_ar_scan WHERE scan_location = '" + strSelectedRoom + "' and scan_location_entry = '" +
                strSelectedRoom + "'";

        Cursor curCorrectScanCount = db.rawQuery(sqlstring,null); // + "' and scan_location_entry = '" +
                //strSelectedRoom + "'", null);
        curCorrectScanCount.moveToFirst();

        txtAssetTally.setText(curCorrectScanCount.getCount() + "/" + curAssetsinroom.getCount());

        listAssetFragments = new ArrayList<>();

        MakeScanAssetData makeScanAssetData = new MakeScanAssetData(barcode, strSelectedRoom);
        makeScanAssetData.setmContext(PopulateRoomActivity.this);

        fragment_MakeAssetViewContent assetViewContent;
        assetViewContent = new fragment_MakeAssetViewContent();
        data = makeScanAssetData.MakeAssetData();
        scan = makeScanAssetData.MakeAsset(scancycle, "S", strLocationscantype);

        txtRoomNumber.setText(strSelectedRoom);

        txtCurrentItem = findViewById(R.id.txtCurrentItem);
        txtCurrentItem.bringToFront();
        lightcolour = lightColourChecking(data,scan);
        txtCurrentItem.setBackgroundResource(lightcolour);

        assetViewContent.setAssetdata(data);
        assetViewContent.setScannedassetdata(scan);
        assetViewContent.setLocationname(strLocationdesc);
        assetViewContent.setResponsibleperson(strResponsibleperson);
        assetViewContent.setScannedLightColour(lightcolour);

        try {
            listAssetFragments.add(assetViewContent);
        } catch (NullPointerException ignore) {
        }

        viewPager.setAdapter(null);
        adapter_RoomScanning adapter_roomScanning;
        adapter_roomScanning = new adapter_RoomScanning(getSupportFragmentManager(), listAssetFragments);
        viewPager.setAdapter(adapter_roomScanning);
        curAsset.close();
        curScanned.close();
        curAssetsinroom.close();
        curCorrectScanCount.close();
        viewPager.setCurrentItem(0);

    }

////////////////////////////////////////////Results////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if (intentcode == 1) {

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

//            assert scanningResult != null;
               try {
                   if (scanningResult.getContents() != null) {
                       barcode = scanningResult.getContents();
                       Intent roomintent = new Intent(PopulateRoomActivity.this, PopulateRoomActivity.class);
                       roomintent.putExtra("ROOM SCAN", strSelectedRoom);
                       roomintent.putExtra("CYCLE", scancycle);
                       roomintent.putExtra("LOCATION SCAN TYPE", "s");
                       roomintent.putExtra("LOCATION NAME", strLocationdesc);
                       roomintent.putExtra("RESPONSIBLE PERSON", strResponsibleperson);
                       roomintent.putExtra("BARCODE", barcode);
                       roomintent.putExtra("LIGHT COLOUR", lightcolour);
                       roomintent.putExtra("SUMMARY VALUE", itemviewid);
                       startActivity(roomintent);
                   } else {
                       Inputbox();
                   }
               } catch (NullPointerException e) {
                   Toast.makeText(PopulateRoomActivity.this,"Please Try Again" + barcode,Toast.LENGTH_LONG).show();
               }

////////////////////////////////////Notes Results///////////////////////////////////////////////////////////////////////////

        } else if (intentcode == 2) {

            try {

                TextView txtComment;
                String commentvalue = intent.getStringExtra("note_description");

                switch (commentvalue) {
                    case "NEW BARCODE":
                        InputboxNewBarcode(commentvalue);

                        break;
                    case "NEW DESCRIPTION":
                        PopulateRoomActivity.intentcode = 4;
                        Intent intentdesc = new Intent(this, SelectDescriptionActivity.class);
                        startActivityForResult(intentdesc, GET_DESC_CODE);
                        break;
                    default:

                        try {
                        listAssetFragments.get(intPagePosition).getScannedassetdata().setComments1(commentvalue);
                        //listAssetFragments.get(intPagePosition).getScannedassetdata().setNotes(commentvalue);
                    } catch (NullPointerException ignore) {
                    }

                    txtComment = Objects.requireNonNull(listAssetFragments.get(intPagePosition).getView()).findViewById(R.id.txtComments);
                    txtComment.setText(commentvalue);
                    SelectAsset.SaveComments saveComments = new SelectAsset.SaveComments();
                    saveComments.setBarcode(listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode());
                    saveComments.execute(listAssetFragments.get(intPagePosition).getScannedassetdata().getComments1());

                    Cursor curCurrentAssetComment = db.rawQuery("SELECT reg_comments1 FROM pro_ar_register WHERE reg_barcode = '" + listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode() + "'", null);
                    curCurrentAssetComment.moveToFirst();

                    if (CompareOneDBValues(curCurrentAssetComment, meta.pro_ar_register.reg_comments1, listAssetFragments.get(intPagePosition).getScannedassetdata().getComments1())) {
                        txtComment.setBackgroundResource(R.drawable.value_differs);
                    } else txtComment.setBackgroundResource(R.drawable.textinput_shape);

                    curCurrentAssetComment.close();
                    break;
                }
            } catch (NullPointerException ignore) {}

    } else if (intentcode == 21) {

        try {

            TextView txtComment;
            String commentvalue = intent.getStringExtra("note_description");

            switch (commentvalue) {
                case "NEW BARCODE":
                    InputboxNewBarcode(commentvalue);

                    break;
                case "NEW DESCRIPTION":
                    PopulateRoomActivity.intentcode = 41;
                    Intent intentdesc = new Intent(this, SelectDescriptionActivity.class);
                    startActivityForResult(intentdesc, GET_DESC_CODE);
                    break;
                default:
                    try {
                        listAssetFragments.get(intPagePosition).getScannedassetdata().setComments2(commentvalue);
                        //listAssetFragments.get(intPagePosition).getScannedassetdata().setNotes(commentvalue);
                    } catch (NullPointerException ignore) {
                    }

                    txtComment = Objects.requireNonNull(listAssetFragments.get(intPagePosition).getView()).findViewById(R.id.txtComments2);
                    txtComment.setText(commentvalue);
                    SelectAsset.SaveComments2 saveComments = new SelectAsset.SaveComments2();
                    saveComments.setBarcode(listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode());
                    saveComments.execute(listAssetFragments.get(intPagePosition).getScannedassetdata().getComments2());

                    Cursor curCurrentAssetComment = db.rawQuery("SELECT reg_comments2 FROM pro_ar_register WHERE reg_barcode = '" + listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode() + "'", null);
                    curCurrentAssetComment.moveToFirst();

                    if (CompareOneDBValues(curCurrentAssetComment, meta.pro_ar_register.reg_comments2, listAssetFragments.get(intPagePosition).getScannedassetdata().getComments2())) {
                        txtComment.setBackgroundResource(R.drawable.value_differs);
                    } else txtComment.setBackgroundResource(R.drawable.textinput_shape);

                    curCurrentAssetComment.close();

                    break;
            }
        } catch (NullPointerException ignore) {}

    } else
////////////////////////////////////Condition Results///////////////////////////////////////////////////////////////////////////

            if (intentcode == 3) {

                Cursor curCurrentAssetCondition = db.rawQuery("SELECT reg_condition_code FROM pro_ar_register WHERE reg_barcode = '" + listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode() + "'", null);
                curCurrentAssetCondition.moveToFirst();

                TextView txtCond;

                try {
                    listAssetFragments.get(intPagePosition).getScannedassetdata().setCondition(intent.getStringExtra("cond_description"));
                } catch (NullPointerException ignore) {
                }
                txtCond = Objects.requireNonNull(listAssetFragments.get(intPagePosition).getView()).findViewById(R.id.txtCondition);
                txtCond.setText(listAssetFragments.get(intPagePosition).getScannedassetdata().getCondition());

                SelectAsset.SaveCondition saveCondition = new SelectAsset.SaveCondition();
                saveCondition.setBarcode(listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode());
                saveCondition.execute(listAssetFragments.get(intPagePosition).getScannedassetdata().getCondition());

                if (CompareOneDBValues(curCurrentAssetCondition, meta.pro_ar_register.reg_condition_code, listAssetFragments.get(intPagePosition).getScannedassetdata().getCondition())) {
                    txtCond.setBackgroundResource(R.drawable.value_differs);
                } else txtCond.setBackgroundResource(R.drawable.textinput_shape);

                curCurrentAssetCondition.close();
            } else
////////////////////////////////////Description Results///////////////////////////////////////////////////////////////////////////

                if (intentcode == 4) {

                    Cursor curCurrentAssetDescription = db.rawQuery("SELECT reg_asset_desc FROM pro_ar_register WHERE reg_barcode = '" + listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode() + "'", null);
                    curCurrentAssetDescription.moveToFirst();

                    TextView txtDesc;
                    TextView txtActualDesc;

                    try {
                        listAssetFragments.get(intPagePosition).getScannedassetdata().setComments1("NEW DESC: "+intent.getStringExtra("desc_description"));
                    } catch (NullPointerException ignore) {}

                    txtDesc = Objects.requireNonNull(listAssetFragments.get(intPagePosition).getView()).findViewById(R.id.txtComments);
                    txtDesc.setText(listAssetFragments.get(intPagePosition).getScannedassetdata().getComments1());

                    txtActualDesc = Objects.requireNonNull(listAssetFragments.get(intPagePosition).getView()).findViewById(R.id.txtDescription);
                    txtActualDesc.setText(listAssetFragments.get(intPagePosition).getScannedassetdata().getComments1());
                    txtActualDesc.setBackgroundResource(R.drawable.textinput_shape);

                    SelectAsset.SaveComments saveDescription = new SelectAsset.SaveComments();
                    saveDescription.setBarcode(listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode());
                    saveDescription.execute(listAssetFragments.get(intPagePosition).getScannedassetdata().getComments1());

                    if (CompareOneDBValues(curCurrentAssetDescription, meta.pro_ar_register.reg_asset_desc, listAssetFragments.get(intPagePosition).getScannedassetdata().getComments1())) {
                        txtDesc.setBackgroundResource(R.drawable.value_differs);
                    } else txtDesc.setBackgroundResource(R.drawable.textinput_shape);

                    curCurrentAssetDescription.close();
                } else

                if (intentcode == 41) {

                    Cursor curCurrentAssetDescription = db.rawQuery("SELECT reg_asset_desc FROM pro_ar_register WHERE reg_barcode = '" + listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode() + "'", null);
                    curCurrentAssetDescription.moveToFirst();

                    TextView txtDesc;
                    TextView txtActualDesc;

                    try {
                        listAssetFragments.get(intPagePosition).getScannedassetdata().setComments2("NEW DESC: "+intent.getStringExtra("desc_description"));
                    } catch (NullPointerException ignore) {}

                    txtDesc = Objects.requireNonNull(listAssetFragments.get(intPagePosition).getView()).findViewById(R.id.txtComments2);
                    txtDesc.setText(listAssetFragments.get(intPagePosition).getScannedassetdata().getComments2());

                    txtActualDesc = Objects.requireNonNull(listAssetFragments.get(intPagePosition).getView()).findViewById(R.id.txtDescription);
                    txtActualDesc.setText(listAssetFragments.get(intPagePosition).getScannedassetdata().getComments2());
                    txtActualDesc.setBackgroundResource(R.drawable.textinput_shape);

                    SelectAsset.SaveComments2 saveDescription = new SelectAsset.SaveComments2();
                    saveDescription.setBarcode(listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode());
                    saveDescription.execute(listAssetFragments.get(intPagePosition).getScannedassetdata().getComments2());

                    if (CompareOneDBValues(curCurrentAssetDescription, meta.pro_ar_register.reg_asset_desc, listAssetFragments.get(intPagePosition).getScannedassetdata().getComments2())) {
                        txtDesc.setBackgroundResource(R.drawable.value_differs);
                    } else txtDesc.setBackgroundResource(R.drawable.textinput_shape);

                    curCurrentAssetDescription.close();
                }

/////////////////////////////////////Camera Results///////////////////////////////////////////////////////////////////////////

        if (intentcode == 6) {

            try {
                GoToGallery(barcode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

/////////////////////////////////////No Results///////////////////////////////////////////////////////////////////////////

            if (requestCode == GET_INPUT_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    int resultReading = intent.getIntExtra("Reading", 1);
                    int resultRetries = intent.getIntExtra("Retries", 0);
                    scan.setScan_quantity(resultReading);
                    scan.setScan_quan_retries(resultRetries);
                    //refreshFrameData(false, "M");
                    try {
                        MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("UPDATE pro_ar_scan SET scan_quantity=" + resultReading + ", scan_quan_retries=" + resultRetries + ",scan_type_barcode='M' WHERE scan_barcode='" + listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode() + "'");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    }
            }
    }

////////////////////////////////////////////Summary////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    private void refreshFrameData() {

            //intPagePosition = 0;

            if (intPagePosition != -1) {
                listAssetFragments.get(intPagePosition).getScannedassetdata().setScan_barcode(barcode);
                listAssetFragments.get(intPagePosition).getScannedassetdata().setScan_location(strSelectedRoom);
                listAssetFragments.get(intPagePosition).setScannedLightColour(R.color.AlreadyScanned);
                SelectAsset.SaveData saveScan = new SelectAsset.SaveData();
                saveScan.setBarcode(barcode);
                saveScan.setRoom(strSelectedRoom);
                saveScan.setType("M");
                saveScan.execute(listAssetFragments.get(intPagePosition).getScannedassetdata());
                String sqlstring = "SELECT scan_barcode FROM pro_ar_scan WHERE scan_location = '" + strSelectedRoom + "' and scan_location_entry = '" +
                        strSelectedRoom + "'";

                Cursor curCorrectScanCount = db.rawQuery(sqlstring, null);
                txtAssetTally.setText(curCorrectScanCount.getCount() + "/" + curAsset.getCount());
                curCorrectScanCount.close();
            } else {
                try {
                    MakeScanAssetData makeScanAssetData = new MakeScanAssetData(barcode, strSelectedRoom);
                    fragment_MakeAssetViewContent assetViewContent = new fragment_MakeAssetViewContent();
                    assetViewContent.setAssetdata(makeScanAssetData.MakeAssetData());
                    assetViewContent.setScannedassetdata(makeScanAssetData.MakeAsset(scancycle, "K", strLocationscantype));
                    assetViewContent.setLocationname(strLocationdesc);
                    assetViewContent.setResponsibleperson(strResponsibleperson);
                    listAssetFragments.add(assetViewContent);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
    }

////////////////////////////////////////////Input////////////////////////////////////////////////////////////////////////////////////////////

    public void InputboxNewBarcode(final String commentv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input New Barcode Value");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String input_value;
            input_value = input.getText().toString().toUpperCase();
            if (input_value.length() >= 5) {
                newbarcodevalue = input_value;

                String commentvalue = commentv;
                commentvalue = commentvalue + ": " + newbarcodevalue;
                try {
                    listAssetFragments.get(intPagePosition).getScannedassetdata().setComments1(commentvalue);
                    listAssetFragments.get(intPagePosition).getScannedassetdata().setNotes(commentvalue);
                } catch (NullPointerException ignore) {
                }

                TextView txtComment = Objects.requireNonNull(listAssetFragments.get(intPagePosition).getView()).findViewById(R.id.txtComments);
                txtComment.setText(commentvalue);
                SelectAsset.SaveComments saveComments = new SelectAsset.SaveComments();
                saveComments.setBarcode(listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode());
                saveComments.execute(listAssetFragments.get(intPagePosition).getScannedassetdata().getComments1());

                Cursor curCurrentAssetComment = db.rawQuery("SELECT reg_comments1 FROM pro_ar_register WHERE reg_barcode = '" + listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode() + "'", null);
                curCurrentAssetComment.moveToFirst();

                if (CompareOneDBValues(curCurrentAssetComment, meta.pro_ar_register.reg_comments1, listAssetFragments.get(intPagePosition).getScannedassetdata().getComments1())) {
                    txtComment.setBackgroundResource(R.drawable.value_differs);
                } else txtComment.setBackgroundResource(R.drawable.textinput_shape);

                curCurrentAssetComment.close();
            } else {
                Toast.makeText(getBaseContext(), "Input value was the incorrect length, please try again", Toast.LENGTH_SHORT).show();
                InputboxNewBarcode(commentv);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public void Inputbox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Asset Barcode");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String input_value;
            input_value = input.getText().toString().toUpperCase();
            if (input_value.length() >= 5) {
                barcode = input_value;
                refreshFrameData();
            } else {
                Toast.makeText(getBaseContext(), "Input value was the incorrect length, please try again", Toast.LENGTH_SHORT).show();
                Inputbox();
            }

            ActivityLogger activityLogger = new ActivityLogger(mContext);
            activityLogger.createLog("input_log"+MainActivity.USER,input_value,true,true,false);

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public void InputboxAsset() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Barcode");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String input_value;
            input_value = input.getText().toString().toUpperCase();

            Cursor assets = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT reg_barcode, reg_location_code FROM pro_ar_register WHERE reg_barcode = '" + input_value + "'",null);
            assets.moveToFirst();

            if (assets.getCount() !=0) {
                Intent roomintent = new Intent(PopulateRoomActivity.this, PopulateRoomActivity.class);
                //assets.move(assetposition);
                roomintent.putExtra("ROOM SCAN", assets.getString(1));
                roomintent.putExtra("CYCLE", scancycle);
                roomintent.putExtra("LOCATION SCAN TYPE", "k");

                Cursor location = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT loc_name, loc_person FROM pro_ar_locations WHERE loc_code = '"+assets.getString(1)+"'",null);
                location.moveToFirst();

                roomintent.putExtra("LOCATION NAME", location.getString(0));
                roomintent.putExtra("RESPONSIBLE PERSON", location.getString(1));
                roomintent.putExtra("BARCODE",input_value);
                location.close();

                MakeScanAssetData makeScanAssetData = new MakeScanAssetData(input_value, assets.getString(1));
                makeScanAssetData.setmContext(PopulateRoomActivity.this);

                assets.close();

                model_pro_ar_asset_rows tempdata = makeScanAssetData.MakeAssetData();
                model_pro_ar_scanasset tempscan = makeScanAssetData.MakeAsset(scancycle, "s", "s");

                roomintent.putExtra("LIGHT COLOUR",lightColourChecking(tempdata,tempscan));

                roomintent.putExtra("SUMMARY VALUE",itemviewid);
                startActivity(roomintent);
            } else {
                Toast.makeText(getBaseContext(), "Input value Not Found, please try again", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        input.setFocusable(true);
        builder.show();
    }

    public void ScanToSearch() {
        intentcode = 1;
        //Intent selectAsset = new Intent(PopulateRoomActivity.this, SelectAsset.class);
        //selectAsset.putExtra("TO ROOMS",4);
        //selectAsset.putExtra("SAVE SCAN",false);
        //selectAsset.putExtra("ROOM SCAN",strSelectedRoom);
        //startActivity(selectAsset);
        SelectAsset selectAsset = new SelectAsset(mContext,mActivity);
        selectAsset.ScanSearchAsset();
    }

////////////////////////////////////////////ContextMenus////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        String sqlstring = "select parm_value from pro_sys_parms where parm = 'manual_scanning' or parm='mark_invest' or parm='dispose/undispose' or parm = 'camera_active' order by parm";
        Cursor moreoptionsparms = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery(sqlstring,null);
        moreoptionsparms.moveToFirst();

        menu.add(0, v.getId(), 0, "Go To Asset");

        try {
            if (moreoptionsparms.getInt(0)==1) {
        menu.add(1, v.getId(), 0, "Go To Gallery");
            }
        } catch (Exception ignore) {}

        moreoptionsparms.moveToNext();

        menu.add(2, v.getId(), 0, "Go To Summary");

        try {
            if (moreoptionsparms.getInt(0)==1) {
            menu.add(3, v.getId(), 0, "Un/Mark as Disposed");
            }
        } catch (Exception ignore) {}

        moreoptionsparms.moveToNext();

        try {
            if (moreoptionsparms.getInt(0)==1 && barcode.startsWith(getResources().getString(R.string.manual_tag))) {
            menu.add(4, v.getId(), 0, "Un/Mark as Seen");
            }
        } catch (Exception ignore) {}

        moreoptionsparms.moveToNext();

        try {
            if (moreoptionsparms.getInt(0)==1) {
                menu.add(5, v.getId(), 0, "Mark for Investigation");
            }
        } catch (Exception ignore) {}

        menu.add(6, v.getId(), 0, "Check Barcode");
        moreoptionsparms.close();
    }

    private int GetCycle() {
        int cycle;
        try {
            Cursor getcycle = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT ar_cycle FROM pro_sys_company", null);
            getcycle.moveToLast();
            cycle = getcycle.getInt(getcycle.getColumnIndex(meta.pro_sys_company.ar_cycle));
            getcycle.close();
        } catch (CursorIndexOutOfBoundsException e) {
            cycle = 0;
        }
        return cycle;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Go To Asset") {
            InputboxAsset();

        } else if (item.getTitle() == "Go To Gallery") {
            GoToGallery(barcode);

        } else if (item.getTitle() == "Un/Mark as Disposed") {
            SelectAsset.SaveDisposedState saveDisposedState = new SelectAsset.SaveDisposedState();
            saveDisposedState.setBarcode(data.getReg_barcode());
            fragment_MakeAssetViewContent tempFragForDisposed = (listAssetFragments.get(intPagePosition));
            if (tempFragForDisposed.getAssetdata().getActive().equals(true)) {
                saveDisposedState.execute("0");
                tempFragForDisposed.getAssetdata().setActive(false);
                txtCurrentItem = findViewById(R.id.txtCurrentItem);
                txtCurrentItem.setBackgroundResource(R.color.PreviouslyDisposed);
            } else {
                saveDisposedState.execute("1");
                tempFragForDisposed.getAssetdata().setActive(true);
                txtCurrentItem = findViewById(R.id.txtCurrentItem);
                lightcolour = lightColourChecking(tempFragForDisposed.getAssetdata(),tempFragForDisposed.getScannedassetdata());
                txtCurrentItem.setBackgroundResource(lightcolour);
            }

        } else if (item.getTitle() == "Go To Summary") {

            Intent roomintent = new Intent(PopulateRoomActivity.this, RoomMainSummary.class);
            roomintent.putExtra("TO ROOMS",2);
            roomintent.putExtra("ROOM SCAN", strSelectedRoom);
            roomintent.putExtra("SUMMARY VALUE",itemviewid);
            startActivity(roomintent);
/*
        } else if (item.getTitle() == "Edit Asset") {

            if (listAssetFragments.get(intPagePosition).getAssetdata().getManual()) {
                Toast.makeText(getBaseContext(), "You Can Now Edit The Description", Toast.LENGTH_SHORT).show();
                listAssetFragments.get(intPagePosition).setScannedLightColour(R.drawable.room_item_manually_added);
            } else
                Toast.makeText(getBaseContext(), "Only Unregistered Assets Can be Editted", Toast.LENGTH_SHORT).show();
*/
        } else if (item.getTitle() == "Un/Mark as Seen") {

            if (listAssetFragments.get(intPagePosition).getAssetdata().getReg_barcode().startsWith((getResources().getString(R.string.manual_tag)))) {

                Cursor currentexists =  db.rawQuery("SELECT scan_barcode FROM pro_ar_scan WHERE scan_barcode = '" + barcode + "'", null);

                if (currentexists.getCount()>0) {
                    MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("DELETE FROM pro_ar_scan WHERE scan_barcode = '" + barcode + "'");


                    Cursor curAssetsinroom = db.rawQuery("SELECT reg_barcode FROM pro_ar_register WHERE reg_location_code = '" + strSelectedRoom + "'", null);
                    curAssetsinroom.moveToFirst();

                    Cursor curCorrectScanCount = db.rawQuery("SELECT scan_barcode FROM pro_ar_scan WHERE scan_location = '" + strSelectedRoom + "' and scan_location_entry = '" + strSelectedRoom + "';", null);
                    curCorrectScanCount.moveToFirst();

                    currentexists.close();

                    txtAssetTally.setText(curCorrectScanCount.getCount() + "/" + curAssetsinroom.getCount());
                    curAssetsinroom.close();
                    curCorrectScanCount.close();

                    TextView txtScanRoom = listAssetFragments.get(intPagePosition).getView().findViewById(R.id.txtScannedRoom);
                    txtScanRoom.setText("not scanned");

                    TextView txtScanDate = listAssetFragments.get(intPagePosition).getView().findViewById(R.id.txtScannedDate);
                    txtScanDate.setText("not scanned");

                    TextView txtColour = findViewById(R.id.txtCurrentItem);
                    txtColour.setBackgroundResource(R.color.ManualScanned);

                    //Toast.makeText(getBaseContext(), "Asset Unmarked", Toast.LENGTH_SHORT).show();

                } else {

                SelectAsset.SaveData saveScan = new SelectAsset.SaveData();
                saveScan.setBarcode(barcode);
                saveScan.setRoom(strSelectedRoom);
                saveScan.setType("M");
                listAssetFragments.get(intPagePosition).getScannedassetdata().setScan_type_barcode("M");
                listAssetFragments.get(intPagePosition).getScannedassetdata().setScan_cycle(GetCycle());

                //model_pro_ar_scanasset scannedmanual = listAssetFragments.get(intPagePosition).getScannedassetdata();
                listAssetFragments.get(intPagePosition).getScannedassetdata().setScan_location(strSelectedRoom);

                Long scan_id = Long.parseLong(barcode.substring(1,4)+ listAssetFragments.get(intPagePosition).getScannedassetdata().getMobnode_id()+strSelectedRoom.substring(1,5));
                Integer route_nr = Integer.parseInt(barcode.substring(1,4));

                listAssetFragments.get(intPagePosition).getScannedassetdata().setScan_id(scan_id);
                listAssetFragments.get(intPagePosition).getScannedassetdata().setRoute_nr(route_nr);

                model_pro_ar_scanasset scannedmanual = listAssetFragments.get(intPagePosition).getScannedassetdata();

                /*
                saveScan.setScanneddata(scannedmanual);
                try {
                    saveScan.execute(scannedmanual);
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/

                TextView txtColour = findViewById(R.id.txtCurrentItem);
                txtColour.setBackgroundResource(R.drawable.room_item_manual_and_scanned);

                Cursor curAssetsinroom = db.rawQuery("SELECT reg_barcode FROM pro_ar_register WHERE reg_location_code = '" + strSelectedRoom + "'", null);
                curAssetsinroom.moveToFirst();

                Cursor curCorrectScanCount = db.rawQuery("SELECT scan_barcode FROM pro_ar_scan WHERE scan_location = '" + strSelectedRoom + "' and scan_location_entry = '" + strSelectedRoom + "';", null);
                curCorrectScanCount.moveToFirst();

                TextView txtScanRoom = listAssetFragments.get(intPagePosition).getView().findViewById(R.id.txtScannedRoom);
                txtScanRoom.setText(strSelectedRoom);

                TextView txtScanDate = listAssetFragments.get(intPagePosition).getView().findViewById(R.id.txtScannedDate);
                txtScanDate.setText(listAssetFragments.get(intPagePosition).getScannedassetdata().getDate());

                //Toast.makeText(getBaseContext(), "Asset Marked", Toast.LENGTH_SHORT).show();

                    ActivityLogger activityLogger = new ActivityLogger(mContext);
                    activityLogger.createLog("manual_log",barcode,true,true,true,listAssetFragments.get(intPagePosition).getScannedassetdata().getScan_quantity());

                    String sqlstring = "INSERT INTO pro_ar_scan (" +
                            meta.pro_ar_scan.InstNode_id + "," +
                            meta.pro_ar_scan.mobnode_id + "," +
                            meta.pro_ar_scan.scan_cycle + "," +
                            meta.pro_ar_scan.scan_id + "," +
                            meta.pro_ar_scan.scan_reader_id + "," +
                            meta.pro_ar_scan.scan_datetime + "," +
                            meta.pro_ar_scan.scan_location + "," +
                            meta.pro_ar_scan.scan_location_entry + "," +
                            meta.pro_ar_scan.scan_type_location + "," +
                            meta.pro_ar_scan.scan_barcode + "," +
                            meta.pro_ar_scan.scan_barcode_entry + "," +
                            meta.pro_ar_scan.scan_type_barcode + "," +
                            meta.pro_ar_scan.scan_notes + "," +
                            meta.pro_ar_scan.scan_user + "," +
                            meta.pro_ar_scan.scan_lattitude + "," +
                            meta.pro_ar_scan.scan_longitude + "," +
                            meta.pro_ar_scan.scan_adj_remainder + "," +
                            meta.pro_ar_scan.scan_condition + "," +
                            meta.pro_ar_scan.scan_comments1 + "," +
                            meta.pro_ar_scan.scan_comments2 + "," +
                            meta.pro_ar_scan.scan_comments3 + "," +
                            meta.pro_ar_scan.scan_readqty + "," +
                            meta.pro_ar_scan.scan_retries + "," +
                            meta.pro_ar_scan.scan_route_nr + ")" +
                            " VALUES ('" +
                            scannedmanual.getInstaNode() + "','" +
                            "','" +
                            scannedmanual.getScan_cycle() + "','" +
                            barcode + scannedmanual.getMobnode_id() + strSelectedRoom.substring(1, 5) + "','" +
                            MainActivity.USER + "','" +
                            GetDate("/") + "','" +
                            strSelectedRoom + "','" +
                            GetLocationEntry(barcode) + "','" +
                            scannedmanual.getScan_type_location() + "','" +
                            barcode + "','" +
                            "','" +
                            "S" + "','" +
                            scannedmanual.getNotes() + "','" +
                            scannedmanual.getUser() + "','" +
                            scannedmanual.getCoordX() + "','" +
                            scannedmanual.getCoordY() + "','" +
                            scannedmanual.getAdjremainder() + "','" +
                            scannedmanual.getCondition() + "','" +
                            scannedmanual.getComments1() + "','" +
                            scannedmanual.getComments2() + "','" +
                            "','" +
                            scan.getScan_quantity() + "','" +
                            scan.getScan_quan_retries() + "','" +
                            scannedmanual.getScan_barcode() + "')";

                    try {
                        MainActivity.sqliteDbHelper.getWritableDatabase().execSQL(sqlstring);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("INSERT MANUAL","");
                        Toast.makeText(getBaseContext(), "Insert failed, please try again", Toast.LENGTH_SHORT).show();
                    }


                    String sqlstringquantity = "select parm_value from pro_sys_parms where parm = 'manual_quantity'";
                    Cursor curQuantity = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery(sqlstringquantity,null);
                    curQuantity.moveToFirst();


                    if (curQuantity.getString(0).equals("1")) {
                        try {
                            int qtyformanual = listAssetFragments.get(intPagePosition).getAssetdata().getReg_qtyformanual();

                            if (qtyformanual > 1) {
                                Intent intent = new Intent(this, InputQuantity.class);
                                intent.putExtra("Quantity", qtyformanual);
                                //intentcode=-9;
                                startActivityForResult(intent, GET_INPUT_CODE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(), "Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    curQuantity.close();

                curCorrectScanCount.requery();
                    curAssetsinroom.requery();

                    txtAssetTally.setText(curCorrectScanCount.getCount() + "/" + curAssetsinroom.getCount());
                    curAssetsinroom.close();
                    curCorrectScanCount.close();

                }

            }

            else {
                Toast.makeText(getBaseContext(), "Asset Unavailable for Editing", Toast.LENGTH_SHORT).show();
            }

        }

        else if (item.getTitle() == "Mark for Investigation") {

            SelectAsset.SaveInvestigationState saveInvestigateState = new SelectAsset.SaveInvestigationState();
            saveInvestigateState.setBarcode(data.getReg_barcode());
            fragment_MakeAssetViewContent tempFragforInvest = (listAssetFragments.get(intPagePosition));
            if (tempFragforInvest.getAssetdata().getActive().equals(true)) {
                saveInvestigateState.execute("0");
                tempFragforInvest.getAssetdata().setReg_investigate(false);
                txtCurrentItem = findViewById(R.id.txtCurrentItem);
                txtCurrentItem.setBackgroundResource(R.drawable.room_item_previously_disposed);
            } else {
                saveInvestigateState.execute("1");
                tempFragforInvest.getAssetdata().setReg_investigate(true);
                txtCurrentItem = findViewById(R.id.txtCurrentItem);
                lightcolour = lightColourChecking(tempFragforInvest.getAssetdata(),tempFragforInvest.getScannedassetdata());
                txtCurrentItem.setBackgroundResource(lightcolour);
            }

        } else if (item.getTitle() == "Check Barcode") {
            ScanToSearch();

        } else {
            return false;
        }
        return true;
    }

////////////////////////////////////////////General////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean CompareOneDBValues(Cursor firstcursorname, String columnfirstname, String checkvalue) {  ////////DB Entry Comparison

        String valueone;
        try {
            valueone = firstcursorname.getString(firstcursorname.getColumnIndex(columnfirstname));

            return !valueone.equals(checkvalue);
        } catch (NullPointerException e) {
            return true;
        } catch (CursorIndexOutOfBoundsException e) {
            return false;
        }
    }
/*
    public int SearchArrayListForAssetHeader(Cursor searchdb, String searchterm) {

        searchdb.moveToFirst();

        for (int i = 0; i < searchdb.getCount(); i++) {
            //fragment_MakeAssetViewContent model = listname.get(i);

            if (searchdb.getString(searchdb.getColumnIndex(meta.pro_ar_register.reg_barcode)).equals(searchterm)) {
                return i;
            }
            searchdb.moveToNext();
        }
        return -1;
    }
    */

    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed() {

        Intent gotomainsummary = new Intent(PopulateRoomActivity.this,RoomMainSummary.class);
        gotomainsummary.putExtra("ROOM SCAN",strSelectedRoom);
        startActivity(gotomainsummary);

        /*
        switch (backcode) {
            case 0:
                Intent scanintent = new Intent(PopulateRoomActivity.this, PopulateRoomActivity.class);
                scanintent.putExtra("ROOM SCAN", strSelectedRoom);
                scanintent.putExtra("CYCLE", scancycle);
                scanintent.putExtra("LOCATION SCAN TYPE", "s");
                scanintent.putExtra("LOCATION NAME", strLocationdesc);
                scanintent.putExtra("RESPONSIBLE PERSON", strResponsibleperson);
                scanintent.putExtra("BARCODE",barcode);
                scanintent.putExtra("LIGHT COLOUR",lightcolour);
                scanintent.putExtra("SUMMARY VALUE",itemviewid);
                startActivity(scanintent);
                break;
            case -1:
                Intent roomintent = new Intent(PopulateRoomActivity.this, SelectAsset.class);
                roomintent.putExtra("TO ROOMS", 1);
                roomintent.putExtra("ROOM SCAN", strSelectedRoom);
                roomintent.putExtra("SUMMARY VALUE", itemviewid);
                startActivity(roomintent);
                //finish();
                break;
        }
*/
    }

    public int lightColourChecking(model_pro_ar_asset_rows assetdata, model_pro_ar_scanasset scannedassetdata) {
        int light;
        light = lightcolour;
        if (assetdata.getManual()) {
            if (!scannedassetdata.getScan_location().equals(assetdata.getReg_location_code())) {
                light = R.color.ManualScanned;
            } else {
                light = R.drawable.room_item_manual_and_scanned;
            }
        } else if (!assetdata.getActive()) {
            light = R.color.PreviouslyDisposed;
        } else if (scannedassetdata.getScan_location_entry().equals("R0000")) {
            light = R.color.NotScanned;
        } else if (scannedassetdata.getScan_location().equals("not scanned")) {
            light = R.color.not_yet_scanned;
        } else if (!scannedassetdata.getScan_location().equals(assetdata.getReg_location_code())) {
            light = R.color.OutOfLocation;
        } else if (scannedassetdata.getScan_location_entry().equals(scannedassetdata.getScan_location())) {
            light = R.color.AlreadyScanned;
        }
        return light;
    }

////////////////////////////////////////////Gallery////////////////////////////////////////////////////////////////////////////////////////////

    public void GoToGallery(final String barcode) {

        Intent gotogallery = new Intent(PopulateRoomActivity.this, GalleryActivity.class);
        gotogallery.putExtra("PHOTO ID",barcode);

        String sql = "SELECT scan_location, scan_lattitude, scan_longitude FROM pro_ar_scan WHERE scan_barcode = '"+barcode+"'";
        gotogallery.putExtra("PIC TEXT SQL STRING",sql);
        gotogallery.putExtra("DETAIL1 TITLE",strDetailName);
        gotogallery.putExtra("DETAIL2 TITLE",strDetailName2);
        startActivity(gotogallery);
    }

////////////////////////////////////////////Listeners////////////////////////////////////////////////////////////////////////////////////////////

    View.OnClickListener listenerscan = v -> {
        intentcode = 1;
        gotonewaasetscanning();                       ///////Starts Instance of New Asset Scan In Current Room
    };

    public void gotonewaasetscanning() {
        Intent gotorooms = new Intent(PopulateRoomActivity.this,RoomMainSummary.class);                    ///////Starts Instance of New Asset Scan In Current Room
        gotorooms.putExtra("came_from_expanded",TRUE);
        gotorooms.putExtra("TO ROOMS",3);
        gotorooms.putExtra("roomorasset",FALSE);
        gotorooms.putExtra("ROOM SCAN",strSelectedRoom);
        gotorooms.putExtra("carried barcode",barcode);
        gotorooms.putExtra("LIGHT COLOUR",lightcolour);
        gotorooms.putExtra("FROM POPULATE",TRUE);
        gotorooms.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(gotorooms);
    }

}