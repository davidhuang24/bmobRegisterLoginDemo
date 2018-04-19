package com.example.davidhuang.bombregisterlogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 帐号密码登录
 * @class  LoginActivity  
 * @author DavidHuang
 *   
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


	private static final String BMOB_APP_KEY="99e9247ef6c06c20c16b4c5d953342bd";

    ImageView iv_left;
    EditText et_account;
    EditText et_password;
    Button btn_login;
    Button btn_onekey;
    Button btn_register;
    Button btn_forget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//初始化Bmob sdk
		Bmob.initialize(this, BMOB_APP_KEY);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		et_account=(EditText) findViewById(R.id.et_account);
		et_password=(EditText) findViewById(R.id.et_password);
		btn_login=(Button) findViewById(R.id.btn_login);
		btn_onekey=(Button) findViewById(R.id.btn_onekey);
		btn_register=(Button) findViewById(R.id.btn_register);
		btn_forget=(Button) findViewById(R.id.btn_forget);
		iv_left.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		btn_onekey.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		btn_forget.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.iv_left:
				finish();
				break;
			case R.id.btn_login:
				login();
				break;
			case R.id.btn_register:
				Intent intent1 = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent1);
				break;
			case R.id.btn_onekey:
				Intent intent2 = new Intent(LoginActivity.this,LoginOneKeyActivity.class);
				startActivity(intent2);
				break;
			case R.id.btn_forget:
				Intent intent3 = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
				startActivity(intent3);
				break;
			default:
		}
	}

	/** 登陆操作 
	 * @method login    
	 * @return void  
	 * @exception   
	 */
	private void login(){
		final String account = et_account.getText().toString();
		String password = et_password.getText().toString();
		if (TextUtils.isEmpty(account)) {//输入合法性检测,简单处理
			Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
		progress.setMessage("正在登录中...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		//V3.3.9提供的新的登录方式，可传用户名/邮箱/手机号码
		BmobUser.loginByAccount(account, password, new LogInListener<User>() {

			@Override
			public void done(User user, BmobException ex) {
				// TODO Auto-generated method stub
				progress.dismiss();
				User currentUser = BmobUser.getCurrentUser(User.class);
				if(ex==null){//登陆成功
					if(account.equals(currentUser.getEmail())&&currentUser.getEmailVerified()==false){//解决“邮箱未验证却可以登入”的问题
						clearEmailOfUser(currentUser.getObjectId());
						BmobUser.logOut();
						Toast.makeText(LoginActivity.this, "您的邮箱未验证，请重新绑定邮箱", Toast.LENGTH_SHORT).show();
						return;
					}
					if(account.equals(currentUser.getMobilePhoneNumber())&&currentUser.getMobilePhoneNumberVerified()==false){//解决“手机号未验证却可以登入”的问题
						clearPhoneNumberOfUser(currentUser.getObjectId());
						BmobUser.logOut();
						Toast.makeText(LoginActivity.this, "您的手机号未验证，请重新绑定手机号", Toast.LENGTH_SHORT).show();
						return;
					}
					Toast.makeText(LoginActivity.this,
							"登录成功---用户名："+user.getUsername()+"，年龄："+user.getAge(),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					intent.putExtra("from", "login");
					startActivity(intent);
					finish();
				}else{
					Toast.makeText(LoginActivity.this,
							"登录失败：code="+ex.getErrorCode()+"，错误描述："+ex.getLocalizedMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 *清除指定记录的email字段
	 */
	void clearEmailOfUser(String userId){
		User user=new User();
		user.setObjectId(userId);
		user.remove("email");
		user.update(new UpdateListener() {
			@Override
			public void done(BmobException ex) {
				if(ex!=null){
					Toast.makeText(LoginActivity.this, "清除当前用户的email失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	/**
	 *清除指定记录的mobilePhoneNumber字段
	 */
	void clearPhoneNumberOfUser(String userId){
		User user=new User();
		user.setObjectId(userId);
		user.remove("mobilePhoneNumber");
		user.update(new UpdateListener() {
			@Override
			public void done(BmobException ex) {
				if(ex!=null){
					Toast.makeText(LoginActivity.this, "清除当前用户的手机号失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}




}
