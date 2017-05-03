package an.it.disasterassistancesystem.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.applications.DASApplication;
import an.it.disasterassistancesystem.fragments.FindDriversFragment;
import an.it.disasterassistancesystem.fragments.FindEmployersFragment;
import an.it.disasterassistancesystem.objects.User;
import an.it.utils.Convert;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private enum TitleMenu {DRIVER_SEARCH, EMPLOYER_SEARCH}

    private TitleMenu mMenu = TitleMenu.DRIVER_SEARCH;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Menu mOptionsMenu;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValue();

    }

    private void initValue() {
        setSupportActionBar(mToolbar);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawer.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        DASApplication app = (DASApplication) getApplication();
        if (app != null && app.getUser() != null) {
            mUser = app.getUser();
            ImageView imv = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.imv_header_view);
            TextView tvName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_header_name);
            TextView tvType = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_header_driver_type);
            tvName.setText(mUser.getFullName());
            tvType.setText("Guest");
            ImageLoader.getInstance().displayImage(mUser.getAvatarURL(), imv, new DisplayImageOptions.Builder()
                    .resetViewBeforeLoading().cacheOnDisc().cacheInMemory()
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .showStubImage(R.drawable.ic_no_image)
                    .showImageOnFail(R.drawable.ic_no_image)
                    .showImageForEmptyUri(R.drawable.ic_no_image)
                    .displayer(new SimpleBitmapDisplayer()).build());
        }


    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
         /*
        button bottom
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setNavTabMenuSelecter() {
        switch (mMenu) {
            case DRIVER_SEARCH:
                mOptionsMenu.getItem(2).setIcon(Convert.getDrawable(this, R.drawable.ic_menu_search_normal));
                mOptionsMenu.getItem(0).setIcon(Convert.getDrawable(this, R.drawable.ic_menu_driver_pressed));
                mOptionsMenu.getItem(1).setIcon(Convert.getDrawable(this, R.drawable.ic_menu_employer_normal));
                mNavigationView.setCheckedItem(R.id.nav_find_drivers);
                loadFindDriversFragment();
                break;
            case EMPLOYER_SEARCH:
                mOptionsMenu.getItem(2).setIcon(Convert.getDrawable(this, R.drawable.ic_menu_search_normal));
                mOptionsMenu.getItem(0).setIcon(Convert.getDrawable(this, R.drawable.ic_menu_driver_normal));
                mOptionsMenu.getItem(1).setIcon(Convert.getDrawable(this, R.drawable.ic_menu_employer_pressed));
                mNavigationView.setCheckedItem(R.id.nav_find_employers);
                loadFindEmployersFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_title, menu);
        this.mOptionsMenu = menu;
        setNavTabMenuSelecter();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.title_find_drivers) {
            mMenu = TitleMenu.DRIVER_SEARCH;
            setNavTabMenuSelecter();
        } else if (id == R.id.title_find_employers) {
            mMenu = TitleMenu.EMPLOYER_SEARCH;
            setNavTabMenuSelecter();
        } else if (id == R.id.title_advanced_search_driver) {
            openAdvancedSearchDriver();
        } else if (id == R.id.title_advanced_search_employer) {
            openAdvancedSearchEmployer();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else */
        if (id == R.id.nav_find_drivers) {
            mMenu = TitleMenu.DRIVER_SEARCH;
            setNavTabMenuSelecter();
        } else if (id == R.id.nav_find_employers) {
            mMenu = TitleMenu.EMPLOYER_SEARCH;
            setNavTabMenuSelecter();
        } else if (id == R.id.nav_profile) {
            openProfile();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private FindDriversFragment loadFindDriversFragment() {
        FindDriversFragment fragment = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
        if (currentFragment instanceof FindDriversFragment) {
            fragment = (FindDriversFragment) currentFragment;
            fragment.loadData();
        } else {
            fragment = new FindDriversFragment();
            setNewPage(fragment);
        }
        return fragment;
    }

    private FindEmployersFragment loadFindEmployersFragment() {
        FindEmployersFragment fragment = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_main);
        if (currentFragment instanceof FindEmployersFragment) {
            fragment = (FindEmployersFragment) currentFragment;
            fragment.loadData();
        } else {
            fragment = new FindEmployersFragment();
            setNewPage(fragment);
        }
        return fragment;
    }

    private void setNewPage(Fragment fr) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, fr).commitAllowingStateLoss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void openAdvancedSearchDriver() {

        Intent i = new Intent(MainActivity.this, AdvancedSearchDriverActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }

    private void openAdvancedSearchEmployer() {
        Intent i = new Intent(MainActivity.this, AdvancedSearchEmployerActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }

    private void openProfile() {
        Intent i = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }
}
