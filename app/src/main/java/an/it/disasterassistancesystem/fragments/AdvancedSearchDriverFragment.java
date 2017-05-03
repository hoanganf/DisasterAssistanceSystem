package an.it.disasterassistancesystem.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.views.ACheckBoxGroup;

public class AdvancedSearchDriverFragment extends Fragment {

    //private EditUserDialog dialg;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_advanced_search_drivers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // do some thing
        ArrayList<String> titles = new ArrayList<String>();
        titles.add("A1");
        titles.add("A2");
        titles.add("A3");
        titles.add("B1");
        titles.add("B2");

        ((ACheckBoxGroup) view.findViewById(R.id.acbgr_license)).setCheckList(titles);
        ArrayList<String> titles1 = new ArrayList<String>();
        titles1.add("Xe ");
        titles1.add("Xe ô ");
        titles1.add("");
        titles1.add("B1");
        titles1.add("B2");
        titles1.add("B2");
        titles1.add("B2");
        titles1.add("B2");
        titles1.add("B2");
        titles1.add("B2");
        titles1.add("B2");
        titles1.add("B2");
        titles1.add("B2");titles1.add("B2");titles1.add("B2");titles1.add("B2");titles1.add("B5");

        ((ACheckBoxGroup) view.findViewById(R.id.acbgr_vehicles)).setCheckList(titles1);
    }
}