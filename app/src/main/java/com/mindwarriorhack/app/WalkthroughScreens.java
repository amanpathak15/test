package com.mindwarriorhack.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.view.Home.HomeActivity;
import com.mindwarriorhack.app.view.SignIn.SignIn;
import com.mindwarriorhack.app.view.SignUp.SignUp;
import com.mindwarriorhack.app.view.fragments.Walkthrough.Walkthrough1;
import com.mindwarriorhack.app.view.fragments.Walkthrough.Walkthrough2;
import com.mindwarriorhack.app.view.fragments.Walkthrough.Walkthrough3;
import com.rd.PageIndicatorView;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WalkthroughScreens extends AppCompatActivity
{
    private ViewPager viewPager;
    private PageIndicatorView springDotsIndicator;
    private TextView content_textView,skip_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrough_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initUI();
    }

    private void initUI(){
        viewPager=findViewById(R.id.viewPager);
        springDotsIndicator=findViewById(R.id.spring_dot_indicator);
        content_textView=findViewById(R.id.content_text_walkthrough);
        skip_text=findViewById(R.id.skip_text);
        skip_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setString(Constant.IS_APP_FIRSTTIME_TAG,"false");
                startActivity(new Intent(WalkthroughScreens.this, SignIn.class));
                finish();
            }
        });
        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(viewPager.getCurrentItem()==0){
                    content_textView.setText("Future-proof your \n employability skills one \n thought at a time");
                }else if(viewPager.getCurrentItem()==1){
                    content_textView.setText("Upgrade your mind in \n just a minute a day with \n daily mind hacks");
                }else if(viewPager.getCurrentItem()==2){
                    content_textView.setText("Share your thoughts & \n feelings with our \n community");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Walkthrough1(), Constant.WALKTHROUGH1_TAG);
        adapter.addFragment(new Walkthrough2(),Constant.WALKTHROUGH2_TAG);
        adapter.addFragment(new Walkthrough3(),Constant.WALKTHROUGH3_TAG);
        viewPager.setAdapter(adapter);
        springDotsIndicator.setViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> fragments=new ArrayList<>();
        private final List<String> fragmentTitle =new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

}
