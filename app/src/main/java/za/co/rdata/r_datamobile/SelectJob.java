package za.co.rdata.r_datamobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.DBHelpers.SymmetricDS_Helper;
import za.co.rdata.r_datamobile.DBHelpers.sqliteDBHelper;
import za.co.rdata.r_datamobile.DBMeta.meta;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_room;
import za.co.rdata.r_datamobile.adapters.adapter_JobRecycler;
import za.co.rdata.r_datamobile.adapters.adapter_RoomRecycler;

public class SelectJob extends AppCompatActivity {

    private static final String TAG = "Checking";
    public static String scanContent = "R0000";
    static public sqliteDBHelper sqliteDbHelper;
    static String strSelectedRoom;
    static Cursor roomcursor;
    String strBarcodescantype;
    String strLocationscantype;
    String inputtitle;
    String roomscantype;
    RecyclerView recyclerView;
    Context mContext;
    Activity mActivity;
    adapter_JobRecycler adapter_roomRecycler;
    String[] a = {"$", "%", "?", "/", "\\\\", "*", "-", "+", "(", ")", "=", "+", "!", "@", "#", "^",};

    public SelectJob() {
        super();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard(this);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SymmetricDS_Helper.Start_SymmetricDS(this.getApplicationContext(), false);
        //startService(new Intent(this,DeviceLocationService.class));
        sqliteDbHelper = sqliteDBHelper.getInstance(this.getApplicationContext());

        Intent iPopJob = getIntent();
        Bundle bSaved = iPopJob.getExtras();
        scanContent = "00000";

        //ArrayList<model_pro_jb_jobcard> arrtoshow = new ArrayList<>();
        ArrayList<model_pro_ar_asset_room> arrtoshow = new ArrayList<>();

        Cursor jobcards;
        jobcards = sqliteDbHelper.getReadableDatabase().rawQuery("SELECT pro_fo_job_no" +
                ",pro_fo_prop_detail" +
                ",pro_fo_status" +
                ",pro_fo_prop_detail" +
                ",pro_fo_job_type from " +
                "pro_fo_jobs", null);
        jobcards.moveToFirst();

        try {
            do {
                arrtoshow.add(new model_pro_ar_asset_room(jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_job_no)),
                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_prop_detail)),
                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_status)),
                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_prop_detail)),
                        jobcards.getString(jobcards.getColumnIndex(meta.pro_fo_jobs.pro_fo_job_type))));
                jobcards.moveToNext();
            }
            while (!jobcards.isAfterLast());

        } catch (CursorIndexOutOfBoundsException ex) {
            Toast.makeText(getBaseContext(), "No Rooms Found, Please check Asset Register", Toast.LENGTH_LONG).show();
        }

        jobcards.close();

        setContentView(R.layout.activity_select_job);
        recyclerView = findViewById(R.id.activity_job_list_listView);

        adapter_roomRecycler = new adapter_JobRecycler(arrtoshow, R.layout.select_job_item);
        //adapter_roomRecycler.setRecyclerViewID(R.id.activity_room_list_listView);
        adapter_roomRecycler.setRetbarcode(R.id.txtJobnumber);
        adapter_roomRecycler.setRetdept(R.id.txtJobtype);
        adapter_roomRecycler.setRetdesc(R.id.txtJobaddress);
        adapter_roomRecycler.setRetframe(R.id.conJobframe);
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
    }

    public void onBackPressed() {
        Intent mainmenu = new Intent(SelectJob.this, MainActivity.class);
        finish();
        startActivity(mainmenu);
    }

}
