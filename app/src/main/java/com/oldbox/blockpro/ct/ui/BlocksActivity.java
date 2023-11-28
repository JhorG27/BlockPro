package com.oldbox.blockpro.ct.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.oldbox.blockpro.ct.R;

public class  BlocksActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lin_bttn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocks);
        initViews();
        initLogic();
    }

    private void initViews(){

        lin_bttn_back = findViewById(R.id.lin_bttn_back);
        lin_bttn_back.setOnClickListener(this);
    }

    private void initLogic(){

    }

    @Override
    public void onClick(View _view) {
        if (_view.getId() == R.id.lin_bttn_back) {
            finish();
        }
    }
}