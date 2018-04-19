package com.example.davidhuang.bombregisterlogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;


/**
 *手机号一键注册登录
 * 
 * @class LoginOneKeyActivity
 * @author DavidHuang
 * @date 2018-4-2 上午14:42:44
 * 
 */
public class LoginOneKeyActivity extends AppCompatActivity implements View.OnClickListener{

	private MyCountTimer timer;
	private ImageView iv_left;
	private TextView tv_title;
	private EditText et_phone;
	private EditText et_code;
	private Button btn_send;
	private Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_onekey);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		tv_title=(TextView) findViewById(R.id.tv_title);
		et_phone=(EditText) findViewById(R.id.et_phone);
		et_code=(EditText) findViewById(R.id.et_verify_code);
		btn_send=(Button) findViewById(R.id.btn_send);
		btn_login=(Button) findViewById(R.id.btn_login);
		iv_left.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		iv_left.setVisibility(View.VISIBLE);
		tv_title.setText("手机号一键注册登录");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.btn_send:
				requestSMSCode();
				break;
			case R.id.btn_login:
				oneKeyLogin();
				break;
			default:
		}
	}


	//计时器
	class MyCountTimer extends CountDownTimer {
		  
        public MyCountTimer(long millisInFuture, long countDownInterval) {  
            super(millisInFuture, countDownInterval);  
        }  
		@Override
        public void onTick(long millisUntilFinished) {  
            btn_send.setText((millisUntilFinished / 1000) +"秒后重发");  
        }  
        @Override
        public void onFinish() {  
        	btn_send.setText("重新发送验证码");  
        }  
    }


	/**
	 *请求服务器发送验证码
	 */
	 private void requestSMSCode() {
		String PhoneNum = et_phone.getText().toString().trim();
		if (InputLeagalCheck.isPhoneNum(PhoneNum)) {
			timer = new MyCountTimer(60000, 1000);   
			timer.start();
			BmobSMS.requestSMSCode(PhoneNum,"SignUpIn", new QueryListener<Integer>(){

				@Override
				public void done(Integer smsId, BmobException ex) {
					if(ex==null){
						Toast.makeText(LoginOneKeyActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
					}else{
						timer.cancel();
						Toast.makeText(LoginOneKeyActivity.this, "验证码发送失败：code="+ex.getErrorCode()+"，错误描述："+ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
					}

				}
			});
		} else {
			Toast.makeText(this, "请输正确格式的入手机号", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 一键登录操作
	 * 
	 * @method oneKeyLogin
	 * @return void
	 * @exception
	 */
	private void oneKeyLogin() {
		final String phone = et_phone.getText().toString();
		final String code = et_code.getText().toString();

		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(code)) {
			Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		final ProgressDialog progress = new ProgressDialog(LoginOneKeyActivity.this);
		progress.setMessage("验证中...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9提供的一键注册或登录方式，可传手机号码和验证码
		BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<User>() {

			@Override
			public void done(User user, BmobException ex) {
				// TODO Auto-generated method stub
				progress.dismiss();
				if(ex==null){
					Toast.makeText(LoginOneKeyActivity.this, "登录成功",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginOneKeyActivity.this,MainActivity.class);
					intent.putExtra("from", "loginonekey");
					startActivity(intent);
					finish();
				}else{
					Toast.makeText(LoginOneKeyActivity.this,
							"登录失败：code="+ex.getErrorCode()+"，错误描述："+ex.getLocalizedMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(timer!=null){
			timer.cancel();
		}
	}
}
