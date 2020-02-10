package za.co.rdata.r_datamobile.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_rows;
import za.co.rdata.r_datamobile.Models.model_pro_ar_scanasset;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.SelectAsset;
import za.co.rdata.r_datamobile.assetModule.PopulateRoomActivity;
import za.co.rdata.r_datamobile.stringTools.SelectCondition;
import za.co.rdata.r_datamobile.stringTools.SelectDescriptionActivity;
import za.co.rdata.r_datamobile.stringTools.SelectNoteActivity;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;

/**
 * Created by James de Scande on 25/10/2017 at 14:16.
 */

@SuppressLint("ValidFragment")
public class fragment_MakeAssetViewContent extends Fragment {

     private TextView txtDescription;// = findViewById(R.id.txtDescription);
     private TextView txtCondition;// = findViewById(R.id.txtCondition);
     private TextView txtComments;// = findViewById(R.id.txtComments);
     private TextView txtComments2;
     private EditText txtAddRemaining;// = findViewById(R.id.txtAddontoremaining);

    private int GET_NOTE_CODE = 2;
    private int GET_COND_CODE = 3;
    private int GET_DESC_CODE = 4;

    private String responsibleperson;
    private String locationname;

    private model_pro_ar_asset_rows assetdata;
    private model_pro_ar_scanasset scannedassetdata;

    private sqliteDBHelper sqlliteDBHelper = new sqliteDBHelper(getContext());

