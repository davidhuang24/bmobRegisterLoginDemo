package com.example.davidhuang.bombregisterlogindemo;

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

/**
 * 绑定Email
 * @class  UserBindEmailActivity
 * @author DavidHuang
 * @date   2018-4-12 下午18:08:53
 *
 */
public class UserBindEmailActivity extends  AppCompatActivity implements View.OnClickListener {

    ImageView iv_left;
    EditText et_email;
    TextView tv_title;
    TextView tv_bind_email;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bind_email);
        iv_left=(ImageView) findViewById(R.id.iv_left);
        et_email=(EditText) findViewById(R.id.et_email);
        tv_bind_email=(TextView) findViewById(R.id.tv_bind_email);
        tv_title=(TextView) findViewById(R.id.tv_title);
        iv_left.setVisibility(View.VISIBLE);
        tv_title.setText("绑定Email");
        iv_left.setOnClickListener(this);
        tv_bind_email.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_bind_email:
                bindEmail();
                break;
            default:
        }
    }
    /**
     *绑定邮箱，需提交1个字段的值：email
     */
    private void bindEmail(){
        email=et_email.getText().toString().trim();
        if(InputLeagalCheck.isEmail(email)){
            User user =new User();
            user.setEmail(email);
            final User currentUser = BmobUser.getCurrentUser(User.class);

            user.update(currentUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException ex) {
                    if(ex==null){//绑定成功
                        requestVerifyEmail();//用户必须先绑定邮箱才能验证邮箱，先验证后绑定会报205错;未验证的邮箱在登入时会限制邮箱登入
                        Toast.makeText(UserBindEmailActivity.this,
                                "邮箱绑定成功", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(UserBindEmailActivity.this,
                                "邮箱绑定失败，code="+ex.getErrorCode()+",错误原因："+ex.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "请输入正确格式的Email", Toast.LENGTH_SHORT).show();
        }


    }
    /**
     *验证邮箱（请求服务器发送验证邮件）,验证成功修改"emailVerified"属性为true
     */
    private void requestVerifyEmail(){
        BmobUser.requestEmailVerify(email, new UpdateListener() {
            @Override
            public void done(BmobException ex) {
                if(ex==null){
                    Toast.makeText(UserBindEmailActivity.this,
                            "验证链接已发送至您的邮箱，请及时验证（注意查看垃圾邮件）",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserBindEmailActivity.this,
                            "验证邮件发送失败：code="+ex.getErrorCode()+"，错误描述："+ex.getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
