package za.co.rdata.r_datamobile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import za.co.rdata.r_datamobile.DBHelpers.DBHelper;
import za.co.rdata.r_datamobile.DBHelpers.SymmetricDS_Helper;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_header;
import za.co.rdata.r_datamobile.Models.model_pro_mr_route_rows;
import za.co.rdata.r_datamobile.fileTools.ActivityLogger;
import za.co.rdata.r_datamobile.fileTools.DBExport;
//import za.co.rdata.r_datamobile.locationTools.DeviceLocationService;
import za.co.rdata.r_datamobile.meterReadingModule.CloseRouteActivity;
import za.co.rdata.r_datamobile.meterReadingModule.MeterReaderController;
import za.co.rdata.r_datamobile.meterReadingModule.MeterReadingActivity;
import za.co.rdata.r_datamobile.meterReadingModule.MeterSearchActivity;

import static za.co.rdata.r_datamobile.stringTools.MakeDate.GetCleanDate;

public class SelectRoute extends AppCompatActivity {

    private ArrayList<model_pro_mr_route_header> Routes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SymmetricDS_Helper.Start_SymmetricDS(this.getApplicationContext(), false);
        //startService(new Intent(this,DeviceLocationService.class));
        setContentView(R.layout.activity_select_route);

        FloatingActionButton fabEmailroute = findViewById(R.id.fabEmailRoutes);
        fabEmailroute.setOnClickListener(view -> {
            DBExport mail = new DBExport();
            String route_backup_name = "AllRoutes" + GetCleanDate();
            String meter_backup_name = "AllMeters" + GetCleanDate();

            mail.exportDB(this, route_backup_name, "SELECT * FROM pro_mr_route_headers",1);
            mail.exportDB(this, meter_backup_name, "SELECT * FROM pro_mr_route_rows",1);

            Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
            i.putExtra(Intent.EXTRA_SUBJECT, "Route Backup");

            i.putExtra(Intent.EXTRA_TEXT, "This is a backup of all route data on the device"+MainActivity.USER+".");

            ArrayList<Uri> uris = new ArrayList<>();

                File filelocation1 = new File(Environment.getExternalStorageDirectory() + "/filesync/Documents/", route_backup_name + ".csv");
                Uri path1 = Uri.fromFile(filelocation1);
                uris.add(path1);
                File filelocation2 = new File(Environment.getExternalStorageDirectory() + "/filesync/Documents/", meter_backup_name + ".csv");
                Uri path2 = Uri.fromFile(filelocation2);
                uris.add(path2);

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
                Toast.makeText(SelectRoute.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

        });

        FloatingActionButton fltMoreOptionsRoutes = findViewById(R.id.fltMoreOptionsRoutes);
        fltMoreOptionsRoutes.setOnClickListener(view -> {
            Inputbox();
        });

    }

    public void Inputbox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Route");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {

            ArrayList<String> routenumbers = new ArrayList<>();
            routenumbers.clear();

            for (model_pro_mr_route_header route : Routes) {
                routenumbers.add(route.getRoute_number());
            }

