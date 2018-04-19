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
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**  
 *  帐号密码注册
 * @class  RegisterActivity  
 * @author DavidHuang
 * @date   2018-4-2 上午10:21:04
 *   
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
	

    private ImageView iv_left;
	private TextView tv_title;
	private EditText et_account;
	private EditText et_password;
	private EditText et_pwd_again;
	private Button btn_register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		tv_title=(TextView) findViewById(R.id.tv_title);
		et_account=(EditText) findViewById(R.id.et_account);
		et_password=(EditText) findViewById(R.id.et_password);
		et_pwd_again=(EditText) findViewById(R.id.et_pwd_again);
		btn_register=(Button) findViewById(R.id.btn_register);
		iv_left.setVisibility(View.VISIBLE);
		tv_title.setText("注册");
		iv_left.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}

	/**注册逻辑
	 */
	private void registerUser(){
		String account = et_account.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		String pwd = et_pwd_again.getText().toString().trim();
		if (TextUtils.isEmpty(account)) {
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!InputLeagalCheck.isPassword(password)) {//输入合法性检测
			Toast.makeText(this, "请输入6-16位字母或者数字，但不能是纯数字或纯密码", Toast.LENGTH_LONG).show();
			return;
		}
		if (!password.equals(pwd)) {
			Toast.makeText(this, "两次密码不一样", Toast.LENGTH_SHORT).show();
			return;
		}
		final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
		progress.setMessage("正在注册中...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		final User user = new User();
		user.setUsername(account);
		user.setPassword(password);
		user.signUp(new SaveListener<User>() {
			@Override
			public void done(User user, BmobException ex) {
				progress.dismiss();
				if(ex==null){
					Toast.makeText(RegisterActivity.this,
							"注册成功---用户名："+user.getUsername()+"，年龄："+user.getAge(),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
					intent.putExtra("from", "rigster");
					startActivity(intent);
					finish();
				}else{
					Toast.makeText(RegisterActivity.this,
							"注册失败: code="+ ex.getErrorCode()+" , 错误描述： "+ ex.getLocalizedMessage(),
							Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.iv_left:
				finish();
				break;
			case R.id.btn_register:
				registerUser();

				break;
			default:
		}
	}
}
