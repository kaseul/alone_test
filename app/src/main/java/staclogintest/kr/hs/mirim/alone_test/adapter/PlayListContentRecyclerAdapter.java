package staclogintest.kr.hs.mirim.alone_test.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import staclogintest.kr.hs.mirim.alone_test.R;
import staclogintest.kr.hs.mirim.alone_test.service.AudioService;
import staclogintest.kr.hs.mirim.alone_test.model.PlaylistContentItem;

public class PlayListContentRecyclerAdapter extends RecyclerView.Adapter<PlayListContentRecyclerAdapter.ViewHolder> {
    public static Context c;
    private Context context;
    boolean isClicked = true;

    private ArrayList<PlaylistContentItem> playlistContentItems = new ArrayList<>();

    public PlayListContentRecyclerAdapter(Context c) {
            this.context = c;
    }

    @Override
    public PlayListContentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.playlistcontent_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final PlayListContentRecyclerAdapter.ViewHolder holder,final int position) {
        holder.contentName.setText(playlistContentItems.get(position).title);
        holder.contentTime.setText(playlistContentItems.get(position).time);

        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AudioService.class);
                if(isClicked) {
                    intent.putExtra("playlist", playlistContentItems);
                    intent.putExtra("position", position);
                    context.startService(intent);
                    c = context;
                    isClicked = false;
                } else {
                    context.stopService(intent);
                    isClicked = true;
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return playlistContentItems.size();
    }

    public void setPlayList(ArrayList<PlaylistContentItem> arr) {
        this.playlistContentItems = arr;
        Collections.sort(playlistContentItems, new Comparator<PlaylistContentItem>() {
            @Override
            public int compare(PlaylistContentItem o1, PlaylistContentItem o2) {
                return o1.order - o2.order;
            }
        });

        notifyDataSetChanged();
    }

    public void addPlayList(PlaylistContentItem pl) {
        this.playlistContentItems.add(pl);
        Collections.sort(playlistContentItems, new Comparator<PlaylistContentItem>() {
            @Override
            public int compare(PlaylistContentItem o1, PlaylistContentItem o2) {
                return o1.order - o2.order;
            }
        });

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView contentName;
        public TextView contentTime;
        public Button btnPlay;

        public ViewHolder(View v) {
            super(v);

            contentName = v.findViewById(R.id.content_name);
            contentTime = v.findViewById(R.id.content_time);
            btnPlay = v.findViewById(R.id.btn_play);
        }
    }
}
