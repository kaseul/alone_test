package staclogintest.kr.hs.mirim.alone_test;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;

import staclogintest.kr.hs.mirim.alone_test.var.varStructure;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.getTabWidget().setDividerDrawable(null);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("",getResources().getDrawable(R.drawable.tab01)).setContent(new Intent(this, HomeActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("",getResources().getDrawable(R.drawable.tab02)).setContent(new Intent(this, PlaylistActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("",getResources().getDrawable(R.drawable.tab03)).setContent(new Intent(this, LibraryActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("",getResources().getDrawable(R.drawable.tab04)).setContent(new Intent(this, CommunityActivity.class)));

    }

    
}
