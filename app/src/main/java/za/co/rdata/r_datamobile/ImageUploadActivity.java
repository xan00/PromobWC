package za.co.rdata.r_datamobile;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import za.co.rdata.r_datamobile.adapters.adapter_ExpandableList;

/**
 * Created by James de Scande on 02/07/2018 at 14:19.
 */

public class ImageUploadActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        // get the listview
        expListView = findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new adapter_ExpandableList(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);
        // Listview Group click listener
        expListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            expListView.expandGroup(groupPosition);
            Toast.makeText(getApplicationContext(),
            "Group Clicked " + listDataHeader.get(groupPosition),
            Toast.LENGTH_SHORT).show();
            return false;
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(groupPosition -> Toast.makeText(getApplicationContext(),
                listDataHeader.get(groupPosition) + " Expanded",
                Toast.LENGTH_SHORT).show());

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(groupPosition -> Toast.makeText(getApplicationContext(),
                listDataHeader.get(groupPosition) + " Collapsed",
                Toast.LENGTH_SHORT).show());

        // Listview on child click listener
        expListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            // TODO Auto-generated method stub
            Toast.makeText(
                    getApplicationContext(),
                    listDataHeader.get(groupPosition)
                            + " : "
                            + listDataChild.get(
                            listDataHeader.get(groupPosition)).get(
                            childPosition), Toast.LENGTH_SHORT)
                    .show();
            return false;
        });
    }

    /*
     * Preparing the list data
     */

    private File makedir(String filepath) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory().toString() + filepath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        }
        catch(Exception e){
            Log.w("creating file error", e.toString());
        }
        return null;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        File dir;

        try {
            dir = new File(Environment.getExternalStorageDirectory().toString() + "/filesync/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        catch(Exception e){
            Log.w("creating file error", e.toString());
        }


        listDataHeader.add("Documents");
        getFileArray(makedir("/filesync/Documents/"),0);

        listDataHeader.add("Images");
        getFileArray(makedir("/filesync/Images/"),1);

    }

    private void getFileArray(File dir, int i) {
        ArrayList<File> arrFileList = new ArrayList<>();

        try {
            //noinspection ConstantConditions
            arrFileList = new ArrayList<>(Arrays.asList(dir.listFiles(pathname -> {
                Log.d(dir.getName() + " Name", pathname.getName());
                return pathname.getName().toUpperCase().startsWith("");

            })));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }


        ArrayList<String> filenames = new ArrayList<>();
        for (File f : arrFileList)
            filenames.add(f.getName());

        listDataChild.put(listDataHeader.get(i), filenames);
    }

}
