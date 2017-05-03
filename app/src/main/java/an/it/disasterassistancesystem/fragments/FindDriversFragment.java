package an.it.disasterassistancesystem.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.adapters.ListDriverAdapter;
import an.it.disasterassistancesystem.objects.Driver;
import an.it.disasterassistancesystem.objects.License;
import an.it.views.ANoScrollListView;

public class FindDriversFragment extends Fragment implements OnItemClickListener {
    private ANoScrollListView lstView;
    private ListDriverAdapter adapter;

    //private EditUserDialog dialg;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        lstView = (ANoScrollListView) v.findViewById(R.id.lstv);
        lstView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    public void loadData() {
        ArrayList<Driver> users = new ArrayList<Driver>();
        ArrayList<License> ls = new ArrayList<License>();

        ls.add(new License(1, "http://192.168.0.5/housesale/PropManager/4/pictures/IMG_20130223_151640.jpg",  "A2", "Chao"));
        ls.add(new License(2, "http://192.168.0.5/housesale/PropManager/4/pictures/IMG_20130223_151640.jpg",  "B2", "Re nhat"));
        Driver driver=new Driver("hoanganf", 1, "http://192.168.0/housesale/PropManager/4/pictures/IMG_20130223_150012.jpg", "An Hoang", "http://192.168.0.5/housesale/PropManager/4/pictures/IMG_20130223_150012.jpg", "ABC12345", "hoanganf@gmail.com", "07013194329", 0, 0, 0, "02/11/1990", "Japan");
        driver.setLicenses(ls);
        users.add(driver);
        users.add(driver);
        users.add(driver);
        users.add(driver);
        users.add(driver);
        users.add(driver);
        users.add(driver);
        users.add(driver);




        adapter = new ListDriverAdapter(getContext(), users);
        lstView.setAdapter(adapter);
        lstView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
        Toast.makeText(getContext(),pos+"",Toast.LENGTH_SHORT).show();
    }

}