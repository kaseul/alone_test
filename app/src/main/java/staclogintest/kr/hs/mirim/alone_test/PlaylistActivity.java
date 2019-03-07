package staclogintest.kr.hs.mirim.alone_test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import staclogintest.kr.hs.mirim.alone_test.adapter.PlayListRecyclerAdapter;
import staclogintest.kr.hs.mirim.alone_test.model.Playlist;

public class PlaylistActivity extends AppCompatActivity {
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    String PlaylistName;

    private RecyclerView playListRecyclerView;
    private PlayListRecyclerAdapter adapter;
    private ArrayList<String> PlaylistNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playListRecyclerView = findViewById(R.id.playlist_recyclerview);

        playListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PlayListRecyclerAdapter(this);

        playListRecyclerView.setAdapter(adapter);

        user = FirebaseAuth.getInstance().getCurrentUser();

        final LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

        Button btnNewPlaylist = findViewById(R.id.btn_new_playlist);

        Map<String, String> userUid = new HashMap<>();
        userUid.put("uid",user.getUid());

        db.collection("Users").document(user.getUid())
                .set(userUid)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("유저 추가", "DocumentSnapshot successfully written!");
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("유저 추가", "Error writing document", e);
                    }
                });

        db.collection("Users").document(user.getUid()).collection("Playlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("데이터 읽기", document.getId() + " => " + document.getData());

                                Playlist pl = new Playlist();
                                pl.name = (String)document.get("name");
                                pl.count = (int)(long)document.get("count");
                                adapter.addPlayList(pl);
                                PlaylistNames.add(pl.name);
                            }
                        } else {
                            Log.w("데이터 읽기", "Error getting documents.", task.getException());
                        }
                    }
                });

        final boolean[] isExist = {false};
        btnNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout dialogLayout = new LinearLayout(PlaylistActivity.this);
                dialogLayout.setOrientation(LinearLayout.VERTICAL);

                final EditText name = new EditText(PlaylistActivity.this);//name:사용자가 입력하는 창
                final TextView check = new TextView(PlaylistActivity.this);
                check.setPadding(10, 0,0,0);
                dialogLayout.addView(name);
                dialogLayout.addView(check);
                check.setText("이름을 입력해주세요");

                name.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if(keyCode == event.KEYCODE_ENTER) return true;
                        return false;
                    }
                });

                name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.toString().equals("")) {
                            check.setText("이름을 입력해주세요");
                            isExist[0] = true;
                        }
                        else {
                            check.setText(s.toString() + "은/는 사용 가능한 이름입니다.");
                            isExist[0] = false;
                            for (String str : PlaylistNames) {
                                if (str.equals(s.toString())) {
                                    check.setText(s.toString() + "은/는 이미 존재합니다. 다른 이름을 입력해주세요");
                                    isExist[0] = true;
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                name.setHint("플레이리스트 이름");//입력 창 예시

                AlertDialog.Builder alert = new AlertDialog.Builder(PlaylistActivity.this);//AlertDialog생성
                alert.setTitle("< 플레이리스트");//제목생성
                alert.setView(dialogLayout);//alert에 name등록=> 등록해야 화면에 보임

                alert.setPositiveButton("만들기", new DialogInterface.OnClickListener() {//저장하기 버튼
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                final AlertDialog dialog = alert.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface d) {
                        Button btnOK = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!isExist[0]) {
                                    PlaylistName = name.getText().toString();
                                    PlaylistNames.add(PlaylistName);
                                    Collections.sort(PlaylistNames, new Comparator<String>() {
                                        @Override
                                        public int compare(String o1, String o2) {
                                            return o1.compareTo(o2);
                                        }
                                    });

                                    Playlist pl = new Playlist();
                                    pl.name = PlaylistName;
                                    pl.count = 0;
                                    adapter.addPlayList(pl);
                                    db.collection("Users").document(user.getUid())
                                            .collection("Playlist")
                                            .document(PlaylistName)
                                            .set(pl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("플레이리스트", "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("플레이리스트", "Error writing document", e);
                                                }
                                            });

                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter.getClicked() > -1) {
            db.collection("Users").document(user.getUid()).collection("Playlist").document(PlaylistNames.get(adapter.getClicked()))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Playlist pl = documentSnapshot.toObject(Playlist.class);
                            adapter.updatePlaylist(pl, adapter.getClicked());
                        }
                    });
        }
    }
}
