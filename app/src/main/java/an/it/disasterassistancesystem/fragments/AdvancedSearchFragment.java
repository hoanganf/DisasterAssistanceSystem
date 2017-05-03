package an.it.disasterassistancesystem.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.adapters.AnITVPAdapter;
import an.it.disasterassistancesystem.objects.ViewPagerViewer;

/**
 * Created by hoang on 10/29/2016.
 */
public class AdvancedSearchFragment extends Fragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_advanced_search, container, false);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        mViewPager = (ViewPager) rootView.findViewById(R.id.vp_content);
        setupViewPager();
        setupTabIcons();
        return rootView;
    }

    private void setupViewPager() {
        ArrayList<ViewPagerViewer> lst = new ArrayList<ViewPagerViewer>();

        lst.add(new ViewPagerViewer() {
            @Override
            public View getView() {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.fragment_advanced_search_drivers, null);


                return v;

            }

            @Override
            public String getViewTitle() {
                return "Find Driver";
            }
        });
        lst.add(new ViewPagerViewer() {
          //  private ATextBoxGroup mLiencesTBG;

            @Override
            public View getView() {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.fragment_advanced_search_employers, null);
           //  //   mLiencesTBG = (ATextBoxGroup) v.findViewById(R.id.atbgr_license);
          /*      ((LinearLayout) v.findViewById(R.id.ln_liences)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ArrayList<String> titles = new ArrayList<String>();
                        titles.add("A1");
                        titles.add("A2");
                        titles.add("A3");
                        titles.add("B1");
                        titles.add("B2");

                        final CheckBoxDialog dialog = new CheckBoxDialog(getContext());
                        dialog.setLstCheck(titles);
                        dialog.setTitle(getResources().getString(R.string.title_licenses));
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {

                                mLiencesTBG.setTextBoxList(dialog.getCheckedList());
                            }
                        });
                    }
                });*/
                return v;
            }

            @Override
            public String getViewTitle() {
                return "Find Employer";
            }
        });
        AnITVPAdapter adapter = new AnITVPAdapter(lst);
        mViewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        mTabLayout.setupWithViewPager(mViewPager);

    }

}