package za.co.rdata.r_datamobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import za.co.rdata.r_datamobile.DBHelpers.DBHelperAssets;
import za.co.rdata.r_datamobile.DBHelpers.SymmetricDS_Helper;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_headers;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_room;
import za.co.rdata.r_datamobile.Models.model_pro_ar_notscannedassets_for_pdf;
import za.co.rdata.r_datamobile.Models.model_pro_ar_scanasset;
import za.co.rdata.r_datamobile.Models.model_pro_ar_scannedassets_for_pdf;
import za.co.rdata.r_datamobile.adapters.adapter_RoomRecycler;
import za.co.rdata.r_datamobile.assetModule.MakeScanAssetData;
import za.co.rdata.r_datamobile.assetModule.PopulateRoomActivity;
import za.co.rdata.r_datamobile.assetModule.RoomMainSummary;
import za.co.rdata.r_datamobile.fileTools.DBExport;
import za.co.rdata.r_datamobile.fileTools.FileActions;
import za.co.rdata.r_datamobile.fileTools.PDFCreator;
import za.co.rdata.r_datamobile.locationTools.DeviceLocationService;
import za.co.rdata.r_datamobile.locationTools.GetLocation;
import za.co.rdata.r_datamobile.scanTools.IntentIntegrator;
import za.co.rdata.r_datamobile.scanTools.IntentResult;

import static za.co.rdata.r_datamobile.stringTools.MakeDate.GetDate;


public class SelectAsset extends AppCompatActivity {
    ///////Variables
    private static final String TAG = "Checking";
    public static String scanContent = "R0000";
    static Cursor getcycle;
    int currentlayout;
    int intentcode = -1;
    int alreadyscannedCount;        //GREEN  1
    int missingfromregisteryCount;  //RED    2
    int manualscanningCount;        //YELLOW 3
    int previouslydisposedcount;    //PURPLE 4
    int wronglocationCount;         //ORANGE 5
    int newlyscannedCount;          //BLUE   6
    int notyetscannedCount;         //PINK   7
    int lightcolour;
    int intTemp;
    int rooms = 0;
    int tempviewid;
    boolean savescan = true;
    ArrayList<model_pro_ar_asset_headers> arrAlreadycount = new ArrayList<>();
    ArrayList<model_pro_ar_asset_headers> arrMissingcount = new ArrayList<>();
    ArrayList<model_pro_ar_asset_headers> arrManualCount = new ArrayList<>();
    ArrayList<model_pro_ar_asset_headers> arrPrevDispCount = new ArrayList<>();
    ArrayList<model_pro_ar_asset_headers> arrWrongLocaCount = new ArrayList<>();
    ArrayList<model_pro_ar_asset_headers> arrNewCount = new ArrayList<>();
    ArrayList<model_pro_ar_asset_headers> arrNotYetScannedCount = new ArrayList<>();

    String strBarcodescantype;
    String strLocationscantype;
    static String strSelectedRoom;
    String inputtitle;
    String roomscantype;
    static Cursor roomcursor;
    RecyclerView recyclerView;
    adapter_RoomRecycler adapter_roomRecycler;
    Context mContext;
    Activity mActivity;

    ArrayList<model_pro_ar_asset_room> arrRooms = new ArrayList<>();
    ArrayList<model_pro_ar_asset_room> arrCompletedRooms = new ArrayList<>();

    String[] a = {"$","%","?","/","\\\\","*","-","+","(",")","=","+","!","@","#","^",};
    model_pro_ar_scanasset scannedasset;

    static public sqliteDBHelper sqliteDbHelper;

    public SelectAsset() {
        mContext=SelectAsset.this;
        mActivity= (Activity) mContext;
    }


    public SelectAsset(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
    }

