package an.it.disasterassistancesystem.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.fragments.LoadingFragment;

/**
 * Created by anit on 11/6/16.
 */

public abstract class SubFragmentActivity extends AppCompatActivity {
    private Button mButtonLeft,mButonRight;
    private LinearLayout mButtonGroup;
    public void setTitle(String title){
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_profile));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_sub);
        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //home button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //bottombutton
        mButtonLeft=(Button)findViewById(R.id.btn_left);
        mButonRight=(Button)findViewById(R.id.btn_right);
        mButtonGroup=(LinearLayout)findViewById(R.id.ln);
        initView();
        initValue();

    }

    public Button getButtonLeft() {
        return mButtonLeft;
    }

    public Button getButonRight() {
        return mButonRight;
    }

    public LinearLayout getButtonGroup() {
        return mButtonGroup;
    }

    protected abstract void initView();
    protected abstract void initValue();
    protected void setNewPage(Fragment fr) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, fr).commitAllowingStateLoss();
    }
    protected LoadingFragment openLoadingFragment() {
        LoadingFragment fragment = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
        if (currentFragment instanceof LoadingFragment) {
            fragment = (LoadingFragment) currentFragment;
        } else {
            fragment = new LoadingFragment();
            setNewPage(fragment);
        }
        return fragment;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {}
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
