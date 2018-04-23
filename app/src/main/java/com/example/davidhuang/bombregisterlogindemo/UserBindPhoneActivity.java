package com.example.davidhuang.bombregisterlogindemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
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
 * 绑定手机号  
 * @class  UserBindPhoneActivity  
 * @author DavidHuang
 * @date   2018-4-6 下午18:08:53
 *   
 */
public class UserBindPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_left;
    EditText et_number;
    EditText et_input;
    TextView tv_title;
    TextView tv_send;
    TextView tv_bind;

	MyCountTimer timer;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		et_number=(EditText) findViewById(R.id.et_number);
		et_input=(EditText) findViewById(R.id.et_input);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_send=(TextView) findViewById(R.id.tv_send);
		tv_bind=(TextView) findViewById(R.id.tv_bind);
		iv_left.setVisibility(View.VISIBLE);
		tv_title.setText("绑定手机号");
		iv_left.setOnClickListener(this);
		tv_send.setOnClickListener(this);
		tv_bind.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.tv_send:
				requestSMSCode();
				break;
			case R.id.tv_bind:
				verifyCode();
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
			tv_send.setText((millisUntilFinished / 1000) +"秒后重发");  
        }  
        @Override
        public void onFinish() {  
        	tv_send.setText("重新发送验证码");  
        }  
    }

	/**
	 *请求验证码
	 */
	private void requestSMSCode() {
		String PhoneNum = et_number.getText().toString().trim();
		if (InputLeagalCheck.isPhoneNum(PhoneNum)) {
			timer = new MyCountTimer(60000, 1000);
			timer.start();
			BmobSMS.requestSMSCode(PhoneNum,"SignUpIn", new QueryListener<Integer>(){

				@Override
				public void done(Integer smsId, BmobException ex) {
					if(ex==null){
						Toast.makeText(UserBindPhoneActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(UserBindPhoneActivity.this,
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
	 *验证验证码
	 */
	private void verifyCode(){
		final String phone = et_number.getText().toString().trim();
		String code = et_input.getText().toString().trim();
		if (!InputLeagalCheck.isPhoneNum(phone )) {
			Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(code)) {
			Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("验证码校验中...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		BmobSMS.verifySmsCode(phone, code, new UpdateListener() {
			@Override
			public void done(BmobException ex) {
				progress.dismiss();
				if(ex==null){
					Toast.makeText(UserBindPhoneActivity.this, "手机号验证成功", Toast.LENGTH_SHORT).show();
					bindMobilePhone(phone);
				}else{
					Toast.makeText(UserBindPhoneActivity.this,
							"验证失败：code="+ex.getErrorCode()+"，错误描述："+ex.getLocalizedMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	/**
	 *绑定手机号
	 */
	private void bindMobilePhone(String phone){
		//绑定手机号时需提交两个字段的值：mobilePhoneNumber、mobilePhoneNumberVerified
		User user =new User();
		user.setMobilePhoneNumber(phone);
		user.setMobilePhoneNumberVerified(true);
		User currentUser = BmobUser.getCurrentUser(User.class);

		final ProgressDialog progress = new ProgressDialog(this);
		progress.setCanceledOnTouchOutside(false);
		progress.setMessage("手机号绑定中...");
		progress.show();
		user.update(currentUser.getObjectId(), new UpdateListener() {
			@Override
			public void done(BmobException ex) {
				progress.dismiss();
				if(ex==null){
					Toast.makeText(UserBindPhoneActivity.this,
							"手机号绑定成功", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(UserBindPhoneActivity.this,
							"手机号绑定失败，code="+ex.getErrorCode()+",错误原因："+ex.getLocalizedMessage(),
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
