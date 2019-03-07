package staclogintest.kr.hs.mirim.alone_test.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import staclogintest.kr.hs.mirim.alone_test.R;
import staclogintest.kr.hs.mirim.alone_test.model.Member;

public class ListAdapter extends BaseAdapter {
    private ArrayList<Member> items;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    SimpleDateFormat sdf = new SimpleDateFormat("yy년-MM월-dd일 ");

    public ListAdapter(ArrayList<Member> items) {
        this.items = items;
        //Collections.reverse(this.items);//최근 작성 글부터 나타나게 해줌
        Collections.sort(this.items, new Comparator<Member>() {
            @Override
            public int compare(Member o1, Member o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
//        for(Member m : this.items) {
//            Log.d("items 값", m.getName());
//        }
//        Log.d("사이즈 확인", this.items.size() +"");
        notifyDataSetChanged();
    }//ListAdaptr

    public void setListAdapter(ArrayList<Member> items) {
        this.items = items;
        Collections.reverse(items);//최근 작성 글부터 나타나게 해줌
    }

    public String calculateTime(Date date) {
        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;

        if (diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = "방금";
        }
        else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            System.out.println(diffTime);
            msg = diffTime + "분전";
        }
        else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime ) + "시간전";
        }
        else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            // day
            msg = (diffTime ) + "일전";
        }
        else {
            msg = sdf.format(date);
        }
        return msg;
    }//calculateTime(Date date)

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }//Object getItem

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Holder holder = new Holder();
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_items, parent, false);
            holder.circleImage = convertView.findViewById(R.id.circleImage);
            holder.name = convertView.findViewById(R.id.name);
            holder.text = convertView.findViewById(R.id.text);
            holder.imageUrl = convertView.findViewById(R.id.imageUrl);
            holder.date = convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }//사용자의 이름, 프로필 등을 가지고 옴.

        Member member = (Member) getItem(position);
//        Log.d("포지션 확인", position +"");

        holder.text.setText(member.getText());
        holder.name.setText(member.getName());

        if(member.getDate()==null){
            holder.date.setText("not found");

        }else holder.date.setText(calculateTime(member.getDate()));
        if (member.getPhotoUrl() == null) {
            holder.circleImage.setBackgroundResource(R.drawable.ic_account_circle_black_24dp);
        } else {
            Glide.with(parent.getContext())
                    .load(member.getPhotoUrl())
                    .into(holder.circleImage);
        }//else

        return convertView;
    }//View getView

//사용자의 관한 것들
    public class Holder{
        CircleImageView circleImage;
        TextView name;
        TextView text;
        ImageView imageUrl;
        TextView date;
        TextView like_num;
    }

//시간 나타내는
    private static class TIME_MAXIMUM
    {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

}//ListAdapter
