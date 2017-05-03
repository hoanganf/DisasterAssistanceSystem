package an.it.disasterassistancesystem.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import an.it.disasterassistancesystem.objects.ViewPagerViewer;

/**
 * Created by hoang on 10/29/2016.
 */

public class AnITVPAdapter extends PagerAdapter {
    private ArrayList<ViewPagerViewer> lstPager;

    public AnITVPAdapter(ArrayList<ViewPagerViewer> lstPager) {
        this.lstPager = lstPager;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (lstPager == null || lstPager.size() == 0) return "";
        else return lstPager.get(position).getViewTitle();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        if (lstPager == null) return 0;
        else return lstPager.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = lstPager.get(position).getView();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }
}