    View.OnClickListener sendmainclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alreadyscannedCount = arrAlreadycount.size();
            manualscanningCount=arrManualCount.size();
            missingfromregisteryCount=arrMissingcount.size();
            previouslydisposedcount=arrPrevDispCount.size();
            notyetscannedCount=arrNotYetScannedCount.size();
            wronglocationCount=arrWrongLocaCount.size();
            newlyscannedCount=arrNewCount.size();
            SendEmail(true,strSelectedRoom,0, notyetscannedCount, alreadyscannedCount);
        }
    };

    View.OnClickListener sendallmailclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alreadyscannedCount = 0;
            manualscanningCount= 0;
            missingfromregisteryCount= 0;
            previouslydisposedcount= 0;
            notyetscannedCount= 0;
            wronglocationCount= 0;
            newlyscannedCount=0;
            SendEmail(true,"%",1, notyetscannedCount, alreadyscannedCount);
        }
    };

    public View.OnClickListener listenerscan = v -> {
        intentcode = 1;
        scanContent="R0000";
        ScanAsset();                          ///////Starts Instance of New Asset Scan In Current Room
    };

    /*
    View.OnClickListener listenerItem = v -> {
        intentcode = 1;
        goToExpandedList(v, strCurrentRoom);           ///////Starts Instance of New Asset Scan In Current Room

    };
*/
    /*
    View.OnClickListener listenerMoreRooms = v -> {
        getRoomItemLists getRoomItemLists = new getRoomItemLists();
        getRoomItemLists.setPosition(0);
        //getRoomItemLists.setNumberofentries(false);
        //getRoomItemLists.execute();
        final FloatingActionButton fltMoreRooms = findViewById(R.id.fltMoreRooms);
        fltMoreRooms.setAlpha(0.0f);

    };
    */
    public View.OnClickListener listenernewroom = v -> {
        ScanRoom();     ///////Clears Current Room and Contained Assets
    };

    /*
    View.OnClickListener listenerRooms = v -> {
        intentcode = 5;
        goToRooms();                         ///////Starts Instance of New Asset Scan In Current Room
    };
    //private LocationManager locationManager;
    //private za.co.rdata.r_datamobile.fileTools.FileActions FileActions;
/*
    public static boolean CompareOneDBValues(Cursor firstcursorname, String columnfirstname, String checkvalue) {  ////////DB Entry Comparison

        String valueone;

        try {
            valueone = firstcursorname.getString(firstcursorname.getColumnIndex(columnfirstname));

            return valueone.equals(checkvalue);
        } catch (NullPointerException e) {
            return false;
        } catch (CursorIndexOutOfBoundsException e) {
            return true;
        }
    }

    public static boolean CompareTwoDBValues(Cursor firstcursorname, Cursor onebeingcomparedwith, String columnfirstname, String columnsecondname) {  ////////DB Entry Comparison

        int valueone;
        int valuetwo;
        try {
            //firstcursorname.move(checkvalue);
            //onebeingcomparedwith.move(checkvalue);
            valueone = firstcursorname.getInt(firstcursorname.getColumnIndex(columnfirstname));
            valuetwo = onebeingcomparedwith.getInt(onebeingcomparedwith.getColumnIndex(columnsecondname));

            //noinspection RedundantIfStatement
            if (valueone==valuetwo) {
                return true;
            } else {
                return false;
            }

            //for (int i = 0; i < firstcursorname.getCount(); i++) {
            //}
        } catch (NullPointerException | CursorIndexOutOfBoundsException e) {
            return false;
        }
    }
*/
    public static int GetCycle() {
        int cycle;
        try {
            getcycle = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT ar_cycle FROM pro_sys_company", null);
            getcycle.moveToLast();
            cycle = getcycle.getInt(getcycle.getColumnIndex(meta.pro_sys_company.ar_cycle));
            getcycle.close();
        } catch (CursorIndexOutOfBoundsException e) {
            cycle = 0;
        }
        return cycle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        SymmetricDS_Helper.Start_SymmetricDS(this.getApplicationContext(), false);
        //startService(new Intent(this,DeviceLocationService.class));
        sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());

        Intent iPoproom = getIntent();
        Bundle bSaved = iPoproom.getExtras();
        scanContent = "R0000";

        ArrayList<model_pro_ar_asset_room> arrtoshow = new ArrayList<>();

        setContentView(R.layout.activity_room_list);
        recyclerView = findViewById(R.id.activity_room_list_listView);

        adapter_roomRecycler = new adapter_RoomRecycler(arrtoshow, R.layout.room_content);
        //adapter_roomRecycler.setRecyclerViewID(R.id.activity_room_list_listView);
        adapter_roomRecycler.setRetbarcode(R.id.Room_Barcode);
        adapter_roomRecycler.setRetdept(R.id.txtDept);
        adapter_roomRecycler.setRetdesc(R.id.txtRoomDescription);
        adapter_roomRecycler.setRetframe(R.id.select_room_view_layout);
        adapter_roomRecycler.setmContext(this);

        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_divider));
        recyclerView.addItemDecoration(itemDecorator);
        //recyclerView.setDrawable(new DividerItemDecoration(this,R.drawable.recycler_divider));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter_roomRecycler);

        //preparelistitems("%",adapter_roomRecycler, arrtoshow);

        try {
            assert bSaved != null;
            rooms = bSaved.getInt("TO ROOMS");
            strSelectedRoom = bSaved.getString("ROOM SCAN");
            intTemp = bSaved.getInt("SUMMARY VALUE");
            savescan=bSaved.getBoolean("SAVE SCAN");
        } catch (NullPointerException | AssertionError ignore) {}

        /*roomcount = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                meta.pro_ar_locations.TableName,
                null, "loc_code like ?", new String[]{strSearch}, null, null, null, null);
        roomcount.moveToFirst();*/

        arrRooms.clear();
        arrCompletedRooms.clear();
        //arrSearch.clear();

        /*String roomassetcount = "SELECT reg_location_code, count(reg_barcode) AS row_count\n" +
                "FROM pro_ar_register\n" +
                "where reg_location_code like 'R%'\n" +
                "group by reg_location_code\n" +
                "order by reg_location_code;";

        String roomscancount = "SELECT scan_location, count(scan_barcode) AS scan_count\n" +
                "FROM pro_ar_scan\n" +
                "where scan_location_entry <> 'R0000' and InstNode_id = 16\n" +
                "group by scan_location\n" +
                "order by scan_location;";*/

        String roomassetcount = "select ifnull(upper(reg_location_code), '')   AS reg_location_code\n" +
                "     , ifnull(loc_name, '')                   AS loc_name\n" +
                "     , ifnull(loc_building, '')               AS loc_building\n" +
                "     , ifnull(loc_person, '')                 AS loc_person\n" +
                "     , ifnull(scan_count, 0)  AS scan_count\n" +
                "     , ifnull(Asset_count, 0) AS Asset_count\n" +
                " from (\n" +
                "         SELECT reg_location_code\n" +
                "              , count(reg_barcode) AS Asset_count\n" +
                "         FROM pro_ar_register\n" +
                "         group by reg_location_code\n" +
                "     ) asset\n" +
                "         left join\n" +
                "     (SELECT scan_location_entry, count(scan_barcode) AS scan_count\n" +
                "      FROM pro_ar_scan\n" +
                "      where scan_location_entry <> 'R0000'\n" +
                "      group by scan_location_entry\n" +
                "     ) scan on scan.scan_location_entry = asset.reg_location_code\n" +
                "         left join\n" +
                "     (SELECT loc_code\n" +
                "           , loc_name\n" +
                "           , loc_building\n" +
                "           , loc_person\n" +
                "      FROM pro_ar_locations\n" +
                "     ) loc on loc.loc_code = asset.reg_location_code\n" +
                " order by asset.reg_location_code;";

     /*   String roomscancount = "select ifnull(upper(reg_location_code), '')   AS reg_location_code\n" +
                "     , ifnull(loc_name, '')                   AS loc_name\n" +
                "     , ifnull(loc_building, '')               AS loc_building\n" +
                "     , ifnull(loc_person, '')                 AS loc_person\n" +
                "     , ifnull(scan_count, 0)  AS scan_count\n" +
                "     , ifnull(Asset_count, 0) AS Asset_count\n" +
                " from (\n" +
                "         SELECT reg_location_code\n" +
                "              , count(reg_barcode) AS Asset_count\n" +
                "         FROM pro_ar_register\n" +
                "         group by reg_location_code\n" +
                "     ) asset\n" +
                "         left join\n" +
                "     (SELECT scan_location_entry, count(scan_barcode) AS scan_count\n" +
                "      FROM pro_ar_scan\n" +
                "      where scan_location_entry <> 'R0000'\n" +
                "        and InstNode_id = 16\n" +
                "      group by scan_location_entry\n" +
                "     ) scan on scan.scan_location_entry = asset.reg_location_code\n" +
                "         left join\n" +
                "     (SELECT loc_code\n" +
                "           , loc_name\n" +
                "           , loc_building\n" +
                "           , loc_person\n" +
                "      FROM pro_ar_locations\n" +
                "     ) loc on loc.loc_code = asset.reg_location_code\n" +
                "   group by reg_location_code\n"+
                " having scan_count <> Asset_count\n" +
                " order by asset.reg_location_code;";*/

        Cursor incompleterooms;
        incompleterooms = sqliteDbHelper.getReadableDatabase().rawQuery(roomassetcount, null);
        incompleterooms.moveToFirst();

        /*Cursor completerooms = sqliteDbHelper.getReadableDatabase().rawQuery(roomassetcount, null);
        completerooms.moveToFirst();*/

        @SuppressLint("CutPasteId") FloatingActionButton flbAllMail = findViewById(R.id.flbAllMail);
        registerForContextMenu(flbAllMail);

        @SuppressLint("CutPasteId") FloatingActionButton fltAllMail = findViewById(R.id.flbAllMail);
        fltAllMail.setOnClickListener(sendallmailclick);
        Button scannewroom = findViewById(R.id.btnScanNewRoomfromList);
        scannewroom.setOnClickListener(listenernewroom);

        try {
            do {
                arrRooms.add(new model_pro_ar_asset_room(incompleterooms.getString(incompleterooms.getColumnIndex(meta.pro_ar_register.reg_location_code)),
                        incompleterooms.getString(incompleterooms.getColumnIndex(meta.pro_ar_locations.loc_name)),
                        incompleterooms.getString(incompleterooms.getColumnIndex(meta.pro_ar_locations.loc_building)),
                        incompleterooms.getString(incompleterooms.getColumnIndex(meta.pro_ar_locations.loc_person)),
                        String.valueOf(incompleterooms.getInt(incompleterooms.getColumnIndex("scan_count")))
                                + '/'
                                + incompleterooms.getInt(incompleterooms.getColumnIndex("Asset_count"))));
                incompleterooms.moveToNext();
            }
            while (!incompleterooms.isAfterLast());

        } catch (CursorIndexOutOfBoundsException ex) {
            Toast.makeText(getBaseContext(), "No Rooms Found, Please check Asset Register", Toast.LENGTH_LONG).show();
        }

        /*do {
            arrCompletedRooms.add(new model_pro_ar_asset_room(completerooms.getString(completerooms.getColumnIndex(meta.pro_ar_locations.loc_code)),
                    completerooms.getString(completerooms.getColumnIndex(meta.pro_ar_locations.loc_name)),
                    completerooms.getString(completerooms.getColumnIndex(meta.pro_ar_locations.loc_building)),
                    completerooms.getString(completerooms.getColumnIndex(meta.pro_ar_locations.loc_building)),

                    String.valueOf(completerooms.getInt(completerooms.getColumnIndex("scan_count")))
                            +'/'
                            +String.valueOf(completerooms.getInt(completerooms.getColumnIndex("Asset_count")))));
            completerooms.moveToNext();
        }
        while (!completerooms.isAfterLast());
*/
        //completerooms.close();
        incompleterooms.close();

        arrtoshow.addAll(arrRooms);
        arrtoshow.addAll(arrCompletedRooms);
        adapter_roomRecycler.notifyDataSetChanged();

        EditText edtSearch = findViewById(R.id.edtSearchRoom);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                arrtoshow.clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<model_pro_ar_asset_room> filteredTitles = new ArrayList<>();
                filteredTitles.clear();
                if (!s.toString().equals("")) {
                    for (int i = 0; i < arrRooms.size(); i++) {
                        if (arrRooms.get(i).getRoomnumber().toUpperCase().contains(s.toString().toUpperCase())) {
                            filteredTitles.add(arrRooms.get(i));
                        }
                    }

                /*    for (int i = 0; i < arrCompletedRooms.size(); i++) {
                        if (arrCompletedRooms.get(i).getRoomnumber().toUpperCase().contains(s.toString().toUpperCase())) {
                            filteredTitles.add(arrCompletedRooms.get(i));
                        }
                    }*/

                    arrtoshow.addAll(filteredTitles);
                    adapter_roomRecycler.notifyDataSetChanged();
                } else {
                    arrtoshow.addAll(arrRooms);
                //    arrtoshow.addAll(arrCompletedRooms);
                    adapter_roomRecycler.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter_roomRecycler.notifyDataSetChanged();
            }
        });

    }
