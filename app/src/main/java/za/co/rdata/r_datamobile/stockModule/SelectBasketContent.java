package za.co.rdata.r_datamobile.stockModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.DBMeta.intentcodes;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_headers;
import za.co.rdata.r_datamobile.R;
import za.co.rdata.r_datamobile.adapters.adapter_AssetRecycler;
import za.co.rdata.r_datamobile.assetModule.RoomMainSummary;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by James de Scande on 20/09/2018 at 13:00.
 */
public class SelectBasketContent extends AppCompatActivity {

    static String strSelectedRoom;
    static String strCurrentRoom;
    ArrayList<model_pro_ar_asset_headers> listArray = new ArrayList<>();
    String temp;
    int lightcolour;
    int v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent iPoproom = getIntent();
        Bundle bSaved = iPoproom.getExtras();
        setContentView(R.layout.activity_basket_comp_list);
        strSelectedRoom = bSaved.getString("ROOM SCAN");
        strCurrentRoom = bSaved.getString(intentcodes.asset_activity.current_room);
        listArray = bSaved.getParcelableArrayList("Array Content");
        temp = bSaved.getString("Toast String");
        lightcolour = bSaved.getInt("LIGHT COLOUR");
        v = bSaved.getInt("Summary Choice");
        goToExpandedList(strSelectedRoom);
    }

    @Override
    protected void onResume() {
        super.onResume();
        goToExpandedList(strSelectedRoom);
    }

    public void goToExpandedList(String strRoom) {       ///////Opens Asset To View From Summary List For Current Room

        TextView list_header = findViewById(R.id.textViewHeader);
        list_header.setText(String.format("ASSET SUMMARY: %s", strRoom));

        RecyclerView listAssetView = findViewById(R.id.activity_asset_list_listView);
        adapter_AssetRecycler adapter_assetRecycler = new adapter_AssetRecycler(listArray,R.layout.select_component_item);
        adapter_assetRecycler.setLightcolour(lightcolour);
        adapter_assetRecycler.setmContext(this);
        adapter_assetRecycler.setCurrentRoom(strRoom);
        adapter_assetRecycler.setRetbarcode(R.id.Asset_reg_barcode);
        adapter_assetRecycler.setRetdesc(R.id.Asset_reg_asset_desc);
        adapter_assetRecycler.setRetroom(R.id.txtRoomID);
        adapter_assetRecycler.setRetframe(R.id.select_asset_view_layout);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.recycler_divider));
        listAssetView.addItemDecoration(itemDecoration);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listAssetView.setLayoutManager(layoutManager);
        listAssetView.setItemAnimator(new DefaultItemAnimator());
        listAssetView.setAdapter(adapter_assetRecycler);

        adapter_assetRecycler.notifyDataSetChanged();
        try {
            Toast.makeText(getBaseContext(), "There Are " + listArray.size() + temp, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getBaseContext(), "There Are " + "0" + temp, Toast.LENGTH_SHORT).show();
        }

        Button scannewroom = findViewById(R.id.btnScanNewBasketfromList);
        scannewroom.setOnClickListener(listenernewBasket);

        Button scannewasset = findViewById(R.id.btnScanNewPartfromList);
        scannewasset.setOnClickListener(listenernewPart);
        }

        public void gotonewaasetscanning() {
            Intent gotorooms = new Intent(SelectBasketContent.this, RoomMainSummary.class);                    ///////Starts Instance of New Asset Scan In Current Room
            gotorooms.putExtra("came_from_expanded",TRUE);
            gotorooms.putExtra("roomorasset",FALSE);
            gotorooms.putExtra("ROOM SCAN", strSelectedRoom);
            if (strCurrentRoom==null) {
                strCurrentRoom = strSelectedRoom;
            }
            gotorooms.putExtra(intentcodes.asset_activity.current_room, strCurrentRoom);
            //gotorooms.putExtra("Count Handler",counthandler);
            gotorooms.putExtra("Toast String",temp);
            gotorooms.putExtra(intentcodes.asset_activity.light_colour,lightcolour);
            gotorooms.putParcelableArrayListExtra("Array Content",listArray);
            gotorooms.putExtra("Summary Choice",v);

            startActivity(gotorooms);
        }

        public void gotonewroomscanning() {
            Intent gotorooms = new Intent(SelectBasketContent.this,RoomMainSummary.class);                    ///////Starts Instance of New Asset Scan In Current Room
            gotorooms.putExtra("came_from_expanded",TRUE);
            gotorooms.putExtra("roomorasset",TRUE);
            gotorooms.putExtra("ROOM SCAN",strSelectedRoom);
            gotorooms.putExtra("Toast String",temp);
            gotorooms.putExtra(intentcodes.asset_activity.light_colour,lightcolour);
            gotorooms.putParcelableArrayListExtra("Array Content",listArray);
            gotorooms.putExtra(intentcodes.asset_activity.current_room, strCurrentRoom);
            gotorooms.putExtra("Summary Choice",v);

            startActivity(gotorooms);
    }

        public View.OnClickListener listenernewPart = v -> {
        ///////Starts Instance of New Asset Scan In Current Room
            gotonewaasetscanning();
        };

        public View.OnClickListener listenernewBasket = v -> {
        ///////Starts Instance of New Asset Scan In Current Room
            gotonewroomscanning();
        };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent gotomainsummary = new Intent(SelectBasketContent.this,SelectBasket.class);
        startActivity(gotomainsummary);
    }
}
