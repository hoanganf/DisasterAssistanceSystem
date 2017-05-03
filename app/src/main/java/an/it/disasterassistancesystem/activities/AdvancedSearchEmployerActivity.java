package an.it.disasterassistancesystem.activities;

import android.support.v4.app.Fragment;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.fragments.AdvancedSearchEmployerFragment;

/**
 * Created by anit on 11/6/16.
 */

public class AdvancedSearchEmployerActivity extends SubFragmentActivity {
    @Override
    protected void initView() {

    }

    @Override
    protected void initValue() {
        openAdvancedSearchEmployerFragment();
    }
    private AdvancedSearchEmployerFragment openAdvancedSearchEmployerFragment() {
        AdvancedSearchEmployerFragment fragment = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
        if (currentFragment instanceof AdvancedSearchEmployerFragment) {
            fragment = (AdvancedSearchEmployerFragment) currentFragment;
        } else {
            fragment = new AdvancedSearchEmployerFragment();
            setNewPage(fragment);
        }
        return fragment;
    }
}
