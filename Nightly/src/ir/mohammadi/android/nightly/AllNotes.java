package ir.mohammadi.android.nightly;

import ir.mohammadi.android.nightly.tools.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AllNotes extends ListActivity {

	Button btn_refresh_list;
	ProgressDialog pDialog;

	ArrayList<HashMap<String, String>> noteList;

	JSONObject notes = null;

	JSONObject jSon = null;

	private static String KEY_SUCCESS = "success";
	private static String KEY_NOTE_ID = "note_id";
	private static String KEY_NOTE_SUBJECT = "note_subject";
	private static String KEY_NOTE_DATE = "note_date";
	private static String userId;

	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_list);
		this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);

		btn_refresh_list = (Button) findViewById(R.id.allNotes_btn_refresh_list);

		btn_refresh_list.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				lv.setAdapter(null);

				ConnectionDetector cd = new ConnectionDetector(
						getApplicationContext());
				if (cd.isConnectedToInternet()) {

					new LoadAllNotes().execute();

				} else {
					CustomToast toast = new CustomToast(
							getApplicationContext(), R.drawable.smiley_error,
							R.string.not_connected_to_internet,
							R.color.toast_attention);
					toast.show();
				}
			}
		});

		Intent gettedIntent = getIntent();
		userId = gettedIntent.getStringExtra("user_id");

		noteList = new ArrayList<HashMap<String, String>>();

		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		if (cd.isConnectedToInternet()) {

			new LoadAllNotes().execute();

		} else {
			CustomToast toast = new CustomToast(getApplicationContext(),
					R.drawable.smiley_error,
					R.string.not_connected_to_internet, R.color.toast_attention);
			toast.show();
		}

		lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String note_id = ((TextView) view
						.findViewById(R.id.list_lbl_id)).getText().toString();
				Intent i = new Intent(getApplicationContext(), NoteDetail.class);
				i.putExtra("note_id", note_id);
				startActivityForResult(i, 100);
			}
		});

		Typeface typeFace = Typeface.createFromAsset(getAssets(),
				"fontsfolder/BYEKAN.TTF");
		btn_refresh_list.setTypeface(typeFace);

	}

	public class myAdapter extends SimpleAdapter {
		public myAdapter(Context context, List<HashMap<String, String>> items,
				int resource, String[] from, int[] to) {
			super(context, items, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);

			Typeface typeFace = Typeface.createFromAsset(getAssets(),
					"fontsfolder/BYEKAN.TTF");

			TextView lbl_subject = ((TextView) view
					.findViewById(R.id.list_lbl_subject));
			TextView lbl_date = ((TextView) view
					.findViewById(R.id.list_lbl_date));

			lbl_date.setTypeface(typeFace);
			lbl_subject.setTypeface(typeFace);

			String reshapePersianText = lbl_date.getText().toString();
			lbl_date.setText(PersianReshape.reshape(reshapePersianText));
			reshapePersianText = null;

			reshapePersianText = lbl_subject.getText().toString();
			lbl_subject.setText(PersianReshape.reshape(reshapePersianText));
			reshapePersianText = null;

			return view;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			Intent intent = getIntent();
			userId = intent.getStringExtra("user_id");
			finish();
			startActivity(intent);
		}
	}

	public class LoadAllNotes extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllNotes.this);
			pDialog.setMessage("لطفا صبر کنید...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

			noteList.clear();
		}

		@Override
		protected String doInBackground(String... args) {

			UserFunctions userFunctions = new UserFunctions();

			jSon = userFunctions.getAllNotes(userId);

			Log.i("AllNotes >> jSon >>", jSon.toString());

			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			try {
				if (jSon.has(KEY_SUCCESS)) {
					String success = jSon.getString(KEY_SUCCESS);
					if (success.equals("1")) {

						notes = jSon.getJSONObject("notes");
						for (int i = 0; i < notes.length(); i++) {
							JSONObject c = notes.getJSONObject(Integer
									.toString(i));

							Log.i("JSONObject c >>", c.toString());

							String id = c.getString(KEY_NOTE_ID);
							String subject = c.getString(KEY_NOTE_SUBJECT);
							String date = c.getString(KEY_NOTE_DATE);

							HashMap<String, String> map = new HashMap<String, String>();
							map.put(KEY_NOTE_ID, id);
							map.put(KEY_NOTE_SUBJECT, subject);
							map.put(KEY_NOTE_DATE, date);

							noteList.add(map);
						}

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			runOnUiThread(new Runnable() {

				public void run() {
					myAdapter adapter = new myAdapter(AllNotes.this, noteList,
							R.layout.list_item, new String[] { KEY_NOTE_ID,
									KEY_NOTE_SUBJECT, KEY_NOTE_DATE },
							new int[] { R.id.list_lbl_id,
									R.id.list_lbl_subject, R.id.list_lbl_date });
					setListAdapter(adapter);
				}
			});
		}
	}
}
