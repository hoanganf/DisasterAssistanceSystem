package an.it.disasterassistancesystem.activities;

import android.support.v4.app.Fragment;
import android.view.View;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.fragments.MyProfileFragment;

/**
 * Created by anit on 12/10/16.
 */

public class ProfileActivity extends SubFragmentActivity {
    @Override
    protected void initView() {

    }

    @Override
    protected void initValue() {
        openMyProfileFragment();
        getButtonGroup().setVisibility(View.GONE);
    }
    private MyProfileFragment openMyProfileFragment() {
        MyProfileFragment fragment = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
        if (currentFragment instanceof MyProfileFragment) {
            fragment = (MyProfileFragment) currentFragment;
        } else {
            fragment = new MyProfileFragment();
            setNewPage(fragment);
        }
        return fragment;
    }
}
