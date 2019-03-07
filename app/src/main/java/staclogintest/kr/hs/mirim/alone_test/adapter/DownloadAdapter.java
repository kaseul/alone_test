package staclogintest.kr.hs.mirim.alone_test.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import staclogintest.kr.hs.mirim.alone_test.R;
import staclogintest.kr.hs.mirim.alone_test.model.DownloadItem;

import static staclogintest.kr.hs.mirim.alone_test.SongListActivity.downloads;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    private Context context;

    public DownloadAdapter(Context c) {
        this.context = c;
    }

    @Override
    public DownloadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownloadAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.download_recycler_item, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final DownloadAdapter.ViewHolder holder, final int position) {
        holder.downloadTitle.setText(downloads.get(position).title);
        holder.downloadContent.setText(downloads.get(position).content);
        if(downloads.get(position).isExist)
            holder.download.setBackgroundColor(R.color.highlight);
        else {
            holder.download.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {

                        if (downloads.get(position).clicked == true)
                            downloads.get(position).clicked = false;
                        else downloads.get(position).clicked = true;

                        if (downloads.get(position).clicked == true) {
                            holder.download.setBackgroundColor(R.color.highlight);
                        } else {
                            holder.download.setBackgroundColor(Color.TRANSPARENT);
                        }
                        notifyDataSetChanged();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return downloads.size();
    }

    public void setDownloads(ArrayList<DownloadItem> arr) {
        notifyDataSetChanged();
    }

    public void addDownloads(DownloadItem item) {
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView downloadTitle;
        public TextView downloadContent;
        public View download;

        public ViewHolder(View v) {
            super(v);
            download = v;

            downloadTitle = v.findViewById(R.id.download_title);
            downloadContent = v.findViewById(R.id.download_content);
        }
    }

}
