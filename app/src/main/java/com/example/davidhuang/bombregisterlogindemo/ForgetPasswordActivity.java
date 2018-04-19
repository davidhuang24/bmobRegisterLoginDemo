package com.example.davidhuang.bombregisterlogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_left;
    private TextView tv_title;
    private Button resetByPhone;
    private Button resetByEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        iv_left=(ImageView) findViewById(R.id.iv_left);
        tv_title= (TextView) findViewById(R.id.tv_title);
        resetByPhone=(Button) findViewById(R.id.btn_reset_by_phonenum);
        resetByEmail=(Button) findViewById(R.id.btn_reset_by_email);
        iv_left.setVisibility(View.VISIBLE);
        tv_title.setText("重置密码");
        resetByEmail.setOnClickListener(this);
        resetByPhone.setOnClickListener(this);
        iv_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_reset_by_phonenum://用手机号更改密码
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
            case R.id.btn_reset_by_email://用email更改密码
                startActivity(new Intent(this, ResetPasswordByEmailActivity.class));
                break;
            default:
        }
    }
}