            if (routenumbers.contains(input.getText().toString())) {
                Intent intent = new Intent(SelectRoute.this, MeterSearchActivity.class);
                intent.putExtra("route_number",input.getText().toString());
                intent.putExtra("came_from_select_route",true);
                //String routenumber = input.getText().toString();
                //MeterReaderController.route_header.setRoute_number(routenumber);
                startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(), "Input value was not available, please try again", Toast.LENGTH_SHORT).show();
                Inputbox();
            }

            //ActivityLogger activityLogger = new ActivityLogger(mContext);
            //activityLogger.createLog("input_log"+MainActivity.USER,input_value,true,true,false);

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private class routedetails_ListAdapter extends ArrayAdapter<model_pro_mr_route_header> {

        routedetails_ListAdapter() {
            super(SelectRoute.this, R.layout.select_route_list_item, Routes);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.select_route_list_item, parent, false);

            model_pro_mr_route_header Route = Routes.get(position);

            try {
                TextView textview_route_number = itemView.findViewById(R.id.route_list_item_Route_Number);
                TextView textview_description = itemView.findViewById(R.id.route_list_item_Description);
                TextView textview_releaseDate = itemView.findViewById(R.id.route_list_item_ReleaseDate);
                TextView textview_Cycle = itemView.findViewById(R.id.route_list_item_Cycle);
                TextView textview_Inst_Node = itemView.findViewById(R.id.route_list_item_Inst_Node);
                TextView textview_Mob_Node = itemView.findViewById(R.id.route_list_item_Mob_Node);
                textview_route_number.setText(Route.getRoute_number());
                textview_description.setText(Route.getDescription());
                textview_Cycle.setText(Route.getCycle());
                textview_Inst_Node.setText(Route.getInstNode_id());
                textview_Mob_Node.setText(Route.getMobnode_id());
                if (Route.getRelease_date() != null) {
                    textview_releaseDate.setText(Route.getRelease_date());
                } else {
                    textview_releaseDate.setText("");
                    textview_description.setText("");
                }

                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date release = null;
                String relDate = Routes.get(position).getRelease_date().substring(0, 10);

                try {
                    release = sdf.parse(relDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ConstraintLayout rllRoute = itemView.findViewById(R.id.rllRoute);

                boolean datebefore = false;
                if (new Date().before(release)) datebefore=true;

                boolean statusnot2 = false;
                int status2 = Routes.get(position).getStatus();
                if (!(status2 == 2)) statusnot2=true;

                boolean statusnot5 = false;
                int status5 = Routes.get(position).getStatus();
                if (!(status5 == 5)) statusnot5=true;

                if (datebefore || (statusnot2 && statusnot5)) {
                    rllRoute.setBackgroundResource(R.drawable.unavailable_route);
                } else {
                    //DBHelper.pro_mr_route_headers.setRouteStatus(Routes.get(position).getInstNode_id(), Routes.get(position).getInstNode_id(), Routes.get(position).getCycle(), Routes.get(position).getRoute_number(), 5);
                    rllRoute.setBackgroundResource(R.drawable.available_route);
                    Cursor cycle = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT max(cycle) from pro_mr_route_headers", null);
                    cycle.moveToFirst();

                    if (Routes.get(position).getStatus() == 2 ) {
                        MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("UPDATE pro_mr_route_headers SET status=5 where route_number='" + Routes.get(position).getRoute_number() + "' and cycle = '"+cycle.getString(0)+"'");
                    }
                    cycle.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return itemView;
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        ListView listView = findViewById(R.id.activity_select_route_listView);
        try {
            Routes.clear();
            Routes.addAll(DBHelper.pro_mr_route_headers.getAvailableRoutes());
            Routes.addAll(DBHelper.pro_mr_route_headers.getUnavailableRoutes());
        } catch (NullPointerException e) {
            e.printStackTrace();
            Intent mainmenu = new Intent(SelectRoute.this, MainActivity.class);
            finish();
            startActivity(mainmenu);
        }

            if (Routes.size() > 0) {
                registerForContextMenu(listView);

                listView.setOnItemClickListener((parent, view, position, id) -> {

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date release = null;
                    String relDate;
                    try {
                        relDate = Routes.get(position).getRelease_date().substring(0, 10);
                    }
                    catch (StringIndexOutOfBoundsException e) {
                        relDate = "20150101";
                    }

                    try {
                        release = sdf.parse(relDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    TextView textView_Route_Number = view.findViewById(R.id.route_list_item_Route_Number);
                    String route_number = (String) textView_Route_Number.getText();
                    Cursor status = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT status from pro_mr_route_headers where route_number = '"+route_number+"'", null);
                    status.moveToFirst();

                    if (status.getInt(0)==2 | status.getInt(0)==5) {
                        status.close();
                        ReadRoute(view);
                    } else {
                        status.close();
                        Toast.makeText(getBaseContext(), "This route is closed and cannot be read", Toast.LENGTH_LONG).show();
                    }

                });
            } else {
                Toast.makeText(getBaseContext(), "No Routes available!", Toast.LENGTH_LONG).show();
            }

            ArrayAdapter<model_pro_mr_route_header> adapter = new routedetails_ListAdapter();
            listView.setAdapter(adapter);
        }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.activity_select_route_listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(Routes.get(info.position).getDescription());
            String[] menuItems = {"Read", "Summary", "Close", "Resend"};
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListView listView = findViewById(R.id.activity_select_route_listView);
        //info.position - listView.getFirstVisiblePosition()
        View view = listView.getChildAt(info.position - listView.getFirstVisiblePosition());
        TextView textView_Route_Number = view.findViewById(R.id.route_list_item_Route_Number);
        TextView textView_Inst_Node = view.findViewById(R.id.route_list_item_Inst_Node);
        TextView textView_Mob_Node = view.findViewById(R.id.route_list_item_Mob_Node);
        String route_number = (String) textView_Route_Number.getText();
        Cursor status = MainActivity.sqliteDbHelper.getReadableDatabase().rawQuery("SELECT status from pro_mr_route_headers where route_number = '"+route_number+"'", null);
        status.moveToFirst();

        switch ((String) item.getTitle()) {
            case "Close":
                if (status.getInt(0)==2 | status.getInt(0)==5) {
                    status.close();
                    CloseRoute(view);
                } else {
                    status.close();
                    Toast.makeText(getBaseContext(), "This route is already closed", Toast.LENGTH_LONG).show();
                }
                break;
            case "Read":
                if (status.getInt(0)==2 | status.getInt(0)==5) {
                    status.close();
                    ReadRoute(view);
                } else {
                    status.close();
                    Toast.makeText(getBaseContext(), "This route is closed and cannot be read", Toast.LENGTH_LONG).show();
                }
                break;
            case "Resend":
                if (status.getInt(0)==3 | status.getInt(0)==8) {
                    status.close();
                    ResendRoute(view);
                } else {
                    status.close();
                    Toast.makeText(getBaseContext(), "Route must be closed to resend", Toast.LENGTH_LONG).show();
                }
                break;
            case "Summary":
                SummaryRoute(route_number,textView_Inst_Node.getText().toString(),textView_Mob_Node.getText().toString());
        }

        return true;
    }

    private void SummaryRoute(String route_number,String inst_node, String mob_node) {
        /*TextView textView_Inst_Node = view.findViewById(R.id.route_list_item_Inst_Node);
        TextView textView_Mob_Node = view.findViewById(R.id.route_list_item_Mob_Node);
        TextView textView_Cycle = view.findViewById(R.id.route_list_item_Cycle);
        TextView textView_Route_Number = view.findViewById(R.id.route_list_item_Route_Number);
        String route_number = (String) textView_Route_Number.getText();

        if (route_number.equals("-1"))
            finish();

        ArrayList<model_pro_mr_route_rows> route_rows;
        route_rows = DBHelper.pro_mr_route_rows.getRouteRows((String) textView_Inst_Node.getText(), (String) textView_Mob_Node.getText(), (String) textView_Cycle.getText(), (String) textView_Route_Number.getText());

        MeterReaderController.route_row_keys.clear();
        boolean addMeter;
        for (model_pro_mr_route_rows r : route_rows) {
            addMeter = false;
            if (r.getMeter_reading() == 0d)
                if (r.getNa_code() == null)
                    addMeter = true;
                else if (r.getNa_code().compareTo("") == 0)
                    addMeter = true;

            if (addMeter)
                MeterReaderController.route_row_keys.add(new MeterReaderController.Keys(
                        r.getCycle(), r.getInstNode_id(), r.getMeter_id(), r.getMobnode_id(), r.getRoute_number(), r.getWalk_sequence()));
        }

        MeterReaderController.notes.clear();
        MeterReaderController.notes = DBHelper.pro_mr_notes.getNotes();

        MeterReaderController.no_access.clear();
        MeterReaderController.no_access = DBHelper.pro_mr_no_access.getNoAccess();

        MeterReaderController.route_header = DBHelper.pro_mr_route_headers.getRouteHeader((String) textView_Inst_Node.getText(), (String) textView_Mob_Node.getText(), (String) textView_Cycle.getText(), (String) textView_Route_Number.getText());

        Intent closeRoute = new Intent(SelectRoute.this, CloseRouteActivity.class);
        startActivity(closeRoute);*/

        Intent intent = new Intent(SelectRoute.this, MeterSearchActivity.class);
        intent.putExtra("route_number",route_number);
        intent.putExtra("inst_node",inst_node);
        intent.putExtra("mob_node",mob_node);
        intent.putExtra("came_from_select_route",true);
        //String routenumber = input.getText().toString();
        //MeterReaderController.route_header.setRoute_number(routenumber);
        startActivity(intent);
    }

    private void ReadRoute(View view) {

        TextView textView_Inst_Node = view.findViewById(R.id.route_list_item_Inst_Node);
        TextView textView_Mob_Node = view.findViewById(R.id.route_list_item_Mob_Node);
        TextView textView_Cycle = view.findViewById(R.id.route_list_item_Cycle);
        TextView textView_Route_Number = view.findViewById(R.id.route_list_item_Route_Number);
        String route_number = (String) textView_Route_Number.getText();
        MeterReaderController.RouteNumber = route_number;
        MeterReadingActivity.InstNode = textView_Inst_Node.getText().toString();
        MeterReadingActivity.MobNode = textView_Mob_Node.getText().toString();
        MeterReadingActivity.Cycle = textView_Cycle.getText().toString();
        MeterReadingActivity.RouteNumber = route_number;

        if (route_number.equals("-1"))
            finish();

        ArrayList<model_pro_mr_route_rows> route_rows;
        route_rows = DBHelper.pro_mr_route_rows.getRouteRows((String) textView_Inst_Node.getText(), (String) textView_Mob_Node.getText(), (String) textView_Cycle.getText(), (String) textView_Route_Number.getText());

        MeterReaderController.route_row_keys.clear();
        for (model_pro_mr_route_rows r : route_rows) {
            MeterReaderController.route_row_keys.add(new MeterReaderController.Keys(
                            r.getCycle(), r.getInstNode_id(), r.getMeter_id(), r.getMobnode_id(), r.getRoute_number(), r.getWalk_sequence())
            );
        }

        MeterReaderController.notes.clear();
        MeterReaderController.notes = DBHelper.pro_mr_notes.getNotes();

        MeterReaderController.no_access.clear();
        MeterReaderController.no_access = DBHelper.pro_mr_no_access.getNoAccess();

        MeterReaderController.route_header = DBHelper.pro_mr_route_headers.getRouteHeader((String) textView_Inst_Node.getText(), (String) textView_Mob_Node.getText(), (String) textView_Cycle.getText(), (String) textView_Route_Number.getText());

        Intent meterReading = new Intent(SelectRoute.this, MeterReadingActivity.class);
        startActivity(meterReading);
    }

    private void ResendRoute(View view) {
        TextView textView_Inst_Node = view.findViewById(R.id.route_list_item_Inst_Node);
        TextView textView_Mob_Node = view.findViewById(R.id.route_list_item_Mob_Node);
        TextView textView_Cycle = view.findViewById(R.id.route_list_item_Cycle);
        TextView textView_Route_Number = view.findViewById(R.id.route_list_item_Route_Number);
        String route_number = (String) textView_Route_Number.getText();

        if (route_number.equals("-1"))
            finish();

        if (textView_Cycle.getText().equals("0"))
        {
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("UPDATE pro_mr_route_headers SET cycle = '1' WHERE route_number = "+route_number);
        } else
        {
            MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("UPDATE pro_mr_route_headers SET cycle = '0' WHERE route_number = "+route_number);
        }

        ArrayList<model_pro_mr_route_rows> route_rows;
        route_rows = DBHelper.pro_mr_route_rows.getRouteRows((String) textView_Inst_Node.getText(), (String) textView_Mob_Node.getText(), (String) textView_Cycle.getText(), (String) textView_Route_Number.getText());

        for (model_pro_mr_route_rows r : route_rows) {
            if (r.getStatus() ==1)
            {
                MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("UPDATE pro_mr_route_rows SET status = 0 WHERE route_number = "+r.getRoute_number()+" " +
                                                                            "and walk_sequence = "+r.getWalk_sequence()+" " +
                                                                            "and meter_id = "+r.getMeter_id());
            } else
            {
                MainActivity.sqliteDbHelper.getWritableDatabase().execSQL("UPDATE pro_mr_route_rows SET status = 1 WHERE route_number = "+r.getRoute_number()+" " +
                        "and walk_sequence = "+r.getWalk_sequence()+" " +
                        "and meter_id = "+r.getMeter_id());
            }
        }
    }

    private void CloseRoute(View view) {
        TextView textView_Inst_Node = view.findViewById(R.id.route_list_item_Inst_Node);
        TextView textView_Mob_Node = view.findViewById(R.id.route_list_item_Mob_Node);
        TextView textView_Cycle = view.findViewById(R.id.route_list_item_Cycle);
        TextView textView_Route_Number = view.findViewById(R.id.route_list_item_Route_Number);
        String route_number = (String) textView_Route_Number.getText();

        if (route_number.equals("-1"))
            finish();

        ArrayList<model_pro_mr_route_rows> route_rows;
        route_rows = DBHelper.pro_mr_route_rows.getRouteRows((String) textView_Inst_Node.getText(), (String) textView_Mob_Node.getText(), (String) textView_Cycle.getText(), (String) textView_Route_Number.getText());

        MeterReaderController.route_row_keys.clear();
        boolean addMeter;
        for (model_pro_mr_route_rows r : route_rows) {
            addMeter = false;
            if (r.getMeter_reading() == 0d)
                if (r.getNa_code() == null)
                    addMeter = true;
                else if (r.getNa_code().compareTo("") == 0)
                    addMeter = true;

            if (addMeter)
                MeterReaderController.route_row_keys.add(new MeterReaderController.Keys(
                        r.getCycle(), r.getInstNode_id(), r.getMeter_id(), r.getMobnode_id(), r.getRoute_number(), r.getWalk_sequence()));
        }

        MeterReaderController.notes.clear();
        MeterReaderController.notes = DBHelper.pro_mr_notes.getNotes();

        MeterReaderController.no_access.clear();
        MeterReaderController.no_access = DBHelper.pro_mr_no_access.getNoAccess();

        MeterReaderController.route_header = DBHelper.pro_mr_route_headers.getRouteHeader((String) textView_Inst_Node.getText(), (String) textView_Mob_Node.getText(), (String) textView_Cycle.getText(), (String) textView_Route_Number.getText());

        Intent closeRoute = new Intent(SelectRoute.this, CloseRouteActivity.class);
        startActivity(closeRoute);
    }

    public void onBackPressed() {

                Intent mainmenu = new Intent(SelectRoute.this, MainActivity.class);
                finish();
                startActivity(mainmenu);

    }

}
