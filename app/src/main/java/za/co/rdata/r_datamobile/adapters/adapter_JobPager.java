package za.co.rdata.r_datamobile.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import za.co.rdata.r_datamobile.Models.model_pro_fo_jobs;
import za.co.rdata.r_datamobile.fragments.fragment_jobMeter;

/**
 * Created by James de Scande on 18/02/2020 at 16:04.
 */

public class adapter_JobPager extends FragmentStatePagerAdapter {

    public int getItemPosition (@NonNull Object object) { return POSITION_NONE; }

    private ArrayList<fragment_jobMeter> listFragments;

    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    public int getCount() {
        return listFragments.size();
    }

    public adapter_JobPager(FragmentManager fm, ArrayList<fragment_jobMeter> listFragments) {
        super(fm);
        this.listFragments = listFragments;
    }

    //ublic model_pro_fo_jobs getFragment(Integer index) {
    //    return listFragments.get(index).get;
    //}
}