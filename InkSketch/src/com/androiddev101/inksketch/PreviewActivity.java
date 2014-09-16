package com.androiddev101.inksketch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gajah.inkcaseLib.InkCase;
import com.gajah.inkcaseLib.InkCaseCompanionException;
import com.gajah.inkcaseLib.InkCaseUtils;

public class PreviewActivity extends Activity {


	private ImageView mPreview;
	private Bitmap mPadBits;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.activity_image_preview);
		mPreview = (ImageView)findViewById(R.id.ivRender);
		((Button)findViewById(R.id.btnCancel)).setOnClickListener(new OnClickCancel());
		((Button)findViewById(R.id.btnSend)).setOnClickListener(new OnClickCSend());
		Bundle extras = getIntent().getExtras();
		byte[] byteArray = extras.getByteArray(Constants.BUNDLE_KEY_BITMAP);
		Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		setBits(bmp);
	}


	public void setBits(Bitmap padBits){
		mPadBits = padBits;
		mPreview.setImageBitmap(padBits);
		mPreview.invalidate();
	}

	/** 
	 * button onclick callbacks 
	 **/
	private class OnClickCancel implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			PreviewActivity.this.finish();
		}
	}

	private class OnClickCSend implements View.OnClickListener{

		@Override
		public void onClick(View view) {
			File fileToSend = new File(view.getContext().getExternalCacheDir(),
					"helloInkCase.jpg");
			try {
				FileOutputStream fOut = new FileOutputStream(fileToSend);

				mPadBits.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (fileToSend.exists()) {
				try {
					sendImageToInkCase(view, fileToSend);
					PreviewActivity.this.finish();
				} catch (Exception e) {
					Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private void sendImageToInkCase(View view, File fileToSend)
			throws InkCaseCompanionException {
		Intent sharingIntent = new Intent(
				InkCase.ACTION_SEND_TO_INKCASE);
		sharingIntent.setType("image/jpeg");
		sharingIntent.putExtra(InkCase.EXTRA_FUNCTION_CODE,
				InkCase.CODE_SEND_WALLPAPER);
		sharingIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.fromFile(fileToSend));
		sharingIntent.putExtra(InkCase.EXTRA_APP_NAME, InkCase.APP_NOW);
		sharingIntent.putExtra(InkCase.EXTRA_FILENAME,
				fileToSend.getName());
		InkCaseUtils.startInkCaseActivity(view.getContext(), sharingIntent);
	}

}
