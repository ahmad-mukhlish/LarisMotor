package com.yayanheryanto.larismotor.view.owner;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.orhanobut.hawk.Hawk;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.fragment.MerkFragment;
import com.yayanheryanto.larismotor.fragment.TipeFragment;
import com.yayanheryanto.larismotor.interfaces.MerkToTipeInterface;

import java.util.ArrayList;
import java.util.List;

import static com.yayanheryanto.larismotor.config.config.DEBUG;

public class MasterActivity extends AppCompatActivity implements MerkToTipeInterface {

    public static ViewPager view_pager;
    private TabLayout tab_layout;
    private int currentPage = 0;
    private SearchView searchView;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        initComponent();
        getSupportActionBar().setElevation(0);
        setTitle("Master Menu");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_master, menu);
        menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tambah:

                if (currentPage==0){
                    Intent intent = new Intent(MasterActivity.this, AddMerkActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MasterActivity.this, AddTipeActivity.class);
                    startActivity(intent);
                }
                return true;

            case R.id.action_search:
                Intent intent1 = new Intent(MasterActivity.this, CariTipeActivity.class);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initComponent() {
        view_pager = findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MerkFragment(), "Merk");
        adapter.addFragment(new TipeFragment(), "Tipe");
        viewPager.setAdapter(adapter);



        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(DEBUG, "page selected " + position);
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void moveToTipe(int halaman) {
        view_pager.setCurrentItem(halaman);
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
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

    @Override
    protected void onPause() {
        super.onPause();
        searchView.setIconifiedByDefault(true);
        menuItem.collapseActionView();
        searchView.onActionViewExpanded();
    }
}