/*
    public double calculateBillAmount(String expression){

        String delim = "/";
        String[] stringTokens = expression.split(delim);
        double result = 0;

        for (String stringToken : stringTokens) {
            result += Double.parseDouble(stringToken);
        }
        return result;
    }
*/
    @Override
    protected void onResume() {
        super.onResume();

        if (intentcode == 1 || intentcode == 0) {

            try {
                if (scanContent.equals("R0000")) {
                    if (savescan) {
                        ScanAsset();
                    } else {
                        //newRoomIntent(strSelectedRoom, scanContent);
                        ScanSearchAsset();
                    }
             //   } else if (scanContent == null) {
               //     Inputbox(1);
                } else if ((scanContent.startsWith("R") | (scanContent.length() < 5))) {
                        Toast.makeText(getBaseContext(), "Error When Scanning Asset, Room Was Scanned Instead", Toast.LENGTH_LONG).show();
                        //ScanAsset(this);
                    Intent gotomainsummary = new Intent(SelectAsset.this,RoomMainSummary.class);
                    gotomainsummary.putExtra("ROOM SCAN",strSelectedRoom);
                        // MakeExpandedList(scanContent);
                } else {
                    strLocationscantype = "S";

                    if (savescan) {
                        ScanCheck(scanContent, strSelectedRoom);
                    }
                        newRoomIntent(strSelectedRoom, scanContent,SelectAsset.this);
                    }
            } catch (NullPointerException ignore) {
                Inputbox(1,this );
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

/////////////////New Asset Results//////////////////////////////////////////////////////////////////////////////////////////////

        super.onActivityResult(requestCode, resultCode, intent);
        if (intentcode == 1) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            scanContent = null;
            try {
                for (String s : a) {
                    assert scanningResult != null;
                    if (scanningResult.getContents().contains(s)) {
                        ScanAsset();
                        Toast.makeText(getBaseContext(), "Error When Scanning, Invalid Characters", Toast.LENGTH_LONG).show();
                        break;
                    } else {
                        scanContent = scanningResult.getContents();
                    }
                }
            } catch (NullPointerException ignore) {
                //Inputbox(1);
            }
            try {
                assert scanningResult != null;
                ArrayList<String> filepath = new ArrayList<>(
                        Arrays.asList(scanningResult.getBarcodeImagePath().split("/")));
                int filenamenumber = filepath.size();
                String filename = filepath.get(filenamenumber - 1);
                Log.d(TAG, filename);
                filepath.remove(filenamenumber - 1);

                StringBuilder dir = new StringBuilder();

                for (int i = 0; i < filepath.size(); i++) {
                    dir.append(filepath.get(i)).append("/");
                }

                FileActions fileActions = new FileActions();
                fileActions.moveFile(dir.toString(), filename, Environment.getExternalStorageDirectory().toString() + "/filesync/", scanContent + "-" + GetDate("-") + ".jpg");
            } catch (NullPointerException ignore) {
            }
        }

/////////////////////////////////////New Room Results///////////////////////////////////////////////////////////////////////////

        if (intentcode == 5) {

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            try {
                for (String s : a) {
                    assert scanningResult != null;
                    if (scanningResult.getContents().contains(s)) {
                        Toast.makeText(getBaseContext(), "Error When Scanning Room, Invalid Characters", Toast.LENGTH_SHORT).show();
                        ScanRoom();
                    }
                }
                assert scanningResult != null;
                if ((scanningResult.getContents().length() != 5) | !scanningResult.getContents().startsWith("R")) {
                    Toast.makeText(getBaseContext(), "Error When Scanning Room, Asset Was Scanned Instead", Toast.LENGTH_SHORT).show();
                    strSelectedRoom = null;
                    ScanRoom();
                } else {
                    strSelectedRoom = scanningResult.getContents();
                }

            } catch (NullPointerException ignore) {
            }


            try {
                if (strSelectedRoom == null | !strSelectedRoom.startsWith("R")) {
                    Inputbox(0, this);
                } else {
/*
                    try {
                        GetLocation getget = new GetLocation(mContext);
                        double lat = getget.getLatitude();
                        double lng = getget.getLongitude();


                        MainActivity.sqliteDbHelper.getReadableDatabase().execSQL("update pro_ar_locations set gps_master_long = "+lng+", gps_master_lat = "+lat+ " where loc_code = "+ selectedroom);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent gotomainsummary = new Intent(SelectAsset.this,RoomMainSummary.class);
                    gotomainsummary.putExtra("ROOM SCAN",strSelectedRoom);
                    startActivity(gotomainsummary);
                //MakeExpandedList(strSelectedRoom);
                */
                    gotoroommainsummary(strSelectedRoom);

                }
            } catch (NullPointerException ignore) {
                Inputbox(0, this);
            }
        }
    }

    public void gotoroommainsummary (String selectedroom) {
        try {
            GetLocation getget = new GetLocation(mContext);
            double lat = getget.getLatitude();
            double lng = getget.getLongitude();
            MainActivity.sqliteDbHelper.getReadableDatabase().execSQL("update pro_ar_locations set gps_master_long = "+lng+", gps_master_lat = "+lat+ " where loc_code = "+ selectedroom);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent gotomainsummary = new Intent(SelectAsset.this,RoomMainSummary.class);
        gotomainsummary.putExtra("ROOM SCAN",strSelectedRoom);
        startActivity(gotomainsummary);
    }

    public void newRoomIntent(String strscanContent, String barcode, Activity mContext) {
        Intent roomintent = new Intent(mContext, PopulateRoomActivity.class);
        roomintent.putExtra("ROOM SCAN", strscanContent);
        roomintent.putExtra("CYCLE", GetCycle());
        roomintent.putExtra("LOCATION SCAN TYPE", "s");
        roomintent.putExtra("LOCATION NAME", GetLocationName(strscanContent));
        roomintent.putExtra("RESPONSIBLE PERSON", GetRespPerson(strscanContent));
        roomintent.putExtra("BARCODE",barcode);
        roomintent.putExtra("LIGHT COLOUR",lightcolour);
        roomintent.putExtra("SUMMARY VALUE", tempviewid);
        startActivity(roomintent);
    }

    public static String GetRespPerson(String locationname) {
        String responsibleperson = "";
        roomcursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT loc_person FROM pro_ar_locations WHERE loc_code = '" + locationname + "'", null);
        roomcursor.moveToLast();
        try {
            responsibleperson = roomcursor.getString(roomcursor.getColumnIndex(meta.pro_ar_locations.loc_person));
        } catch (Exception ignore) {
        }
        roomcursor.close();
        return responsibleperson;
    }

    public void ScanCheck(String scanContent, String strSelectedRoom) {       ///////Database Handler

        MakeScanAssetData makeScanAssetData = new MakeScanAssetData(scanContent, strSelectedRoom);
        makeScanAssetData.setmContext(SelectAsset.this);
        scannedasset = makeScanAssetData.MakeAsset(GetCycle(), strBarcodescantype, strLocationscantype);
        SaveData saveData = new SaveData();
        saveData.setRoom(strSelectedRoom);
        saveData.setBarcode(scanContent);
        saveData.setScanneddata(scannedasset);
        saveData.setType(strBarcodescantype);
        saveData.execute(scannedasset);
    }

    public void ScanAsset() {               ///////Start Instance Of New Asset Scan

        currentlayout = 1;
        intentcode = 1;
        inputtitle = "Please Input Barcode";
        strBarcodescantype = "s";
        savescan=true;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        IntentIntegrator scanAssetintent = new IntentIntegrator(mActivity);
        scanAssetintent.setBeepEnabled(sharedPref.getBoolean("scan_beep",false));

        scanAssetintent.setOrientationLocked(false);
        scanAssetintent.initiateScan();
    }

    public void ScanRoom() {         ///////Starts Instance to Scan New Room

        intentcode = 5;
        inputtitle = ("Input Room Barcode");
        roomscantype = "s";
        strSelectedRoom=null;

        IntentIntegrator scanRoomintent = new IntentIntegrator(mActivity);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        scanRoomintent.setBeepEnabled(sharedPref.getBoolean("scan_beep",false));
        scanRoomintent.setOrientationLocked(false);
        scanRoomintent.initiateScan();
        //goToRooms();
    }

    public void Inputbox(final int state,Context mContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(inputtitle);

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String input_value;
            input_value = input.getText().toString().toUpperCase();
            Log.d(TAG, "Check input:" + input_value);
           // if (input_value.length() == 5) {

                switch (state) {
                    case 0:
                        scanContent = input_value;
                        roomscantype = "k";
                        strSelectedRoom = input_value;
                        if (strSelectedRoom.startsWith("R") & (strSelectedRoom.length()==5)) {
                            Intent gotomainsummary = new Intent(SelectAsset.this,RoomMainSummary.class);
                            gotomainsummary.putExtra("ROOM SCAN",strSelectedRoom);
                           startActivity(gotomainsummary);
                            //MakeExpandedList(strSelectedRoom);
                        } else {
                            strSelectedRoom=null;
                            Toast.makeText(getBaseContext(), "Input value was the incorrect.", Toast.LENGTH_SHORT).show();
                            Inputbox(0, this);
                        }
                        break;
                    case 1:
                        if (input_value!=null && !input_value.equals("") && input_value.length()>=5) {
                            scanContent = input_value;

                            if (!scanContent.startsWith("R")) {
                                intentcode = 1;
                                strBarcodescantype = "k";
                                strLocationscantype = "k";
                                //lightcolour = R.drawable.room_item_already_scanned;
                                if (savescan) {
                                    ScanCheck(scanContent, strSelectedRoom);
                                }
                                newRoomIntent(strSelectedRoom, scanContent,SelectAsset.this);
                            } else {
                                intentcode = 5;
                                roomscantype = "k";
                                Intent gotomainsummary = new Intent(SelectAsset.this,RoomMainSummary.class);
                                gotomainsummary.putExtra("ROOM SCAN",strSelectedRoom);
                            }
                        }
                        break;
                }
            //} else {
               // Toast.makeText(getBaseContext(), "Input value was the incorrect length, please try again", Toast.LENGTH_SHORT).show();
               // Inputbox(state);
           // }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();

        });
        builder.show();

    }
