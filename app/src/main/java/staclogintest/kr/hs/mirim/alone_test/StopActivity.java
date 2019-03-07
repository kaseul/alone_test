package staclogintest.kr.hs.mirim.alone_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import staclogintest.kr.hs.mirim.alone_test.service.AudioService;

import static staclogintest.kr.hs.mirim.alone_test.adapter.PlayListContentRecyclerAdapter.c;

public class StopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);

        Intent intent = new Intent(c, AudioService.class);
        stopService(intent);

        finish();
    }
}
