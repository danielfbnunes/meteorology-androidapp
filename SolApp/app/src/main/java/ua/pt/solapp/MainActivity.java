package ua.pt.solapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String city = "";

    public static Map<String, Double[]> markers;
    public static Map<Integer, String> weather_types;
    public static Map<Integer, String> wind_types;
    public static Map<String, Map<String, String>> averages;
    public static Boolean FIRST_FETCH = true;

    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.configureDagger();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        //showFragment(savedInstanceState);
    }


    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    // ---


    private void setupViewPager(ViewPager viewPager) {
        if ((getIntent().getExtras() == null)) {
            city = "Aveiro";
        } else {
            city = getIntent().getExtras().getString("city");
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        CityProfileFragment city_profile = new CityProfileFragment();
        Bundle b = new Bundle();
        b.putString("day", "0");
        b.putString("city", city);
        city_profile.setArguments(b);
        adapter.addFragment(city_profile, "TODAY");
        city_profile = new CityProfileFragment();
        b = new Bundle();
        b.putString("day", "1");
        b.putString("city", city);
        city_profile.setArguments(b);
        adapter.addFragment(city_profile, "TOMORROW");
        city_profile = new CityProfileFragment();
        b = new Bundle();
        b.putString("day", "2");
        b.putString("city", city);
        city_profile.setArguments(b);
        adapter.addFragment(city_profile, "AFTER TOMORROW");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    private void configureDagger(){
        AndroidInjection.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_search:
                startActivity(new Intent(this, Search.class));
                break;
            case R.id.action_map:
                startActivity(new Intent(this, MapsActivity.class));
                break;
        }
        return true;
    }
}