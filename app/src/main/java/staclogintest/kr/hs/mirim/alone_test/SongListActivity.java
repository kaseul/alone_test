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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import staclogintest.kr.hs.mirim.alone_test.adapter.DownloadAdapter;
import staclogintest.kr.hs.mirim.alone_test.model.DownloadItem;
import staclogintest.kr.hs.mirim.alone_test.model.PlaylistContentItem;


import static staclogintest.kr.hs.mirim.alone_test.PlaylistContentActivity.downloaded;
import static staclogintest.kr.hs.mirim.alone_test.PlaylistContentActivity.playlistContentItemArrayList;
import static staclogintest.kr.hs.mirim.alone_test.SignInActivity.var;

public class SongListActivity extends AppCompatActivity {
    FirebaseFirestore db;

    private RecyclerView downloadRecyclerView;
    private DownloadAdapter adapter;
    public static ArrayList<DownloadItem> downloads = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        downloadRecyclerView = findViewById(R.id.download_recyclerview);
        downloadRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DownloadAdapter(getApplication());
        downloadRecyclerView.setAdapter(adapter);

        downloads.clear();

        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(var.UserID).collection("Download")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("다운로드 읽기", document.getId() + " => " + document.getData());

                                DownloadItem download = new DownloadItem();
                                download.content = (String)document.get("content");
                                download.title = (String)document.get("title");
                                download.url = (String)document.get("url");
                                for(String str : downloaded) {
                                    if(download.title.equals(str)){
                                        download.isExist = true;
                                        break;
                                    }
                                }
                                downloads.add(download);
                            }
                            adapter.setDownloads(downloads);
                        } else {
                            Log.w("다운로드 읽기", "Error getting documents.", task.getException());
                        }
                    }
                });

        Button btnAddto = findViewById(R.id.btn_add_to_playlist);
        btnAddto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlistContentItemArrayList.clear();
                for(DownloadItem item : downloads) {
                    if(item.clicked) {
                        PlaylistContentItem playlistContentItem = new PlaylistContentItem();
                        playlistContentItem.title = item.title;
                        playlistContentItem.content = item.content;
                        playlistContentItem.url = item.url;
                        playlistContentItem.order = var.order++;
                        playlistContentItemArrayList.add(playlistContentItem);
                        downloaded.add(item.title);
                    }
                }
                Intent intent = new Intent(getApplicationContext(), PlaylistContentActivity.class);
                setResult(10, intent);
                finish();
            }
        });

    }
}