/*
    protected void RefreshLocation() {

        SaveNewAssetLocation saveNewAssetLocation = new SaveNewAssetLocation();
        saveNewAssetLocation.execute();
    }
*/
    public void SendEmail(@SuppressWarnings("SameParameterValue") Boolean ascsv, String all, int fulldump, int notyetscannedCount, int alreadyscannedCount) {

        String scanned = null;
        String notscanned = null;
        if (fulldump == 1) {
            scanned = "SELECT * FROM pro_ar_scan WHERE " + meta.pro_ar_scan.scan_location + " like '" + all + "'";
            notscanned = "SELECT * From pro_ar_register where reg_location_code like '" + all + "' and reg_barcode not in (SELECT scan_barcode from pro_ar_scan where scan_location_entry like '" +all+ "') and reg_isactive=1";
        } if (fulldump == 0) {
            scanned = "SELECT pro_ar_register.reg_barcode AS 'Already Scanned Barcode', pro_ar_register.reg_asset_desc AS Description, pro_ar_register.reg_location_code AS 'Registered Room' From pro_ar_register where reg_barcode in (SELECT scan_barcode from pro_ar_scan where scan_location like '" + all + "')";
            notscanned = "SELECT pro_ar_register.reg_barcode AS 'Not Scanned Barcode', pro_ar_register.reg_asset_desc AS Description, pro_ar_register.reg_location_code AS 'Registered Room' From pro_ar_register where reg_location_code like '" + all + "' and reg_barcode not in (SELECT scan_barcode from pro_ar_scan where scan_location_entry like '" +all+ "') and reg_isactive=1";
        } if (fulldump == 2 ){
            scanned = "select\n" +
                    "       scan_barcode as Barcode,\n" +
                    "       reg_asset_code as 'Asset Code',\n" +
                    "       reg_asset_desc as Description,\n" +
                    "       scan_location as 'Scanned Location',\n" +
                    "       scan_location_entry as 'Registered Location',\n" +
                    "       scan_reader_id as Reader,\n" +
                    "       scan_datetime as 'Scan Date',\n" +
                    "       scan_lattitude,\n" +
                    "       scan_longitude\n" +
                    "from pro_ar_scan left join pro_ar_register on scan_barcode = reg_barcode";
        }

        //String notscanned = "SELECT pro_ar_register.reg_barcode, pro_ar_register.reg_asset_desc FROM pro_ar_register LEFT JOIN pro_ar_scan ON pro_ar_register.reg_barcode = pro_ar_scan.scan_barcode WHERE pro_ar_scan.scan_barcode is null and pro_ar_register.reg_location_code like '" + all + "'";
        //String notscanned = "SELECT pro_ar_register.reg_barcode AS 'Missing Asset Barcode', pro_ar_register.reg_asset_desc AS Description FROM mdata.pro_ar_register where reg_barcode not in (SELECT scan_barcode FROM pro_ar_scan where scan_location_entry like '" + all + "') and reg_location_code like '" + all + "'";

        String email;

        String scannedpdf = "SELECT scan_location,scan_barcode,scan_user,scan_datetime FROM pro_ar_scan WHERE " + meta.pro_ar_scan.scan_location + " like '" + all + "'";
        String notscannedpdf = "SELECT pro_ar_register.reg_location_code, pro_ar_register.reg_barcode, " +
                "pro_ar_register.reg_asset_desc FROM pro_ar_register LEFT JOIN pro_ar_scan ON pro_ar_register.reg_barcode = pro_ar_scan.scan_barcode WHERE pro_ar_scan.scan_barcode is null and pro_ar_register.reg_location_code like '" + all + "'";

        PDFCreator pdfCreator = new PDFCreator();
        String date = GetDate("-");


        try {
            Cursor emailcursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT * FROM pro_ar_locations WHERE loc_code like '" + all + "'", null);
            emailcursor.moveToFirst();
            email = roomcursor.getString(roomcursor.getColumnIndex(meta.pro_ar_locations.loc_email));
            emailcursor.close();
        } catch (Exception e) {
            email = "";
        }

/////////////////////////////////////////////////CSV Section////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (ascsv) {
            DBExport mail = new DBExport();

            mail.exportDB(all + "-scanned-" + date, scanned,fulldump);
            if (fulldump!=2) {
                mail.exportDB(all + "-not_scanned-" + date, notscanned, fulldump);
            }
            } else {

/////////////////////////////////////////////////////////////////PDF Section/////////////////////////////////////////////////////////////////////////////////////

            ArrayList<model_pro_ar_scannedassets_for_pdf> scanpdfcontent = new ArrayList<>();
            Cursor pdfscandata = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery(scannedpdf, null);
            pdfscandata.moveToFirst();

            int scancolumn1 = 0;
            int scancolumn2 = 0;
            int scancolumn3 = 0;
            int scancolumn4 = 0;
            int scancolumn5 = 0;
            int scancolumn6 = 0;

            for (int i = 0; i < scanpdfcontent.size() - 1; ) {

                Cursor pdfscandatalocationname = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT loc_code, loc_name FROM pro_ar_location where loc_code = " + pdfscandata.getString(0), null);
                Cursor pdfscandatabarcodedesc = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT reg_barcode, reg_asset_desc FROM pro_ar_register where reg_barcode = " + pdfscandata.getString(1), null);
                pdfscandatalocationname.moveToFirst();
                pdfscandatabarcodedesc.moveToFirst();

                scanpdfcontent.add(new model_pro_ar_scannedassets_for_pdf(
                        pdfscandata.getString(0),
                        pdfscandatalocationname.getString(1),
                        pdfscandata.getString(1),
                        pdfscandatabarcodedesc.getString(1),
                        pdfscandata.getString(2),
                        pdfscandata.getString(3)));

                pdfCreator.checklonglength(pdfscandata.getString(0).length(), scancolumn1);
                pdfCreator.checklonglength(pdfscandatalocationname.getString(1).length(), scancolumn2);
                pdfCreator.checklonglength(pdfscandata.getString(1).length(), scancolumn3);
                pdfCreator.checklonglength(pdfscandatabarcodedesc.getString(1).length(), scancolumn4);
                pdfCreator.checklonglength(pdfscandata.getString(2).length(), scancolumn5);
                pdfCreator.checklonglength(pdfscandata.getString(3).length(), scancolumn6);

                pdfscandata.moveToNext();
                pdfscandatalocationname.close();
                pdfscandatabarcodedesc.close();
            }

            pdfscandata.close();

            ArrayList<model_pro_ar_notscannedassets_for_pdf> notscannedpdfcontent = new ArrayList<>();
            Cursor pdfnotscandata = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery(notscannedpdf, null);
            pdfnotscandata.moveToFirst();

            int notscancolumn1 = 0;
            int notscancolumn2 = 0;
            int notscancolumn3 = 0;
            int notscancolumn4 = 0;

            for (int i = 0; i < scanpdfcontent.size() - 1; ) {

                Cursor pdfscandatalocationname = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT loc_code, loc_name FROM pro_ar_location where loc_code = " + pdfscandata.getString(0), null);
                //Cursor pdfscandatabarcodedesc = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT reg_barcode, reg_asset_desc FROM pro_ar_register where reg_barcode = "+pdfscandata.getString(1),null);
                pdfscandatalocationname.moveToFirst();
                //pdfscandatabarcodedesc.moveToFirst();

                notscannedpdfcontent.add(new model_pro_ar_notscannedassets_for_pdf(
                        pdfnotscandata.getString(1),
                        pdfnotscandata.getString(2),
                        pdfnotscandata.getString(0),
                        pdfscandatalocationname.getString(1)));

                pdfCreator.checklonglength(pdfnotscandata.getString(1).length(), notscancolumn1);
                pdfCreator.checklonglength(pdfnotscandata.getString(2).length(), notscancolumn2);
                pdfCreator.checklonglength(pdfnotscandata.getString(0).length(), notscancolumn3);
                pdfCreator.checklonglength(pdfscandatalocationname.getString(1).length(), notscancolumn4);

                pdfnotscandata.moveToNext();
                pdfscandatalocationname.close();
                //pdfscandatabarcodedesc.close();
            }

            pdfnotscandata.close();
            pdfCreator.stringtopdf(all + "-scanned-" + date, scanpdfcontent, new String[]{"Location Code", "Location Description", "Asset Barcode", "Asset Description", "Scanned By", "Scanned On"}, new int[]{notscancolumn1, notscancolumn2, notscancolumn3, notscancolumn4});
            pdfCreator.stringtopdf(all + "-not_scanned-" + date, notscannedpdfcontent, new String[]{"Asset Barcode", "Asset Description", "Location Code", "Location Description"}, new int[]{scancolumn1, scancolumn2, scancolumn3, scancolumn4, scancolumn5, scancolumn6});

        }

