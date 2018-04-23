package com.example.davidhuang.bombregisterlogindemo;

import cn.bmob.v3.BmobUser;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 首页
 * @class  MainActivity
 * @author DaviaHuang
 * @date   2018-4-6 下午5:02:13
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView iv_left;
    TextView tv_title;
    TextView tv_user;
    Button btn_bind_phonenum;
    Button btn_bind_emial;
    Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_left=(ImageView) findViewById(R.id.iv_left);
        btn_bind_phonenum =(Button) findViewById(R.id.btn_bind);
        btn_bind_emial=(Button) findViewById(R.id.btn_bind_email);
        btn_logout=(Button) findViewById(R.id.btn_logout);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_user= (TextView) findViewById(R.id.tv_user);

        iv_left.setVisibility(View.VISIBLE);
        tv_title.setText("首页");

        iv_left.setOnClickListener(this);
        btn_bind_phonenum.setOnClickListener(this);
        btn_bind_emial.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }

    private void UpdateUser(){//获取当前缓存用户
        User currentUser = BmobUser.getCurrentUser(User.class);
        tv_user.setText("用户名："+currentUser.getUsername()+"  年龄："+currentUser.getAge()+
                "  Email："+currentUser.getEmail()+"  手机号"+currentUser.getMobilePhoneNumber());
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        UpdateUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_logout://登出
                BmobUser.logOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btn_bind://绑定手机号
                startActivity(new Intent(this, UserBindPhoneActivity.class));
                break;
            case R.id.btn_bind_email://绑定email
                startActivity(new Intent(this, UserBindEmailActivity.class));
                break;
            default:
        }
    }
}
