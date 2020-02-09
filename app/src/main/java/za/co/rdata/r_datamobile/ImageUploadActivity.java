package za.co.rdata.r_datamobile;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import za.co.rdata.r_datamobile.fileTools.IconTreeItemHolder;

import static com.unnamed.b.atv.model.TreeNode.*;

/**
 * Created by James de Scande on 02/07/2018 at 14:19.
 */

public class ImageUploadActivity extends AppCompatActivity {

    private TreeNode root = root();
    private AndroidTreeView treeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        ConstraintLayout managerView;
        managerView = (ConstraintLayout) this.getLayoutInflater().inflate(R.layout.activity_image_upload, null);

        Context context = this;
        treeView = new AndroidTreeView(this, root);
        treeView.setDefaultAnimation(true);
        treeView.setDefaultContainerStyle(0);
        treeView.setUse2dScroll(true);
        treeView.setSelectionModeEnabled(true);
        treeView.setDefaultViewHolder(IconTreeItemHolder.class);

        TreeNode images = new TreeNode("Images");

        root.addChild(images);

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
            images.addChildren(new TreeNode(f.getName()));

        LinearLayout contentView = managerView.findViewById(R.id.lin_image_browser);
        //contentView.removeView(tView.getView());
        int i = contentView.getChildCount();

        try {
            contentView.addView(treeView.getView());
            treeView.expandAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class TreeViewHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {

        Context mContext;
        //String mTree;

        TreeViewHolder(Context context) {
            super(context);
        }


        @Override
        public View createNodeView(TreeNode node, IconTreeItemHolder.IconTreeItem value) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            final View view = inflater.inflate(R.layout.treeviewnode, null, false);
            TextView txtFileName = view.findViewById(R.id.txtTreeFileName);
            txtFileName.setText(node.getValue().toString());
            return view;
        }
    }

}
