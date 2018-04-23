package com.example.davidhuang.bombregisterlogindemo;

import android.app.ProgressDialog;
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
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 用手机号重置密码
 * @class ResetPasswordActivity
 * @author DavidHuang
 * @date 2018-4-5 19:03:44
 * 
 */
public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

	private static final String TAG = "ResetPasswordActivity";

	MyCountTimer timer;

    ImageView iv_left;
    TextView tv_title;
    EditText et_phone;
    EditText et_code;
    Button btn_send;
    EditText et_pwd;
    Button btn_reset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pwd);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		tv_title=(TextView) findViewById(R.id.tv_title);
		et_phone=(EditText) findViewById(R.id.et_phonenum);
		et_code=(EditText) findViewById(R.id.et_code);
		et_pwd=(EditText) findViewById(R.id.et_pwd);
		btn_send=(Button) findViewById(R.id.btn_send_code);
		btn_reset=(Button) findViewById(R.id.btn_reset_pwd);
		iv_left.setVisibility(View.VISIBLE);
		tv_title.setText("重置密码");
		iv_left.setOnClickListener(this);
		btn_reset.setOnClickListener(this);
		btn_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.btn_send_code:
				requestSMSCode();
				break;
			case R.id.btn_reset_pwd:
				resetPasswordByPhoneNum();
				break;
			default:
		}
	}

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
	 *请求验证码
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
						Toast.makeText(ResetPasswordActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(ResetPasswordActivity.this,
								"验证码发送失败：code="+ex.getErrorCode()+"，错误描述："+ex.getLocalizedMessage(),
								Toast.LENGTH_SHORT).show();
						timer.cancel();
					}

				}
			});
		} else {
			Toast.makeText(this, "请输入正确格式的手机号", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 重置密码
	 */
	private void resetPasswordByPhoneNum() {
		final String code = et_code.getText().toString().trim();
		final String pwd = et_pwd.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!InputLeagalCheck.isPassword(pwd)) {
			Toast.makeText(this, "请输入6-16位字母或者数字，但不能是纯数字或纯密码", Toast.LENGTH_LONG).show();
			return;
		}
		final ProgressDialog progress = new ProgressDialog(ResetPasswordActivity.this);
		progress.setMessage("正在重置密码...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		BmobUser.resetPasswordBySMSCode(code, pwd, new UpdateListener() {
			@Override
			public void done(BmobException ex) {
				progress.dismiss();
				if(ex==null){
					Toast.makeText(ResetPasswordActivity.this,
							"密码重置成功", Toast.LENGTH_SHORT).show();
					finish();
				}else {
					Toast.makeText(ResetPasswordActivity.this,
							"密码重置失败：code="+ex.getErrorCode()+"，错误描述："+ex.getLocalizedMessage(),
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
