package za.co.rdata.r_datamobile.fileTools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;

import za.co.rdata.r_datamobile.R;

public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    private TextView tvValue;

    public IconTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.treeviewnode, null, false);
        tvValue = view.findViewById(R.id.txtTreeFileName);
        tvValue.setText(value.text);

        //final ImageView iconView = (ImageView) view.findViewById(R.id.icon);
        //iconView.setImageIcon(context.getResources().getString(value.icon));


        /*view.findViewById(R.id.btn_addFolder).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        TreeNode newFolder = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "New Folder"));
        getTreeView().addNode(node, newFolder);
        }
        });*/

        /*view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        getTreeView().removeNode(node);
        }
        });

        //if My computer
        if (node.getLevel() == 1) {
        view.findViewById(R.id.btn_delete).setVisibility(View.GONE);
        }*/

        return view;
    }

        /*@Override
        public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
        }*/

    public static class IconTreeItem {
        public int icon;
        public String text;

        public IconTreeItem(int icon, String text) {
            this.icon = icon;
            this.text = text;
        }
    }
}




