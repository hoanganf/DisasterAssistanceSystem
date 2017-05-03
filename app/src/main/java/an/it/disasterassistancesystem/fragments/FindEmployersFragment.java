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
import an.it.disasterassistancesystem.adapters.ListEmployerAdapter;
import an.it.disasterassistancesystem.objects.Employer;
import an.it.disasterassistancesystem.objects.Recruitment;
import an.it.views.ANoScrollListView;

public class FindEmployersFragment extends Fragment implements OnItemClickListener {
    private ANoScrollListView lstView;
    private ListEmployerAdapter adapter;

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
        ArrayList<Employer> users = new ArrayList<Employer>();
        ArrayList<Recruitment> ls = new ArrayList<Recruitment>();

        ls.add(new Recruitment(1,1,"A2", 5,3,"Theo km", 1000, "Dac biet co kha nang noi tieng anh") );
        ls.add(new Recruitment(1,1,"B2", 5,3,"Cuoc theo chuyen", 100000, "Dac biet co kha nang noi tieng anh") );
        Employer employer = new Employer("hoanganf", 1,"http://192.168.0.5/housesale/PropManager/4/pictures/IMG_20130223_150012.jpg", "CTCP TM HIEN LUONG", "2015", "Japan","hait.contact@gmail.com", "0973304329");
        employer.setRecruitments(ls);
        users.add(employer );
        users.add(employer );
        users.add(employer );
        users.add(employer );
        users.add(employer );
        users.add(employer );
        users.add(employer );

        adapter = new ListEmployerAdapter(getContext(), users);
        lstView.setAdapter(adapter);
        lstView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
        Toast.makeText(getContext(),pos+"",Toast.LENGTH_SHORT).show();
    }

}