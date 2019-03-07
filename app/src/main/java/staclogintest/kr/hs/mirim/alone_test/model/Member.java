package staclogintest.kr.hs.mirim.alone_test.model;

import java.io.Serializable;
import java.util.Date;

public class Member implements Serializable{
    private String id;          // DB에 저장할 ID
    private String text;        // 내용
    private String name;        // 이름
    private String photoUrl;    // 프로필 사진 경로
    private String imageUrl;    // 첨부 이미지 경로
    private Date date;  //날짜
    public Member() { }

    public Member(String text, String name, String photoUrl, String imageUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.imageUrl = imageUrl;
    }//Member

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
