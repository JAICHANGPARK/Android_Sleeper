package com.dreamwalker.sleeper.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dreamwalker.sleeper.Adapter.SectionsPagerAdapter;
import com.dreamwalker.sleeper.FragmentMain.HRFragment;
import com.dreamwalker.sleeper.FragmentMain.SoundFragment;
import com.dreamwalker.sleeper.FragmentMain.Spo2Fragment;
import com.dreamwalker.sleeper.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class SleepMainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // TODO: 2017-11-16  Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        // TODO: 2017-11-16 성능향상을 위한 방법. 페이지 수가 3개 이므로 2로 설정하면 3개의 페이지를 미리 로딩한다.
        mViewPager.setOffscreenPageLimit(2);

        //mViewPager.setAdapter(mSectionsPagerAdapter);
        // TODO: 2017-11-16 Tablayout을 viewPager와 연동시킨다. 
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // TODO: 2017-11-16 페이지의 특정 페이지로 이동시키기 위한 처리.
        // 특정 탭을 열기위해서
        int defaultValue = 0;
        int page = getIntent().getIntExtra("One", defaultValue);
        // TODO: 2017-10-27 특정 탭으로 이동 시켜주는 함수
        mViewPager.setCurrentItem(page);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HRFragment(), "심박수");
        adapter.addFragment(new Spo2Fragment(), "산소포화도");
        adapter.addFragment(new SoundFragment(), "코골이");
        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sleep_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(SleepMainActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_monitor) {
            Intent intent = new Intent(SleepMainActivity.this, MonitoringActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_search) {
            Calendar now = Calendar.getInstance();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    SleepMainActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setTitle("Select date :)");
            datePickerDialog.show(getFragmentManager(), "DatePicker");
            datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
        //Toast.makeText(this, "year" + i + "month" + (i1 + 1) + "day" + "i2", Toast.LENGTH_SHORT).show();

        int selectYear = i;
        int selectMonth = (i1 + 1);
        int selectDayofWeek = (i2);

        String year = String.valueOf(selectYear);
        year = year.substring(2);
        String month = String.valueOf(selectMonth);
        String dayOfMonth = String.valueOf(selectDayofWeek);

        Toast.makeText(this, year + "-" + month + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
    }
}
