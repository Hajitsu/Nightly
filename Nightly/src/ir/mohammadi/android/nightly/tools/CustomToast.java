package ir.mohammadi.android.nightly.tools;

import ir.mohammadi.android.nightly.PersianReshape;
import ir.mohammadi.android.nightly.R;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast extends Toast {
	private Context context;
	private int imageId;
	private int messageId = 0;
	private int colorId = 0;
	private String message = null;

	public CustomToast(Context context, int imageId, int messageId, int colorId) {
		super(context);
		this.context = context;
		this.messageId = messageId;
		this.imageId = imageId;
		this.colorId = colorId;
	}

	public CustomToast(Context context, int imageId, String message, int colorId) {
		super(context);
		this.context = context;
		this.message = message;
		this.imageId = imageId;
		this.colorId = colorId;
	}

	public void show() {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = (View) inflater.inflate(R.layout.toast_layout, null);

		ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
		image.setImageResource(imageId);

		TextView text = (TextView) layout.findViewById(R.id.toast_text);

		AssetManager assetManager = context.getAssets();
		Typeface typeFace = Typeface.createFromAsset(assetManager,
				"fontsfolder/BYEKAN.TTF");
		text.setTypeface(typeFace);

		String reshapePersianText = (messageId == 0) ? message : context
				.getText(messageId).toString();
		text.setText(PersianReshape.reshape(reshapePersianText));
		text.setTextColor(context.getResources().getColor(colorId));

		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
}
