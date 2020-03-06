package za.co.rdata.r_datamobile.assetModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.DBHelpers.DataCursorLoaders;
import za.co.rdata.r_datamobile.DBMeta.intentcodes;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_headers;
import za.co.rdata.r_datamobile.Models.model_pro_ar_scanasset;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectAsset;
import za.co.rdata.r_datamobile.fileTools.ActivityLogger;
import za.co.rdata.r_datamobile.scanTools.IntentIntegrator;
import za.co.rdata.r_datamobile.scanTools.IntentResult;

import static za.co.rdata.r_datamobile.SelectAsset.GetCycle;
import static za.co.rdata.r_datamobile.SelectAsset.GetLocationName;
import static za.co.rdata.r_datamobile.SelectAsset.GetRespPerson;

/**
 * Created by James de Scande on 12/09/2018 at 12:53.
 */

public class RoomMainSummary extends AppCompatActivity {

    private static final String TAG = "";
    String strSelectedRoom;
    String strSelectedRoomBackup;

    protected ArrayList<model_pro_ar_asset_headers> arrAlreadycount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrMissingcount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrManualCount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrPrevDispCount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrWrongLocaCount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrNewCount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> arrNotYetScannedCount = new ArrayList<>();
    protected ArrayList<model_pro_ar_asset_headers> listArray = new ArrayList<>();

    int lightcolour = 0;
    int intentcode = 0;
    String scanContent;
    String inputtitle;
    String roomscantype;
    String strBarcodescantype;
    String strLocationscantype;

    Context mContext = RoomMainSummary.this;
    Activity mActivity = (Activity) mContext;
    private model_pro_ar_scanasset scannedasset;
    private int tempviewid;

    private boolean savescan;
    private boolean fromexpanded = false;
    private boolean roomorasset = false;
    private int currentlayout;
    private String carriedToastMessage;
    private int carriedSummaryChoice;
    private int carriedLightColour;
    private boolean fromPopulate;
    private String carriedBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_tally_sum);
        Bundle fromSelectAsset = getIntent().getExtras();
        fromexpanded = fromSelectAsset.getBoolean("came_from_expanded");
        fromPopulate = fromSelectAsset.getBoolean("FROM POPULATE");
        roomorasset = fromSelectAsset.getBoolean("roomorasset");
        strSelectedRoomBackup = fromSelectAsset.getString(intentcodes.asset_activity.current_room);
        carriedLightColour = fromSelectAsset.getInt("LIGHT COLOUR");
        carriedBarcode = fromSelectAsset.getString("carried barcode","");

        if (!fromexpanded) {
            strSelectedRoom = fromSelectAsset.getString("ROOM SCAN");
            lightcolour = fromSelectAsset.getInt("LIGHT COLOUR");
            MakeExpandedList(strSelectedRoom);
        } else {
            carriedToastMessage = fromSelectAsset.getString("Toast String");
            listArray = fromSelectAsset.getParcelableArrayList("Array Content");
            carriedSummaryChoice = fromSelectAsset.getInt("Summary Choice");
            strSelectedRoom = fromSelectAsset.getString("ROOM SCAN");
            lightcolour = fromSelectAsset.getInt("LIGHT COLOUR");

            if (roomorasset) {
                ScanRoom();
            } else {
                  ScanAsset();
            }
        }
    }

    public View.OnClickListener listenernewroom = v -> {
        ScanRoom();     ///////Clears Current Room and Contained Assets
    };

    public void ScanAsset() {               ///////Start Instance Of New Asset Scan

        currentlayout = 1;
        intentcode = 1;
        inputtitle = "Please Input Barcode";
        strBarcodescantype = "S";
        savescan=true;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        IntentIntegrator scanAssetintent = new IntentIntegrator(mActivity);
        scanAssetintent.setBeepEnabled(sharedPref.getBoolean("scan_beep",false));

        scanAssetintent.setOrientationLocked(false);
        scanAssetintent.initiateScan();
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
        scanAssetintent.addExtra("INTENT CODE",11);

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

    public View.OnClickListener listenerscan = v -> {
        ScanAsset();                          ///////Starts Instance of New Asset Scan In Current Room

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        /////////////////New Asset Results//////////////////////////////////////////////////////////////////////////////////////////////

        super.onActivityResult(requestCode, resultCode, data);
        String[] a = {"$", "%", "?", "/", "\\\\", "*", "-", "+", "(", ")", "=", "+", "!", "@", "#", "^",};

        if (intentcode == 1) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
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
                SelectAsset selectAsset = new SelectAsset(mContext, mActivity);
                if (scanContent.equals("R0000")) {
                    if (savescan) {
                        selectAsset.ScanAsset();
                    } else {
                        //newRoomIntent(strSelectedRoom, scanContent);
                        selectAsset.ScanSearchAsset();
                    }
                    //   } else if (scanContent == null) {
                    //     Inputbox(1);
                } else if ((scanContent.startsWith("R") | (scanContent.length() < 5))) {
                    Toast.makeText(getBaseContext(), "Error When Scanning Asset, Room Was Scanned Instead", Toast.LENGTH_LONG).show();
                    //ScanAsset(this);
                    Intent gotomainsummary = new Intent(RoomMainSummary.this, RoomMainSummary.class);
                    gotomainsummary.putExtra("ROOM SCAN", strSelectedRoom);
                    // MakeExpandedList(scanContent);
                } else {
                    strLocationscantype = "S";

                    if (savescan) {
                        ScanCheck(scanContent, strSelectedRoom);
                    }
                    newRoomIntent(strSelectedRoom, scanContent, RoomMainSummary.this);
                }
            } catch (NullPointerException ignore) {
                Inputbox(1, this);
            }
        }

