package staclogintest.kr.hs.mirim.alone_test.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import staclogintest.kr.hs.mirim.alone_test.model.ListViewBtnItem;
import staclogintest.kr.hs.mirim.alone_test.R;

import static staclogintest.kr.hs.mirim.alone_test.SignInActivity.var;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

    private Context context;
    MediaPlayer music;
    List<ListViewBtnItem> searchList;
    Map<String,String> postValues = new HashMap<>();
    ListViewBtnItem item;

    public MyListAdapter(List<ListViewBtnItem> list, Context context) {
        searchList = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(searchList.get(position) != null) {
            holder.title.setText(searchList.get(position).title);
            holder.content.setText(searchList.get(position).content);
            if(searchList.get(position).ifdownload == true) {
                Log.d("다운로드 확인", String.valueOf(searchList.get(position).ifdownload));
                holder.btnDown.setText("삭제");
            }
        }

        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(music == null) {
                music = new MediaPlayer();

                try {
                    music.setDataSource(searchList.get(position).url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (!music.isPlaying()) {
                try {
                    music.prepare();
                    music.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                music.stop();
                music.release();
                music = null;
            }

            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference soundRef = database.getReference().child("library");

        holder.btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnDown.setVisibility(View.GONE);
//                if(var.UserID.equals("E3L4wG6Xm8gsc4xFYi4TGJe1VyV2")) {
//                    DatabaseReference keyRef = soundRef.push();
//
//                    postValues.put("title",searchList.get(position).title);
//                    postValues.put("content",searchList.get(position).content);
//                    postValues.put("url",searchList.get(position).url);
//                    postValues.put("key",keyRef.getKey());
//
//                    keyRef.setValue(postValues);
//                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Users").document(var.UserID).collection("Download").document(searchList.get(position).title)
                        .set(searchList.get(position))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("다운로드", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("다운로드", "Error writing document", e);
                            }
                        });

            }
        });

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public void setMyListAdapter(List<ListViewBtnItem> arr) {
        this.searchList = arr;

        notifyDataSetChanged();
    }

    public void addMyListAdapter(ListViewBtnItem pl) {
        this.searchList.add(pl);

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;
        public Button btnPlay;
        public Button btnDown;

        public ViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.title_textview);
            content = v.findViewById(R.id.content_textview);
            btnPlay = v.findViewById(R.id.btn_play);
            btnDown = v.findViewById(R.id.btn_down);
        }
    }


}




