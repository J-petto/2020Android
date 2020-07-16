package com.example.moviedatabase1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

// SQLite 데이터베이스를 활용한 영화 등록 및 조회 예제
public class MainActivity extends AppCompatActivity {
    private ListView myListView;
    DBHelper mydb;
    ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 데이터베이스 관리 객체 생성
        mydb = new DBHelper(this);
        // 테이블의 모든 항목 조회 결과를 담은 ArrayList 반환
        ArrayList array_list = mydb.getAllMovies();

        // ArrayList와 연결한 ArrayAdapter 생성
        mAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);

        // ListView 생성 후 ArrayAdapter 연결
        myListView = (ListView) findViewById(R.id.listView1);
        myListView.setAdapter(mAdapter);

        // ListView에 이벤트 처리
        myListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            // 이벤트가 발생한 항목의 영화 번호를 획득하여 인텐트를 통해 DisplayMovie 객체로 전달
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg4) {
                String item = (String) ((ListView) parent).getItemAtPosition(position);
                String[] strArray = item.split(" ");
                int id=Integer.parseInt(strArray[0]);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id);
                Intent intent = new Intent(getApplicationContext(), DisplayMovie.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    @Override
    // 액티비티 재활성화시 어댑터를 초기화하고 테이블 모든 항목을 조회한 후 어댑터에 추가
    // 새로 만들어진 어댑터의 재활성화
    protected void onResume() {
        super.onResume();
        mAdapter.clear();
        mAdapter.addAll(mydb.getAllMovies());
        mAdapter.notifyDataSetChanged();
    }

    // 영화 추가를 위한 이벤트 처리
    // 영화 번호를 0로 설정하여 인텐트를 통해 DisplayMovie 객체로 전달
    public void onClick(View target) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", 0);
        Intent intent = new Intent(getApplicationContext(), DisplayMovie.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}