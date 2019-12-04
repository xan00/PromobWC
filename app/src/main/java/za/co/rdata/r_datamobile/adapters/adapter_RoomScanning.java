package za.co.rdata.r_datamobile.adapters;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import za.co.rdata.r_datamobile.Models.model_pro_ar_asset_rows;
import za.co.rdata.r_datamobile.fragments.fragment_MakeAssetViewContent;

/**
 * Created by James de Scande on 27/10/2017 at 09:41.
 */

public class adapter_RoomScanning extends FragmentStatePagerAdapter {

    public int getItemPosition (@NonNull Object object) { return POSITION_NONE; }

    private ArrayList<fragment_MakeAssetViewContent> listFragments;

    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    public int getCount() {
        return listFragments.size();
    }

    public adapter_RoomScanning(FragmentManager fm, ArrayList<fragment_MakeAssetViewContent> listFragments) {
        super(fm);
        this.listFragments = listFragments;
    }

    public model_pro_ar_asset_rows getFragment(Integer index) {
        return listFragments.get(index).getAssetdata();
    }
}
