package za.co.rdata.r_datamobile;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageUploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        TreeNode root = TreeNode.root();

        TreeNode parent = new TreeNode("Images");
        root.addChild(parent);

        File dir = null;
        ArrayList<File> arrGallery = new ArrayList<>();

        try {
            dir = new File(Environment.getExternalStorageDirectory().toString() + "/filesync/Images/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        catch(Exception e){
            Log.w("creating file error", e.toString());
        }

        try {
            //noinspection ConstantConditions
            arrGallery = new ArrayList<>(Arrays.asList(dir.listFiles(pathname -> {
                Log.d("Picture Name", pathname.getName());
                return pathname.getName().toUpperCase().startsWith("")
                        & pathname.getName().toLowerCase().endsWith(".jpg");

            })));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        for (File f : arrGallery)
        parent.addChildren(new TreeNode(f.getName()));

        AndroidTreeView tView = new AndroidTreeView(this, root);
        tView.expandAll();

        LinearLayout linearLayout = findViewById(R.id.lin_image_browser);
        linearLayout.addView(tView.getView());


    }
}