    private View.OnClickListener addonclear = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            txtAddRemaining.setText("");     ///////Closes Current Room And Moves To Room Summary
        }
    };
    private View.OnLongClickListener listen_noteTextView = v -> {
        PopulateRoomActivity.intentcode = 2;
        SelectNoteActivity.tablename = meta.pro_ar_notes.TableName;
        Intent intent = new Intent(getActivity(), SelectNoteActivity.class);
        startActivityForResult(intent, GET_NOTE_CODE);      ///////Starts Instance Of Notes for Note Value in Registry
        return true;
    };
    private View.OnLongClickListener listen_noteTextView2 = v -> {
        PopulateRoomActivity.intentcode = 21;
        SelectNoteActivity.tablename = meta.pro_ar_notes.TableName;
        Intent intent = new Intent(getActivity(), SelectNoteActivity.class);
        startActivityForResult(intent, GET_NOTE_CODE);      ///////Starts Instance Of Notes for Note Value in Registry
        return true;
    };
    private View.OnLongClickListener listen_condTextView = v -> {
        PopulateRoomActivity.intentcode = 3;
        Intent intent = new Intent(getActivity(), SelectCondition.class);
        startActivityForResult(intent, GET_COND_CODE);      ///////Starts Instance Of Condition Value in Registry
        return true;
    };
    private View.OnLongClickListener listen_descTextView = v -> {
        PopulateRoomActivity.intentcode = 4;
        Intent intent = new Intent(getActivity(), SelectDescriptionActivity.class);
        startActivityForResult(intent, GET_DESC_CODE);      ///////Starts Instance Of Condition Value in Registry
        return true;
    };


    private SQLiteDatabase db = MainActivity.sqliteDbHelper.getReadableDatabase();

    private void ScannedBoxColourChange(int light) {

        //txtScannedColour = new TextView(getContext());
        //txtScannedColour = viewFragment.findViewById(R.id.isscannedlight);
        //this.txtScannedColour.setBackgroundResource(light);
        if (light == R.drawable.room_item_not_yet_scanned) {
            txtComments.setEnabled(false);
            txtComments2.setEnabled(false);
            txtCondition.setEnabled(false);
            txtDescription.setEnabled(false);
            txtAddRemaining.setEnabled(false);
        } else if ((light == R.drawable.room_item_manually_added) | (light == R.drawable.room_item_notscanned)) {
            txtComments.setEnabled(true);
            txtComments2.setEnabled(true);
            txtCondition.setEnabled(true);
            txtDescription.setEnabled(true);
            txtAddRemaining.setEnabled(true);
        } else {
            txtComments.setEnabled(true);
            txtComments2.setEnabled(true);
            txtCondition.setEnabled(true);
            txtDescription.setEnabled(false);
            txtAddRemaining.setEnabled(true);
        }
    }

    private static boolean CompareOneDBValues(Cursor firstcursorname, String columnfirstname, String checkvalue) {  ////////DB Entry Comparison

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

    public void setResponsibleperson(String responsibleperson) {
        this.responsibleperson = responsibleperson;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }

    public model_pro_ar_asset_rows getAssetdata() {
        return assetdata;
    }

    public void setAssetdata(model_pro_ar_asset_rows assetdata) {
        this.assetdata = assetdata;
    }

    public model_pro_ar_scanasset getScannedassetdata() {
        return scannedassetdata;
    }

    public void setScannedassetdata(model_pro_ar_scanasset scannedassetdata) {
        this.scannedassetdata = scannedassetdata;
    }

    public void setScannedLightColour(int light) {
    }

////////////////////////////////////////////Listeners////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Cursor curCurrentAssetData = db.rawQuery("SELECT * FROM pro_ar_register WHERE reg_barcode = '" + assetdata.getReg_barcode() + "'", null);
        curCurrentAssetData.moveToFirst();

        Cursor curCurrentScanData = db.rawQuery("SELECT * FROM pro_ar_scan WHERE scan_barcode = '" + scannedassetdata.getScan_barcode() + "'", null);
        curCurrentScanData.moveToFirst();

        View viewFragment = inflater.inflate(R.layout.activity_asset_capture, container, false);

        // = findViewById(R.id.txtRoomNumber);
        TextView txtRoomNum = viewFragment.findViewById(R.id.txtRoomNumber);
        txtRoomNum.setText(assetdata.getReg_location_code());

        // = findViewById(R.id.txtLocation);
        TextView txtLocation = viewFragment.findViewById(R.id.txtLocation);
        txtLocation.setText(GetLocationName(assetdata.getReg_location_code()));

        // = findViewById(R.id.txtDepartment);
        TextView txtDepartment = viewFragment.findViewById(R.id.txtDepartment);
        txtDepartment.setText(assetdata.getReg_dept_code());

        // = findViewById(R.id.txtResponsiblePerson);
        TextView txtResp = viewFragment.findViewById(R.id.txtResponsiblePerson);
        txtResp.setText(responsibleperson);

        // = findViewById(R.id.txtAssetCode);
        TextView txtAssetCode = viewFragment.findViewById(R.id.txtAssetCode);
        txtAssetCode.setText(assetdata.getReg_barcode());

        // = findViewById(R.id.txtSerialNumber);
        TextView txtSerialNr = viewFragment.findViewById(R.id.txtSerialNumber);
        txtSerialNr.setText(assetdata.getReg_asset_serial_nr());

        txtDescription = viewFragment.findViewById(R.id.txtDescription);
        txtDescription.setText(assetdata.getReg_asset_desc());

        txtCondition = viewFragment.findViewById(R.id.txtCondition);
        txtCondition.setText(assetdata.getReg_condition_code());

        txtComments = viewFragment.findViewById(R.id.txtComments);
        txtComments.setText(assetdata.getReg_comments1());

        txtComments2 = viewFragment.findViewById(R.id.txtComments2);
        txtComments2.setText(assetdata.getReg_comments2());

        // = findViewById(R.id.txtUsefulLife);
        TextView txtUseLife = viewFragment.findViewById(R.id.txtUsefulLife);
        txtUseLife.setText(assetdata.getReg_useful_life());

        // = findViewById(R.id.txtUsefulRemaining);
        TextView txtUseRemaining = viewFragment.findViewById(R.id.txtUsefulRemaining);
        txtUseRemaining.setText(assetdata.getReg_useful_remainder());

        txtAddRemaining = viewFragment.findViewById(R.id.txtAddontoremaining);
        txtAddRemaining.setText(scannedassetdata.getAdjremainder());

        TextView txtQtyforManual = viewFragment.findViewById(R.id.txtQtyfoManual);
        TextView txtQtyLabel = viewFragment.findViewById(R.id.textView27);

        ConstraintLayout constraintLayout = viewFragment.findViewById(R.id.cslAssetCapture);
        ConstraintSet constraintSet = new ConstraintSet();

        try {
        /*
            if ((assetdata.getReg_qtyformanual() > 0) && (assetdata.getReg_barcode().startsWith(getResources().getString(R.string.manual_tag)))) {
                txtQtyforManual.setAlpha(1);

                txtQtyLabel.setAlpha(1);
                txtQtyforManual.setText(String.valueOf(assetdata.getReg_qtyformanual()));
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.textView18, ConstraintSet.TOP, R.id.textView27, ConstraintSet.BOTTOM, 8);
                constraintSet.applyTo(constraintLayout);
            } else {
            */
                txtQtyforManual.setAlpha(0);
                txtQtyLabel.setAlpha(0);
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.textView18, ConstraintSet.TOP, R.id.textView24, ConstraintSet.BOTTOM, 8);
                constraintSet.applyTo(constraintLayout);


        } catch (NullPointerException e) {
            txtQtyforManual.setAlpha(0);
            txtQtyLabel.setAlpha(0);
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.textView18, ConstraintSet.TOP, R.id.textView24, ConstraintSet.BOTTOM, 33);
            constraintSet.applyTo(constraintLayout);
        } catch (RuntimeException e) {
            txtQtyforManual.setAlpha(0);
            txtQtyLabel.setAlpha(0);
        }


        TextView txtScanRoom = viewFragment.findViewById(R.id.txtScannedRoom);
        try {
            txtScanRoom.setText(scannedassetdata.getScan_location());
        } catch (NullPointerException | CursorIndexOutOfBoundsException e) {
            txtScanRoom.setText(R.string.not_yet_scanned);
        }

        TextView txtScanDate = viewFragment.findViewById(R.id.txtScannedDate);
        try {
            txtScanDate.setText(curCurrentScanData.getString(curCurrentScanData.getColumnIndex(meta.pro_ar_scan.scan_datetime)));
        } catch (NullPointerException | CursorIndexOutOfBoundsException e) {
            txtScanDate.setText(R.string.not_yet_scanned);
        }

        Cursor curCurrentAssetCondition = db.rawQuery("SELECT reg_condition_code FROM pro_ar_register WHERE reg_barcode = '" + txtAssetCode.getText().toString() + "'", null);
        curCurrentAssetCondition.moveToFirst();

        //TextView txtCond = listAssetFragments.get(intPagePosition).getView().findViewById(R.id.txtCondition);

        if (!CompareOneDBValues(curCurrentAssetCondition, meta.pro_ar_register.reg_condition_code, txtCondition.getText().toString())) {
            txtCondition.setBackgroundResource(R.drawable.value_differs);
        } else txtCondition.setBackgroundResource(R.drawable.textinput_shape);

        curCurrentAssetCondition.close();

        Cursor curCurrentAssetComment = db.rawQuery("SELECT reg_comments1 FROM pro_ar_register WHERE reg_barcode = '" + txtAssetCode.getText().toString() + "'", null);
        curCurrentAssetComment.moveToFirst();

        //TextView txtComment = listAssetFragments.get(intPagePosition).getView().findViewById(R.id.txtComments);

        if (!CompareOneDBValues(curCurrentAssetComment, meta.pro_ar_register.reg_comments1, txtComments.getText().toString())) {
            txtComments.setBackgroundResource(R.drawable.value_differs);
        } else txtComments.setBackgroundResource(R.drawable.textinput_shape);

        curCurrentAssetComment.close();

        Cursor curCurrentAssetComment2 = db.rawQuery("SELECT reg_comments2 FROM pro_ar_register WHERE reg_barcode = '" + txtAssetCode.getText().toString() + "'", null);
        curCurrentAssetComment2.moveToFirst();

        if (!CompareOneDBValues(curCurrentAssetComment2, meta.pro_ar_register.reg_comments2, txtComments2.getText().toString())) {
            txtComments2.setBackgroundResource(R.drawable.value_differs);
        } else txtComments2.setBackgroundResource(R.drawable.textinput_shape);

        curCurrentAssetComment2.close();

        if (!assetdata.getActive()) {
        } else if (assetdata.getManual()) {
        } else if (scannedassetdata.getScan_location_entry().equals("R0000")) {
        } else if (scannedassetdata.getScan_location().equals("not scanned")) {
        } else if (!locationname.contentEquals(txtLocation.getText())) {
        } else
            ;

        Log.d("Checking Light Colour", "Entry:" + scannedassetdata.getScan_location_entry() + "   " + "Scanned:" + scannedassetdata.getScan_location());

        txtComments.setOnLongClickListener(listen_noteTextView);
        txtComments.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        txtComments2.setOnLongClickListener(listen_noteTextView2);
        txtCondition.setOnLongClickListener(listen_condTextView);
        txtCondition.setOnTouchListener(new CustomTouchListener());


        txtDescription.setOnLongClickListener(listen_descTextView);
        txtAddRemaining.setOnClickListener(addonclear);
        txtAddRemaining.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                s = txtAddRemaining.getText();                ///////Extracts AdjRemainder to String

                String templife = s.toString().trim();

                try {
                    scannedassetdata.setAdjremainder(templife);
                } catch (NullPointerException e) {
                    scannedassetdata.setAdjremainder("0");
                } finally {
                    SelectAsset.SaveAdjRemainder saveAdjRemainder = new SelectAsset.SaveAdjRemainder();
                    saveAdjRemainder.setBarcode(assetdata.getReg_barcode());
                    saveAdjRemainder.execute(scannedassetdata.getAdjremainder());
                }
            }
        });

        curCurrentAssetData.close();
        curCurrentScanData.close();

        return viewFragment;
    }

    class CustomTouchListener implements View.OnTouchListener {

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ((TextView) view).setTextColor(0xFFFFFFFF); // white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    ((TextView) view).setTextColor(getResources().getColor(R.color.new_input_box)); // lightblack
                    break;
            }
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        txtComments.setOnLongClickListener(listen_noteTextView);
        txtCondition.setOnLongClickListener(listen_condTextView);
        txtDescription.setOnLongClickListener(listen_descTextView);
        txtAddRemaining.setOnClickListener(addonclear);
        ScannedBoxColourChange(lightColourChecking(getAssetdata(), getScannedassetdata()));
    }

    private int lightColourChecking(model_pro_ar_asset_rows assetdata, model_pro_ar_scanasset scannedassetdata) {
        int light;
        if (!assetdata.getActive()) {
            light = R.drawable.room_item_previously_disposed;
        } else if (assetdata.getManual()) {
            light = R.drawable.room_item_manually_added;
        } else if (scannedassetdata.getScan_location_entry().equals("R0000")) {
            light = R.drawable.room_item_notscanned;
        } else if (scannedassetdata.getScan_location().equals("not scanned")) {
            light = R.drawable.room_item_not_yet_scanned;
        } else if (!scannedassetdata.getScan_location().equals(scannedassetdata.getScan_location_entry())) {
            light = R.drawable.room_item_out_of_location;
        } else
            light = R.drawable.room_item_already_scanned;

        return light;
    }

    private String GetLocationName(String locationname) {

        String locationnameresult;
        Log.d("GETLOCATIONNAMECHECK", locationname);
        Cursor curRoomcursor = db.rawQuery("SELECT loc_name FROM pro_ar_locations WHERE loc_code = '" + locationname + "'", null);
        curRoomcursor.moveToFirst();
        try {
            locationnameresult = curRoomcursor.getString(curRoomcursor.getColumnIndex(meta.pro_ar_locations.loc_name));
        } catch (CursorIndexOutOfBoundsException e) {
            locationnameresult = "missing";
        }
        Log.d("GETLOCATIONNAMERESULT", locationnameresult);
        curRoomcursor.close();
        return locationnameresult;
    }

}