///////////////////////////////////////////////////////Email Body////////////////////////////////////////////////////////////////////////////////////////////////////////

        int total = notyetscannedCount + alreadyscannedCount;

        Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, "Room: " + "" + all + "" + " Scanned Data Report");
        /*
        i.putExtra(Intent.EXTRA_TEXT, "This is a report for room " + all + " as at " + date.substring(0, 10) + "." + "\n\n"
                + notyetscannedCount + " asset(s) have yet to be scanned." + "\n"
                + alreadyscannedCount + " asset(s) have already been scanned." + "\n"
                + missingfromregisteryCount + " asset(s) are not in the registry." + "\n"
                + wronglocationCount + " asset(s) are in the wrong location." + "\n"
                + manualscanningCount + " asset(s) are in the manual mode." + "\n"
                + previouslydisposedcount + " asset(s) are disposed." + "\n\n"
                +String.valueOf(total) + " asset(s) are registered in "+all+"."
                + "\n\n"
                + "Regards/Groete.");
*/

        i.putExtra(Intent.EXTRA_TEXT, "Good day"+"\n\n"
                + "An Asset Count was done at your office, " + all + ", on " + date.substring(0, 10) + "." + "\n\n"
                + "Attached are reports of assets which were scanned and those not yet scanned (Missing)." + "\n\n"
                + "The assets which are not yet scanned, should be ready for scanning on ("+GetDate("/")+") by the Asset & Fleet Department." + "\n\n"
                + "Assets which are not scanned on the given date, will be regarded as missing and will be reported to the Department's Director." + "\n\n"
                + notyetscannedCount + " asset(s) have yet to be scanned." + "\n"
                + alreadyscannedCount + " asset(s) have already been scanned." + "\n\n"
                + total + " asset(s) are registered to "+all+"."
                + "\n\n"
                + "Regards/Groete.");

        ArrayList<Uri> uris = new ArrayList<>();

        if (ascsv) {
            File filelocation1 = new File(Environment.getExternalStorageDirectory() + "/filesync/Documents/", all + "-scanned-" + date + ".csv");
            Uri path1 = Uri.fromFile(filelocation1);
            uris.add(path1);
            if (fulldump!=2) {
                File filelocation2 = new File(Environment.getExternalStorageDirectory() + "/filesync/Documents/", all + "-not_scanned-" + date + ".csv");
                Uri path2 = Uri.fromFile(filelocation2);
                uris.add(path2);
            }
        } else {
            File filelocation1 = new File(Environment.getExternalStorageDirectory() + "/filesync/Documents/", all + "-scanned-" + date + ".csv");
            Uri path1 = Uri.fromFile(filelocation1);
            uris.add(path1);
            if (fulldump!=2) {
                File filelocation2 = new File(Environment.getExternalStorageDirectory() + "/filesync/Documents/", all + "-not_scanned-" + date + ".csv");
                Uri path2 = Uri.fromFile(filelocation2);
                uris.add(path2);
            }
        }

        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        try {

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    @SuppressWarnings("JavaReflectionMemberAccess") Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(mContext, "Intent Switch Failed", Toast.LENGTH_SHORT).show();
            mContext.startActivity(Intent.createChooser(i, "Send mail..."));
        }
    }

    public static String GetLocationName(String locationname) {

        String locationnameresult;
        roomcursor = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT loc_name FROM pro_ar_locations WHERE loc_code = '" + locationname + "'", null);
        roomcursor.moveToFirst();

        try {
            locationnameresult = roomcursor.getString(roomcursor.getColumnIndex(meta.pro_ar_locations.loc_name));
        } catch (CursorIndexOutOfBoundsException ex) {
            locationnameresult = "R0000";
        }
        roomcursor.close();
        return locationnameresult;
    }

    public void onBackPressed() {
                Intent mainmenu = new Intent(SelectAsset.this, MainActivity.class);
                finish();
                startActivity(mainmenu);
    }

    public void ScanSearchAsset() {               ///////Start Instance Of New Asset Scan without Data Upload

        currentlayout = 1;
        intentcode = 1;
        inputtitle = "Please Input Barcode";
        strBarcodescantype = "s";
        savescan=false;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        IntentIntegrator scanAssetintent = new IntentIntegrator(mActivity);
        scanAssetintent.setBeepEnabled(sharedPref.getBoolean("scan_beep",false));
        scanAssetintent.addExtra("INTENT CODE",1);

        scanAssetintent.setOrientationLocked(false);
        scanAssetintent.initiateScan();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Resend Scans");
        menu.add(1, v.getId(), 1, "Scan Detail");
        menu.add(2, v.getId(), 2, "Full Dump");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //SelectAsset selectAsset = new SelectAsset();
        if (item.getTitle() == "Resend Scans") {
            int cyclenew = GetCycle() + 1;
            MainActivity.sqliteDbHelper.getReadableDatabase().execSQL("update pro_ar_scan set scan_cycle = '"+cyclenew+"'");
            Toast.makeText(this, "Scans Reset",Toast.LENGTH_SHORT).show();
        } else if (item.getTitle() == "Scan Detail") {
            SendEmail(true,"%",2, notyetscannedCount, alreadyscannedCount);
        } else if (item.getTitle() == "Full Dump") {
            SendEmail(true,"%",1, notyetscannedCount, alreadyscannedCount);
        }
        return true;
    }

    public static class SaveData extends AsyncTask<model_pro_ar_scanasset, String, Boolean> {

        String barcode;
        String room;
        model_pro_ar_scanasset scanneddata;
        String scantype;

        public void setScanneddata(model_pro_ar_scanasset scanneddata) {
            this.scanneddata = scanneddata;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public void setType(String scantype) {
            this.scantype = scantype;
        }

        @Override
        protected Boolean doInBackground(model_pro_ar_scanasset... rows) { ///////Async Task Handler For SQL Handling

            DBHelperAssets.pro_ar_asset_rows.setScandata(scanneddata, barcode, room, scantype);
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

    public static class SaveComments extends AsyncTask<String, Integer, Long> {

        String barcode;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {
            DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments1(strings[0], barcode);
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments2(strings[0], barcode);
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments3(strings[0], barcode);
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderNotes(strings[0], barcode);

            return null;
        }
    }

    public static class SaveComments2 extends AsyncTask<String, Integer, Long> {

        String barcode;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments1(strings[0], barcode);
            DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments2(strings[0], barcode);
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments3(strings[0], barcode);
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderNotes(strings[0], barcode);

            return null;
        }
    }
/*
    public static class SaveComments3 extends AsyncTask<String, Integer, Long> {

        String barcode;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments1(strings[0], barcode);
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments2(strings[0], barcode);
            DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments3(strings[0], barcode);
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderNotes(strings[0], barcode);

            return null;
        }
    }

    public static class SaveNotes extends AsyncTask<String, Integer, Long> {

        String barcode;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments1(strings[0], barcode);
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments2(strings[0], barcode);
            //DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderComments3(strings[0], barcode);
            DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderNotes(strings[0], barcode);

            return null;
        }
    }
*/
    public static class SaveCondition extends AsyncTask<String, Integer, Long> {

        String barcode;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {
            DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderCondition(strings[0], barcode);
            return null;
        }
    }
/*
    public static class SaveDescription extends AsyncTask<String, Integer, Long> {

        String barcode;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {
            DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderDescription(strings[0], barcode);
            return null;
        }
    }
*/
    public static class SaveAdjRemainder extends AsyncTask<String, Integer, Long> {

        String barcode;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {
            DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderAdjRemainder(strings[0], barcode);
            return null;
        }
    }

    public static class SaveDisposedState extends AsyncTask<String, Integer, Long> {

        String barcode;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {

            DBHelperAssets.pro_ar_asset_rows.updateDisposedState(strings[0], barcode);
            return null;
        }
    }

    public static class SaveInvestigationState extends AsyncTask<String, Integer, Long> {

        String barcode;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {

            DBHelperAssets.pro_ar_asset_rows.updateInvestigateState(strings[0], barcode);
            return null;
        }
    }
/*
    public static class SaveManualState extends AsyncTask<String, Integer, Long> {

        String barcode;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {
            DBHelperAssets.pro_ar_asset_rows.updateManualState(strings[0], barcode);
            return null;
        }
    }

    private static class SaveNewLoca extends AsyncTask<String, Integer, Long> {

        String locaX;
        String locaY;
        String barcode;
        String insta;

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @SuppressWarnings("unused")
        public void setLocaX(String locaX) {
            this.locaX = locaX;
        }

        @SuppressWarnings("unused")
        public void setLocaY(String locaY) {
            this.locaY = locaY;
        }

        @SuppressWarnings("unused")
        public void setInsta(String insta) {
            this.insta = insta;
        }

        @Override
        protected Long doInBackground(String... strings) {
            DBHelperAssets.pro_ar_asset_rows.updateLocationGPS(locaY, locaX, barcode, insta);
            return null;
        }
    }

    private static class SaveNewAssetLocation extends AsyncTask<String, Integer, Long> {

        String barcode, locaX, locaY;

        public void setLocaX(String locaX) {
            this.locaX = locaX;
        }

        public void setLocaY(String locaY) {
            this.locaY = locaY;
        }


        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        protected Long doInBackground(String... strings) {

            Cursor locationcursor = MainActivity.sqliteDbHelper.getReadableDatabase().query(
                    meta.pro_ar_scan.TableName,
                    null, meta.pro_ar_scan.scan_barcode + " = ?", new String[]{strSelectedBarcode}, null, null, null, null);
            locationcursor.moveToLast();

            try {
                //if (!location.equals(null) || !location.equals("")) {
                    DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderLocation(location, strSelectedBarcode);
                    DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderLocationEntry(assetcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_location_code)), barcode);
                //}

                if (locationcursor.getString(locationcursor.getColumnIndex(meta.pro_ar_scan.scan_adj_remainder)) == null) {
                    DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderAdjRemainder("0", barcode);
                }

                DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderRouteNr(locationcursor.getString(assetcursor.getColumnIndex(meta.pro_ar_register.reg_route_nr)), barcode);


            } catch (NullPointerException ignore) {
            } catch (CursorIndexOutOfBoundsException e) {
                DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderLocationEntry("R0000", barcode);
            }

            try {
                DBHelperAssets.pro_ar_asset_rows.updateAssetHeaderCoords(locaY, locaX, barcode);
            } catch (Exception ignore) {
            }

            locationcursor.close();

            return null;
        }
    }
*/
    }

