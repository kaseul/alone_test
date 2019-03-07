package staclogintest.kr.hs.mirim.alone_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;

import staclogintest.kr.hs.mirim.alone_test.adapter.ListAdapter;
import staclogintest.kr.hs.mirim.alone_test.model.Member;

import static staclogintest.kr.hs.mirim.alone_test.SignInActivity.var;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    ImageView userImg;

    ArrayList<Member> items = new ArrayList<>();//멤버
    private GoogleApiClient mGoogleApiClient;

    private ListView listView;
    private ListAdapter adapter;

    private String mUsername;
    private String mPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userImg = findViewById(R.id.userImg);
        TextView nickname = findViewById(R.id.nickname);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        Log.d("아이디",mFirebaseUser.getDisplayName());

        if (mFirebaseUser.getPhotoUrl() != null) {
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
        }//else 안에 if

        nickname.setText(mFirebaseUser.getDisplayName());
        Glide.with(getApplicationContext())
                .load(mPhotoUrl)
                .into(userImg);

        var.UserName = mFirebaseUser.getDisplayName();
        var.PhotoUrl = mFirebaseUser.getPhotoUrl().toString();

        TabHost tabhost = (TabHost)findViewById(R.id.tabhost);
        tabhost.setup();

        TabHost.TabSpec ts1 = tabhost.newTabSpec("Tab1");
        ts1.setIndicator("알람");
        ts1.setContent(R.id.tab_view1);
        tabhost.addTab(ts1);

        TabHost.TabSpec ts2 = tabhost.newTabSpec("Tab2");
        ts2.setIndicator("내 글");
        ts2.setContent(R.id.tab_view2);
        tabhost.addTab(ts2);

        TabHost.TabSpec ts3 = tabhost.newTabSpec("Tab3");
        ts3.setIndicator("소리함");
        ts3.setContent(R.id.tab_view3);
        tabhost.addTab(ts3);


//        btn_input.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("mUsername", mUsername);
//                intent.putExtra("mPhotoUrl", mPhotoUrl);
//
//                startActivity(intent);
//            }//onClick
//        });//btn_input.setOnClickListener
    }
}
