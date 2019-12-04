package za.co.rdata.r_datamobile.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import za.co.rdata.r_datamobile.fragments.fragment_meterReading;
import za.co.rdata.r_datamobile.meterReadingModule.MeterReaderController;

/**
 * Project: R-DataMobile
 * Created by wcrous on 11/12/2015.
 */
public class adapter_MeterReading extends FragmentStatePagerAdapter {

    private List<Fragment> listFragments;
//    private static

    public adapter_MeterReading(FragmentManager fm, List<Fragment> listFragments) {
        super(fm);
        this.listFragments = listFragments;
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    public MeterReaderController.Keys getFragment(Integer index) {
        try {
            return ((fragment_meterReading) listFragments.get(index)).getRid();
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public Integer getKeyPosition(MeterReaderController.Keys key) {
        Integer position = 0;
        for (Integer x = 0; x < listFragments.size(); x++) {
            if (MeterReaderController.Keys.Equals(((fragment_meterReading) listFragments.get(x)).getRid(), key))
                position = x;
        }
        return position;
    }
}
