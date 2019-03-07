package staclogintest.kr.hs.mirim.alone_test.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import staclogintest.kr.hs.mirim.alone_test.PlaylistContentActivity;
import staclogintest.kr.hs.mirim.alone_test.R;
import staclogintest.kr.hs.mirim.alone_test.model.Playlist;

public class PlayListRecyclerAdapter extends RecyclerView.Adapter<PlayListRecyclerAdapter.ViewHolder> {
    private Context context;

    private ArrayList<Playlist> playlists = new ArrayList<>();
    public int clicked = -1;

    public PlayListRecyclerAdapter(Context c) {
        this.context = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.playlist_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.playlistName.setText(playlists.get(position).name);
        holder.playlistNum.setText(playlists.get(position).count + "");

        holder.playlistView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                //Toast.makeText(context, position + " : " + playlists.get(position).docName + " " + holder.playlistName.getText() + " " + holder.playlistNum.getText(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, playlists.get(position).name, Toast.LENGTH_SHORT).show();
                clicked = position;
                Intent intent = new Intent(context, PlaylistContentActivity.class);
                intent.putExtra("docName", playlists.get(position).name);
                context.startActivity(intent);
            }
        });

    }

    public int getClicked() {
        return clicked;
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public void setPlayList(ArrayList<Playlist> arr) {
        this.playlists = arr;
        Collections.sort(playlists, new Comparator<Playlist>() {
            @Override
            public int compare(Playlist o1, Playlist o2) {
                return o1.name.compareTo(o2.name);
            }
        });

        notifyDataSetChanged();
    }

    public void addPlayList(Playlist pl) {
        this.playlists.add(pl);
        Collections.sort(playlists, new Comparator<Playlist>() {
            @Override
            public int compare(Playlist o1, Playlist o2) {
                return o1.name.compareTo(o2.name);
            }
        });
        notifyDataSetChanged();
    }

    public void updatePlaylist(Playlist pl, int position) {
        this.playlists.set(position, pl);
        Log.d("position", "" + position + pl.count);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView playlistName;
        public TextView playlistNum;
        public TextView playlistMusic;
        public View playlistView;

        public ViewHolder(View v) {
            super(v);
            playlistView = v;

            playlistName = v.findViewById(R.id.playlist_name);
            playlistMusic = v.findViewById(R.id.text_music);
            playlistNum = v.findViewById(R.id.playlist_num);
        }
    }

}
