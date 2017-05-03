package an.it.disasterassistancesystem.activities;

import android.support.v4.app.Fragment;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.fragments.AdvancedSearchDriverFragment;

/**
 * Created by anit on 11/6/16.
 */

public class AdvancedSearchDriverActivity extends SubFragmentActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initValue() {
        openAdvancedSearchDriverFragment();
    }
    private AdvancedSearchDriverFragment openAdvancedSearchDriverFragment() {
        AdvancedSearchDriverFragment fragment = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
        if (currentFragment instanceof AdvancedSearchDriverFragment) {
            fragment = (AdvancedSearchDriverFragment) currentFragment;
        } else {
            fragment = new AdvancedSearchDriverFragment();
            setNewPage(fragment);
        }
        return fragment;
    }
}
