package si.uni_lj.fri.pbd.miniapp3.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.SectionsPageAdapter;

public class MainActivity extends AppCompatActivity {

    private int NUM_OF_TABS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        configureTabLayout();
    }

    private void configureTabLayout(){

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        SectionsPageAdapter tabPagerAdapter = new SectionsPageAdapter(this, NUM_OF_TABS);
        viewPager.setAdapter(tabPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 0)
                {
                    tab.setText("SEARCH RECIPE BY INGREDIENT");
                }
                if(position == 1)
                {
                    tab.setText("FAVORITE RECIPES");
                }
            }
        }).attach();

    }
}
