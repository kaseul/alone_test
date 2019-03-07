package staclogintest.kr.hs.mirim.alone_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import staclogintest.kr.hs.mirim.alone_test.model.Member;

public class CreateMemoActivity extends AppCompatActivity {

    EditText et_memo;
    Button btn_create, btn_cancel;
    TextView txt_memo_date;
    private DatabaseReference mFirebaseDatabaseReference;

    // 사용자 이름과 사진
    private String mUsername;
    private String mPhotoUrl;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);
        btn_cancel = findViewById(R.id.btn_cancel);
        Intent intent = getIntent();
        mUsername = intent.getStringExtra("mUsername");
        mPhotoUrl = intent.getStringExtra("mPhotoUrl");

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        et_memo = findViewById(R.id.et_memo);
        btn_create = findViewById(R.id.btn_create);
        txt_memo_date = findViewById(R.id.txt_memo_date);

        Log.d("testmemo", mUsername+"  "+mPhotoUrl);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member member = new Member(et_memo.getText().toString(),
                        mUsername,
                        mPhotoUrl,
                        null);
                member.setDate(new Date());

                mFirebaseDatabaseReference.child("messages")
                        .push()
                        .setValue(member);
                et_memo.setText("");

              //  Intent saveIntent = new Intent(CreateMemoActivity.this, CommunityActivity.class);
              //  startActivity(saveIntent);
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
