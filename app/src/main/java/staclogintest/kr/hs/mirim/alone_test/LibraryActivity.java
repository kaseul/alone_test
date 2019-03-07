package staclogintest.kr.hs.mirim.alone_test;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import staclogintest.kr.hs.mirim.alone_test.adapter.MyListAdapter;
import staclogintest.kr.hs.mirim.alone_test.model.ListViewBtnItem;

import static staclogintest.kr.hs.mirim.alone_test.SignInActivity.var;

public class LibraryActivity extends AppCompatActivity {
    ListViewBtnItem item;

    private MyListAdapter adapter;
    private ArrayList<ListViewBtnItem> list;
    private ArrayList<ListViewBtnItem> searchList;
    private RecyclerView libraryListRecyclerView;
    private EditText editSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        libraryListRecyclerView = findViewById(R.id.librarylist_recyclerview);
        editSearch = findViewById(R.id.search);

        list = new ArrayList<ListViewBtnItem>();
        searchList = new ArrayList<ListViewBtnItem>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference soundRef = database.getReference().child("library");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                searchList.clear();
                for(DataSnapshot soundsData : dataSnapshot.getChildren()){
                    item = soundsData.getValue(ListViewBtnItem.class);
                    list.add(item);
                    searchList.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };
        soundRef.addValueEventListener(postListener);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(var.UserID).collection("Download")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("다운로드 읽기", document.getId() + " => " + document.getData());
                                int count = 0;
                                ListViewBtnItem libraryitem;
                                for(ListViewBtnItem item : list) {
                                    if(item.title.equals(document.get("title"))) {
                                        libraryitem = item;
                                        libraryitem.ifdownload = true;
                                        list.set(count, libraryitem);
                                        Log.d("다운로드가 되어있는지", String.valueOf(libraryitem.ifdownload) + count);
                                        break;
                                    }
                                    count++;
                                }
                            }
                            adapter = new MyListAdapter(list, getApplication());
                            libraryListRecyclerView.setAdapter(adapter);
                            adapter.setMyListAdapter(list);
                        } else {
                            Log.w("다운로드 읽기", "Error getting documents.", task.getException());
                        }
                    }
                });

//        item = new ListViewBtnItem();
//        item.title = "키보드 소리";
//        item.content = "#타자소리 #일상";
//        item.url = "https://firebasestorage.googleapis.com/v0/b/alone-testing.appspot.com/o/record%2F%ED%82%A4%EB%B3%B4%EB%93%9C%20%EC%86%8C%EB%A6%AC.mp3?alt=media&token=64f2524f-c78b-484f-aebf-9e61d802b233";
//        list.add(item);
//
//        item = new ListViewBtnItem();
//        item.title = "기침 소리";
//        item.content = "#기침 #남자";
//        item.url = "https://firebasestorage.googleapis.com/v0/b/alone-testing.appspot.com/o/record%2F%EA%B8%B0%EC%B9%A8%20%EC%86%8C%EB%A6%AC.mp3?alt=media&token=db706308-055d-4579-9f1d-1b5f52de9c14";
//        list.add(item);
//
//        item = new ListViewBtnItem();
//        item.title = "설거지 소리";
//        item.content = "#설거지 #부엌";
//        item.url = "https://firebasestorage.googleapis.com/v0/b/alone-testing.appspot.com/o/record%2F%EC%84%A4%EA%B1%B0%EC%A7%80%20%EC%86%8C%EB%A6%AC.mp3?alt=media&token=b11f5998-861a-4569-a7cc-6d9ca6194acd";
//        list.add(item);
//
//        item = new ListViewBtnItem();
//        item.title = "헤어드라이기 소리";
//        item.content = "#헤어드라이기 #일상";
//        item.url = "https://firebasestorage.googleapis.com/v0/b/alone-testing.appspot.com/o/record%2F%ED%97%A4%EC%96%B4%EB%93%9C%EB%9D%BC%EC%9D%B4%EA%B8%B0%20%EC%86%8C%EB%A6%AC.mp3?alt=media&token=b058f01d-d058-4fe3-a413-25e9b37de737";
//        list.add(item);

        editSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }

        });

        libraryListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyListAdapter(list, this) ;
        libraryListRecyclerView.setAdapter(adapter);
    }

    // 검색을 수행하는 메소드
    public void search(String charText) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(searchList);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < searchList.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (searchList.get(i).content.toLowerCase().contains(charText.toLowerCase()) || matchString(searchList.get(i).content,charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(searchList.get(i));
                } else if(searchList.get(i).title.toLowerCase().contains(charText.toLowerCase()) || matchString(searchList.get(i).title,charText)) {
                    list.add(searchList.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    private static final char HANGUL_BEGIN_UNICODE = 44032; // 가
    private static final char HANGUL_LAST_UNICODE = 55203; // 힣
    private static final char HANGUL_BASE_UNIT = 588;//각자음 마다 가지는 글자수
    //자음
    private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };


    /**
     * 해당 문자가 INITIAL_SOUND인지 검사.
     * @param searchar
     * @return
     */
    private static boolean isInitialSound(char searchar){
        for(char c:INITIAL_SOUND){
            if(c == searchar){
                return true;
            }
        }
        return false;
    }

    /**
     * 해당 문자의 자음을 얻는다.
     *
     * @param c 검사할 문자
     * @return
     */
    private static char getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin / HANGUL_BASE_UNIT;
        return INITIAL_SOUND[index];
    }

    /**
     * 해당 문자가 한글인지 검사
     * @param c 문자 하나
     * @return
     */
    private static boolean isHangul(char c) {
        return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
    }

    /** * 검색을 한다. 초성 검색 완벽 지원함.
     * @param value : 검색 대상 ex> 초성검색합니다
     * @param search : 검색어 ex> ㅅ검ㅅ합ㄴ
     * @return 매칭 되는거 찾으면 true 못찾으면 false. */
    public static boolean matchString(String value, String search){
        int t = 0;
        int seof = value.length() - search.length();
        int slen = search.length();
        if(seof < 0)
            return false; //검색어가 더 길면 false를 리턴한다.
        for(int i = 0;i <= seof;i++){
            t = 0;
            while(t < slen){
                if(isInitialSound(search.charAt(t))==true && isHangul(value.charAt(i+t))){
                    //만약 현재 char이 초성이고 value가 한글이면
                    if(getInitialSound(value.charAt(i+t))==search.charAt(t))
                        //각각의 초성끼리 같은지 비교한다
                        t++;
                    else
                        break;
                } else {
                    //char이 초성이 아니라면
                    if(value.charAt(i+t)==search.charAt(t))
                        //그냥 같은지 비교한다.
                        t++;
                    else
                        break;
                }
            }
            if(t == slen)
                return true; //모두 일치한 결과를 찾으면 true를 리턴한다.
        }
        return false; //일치하는 것을 찾지 못했으면 false를 리턴한다.
    }
}
