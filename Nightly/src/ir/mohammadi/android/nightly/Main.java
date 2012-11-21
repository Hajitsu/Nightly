package ir.mohammadi.android.nightly;

import ir.mohammadi.android.nightly.sqlite.UserInfoDataSource;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends Activity {

	Button btn_newNote;
	Button btn_allNote;
	Button btn_sendEmail;
	Button btn_site;
	Button btn_about;

	String user_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);

		btn_newNote = (Button) findViewById(R.id.main_btn_newNotes);
		btn_allNote = (Button) findViewById(R.id.main_btn_allNotes);
		btn_sendEmail = (Button) findViewById(R.id.main_btn_sendEmail);
		btn_site = (Button) findViewById(R.id.main_btn_site);
		btn_about = (Button) findViewById(R.id.main_btn_about_app);
		Button btnExit = (Button) findViewById(R.id.main_btn_exit);

		btnExit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
						Main.this);
				myAlertDialog.setTitle("هشدار");
				myAlertDialog
						.setMessage("در صورت خارج شدن، باید دوباره لاگین کنید.");
				myAlertDialog.setPositiveButton("تایید",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								UserInfoDataSource dataSource = new UserInfoDataSource(
										Main.this);
								dataSource.open();

								if (dataSource.getUserInfo() != null)
									dataSource.deleteUserInfo();
								System.exit(0);
							}
						});
				myAlertDialog.setNegativeButton("نه",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								// do something when the Cancel button is
								// clicked
							}
						});
				myAlertDialog.show();

			}
		});

		Intent gettedIntent = getIntent();
		user_id = gettedIntent.getStringExtra("user_id");

		btn_newNote.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), NewNote.class);
				i.putExtra("user_id", user_id);
				startActivity(i);
			}
		});

		btn_allNote.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), AllNotes.class);
				i.putExtra("user_id", user_id);
				startActivity(i);
			}
		});

		btn_about.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(Main.this)
						.setTitle("درباره برنامه")
						.setMessage(
								"این برنامه برای ثبت خاطرات شما توسعه داده شده است. شما هر لحظه که به اینترنت متصل هستید با ورود به حساب خود، می‌توانید به ثبت خاطرات خود بپردازید. در این برنامه سعی شده تا با کمترین حجم تبادل داده‌ها و بالاترین سرعت، ثبت خاطرات را برای شما لذت بخش کند.")
						.setPositiveButton("تایید ;-)",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		});

		btn_sendEmail.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						"hamid.mohammadi.ir@gmail.com");
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"درباره شبانه");
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

				startActivity(Intent.createChooser(emailIntent,
						"ایمیل خود را انتخاب کنید:"));
			}
		});

		btn_site.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://1mohammadi.ir"));
				startActivity(browserIntent);
			}
		});

		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fontsfolder/BYEKAN.TTF");

		btn_about.setTypeface(typeFace);
		btn_allNote.setTypeface(typeFace);
		btn_newNote.setTypeface(typeFace);
		btn_sendEmail.setTypeface(typeFace);
		btn_site.setTypeface(typeFace);
		btnExit.setTypeface(typeFace);

		String reshapePersianText = btn_about.getText().toString();
		btn_about.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = btn_allNote.getText().toString();
		btn_allNote.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = btn_newNote.getText().toString();
		btn_newNote.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = btn_sendEmail.getText().toString();
		btn_sendEmail.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = btn_site.getText().toString();
		btn_site.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

		reshapePersianText = btnExit.getText().toString();
		btnExit.setText(PersianReshape.reshape(reshapePersianText));
		reshapePersianText = null;

	}
}
