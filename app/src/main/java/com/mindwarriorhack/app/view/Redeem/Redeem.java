package com.mindwarriorhack.app.view.Redeem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.view.Home.HomeActivity;

public class Redeem extends AppCompatActivity implements View.OnClickListener
{
    private EditText redeem_code;
    private ImageView backRedeem;
    private Button redeem_button;
    private TextView terms_conditions,tc_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        initUI();
    }

    private void initUI(){
        redeem_code=findViewById(R.id.redeem_code);
        backRedeem=findViewById(R.id.backRedeem);
        backRedeem.setOnClickListener(this);
        redeem_button=findViewById(R.id.redeem_button);
        terms_conditions=findViewById(R.id.terms_conditions);
        tc_text=findViewById(R.id.tc_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backRedeem:
                finish();
                break;

            case R.id.redeem_button:
                break;
        }
    }
}
