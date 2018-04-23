package com.example.davidhuang.bombregisterlogindemo;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


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
    Button btn_bind_phone_num;
    Button btn_bind_email;
    Button btn_logout;
    CircleImageView civ_user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_left=(ImageView) findViewById(R.id.iv_left);
        btn_bind_phone_num =(Button) findViewById(R.id.btn_bind);
        btn_bind_email =(Button) findViewById(R.id.btn_bind_email);
        btn_logout=(Button) findViewById(R.id.btn_logout);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_user= (TextView) findViewById(R.id.tv_user);
        civ_user_image=(CircleImageView)findViewById(R.id.user_image) ;

        iv_left.setVisibility(View.VISIBLE);
        tv_title.setText("首页");

        iv_left.setOnClickListener(this);
        btn_bind_phone_num.setOnClickListener(this);
        btn_bind_email.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        civ_user_image.setOnClickListener(this);

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
            case R.id.user_image://设置头像
                showPopupWindow();
                break;
            case R.id.pop_album://
                Toast.makeText(this, "相册", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            case R.id.pop_camera://
                Toast.makeText(this, "相机", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            case R.id.pop_cancel://
                Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            default:
        }
    }

    private PopupWindow popupWindow;
    public void showPopupWindow(){
        View contentView= LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_window,null);
        popupWindow=new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tv_album=(TextView) contentView.findViewById(R.id.pop_album);
        TextView tv_camera=(TextView) contentView.findViewById(R.id.pop_camera);
        TextView tv_cancel=(TextView) contentView.findViewById(R.id.pop_cancel);
        tv_album.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
}
