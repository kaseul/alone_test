package staclogintest.kr.hs.mirim.alone_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import staclogintest.kr.hs.mirim.alone_test.adapter.PlayListContentRecyclerAdapter;
import staclogintest.kr.hs.mirim.alone_test.model.PlaylistContentItem;

import static staclogintest.kr.hs.mirim.alone_test.SignInActivity.var;

public class PlaylistContentActivity extends AppCompatActivity {
    public static final int PlaylistContent = 10;
    FirebaseFirestore db;
    public static ArrayList<PlaylistContentItem> playlistContentItemArrayList = new ArrayList<>();
    public static ArrayList<String> downloaded = new ArrayList<>();

    private RecyclerView PlayListContentRecycler;
    private PlayListContentRecyclerAdapter adapter;
    String docName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_content);

        Intent intent = getIntent();
        docName = intent.getStringExtra("docName");

        PlayListContentRecycler = findViewById(R.id.playlistcontent_recyclerview);
        PlayListContentRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlayListContentRecyclerAdapter(getApplication());
        PlayListContentRecycler.setAdapter(adapter);

        Button btnAdd = findViewById(R.id.btn_add_sound);
        btnAdd.setOnClickListener(addHandler);

        downloaded.clear();

        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(var.UserID).collection("Playlist").document(docName).collection("Records")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("다운로드 읽기", document.getId() + " => " + document.getData());

                                PlaylistContentItem playlistContentItem = new PlaylistContentItem();
                                playlistContentItem.title = (String)document.get("title");
                                playlistContentItem.content = (String)document.get("content");
                                playlistContentItem.url = (String)document.get("url");
                                playlistContentItem.order = (int)(long)document.get("order");
                                adapter.addPlayList(playlistContentItem);
                                downloaded.add(playlistContentItem.title);
                            }
                            var.order = downloaded.size();
                        } else {
                            Log.w("다운로드 읽기", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    View.OnClickListener addHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SongListActivity.class);
            startActivityForResult(intent, PlaylistContent);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(PlaylistContent == resultCode) {
            for(PlaylistContentItem item : playlistContentItemArrayList) {
                adapter.addPlayList(item);
                db.collection("Users").document(var.UserID).collection("Playlist").document(docName).collection("Records").document(item.title)
                        .set(item)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("레코드 추가", "DocumentSnapshot successfully written!");
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("레코드 추가", "Error writing document", e);
                                  }
                        });
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        db.collection("Users").document(var.UserID).collection("Playlist").document(docName)
                .update("count", adapter.getItemCount())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("곡 수 수정", "DocumentSnapshot successfully written!");
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("곡 수 수정", "Error writing document", e);
                    }
                });

    }
}
