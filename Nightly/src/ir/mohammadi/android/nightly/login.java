package ir.mohammadi.android.nightly;

import ir.mohammadi.android.nightly.sqlite.UserInfo;
import ir.mohammadi.android.nightly.sqlite.UserInfoDataSource;
import ir.mohammadi.android.nightly.tools.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class login extends Activity {

	ProgressDialog pDialog;

	Button btnLogin;
	Button btnRegister;
	Button btnExit;

	EditText txtEmail;
	EditText txtPassword;

	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR_MSG = "error_message";

	private static String EXECUTE_RESULT = null;

	JSONObject jSon;

	UserInfoDataSource dataSource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		String reshapePersianText = null;

		txtEmail = (EditText) findViewById(R.id.login_txt_email);
		txtPassword = (EditText) findViewById(R.id.login_txt_password);

		btnLogin = (Button) findViewById(R.id.login_btn_login);
		btnRegister = (Button) findViewById(R.id.login_btn_register);
		btnExit = (Button) findViewById(R.id.login_btn_exit);

		btnExit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				UserInfoDataSource dataSource = new UserInfoDataSource(
						login.this);
				dataSource.open();

				if (dataSource.getUserInfo() != null)
					dataSource.deleteUserInfo();
				System.exit(0);
			}
		});

		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				if (android.util.Patterns.EMAIL_ADDRESS.matcher(
						txtEmail.getText().toString().trim()).matches()) {

					if (txtEmail.getText().toString().trim().length() != 0
							&& txtPassword.getText().toString().trim().length() != 0) {

						ConnectionDetector cd = new ConnectionDetector(
								getApplicationContext());
						if (cd.isConnectedToInternet()) {

							new LoginUser().execute();

						} else {
CustomToast toast = new CustomToast(
		getApplicationContext(),
		R.drawable.smiley_error,
		R.string.not_connected_to_internet,
		R.color.toast_attention);
toast.show();
						}
					} else {
						CustomToast toast = new CustomToast(
								getApplicationContext(),
								R.drawable.smiley_error,
								R.string.reg_fields_require,
								R.color.toast_attention);
						toast.show();
					}
				} else {
					CustomToast toast = new CustomToast(
							getApplicationContext(), R.drawable.smiley_error,
							R.string.reg_correct_email, R.color.toast_attention);
					toast.show();
				}
			}
		});

		btnRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), register.class);
				startActivity(i);
				finish();
			}
		});

		dataSource = new UserInfoDataSource(login.this);
		dataSource.open();

		UserInfo userInfo = dataSource.getUserInfo();

		dataSource.close();

		if (userInfo != null) {

			Intent i = new Intent(getApplicationContext(), Main.class);
			i.putExtra("user_id", userInfo.getUserId());
			startActivity(i);
			finish();
		}

		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fontsfolder/BYEKAN.TTF");

		TextView lbl_login = (TextView) findViewById(R.id.login_lbl_register);
		TextView lbl_email = (TextView) findViewById(R.id.login_lbl_email);
		TextView lbl_password = (TextView) findViewById(R.id.login_lbl_password);

		lbl_login.setTypeface(typeFace);
		lbl_email.setTypeface(typeFace);
		lbl_password.setTypeface(typeFace);

		btnLogin.setTypeface(typeFace);
		btnExit.setTypeface(typeFace);
		btnRegister.setTypeface(typeFace);

		reshapePersianText = lbl_login.getText().toString();
		lbl_login.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = lbl_email.getText().toString();
		lbl_email.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = lbl_password.getText().toString();
		lbl_password.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = btnLogin.getText().toString();
		btnLogin.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = btnRegister.getText().toString();
		btnRegister.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = btnExit.getText().toString();
		btnExit.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

	}

	class LoginUser extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(login.this);
			pDialog.setMessage("لطفا صبر کنید...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			String email = txtEmail.getText().toString();
			String password = txtPassword.getText().toString();

			UserFunctions userFunctions = new UserFunctions();

			jSon = userFunctions.loginUser(email, password);

			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			try {
				if (jSon.getString(KEY_SUCCESS) != null) {
					String result = jSon.getString(KEY_SUCCESS);
					if (Integer.parseInt(result) == 1) {

						CustomToast toast = new CustomToast(
								getApplicationContext(),
								R.drawable.smiley_register,
								R.string.welcome_to_nightly,
								R.color.toast_successful);
						toast.show();

						UserInfoDataSource dataSource = new UserInfoDataSource(
								login.this);
						dataSource.open();
						dataSource.createUserInfo(jSon.getString("user_id"),
								jSon.getString("user_email"),
								jSon.getString("user_password"));

						Intent i = new Intent(getApplicationContext(),
								Main.class);
						i.putExtra("user_id", jSon.getString("user_id"));
						startActivity(i);
						finish();
					}

				} else {
					EXECUTE_RESULT = jSon.getString(KEY_ERROR_MSG);

					CustomToast toast = new CustomToast(
							getApplicationContext(), R.drawable.smiley_error,
							EXECUTE_RESULT, R.color.toast_error);
					toast.show();

					Log.i("Login Error >> ", EXECUTE_RESULT);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}

	@Override
	protected void onStop() {
		dataSource.close();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dataSource.close();
	}
}
