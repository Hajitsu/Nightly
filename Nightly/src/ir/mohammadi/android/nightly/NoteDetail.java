package ir.mohammadi.android.nightly;

import ir.mohammadi.android.nightly.tools.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDetail extends Activity {

	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_message";

	private ProgressDialog pDialog;

	private EditText txt_note_content;

	private String Key_NOTE_ID = "note_id";

	String noteId;
	String userId;

	JSONObject jObj;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);
		this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);

		txt_note_content = (EditText) findViewById(R.id.note_txt_note);

		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fontsfolder/BYEKAN.TTF");

		txt_note_content.setTypeface(typeFace);

		Intent i = getIntent();
		userId = i.getStringExtra("user_id");
		noteId = i.getStringExtra(Key_NOTE_ID);
		Log.i("Note Id is >>", noteId.toString());

		i.removeExtra(Key_NOTE_ID);

		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		if (cd.isConnectedToInternet()) {

			new GetNoteDetail().execute();

		} else {

			CustomToast toast = new CustomToast(getApplicationContext(),
					R.drawable.smiley_error,
					R.string.not_connected_to_internet, R.color.toast_attention);
			toast.show();
		}
	}

	class DeleteNote extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NoteDetail.this);
			pDialog.setMessage("در حال حذف شبانه...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			UserFunctions userFunctions = new UserFunctions();
			jObj = userFunctions.deleteNote(noteId);
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			try {
				if (jObj.has(KEY_SUCCESS)) {

					CustomToast toast = new CustomToast(
							getApplicationContext(), R.drawable.smiley_success,
							R.string.note_delete_successfuly,
							R.color.toast_successful);
					toast.show();

					Intent i = new Intent();
					setResult(100, i);
					NoteDetail.this.finish();

				} else if (jObj.has(KEY_ERROR)) {

					CustomToast toast = new CustomToast(
							getApplicationContext(), R.drawable.smiley_error,
							jObj.getString(KEY_ERROR_MSG), R.color.toast_error);
					toast.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class UpdateNote extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NoteDetail.this);
			pDialog.setMessage("در حال ویرایش شبانه...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String note_content = txt_note_content.getText().toString();
			UserFunctions userFunctions = new UserFunctions();
			jObj = userFunctions.updateNote(noteId, note_content);
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			try {
				if (jObj.has(KEY_SUCCESS)) {

					CustomToast toast = new CustomToast(
							getApplicationContext(), R.drawable.smiley_success,
							R.string.note_update_note_successfuly,
							R.color.toast_successful);
					toast.show();

				} else if (jObj.has(KEY_ERROR)) {

					CustomToast toast = new CustomToast(
							getApplicationContext(), R.drawable.smiley_error,
							jObj.getString(KEY_ERROR_MSG), R.color.toast_error);
					toast.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	class GetNoteDetail extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NoteDetail.this);
			pDialog.setMessage("در حال دریافت شبانه...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			UserFunctions userFunctions = new UserFunctions();
			jObj = userFunctions.GetNoteDetail(noteId);
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			try {
				if (jObj.has(KEY_SUCCESS)) {
					if (jObj.getString(KEY_SUCCESS).equals("1")) {
						txt_note_content.append(jObj.getString("note_content"));
					}
				} else if (jObj.has(KEY_ERROR)) {

					CustomToast toast = new CustomToast(
							getApplicationContext(), R.drawable.smiley_error,
							jObj.getString(KEY_ERROR_MSG), R.color.toast_error);
					toast.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.note_detail_menu, menu);
		getLayoutInflater().setFactory(new LayoutInflater.Factory() {

			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try {
						LayoutInflater li = LayoutInflater.from(context);
						final View view = li.createView(name, null, attrs);
						new Handler().post(new Runnable() {

							public void run() {
								// view.setBackgroundResource(R.drawable.save);
								Typeface typeFace = Typeface.createFromAsset(
										getAssets(), "fontsfolder/BYEKAN.TTF");
								((TextView) view).setTypeface(typeFace);
							}
						});
						return view;
					} catch (InflateException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.note_detail_menu_update: {

			if (txt_note_content.getText().toString().length() > 15) {
				ConnectionDetector cd = new ConnectionDetector(
						getApplicationContext());
				if (cd.isConnectedToInternet()) {

					new UpdateNote().execute();

				} else {

					CustomToast toast = new CustomToast(
							getApplicationContext(), R.drawable.smiley_error,
							R.string.not_connected_to_internet,
							R.color.toast_attention);
					toast.show();
				}
			} else {
				Toast.makeText(NoteDetail.this, R.string.note_length_short,
						Toast.LENGTH_SHORT).show();

				CustomToast toast = new CustomToast(getApplicationContext(),
						R.drawable.smiley_error, R.string.note_length_short,
						R.color.toast_attention);
				toast.show();
			}

		}
			break;
		case R.id.note_detail_menu_delete: {

			ConnectionDetector cd = new ConnectionDetector(
					getApplicationContext());
			if (cd.isConnectedToInternet()) {

				new DeleteNote().execute();

			} else {

				CustomToast toast = new CustomToast(getApplicationContext(),
						R.drawable.smiley_error,
						R.string.not_connected_to_internet,
						R.color.toast_attention);
				toast.show();
			}

		}
			break;
		}
		return (super.onOptionsItemSelected(item));
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (getIntent().hasExtra(Key_NOTE_ID))
			getIntent().removeExtra(Key_NOTE_ID);
	}
}
