package tl.com.soilapp;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationFragment.OnFragmentInteractionListener,
        PhotoFragment.OnFragmentInteractionListener,RecordFragment.OnFragmentInteractionListener {

    private ViewPager mainActivityViewPager;
    private BottomNavigationView bottomNavView;
    MainActivityViewPagerAdapter adapter;
    private LocationFragment locationFragment;
    private PhotoFragment photoFragment;
    private RecordFragment recordFragment;

    private TextView mTextMessage;

    private boolean flag;
    private static Context context;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_location:
                    mainActivityViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_photo:
                    mainActivityViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_record:
                    mainActivityViewPager.setCurrentItem(2);
                    return true;
            }
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        initView();
        initListener();

        context = getApplicationContext();

    }

    private void initListener() {

        bottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //        为ViewPager设置Adapter
        adapter = new MainActivityViewPagerAdapter(getSupportFragmentManager());
//        为Adapter添加Fragment
        adapter.addFragment(new LocationFragment());
        adapter.addFragment(new PhotoFragment());
        adapter.addFragment(new RecordFragment());
        mainActivityViewPager.setAdapter(adapter);
        //        为 ViewPager 设置监听事件
        mainActivityViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当 ViewPager 滑动后设置BottomNavigationView 选中相应选项
                bottomNavView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPermission();//针对6.0以上版本做权限适配
    }

    private void initView() {

        //        获取到两个控件的对象
        mainActivityViewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavView = (BottomNavigationView) findViewById(R.id.bottom_nav);
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }
    }

    /**
     * 权限的结果回调函数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            flag = grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED;
        }
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
