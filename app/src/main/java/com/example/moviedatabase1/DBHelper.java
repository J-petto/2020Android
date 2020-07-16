package com.example.moviedatabase1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

// 데이터베이스 관리 클래스
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DutchPay.db";  // 데이터베이스 이름
    public static final String PAY_TABLE_NAME = "pay";  // 테이블 이름
    public static final String PAY_COLUMN_ID = "id";  // id값
    public static final String PAY_COLUMN_TITLE = "title";  // 타이틀
    public static final String PAY_COLUMN_PRICE = "price";  // 총 가격
    public static final String PAY_COLUMN_DATE = "date";  // 날짜
    public static final String PAY_COLUMN_MEMBER = "member";  // 인원
    public static final String PAY_COLUMN_NPRICE = "nprice";  // 터치페이 가격

    // 데이터베이스 관리 클래스 생성자 : 데이터베이스 생성
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table pay " +
                        "(id integer primary key,title text, price text, date text, member text, nprice text, account text, accountName text)"
        );
    }

    @Override
    // 테이블 초기화 - 기존 테이블 삭제 및 테이블 재생성
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pay");
        onCreate(db);
    }

    // 영화 항목 삽입
    public boolean insertMovie(String title, String price, String date, String member, String nprice) {
        // 쓰기 데이터베이스 생성
        SQLiteDatabase db = this.getWritableDatabase();

        // 삽입할 항목을 ContentValues 객체에 삽입
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("price", price);
        contentValues.put("date", date);
        contentValues.put("member", member);
        contentValues.put("nprice", nprice);

        // 항목 추가
        db.insert("pay", null, contentValues);
        return true;
    }

    // 영화 항목 조회 후 Cursor 객체로 반환
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from pay where id=" + id + "", null);
        return res;
    }

    // 테이블 레코드의 수 조회
    public int numberOfRows() {
        // 읽기 데이터베이스 생성 후 항목의 갯수 조회
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PAY_TABLE_NAME);
        return numRows;
    }

    // 영화 항목 수정
    public boolean updateMovie(Integer id, String title, String price, String date, String member, String nprice) {
        // 쓰기 데이터베이스 생성
        SQLiteDatabase db = this.getWritableDatabase();

        // 수정할 항목을 ContentValues 객체에 삽입
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("price", price);
        contentValues.put("date", date);
        contentValues.put("member", member);
        contentValues.put("nprice", nprice);

        // 해당 항목에 수정
        db.update("pay", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    // 영화 항목 삭제
    public Integer deleteMovie(Integer id) {
        // 쓰기 데이터베이스 생성 후 영화 항목 삭제
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("pay",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    // 영화 항목 조회 후 ArrayList로 반환
    public ArrayList getAllMovies() {
        // ArrayList 생성
        ArrayList array_list = new ArrayList();
        // 읽기 데이터베이스 생성
        SQLiteDatabase db = this.getReadableDatabase();
        // 모든 영화 데이터 조회 후 Cursor 객체에 반환
        Cursor res = db.rawQuery("select * from pay", null);

        // Cursor 선두로 이동하여 모든 영화 번호와 영화 이름 획득하여 ArrayList에 추가
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(PAY_COLUMN_ID))+ " "+
                    res.getString(res.getColumnIndex(PAY_COLUMN_TITLE)) + " / 금액 :  " +
                    res.getString(res.getColumnIndex(PAY_COLUMN_PRICE)));
            res.moveToNext();
        }
        // ArrayList 반환
        return array_list;
    }
}