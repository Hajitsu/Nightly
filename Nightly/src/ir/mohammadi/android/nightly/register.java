package ir.mohammadi.android.nightly;

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

public class register extends Activity {

	ProgressDialog pDialog;
	Button btnRegister;

	EditText txtEmail;
	EditText txtPassword;
	EditText txtName;

	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR_MSG = "error_message";

	private static String EXECUTE_RESULT = null;

	JSONObject jSon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		txtEmail = (EditText) findViewById(R.id.reg_txt_email);
		txtPassword = (EditText) findViewById(R.id.reg_txt_password);
		txtName = (EditText) findViewById(R.id.reg_txt_name);

		btnRegister = (Button) findViewById(R.id.reg_btn_register);

		btnRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				if (android.util.Patterns.EMAIL_ADDRESS.matcher(
						txtEmail.getText().toString().trim()).matches()) {

					if (txtEmail.getText().toString().trim().length() != 0
							&& txtPassword.getText().toString().trim().length() != 0
							&& txtName.getText().toString().trim().length() != 0) {

						ConnectionDetector cd = new ConnectionDetector(
								getApplicationContext());
						if (cd.isConnectedToInternet()) {

							new RegisterUser().execute();

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

		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fontsfolder/BYEKAN.TTF");

		TextView lbl_register = (TextView) findViewById(R.id.reg_lbl_register);
		TextView lbl_email = (TextView) findViewById(R.id.reg_lbl_email);
		TextView lbl_password = (TextView) findViewById(R.id.reg_lbl_password);
		TextView lbl_name = (TextView) findViewById(R.id.reg_lbl_name);

		lbl_register.setTypeface(typeFace);
		lbl_email.setTypeface(typeFace);
		lbl_password.setTypeface(typeFace);
		lbl_name.setTypeface(typeFace);
		btnRegister.setTypeface(typeFace);

		String reshapePersianText = lbl_register.getText().toString();
		lbl_register.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = lbl_email.getText().toString();
		lbl_email.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = lbl_password.getText().toString();
		lbl_password.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = lbl_name.getText().toString();
		lbl_name.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = btnRegister.getText().toString();
		btnRegister.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

	}

	class RegisterUser extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(register.this);
			pDialog.setMessage("لطفا صبر کنید...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			String email = txtEmail.getText().toString();
			String password = txtPassword.getText().toString();
			String name = txtName.getText().toString();

			UserFunctions userFunctions = new UserFunctions();

			jSon = userFunctions.registerUser(email, password, name);

			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();

			try {
				if (jSon.has(KEY_SUCCESS)) {
					if (jSon.getString(KEY_SUCCESS).equals("1")) {

						UserInfoDataSource dataSource = new UserInfoDataSource(
								register.this);
						dataSource.open();
						dataSource.createUserInfo(jSon.getString("user_id"),
								jSon.getString("user_email"),
								jSon.getString("user_password"));

						EXECUTE_RESULT = "به شبانه خوش آمدید ;-)";

						CustomToast toast = new CustomToast(
								getApplicationContext(),
								R.drawable.smiley_register,
								R.string.welcome_to_nightly,
								R.color.toast_successful);
						toast.show();

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

					Log.i("Register Error >> ", EXECUTE_RESULT);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
