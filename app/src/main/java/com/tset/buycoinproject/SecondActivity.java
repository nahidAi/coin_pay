package com.tset.buycoinproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView = findViewById(R.id.textView);
        int myId = getIntent().getIntExtra("id",0);

        switch (myId){
            case (R.id.card_1):
                textView.setText("ورود به مرحله اول ");
                break;
            case (R.id.card_2):
                textView.setText("ورود به مرحله دوم  ");
                break;
            case (R.id.card_3):
                textView.setText("ورود به مرحله سوم ");
                break;
            case (R.id.card_4):
                textView.setText("ورود به مرحله چهارم ");
                break;
            case (R.id.card_5):
                textView.setText("ورود به مرحله پنجم ");
                break;
        }
    }
}