/////////////////////////////////////New Room Results///////////////////////////////////////////////////////////////////////////

        if (intentcode == 5) {

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

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
                assert strSelectedRoom != null;
                if (strSelectedRoom == null | !strSelectedRoom.startsWith("R")) {
                    Inputbox(0, this);
                } else {
                    Intent gotomainsummary = new Intent(RoomMainSummary.this, RoomMainSummary.class);
                    gotomainsummary.putExtra("ROOM SCAN", strSelectedRoom);
                    startActivity(gotomainsummary);
                    //MakeExpandedList(strSelectedRoom);
                }
            } catch (NullPointerException ignore) {
                Inputbox(0, this);
            }
        }

    }

    protected void onResume() {
        super.onResume();
    }

    public void newRoomIntent(String strscanContent, String barcode, Activity mContext) {
        Intent roomintent = new Intent(mContext, PopulateRoomActivity.class);
        roomintent.putExtra("ROOM SCAN", strscanContent);
        roomintent.putExtra(intentcodes.asset_activity.current_room, strSelectedRoomBackup);
        roomintent.putExtra("CYCLE", GetCycle());
        roomintent.putExtra("LOCATION SCAN TYPE", "s");
        roomintent.putExtra("LOCATION NAME", GetLocationName(strscanContent));
        roomintent.putExtra("RESPONSIBLE PERSON", GetRespPerson(strscanContent));
        roomintent.putExtra("BARCODE",barcode);
        roomintent.putExtra("LIGHT COLOUR",lightcolour);
        roomintent.putExtra("SUMMARY VALUE", tempviewid);
        startActivity(roomintent);
    }

    public void ScanCheck(String scanContent, String strSelectedRoom) {       ///////Database Handler

        MakeScanAssetData makeScanAssetData = new MakeScanAssetData(scanContent, strSelectedRoom);
        makeScanAssetData.setmContext(mContext);
        scannedasset = makeScanAssetData.MakeAsset(GetCycle(), strBarcodescantype, strLocationscantype);
        SelectAsset.SaveData saveData = new SelectAsset.SaveData();
        saveData.setRoom(strSelectedRoom);
        saveData.setBarcode(scanContent);
        saveData.setScanneddata(scannedasset);
        saveData.setType("S");
        saveData.execute(scannedasset);
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
                        Intent gotomainsummary = new Intent(RoomMainSummary.this,RoomMainSummary.class);
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

                            newRoomIntent(strSelectedRoom, scanContent,RoomMainSummary.this);

                        } else {
                            intentcode = 5;
                            roomscantype = "k";
                            //NewRoom();
                            Intent gotomainsummary = new Intent(RoomMainSummary.this,RoomMainSummary.class);
                            gotomainsummary.putExtra("ROOM SCAN",strSelectedRoom);
                            //MakeExpandedList(scanContent);
                        }
                    }
                    break;
            }
            //} else {
            // Toast.makeText(getBaseContext(), "Input value was the incorrect length, please try again", Toast.LENGTH_SHORT).show();
            // Inputbox(state);
            // }
            ActivityLogger activityLogger = new ActivityLogger(mContext);
            activityLogger.createLog("input_log"+ MainActivity.USER,input_value,true,true,false);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            if (fromPopulate) {
               newRoomIntent(strSelectedRoom,carriedBarcode,RoomMainSummary.this);
            }
            else if (fromexpanded) {
                gotoroom(0,carriedToastMessage,lightcolour,carriedSummaryChoice, listArray, strSelectedRoomBackup);
            }
        });
        builder.show();

    }

    View.OnClickListener listenerItem = v -> {

        lightcolour = 0;
        int counthandler = 0;
        String temp = null;

        switch (v.getId()) {
            case R.id.lnlOut:
                lightcolour = R.drawable.room_item_out_of_location;
                listArray = arrWrongLocaCount;
                counthandler = 5;
                temp = " Assets In The Wrong Room";
                break;
            case R.id.lnlAlready:
                lightcolour = R.drawable.room_item_already_scanned;
                listArray = arrAlreadycount;
                counthandler = 1;
                temp = " Assets In The Room Already Scanned";
                break;
            case R.id.lnlDisposed:
                lightcolour = R.drawable.room_item_previously_disposed;
                listArray = arrPrevDispCount;
                counthandler = 4;
                temp = " Assets In That Were Previously Disposed Of";
                break;
            case R.id.lnlManual:
                lightcolour = R.drawable.room_item_manually_added;
                counthandler = 3;
                listArray = arrManualCount;
                temp = " Assets In That Were Added Manually";
                break;
            case R.id.lnlNewCount:
                lightcolour = R.drawable.room_item_newly_scanned;
                counthandler = 6;
                listArray = arrNewCount;
                temp = " Assets In That Were Scanned Elsewhere";
                break;
            case R.id.lnlNotScanned:
                lightcolour = R.drawable.room_item_notscanned;
                listArray = arrMissingcount;
                counthandler = 2;
                temp = " Assets That Are Missing From The Registry";
                break;
            case R.id.lnlNotYetScanned:
                lightcolour = R.drawable.room_item_not_yet_scanned;
                listArray = arrNotYetScannedCount;
                counthandler = 7;
                temp = " Assets That Have Not Been Scanned in the Room";
                break;
        }

        TextView summary_title = findViewById(R.id.txtAsset_Summ_Header);
        String title = String.valueOf(summary_title.getText());
        String titleextract = title.substring(title.indexOf(" ")+1,title.lastIndexOf(" "));

        gotoroom(counthandler,temp,lightcolour,v.getId(), listArray, titleextract);
    };

    private void gotoroom(int counthandler, String toast, int lightcolour, int v, ArrayList list, String selectroom) {
        Intent expandedlistforroom = new Intent(RoomMainSummary.this,ExpandedSummaryOfRoom.class);
        expandedlistforroom.putExtra("Count Handler",counthandler);
        expandedlistforroom.putExtra("Toast String",toast);
        expandedlistforroom.putParcelableArrayListExtra("Array Content",list);
        expandedlistforroom.putExtra("LIGHT COLOUR",lightcolour);
        expandedlistforroom.putExtra("Summary Choice",v);
        expandedlistforroom.putExtra("ROOM SCAN",selectroom);
        startActivity(expandedlistforroom);
    }

    View.OnClickListener listenerRooms = v -> {
        Intent gotorooms = new Intent(RoomMainSummary.this,SelectAsset.class);                    ///////Starts Instance of New Asset Scan In Current Room
        startActivity(gotorooms);
    };

    View.OnClickListener sendmainclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int alreadyscannedCount = arrAlreadycount.size();
            //int manualscanningCount=arrManualCount.size();
            //int missingfromregisteryCount=arrMissingcount.size();
            //int previouslydisposedcount=arrPrevDispCount.size();
            int notyetscannedCount=arrNotYetScannedCount.size();
            //int wronglocationCount=arrWrongLocaCount.size();
            //int newlyscannedCount=arrNewCount.size();

            SelectAsset selectAsset = new SelectAsset(mContext,mActivity);
            selectAsset.SendEmail(true,strSelectedRoom,0,notyetscannedCount,alreadyscannedCount);
        }
    };

    @SuppressLint("NewApi")
    public void MakeExpandedList(String strSelectedRoomclicked) {        ///////Creates Instance of New Summary For Current Room

        setContentView(R.layout.room_tally_sum);

        View view = this.getCurrentFocus();
        if (view != null) {
            @SuppressLint({"NewApi", "LocalSuppress"}) InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        TextView summary_title = findViewById(R.id.txtAsset_Summ_Header);
        summary_title.setText(String.format("ROOM: %s SUMMARY", strSelectedRoomclicked));

        Button gotosummary = findViewById(R.id.btnFinishSummary);
        gotosummary.setOnClickListener(listenernewroom);

        Button scannewasset = findViewById(R.id.btnScanAssetFromSumm);
        scannewasset.setOnClickListener(listenerscan);

        RoomSummaryActivity roomSummaryActivity = new RoomSummaryActivity(mContext);
        roomSummaryActivity.setStrCurrentRoom(strSelectedRoomclicked);

        TextView txtnotscancount = findViewById(R.id.txtNotSCannedCount);
        arrMissingcount =roomSummaryActivity.GetNotinRegister();
        ConstraintLayout lnlMissingforReg = findViewById(R.id.lnlNotScanned);
        lnlMissingforReg.setOnClickListener(listenerItem);
        txtnotscancount.setText(String.valueOf(arrMissingcount.size()));

        TextView txtalreadyscancount = findViewById(R.id.txtAlreadyScannedCount);
        arrAlreadycount = roomSummaryActivity.GetAlready();
        ConstraintLayout lnlAlready = findViewById(R.id.lnlAlready);
        lnlAlready.setOnClickListener(listenerItem);
        txtalreadyscancount.setText(String.valueOf(arrAlreadycount.size()));

        TextView txtmanualscancount = findViewById(R.id.txtManualscancount);
        arrManualCount = roomSummaryActivity.GetManual();
        ConstraintLayout lnlManual = findViewById(R.id.lnlManual);
        lnlManual.setOnClickListener(listenerItem);
        txtmanualscancount.setText(String.valueOf(arrManualCount.size()));

        TextView txtprevscancount = findViewById(R.id.txtPrevscanCount);
        arrPrevDispCount = roomSummaryActivity.GetPreviouslyDisposed();
        ConstraintLayout lnlPrevDis = findViewById(R.id.lnlDisposed);
        lnlPrevDis.setOnClickListener(listenerItem);
        txtprevscancount.setText(String.valueOf(arrPrevDispCount.size()));

        TextView txtoutlocacount = findViewById(R.id.txtOutLocaCount);
        arrWrongLocaCount = roomSummaryActivity.GetOutofLocation();
        ConstraintLayout lnlOutOfLoc = findViewById(R.id.lnlOut);
        lnlOutOfLoc.setOnClickListener(listenerItem);
        txtoutlocacount.setText(String.valueOf(arrWrongLocaCount.size()));

        TextView txtnotyetscancount = findViewById(R.id.txtNotYetScannedCount);
        arrNotYetScannedCount = roomSummaryActivity.GetMissing();
        ConstraintLayout lnlNotYet = findViewById(R.id.lnlNotYetScanned);
        lnlNotYet.setOnClickListener(listenerItem);
        txtnotyetscancount.setText(String.valueOf(arrNotYetScannedCount.size()));

        TextView txtscannedelsewhere = findViewById(R.id.txtNewAssetCount);
        arrNewCount = roomSummaryActivity.GetOtherLocation();
        ConstraintLayout lnlOtherPlace = findViewById(R.id.lnlNewCount);
        lnlOtherPlace.setOnClickListener(listenerItem);
        txtscannedelsewhere.setText(String.valueOf(arrNewCount.size()));

        int scanneditemsCount = arrAlreadycount.size() + arrNotYetScannedCount.size();          //TOTAL
        TextView totalcount = findViewById(R.id.txtTotalAssetCount);
        totalcount.setText(String.format("%s/%s", String.valueOf(arrAlreadycount.size()), String.valueOf(scanneditemsCount)));

        DataCursorLoaders dataCursorLoaders = new DataCursorLoaders(RoomMainSummary.this, "Select loc_code FROM pro_ar_locations");
        Cursor roomassetcount = dataCursorLoaders.loadInBackground();

        ConstraintLayout lnlRooms = findViewById(R.id.lnlRoom);
        lnlRooms.setOnClickListener(listenerRooms);
        TextView txtroomcount = findViewById(R.id.txtTotalRooms);
        txtroomcount.setText(String.valueOf(roomassetcount.getCount()));

        FloatingActionButton sendmail = findViewById(R.id.fltEmailCSV);
        sendmail.setOnClickListener(sendmainclick);
        registerForContextMenu(scannewasset);

    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent menu = new Intent(RoomMainSummary.this,SelectAsset.class);
        startActivity(menu);
    }
}
