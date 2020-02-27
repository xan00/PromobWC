package za.co.rdata.r_datamobile.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.GalleryActivity;
import za.co.rdata.r_datamobile.MainActivity;
import za.co.rdata.r_datamobile.Models.model_pro_mr_no_access;
import za.co.rdata.r_datamobile.Models.model_pro_mr_notes;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_rows;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.locationTools.CalculateCoordinateDistance;
import za.co.rdata.r_datamobile.meterReadingModule.InputReadingActivity;
import za.co.rdata.r_datamobile.meterReadingModule.MeterReaderController;
import za.co.rdata.r_datamobile.meterReadingModule.MeterReadingActivity;
import za.co.rdata.r_datamobile.stringTools.MakeDate;
import za.co.rdata.r_datamobile.stringTools.SelectNoAccessActivity;
import za.co.rdata.r_datamobile.stringTools.SelectNoteActivity;

/**
 * Project: R-DataMobile
 * Created by james de scande on 18/02/2020.
 */

public class fragment_jobMeter extends Fragment {

    private MeterReaderController.Keys rid;
    private model_pro_mr_route_rows route_row = null;
    private boolean row_changed = false;
    private TextView txtfragJobNumber;
    private TextView txtfragJobAddress;
    private TextView txtfragJobtype;
    private TextView txtfragJobStatus;
    private Button btnDeleteReading;
    private int GET_READING_REQUEST_CODE = 1;
    private int GET_NOTE_CODE = 2;
    private int GET_NO_ACCESS_CODE = 3;
    private View viewFragment;
    public static String tablename = meta.pro_mr_route_notes.TableName;

    public MeterReaderController.Keys getRid() {
        return rid;
    }

    public void setRid(MeterReaderController.Keys rid) {
        this.rid = rid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewFragment = inflater.inflate(R.layout.content_job_card_meter, container, false);

        txtfragJobNumber = viewFragment.findViewById(R.id.txtfragJobNumber);
        txtfragJobtype = viewFragment.findViewById(R.id.txtfragJobtype);
        txtfragJobStatus = viewFragment.findViewById(R.id.txtfragJobStatus);
        txtfragJobAddress = viewFragment.findViewById(R.id.txtfragJobAddress);
//        Button btnDeleteNote = viewFragment.findViewById(R.id.btnDeleteNote);
//        Button btnDeleteNoAccess = viewFragment.findViewById(R.id.btnDeleteNoAccess);
//        btnDeleteReading = viewFragment.findViewById(R.id.F_Meter_Reading_B_DeleteReading);

        //textViewReading.setOnClickListener(listen_readingTextView);
        //textViewNoteDescription.setOnLongClickListener(listen_noteTextView);
        //textViewNaAccessDescription.setOnLongClickListener(listen_noAccessTextView);
        //btnDeleteNote.setOnLongClickListener(listen_btnDeleteNote);
        //btnDeleteNoAccess.setOnLongClickListener(listen_btnDeleteNoAccess);
        //btnDeleteReading.setOnLongClickListener(listen_btnDeleteReading);

        //GetData getData = new GetData();
        //getData.execute();

        return viewFragment;
    }


}
