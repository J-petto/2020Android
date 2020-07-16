package com.example.moviedatabase1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.service.vr.VrListenerService;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// 영화 항목의 추가, 수정, 삭제를 위한 액티비티 클래스
public class DisplayMovie extends AppCompatActivity {

    private DBHelper mydb;
    TextView title;
    TextView price;
    TextView date;
    TextView member;
    TextView nPrice;
    int id = 0;

    int mb;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    // 액키비티 생성 후 인텐트로 부터 추가, 수정, 삭제를 위한 매개변수 획득
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_movie);

        EditText et_Date = (EditText) findViewById(R.id.date);
        et_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DisplayMovie.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 테이블 항목 출력을 위한 위젯 생성
        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        date = (TextView) findViewById(R.id.date);
        member = (TextView) findViewById(R.id.member);
        nPrice = (TextView) findViewById(R.id.nPrice);
        // 데이터베이스 관리 객체 생성
        mydb = new DBHelper(this);

        // 인텐트를 통해 전달된 매개변수 획득
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // 영화 번호 획득
            int Value = extras.getInt("id");

            // 영화 번호에 해당하는 영화 데이터 조회
            if (Value > 0) {
                // 영화 데이터를 Cursor 객체로 반환
                Cursor rs = mydb.getData(Value);
                id = Value;

                // Cursor 객체 선두로 이동하여 해당 테이블 항목 획득
                rs.moveToFirst();
                String t = rs.getString(rs.getColumnIndex(DBHelper.PAY_COLUMN_TITLE));
                String p = rs.getString(rs.getColumnIndex(DBHelper.PAY_COLUMN_PRICE));
                String d = rs.getString(rs.getColumnIndex(DBHelper.PAY_COLUMN_DATE));
                String m = rs.getString(rs.getColumnIndex(DBHelper.PAY_COLUMN_MEMBER));
                String np = rs.getString(rs.getColumnIndex(DBHelper.PAY_COLUMN_NPRICE));

                // Cursor 객체 초기화
                if (!rs.isClosed()) {
                    rs.close();
                }

               // 삽입 버튼 보이지 않게 조정
                Button b = (Button) findViewById(R.id.save);
                b.setVisibility(View.INVISIBLE);

                // 조회된 정보를 텍스트뷰 위젯에 출력
                title.setText((CharSequence) t);
                price.setText((CharSequence) p);
                date.setText((CharSequence) d);
                member.setText((CharSequence) m);
                mb = Integer.parseInt(m);
                nPrice.setText((CharSequence) np);
            }
        }
    }

    public void insert(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateMovie(id, title.getText().toString(), price.getText().toString(), date.getText().toString(), member.getText().toString(), nPrice.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "수정되었음", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "수정되지 않았음", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mydb.insertMovie(title.getText().toString(), price.getText().toString(), date.getText().toString(), member.getText().toString(), nPrice.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "추가되었음", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "추가되지 않았음", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
    }

    public void delete(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                mydb.deleteMovie(id);
                Toast.makeText(getApplicationContext(), "삭제되었음", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "삭제되지 않았음", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void edit(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("id");
            if (value > 0) {
                if (mydb.updateMovie(id, title.getText().toString(), price.getText().toString(), date.getText().toString(), member.getText().toString(), nPrice.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "수정되었음", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "수정되지 않았음", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void mPlus(View view){
        mb = mb + 1;
        member.setText(String.valueOf(mb));
    }

    public void mMinus(View view){
        mb = mb - 1;
        member.setText(String.valueOf(mb));
    }

    public void nanugi(View view){
        int p = Integer.parseInt(price.getText().toString());
        int m = Integer.parseInt(member.getText().toString());

        float result = p/m;
        nPrice.setText(String.format("%.0f", result));
    }

    public void clip(View view){
        Toast.makeText(getApplicationContext(), "금액 텍스트가 복사되었습니다.", Toast.LENGTH_SHORT).show();

        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText("DutchPay", date.getText()+" 각 더치페이 금액은" + nPrice.getText() + "원 입니다.");

        clipboardManager.setPrimaryClip(clipData);
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = (EditText) findViewById(R.id.date);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }
}