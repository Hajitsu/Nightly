package ir.mohammadi.android.nightly;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

public class UserFunctions {

	private JSONParser jsonParser;

	private static final String apisURL = "http://android.1mohammadi.ir/nightly/";

	private static final String login_tag = "login";
	private static final String register_tag = "register";
	private static final String getAllNotes_tag = "getNotesList";
	private static final String saveNote_tag = "savenote";
	private static final String updateNote_tag = "updateNotes";
	private static final String getNoteDetail_tag = "getNoteDetail";
	private static final String deleteNote_tag = "deleteNotes";

	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	public JSONObject registerUser(String email, String password, String name) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("user_email", email));
		params.add(new BasicNameValuePair("user_password", password));
		params.add(new BasicNameValuePair("user_name", name));

		Log.i("Register params before getting from net >>", params.toString());

		JSONObject jsonObject = jsonParser.getJSONFromUrl(apisURL, params);

		Log.i("Register params after getting from net >>",
				jsonObject.toString());

		return jsonObject;
	}

	public JSONObject loginUser(String email, String password) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("user_email", email));
		params.add(new BasicNameValuePair("user_password", password));

		Log.i("Login params before getting from net >>", params.toString());

		JSONObject jsonObject = jsonParser.getJSONFromUrl(apisURL, params);

		Log.i("Login params after getting from net >>", jsonObject.toString());

		return jsonObject;
	}

	public JSONObject getAllNotes(String id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getAllNotes_tag));
		params.add(new BasicNameValuePair("user_id", id));

		Log.i("getAllNotes params before getting from net >>",
				params.toString());

		JSONObject jsonObject = jsonParser.getJSONFromUrl(apisURL, params);

		Log.i("getAllNotes params after getting from net >>",
				jsonObject.toString());

		return jsonObject;
	}

	public JSONObject saveNote(String user_id, String note_content) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", saveNote_tag));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("note_content", note_content));

		Log.i("saveNote params before getting from net >>", params.toString());

		JSONObject jsonObject = jsonParser.getJSONFromUrl(apisURL, params);

		Log.i("saveNote params after getting from net >>",
				jsonObject.toString());

		return jsonObject;
	}

	public JSONObject updateNote(String note_id, String note_content) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", updateNote_tag));
		params.add(new BasicNameValuePair("note_id", note_id));
		params.add(new BasicNameValuePair("note_content", note_content));

		Log.i("updateNote params before getting from net >>", params.toString());

		JSONObject jsonObject = jsonParser.getJSONFromUrl(apisURL, params);

		Log.i("updateNote params after getting from net >>",
				jsonObject.toString());

		return jsonObject;
	}
	
	public JSONObject deleteNote(String note_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", deleteNote_tag));
		params.add(new BasicNameValuePair("note_id", note_id));

		Log.i("deleteNote params before getting from net >>", params.toString());

		JSONObject jsonObject = jsonParser.getJSONFromUrl(apisURL, params);

		Log.i("deleteNote params after getting from net >>",
				jsonObject.toString());

		return jsonObject;
	}

	public JSONObject GetNoteDetail(String note_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getNoteDetail_tag));
		params.add(new BasicNameValuePair("note_id", note_id));

		Log.i("GetNoteDetail params before getting from net >>",
				params.toString());

		JSONObject jsonObject = jsonParser.getJSONFromUrl(apisURL, params);

		Log.i("GetNoteDetail params after getting from net >>",
				jsonObject.toString());

		return jsonObject;
	}
}
