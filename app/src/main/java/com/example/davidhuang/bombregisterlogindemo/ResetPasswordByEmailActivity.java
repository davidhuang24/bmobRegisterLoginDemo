package com.example.davidhuang.bombregisterlogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ResetPasswordByEmailActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_left;
    private TextView tv_title;
    private TextView reset;
    private EditText et_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_by_email);
        iv_left=(ImageView) findViewById(R.id.iv_left);
        tv_title=(TextView) findViewById(R.id.tv_title);
        reset=(TextView) findViewById(R.id.tv_restpwd_byemail);
        et_email=(EditText) findViewById(R.id.et_email);
        iv_left.setVisibility(View.VISIBLE);
        tv_title.setText("用Emial重置密码");
        iv_left.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_restpwd_byemail:
                resetPwdByEmail();
                break;
            default:
        }
    }

    private void resetPwdByEmail(){
        String email=et_email.getText().toString().trim();
        if(InputLeagalCheck.isEmail(email)){
            BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        Toast.makeText(ResetPasswordByEmailActivity.this,
                                "重置密码请求成功，请到您的邮箱进行重置操作", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(ResetPasswordByEmailActivity.this,
                                "重置密码请求失败，code="+e.getErrorCode()+",错误原因："+e.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "请输入正确格式的Email", Toast.LENGTH_SHORT).show();
        }

    }
}
