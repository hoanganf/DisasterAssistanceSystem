package an.it.disasterassistancesystem.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.views.ACheckBoxGroup;
import an.it.disasterassistancesystem.views.ATextBoxGroup;

/**
 * Created by anit on 11/6/16.
 */

public class RecruiterRegistrationActivity extends AppCompatActivity {
    private ATextBoxGroup mATextBoxGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_advanced_search_drivers);
        initView();
       // initValue();
    }
    private void initView(){
        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.title_advanced_search_driver));
        }
        //

    }
    private void initValue(){
        ArrayList<String> titles = new ArrayList<String>();
        titles.add("A1");
        titles.add("A2");
        titles.add("A3");
        titles.add("B1");
        titles.add("B2");

        ((ACheckBoxGroup) findViewById(R.id.acbgr_license)).setCheckList(titles);
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

        ((ACheckBoxGroup) findViewById(R.id.acbgr_vehicles)).setCheckList(titles1);
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
