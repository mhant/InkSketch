package com.androiddev101.inksketch;

import java.io.ByteArrayOutputStream;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


public class MainActivity extends ActionBarActivity {

	private TouchDraw mPad;
	private RatingBar mBrushRating;
	private ImageView mSketchArea;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPad = (TouchDraw) findViewById(R.id.tdPad);
		mBrushRating = (RatingBar) findViewById(R.id.rbBurshStroke);
		mBrushRating.setOnRatingBarChangeListener(new BrushSizeChangedListener());
		//init sketch area to 800x600 Bitmap with color yellow
		mSketchArea = (ImageView) findViewById(R.id.ivSketchArea);
		int w = 600, h = 800;
		Bitmap.Config conf = Bitmap.Config.RGB_565;
		Bitmap bmp = Bitmap.createBitmap(w, h, conf);
		bmp.eraseColor(Color.YELLOW);
		mSketchArea.setImageBitmap(bmp);
		mSketchArea.invalidate();
		//once redrawn resize drawing pad
		RelativeLayout padParent = (RelativeLayout) mPad.getParent();
		RelativeLayout.LayoutParams params = (LayoutParams) padParent.getLayoutParams();
		int id = mSketchArea.getId();
		params.addRule(RelativeLayout.ALIGN_LEFT, id);
		params.addRule(RelativeLayout.ALIGN_RIGHT, id);
		params.addRule(RelativeLayout.ALIGN_TOP, id);
		params.addRule(RelativeLayout.ALIGN_BOTTOM, id);
		padParent.setLayoutParams(params);
		padParent.invalidate();

	}

	public void onClickCompile(View view){
		Bitmap padBits = mPad.getBitmapFromView();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		padBits.compress(Bitmap.CompressFormat.PNG, 50, stream);
		byte[] byteArray = stream.toByteArray();
		Intent intent = new Intent(this, PreviewActivity.class);
		intent.putExtra(Constants.BUNDLE_KEY_BITMAP, byteArray);
		MainActivity.this.startActivity(intent);
	}

	public void onClickClear(View view){
		mPad.clear();
	}

	private class BrushSizeChangedListener implements OnRatingBarChangeListener{


		@Override
		public void onRatingChanged(RatingBar ratingBar, float progress, boolean fromUser) {
			if(fromUser){
				switch((int)progress){
				case 1:
					mPad.changeStroke(TouchDraw.PAINT_BRUSH_WIDTH_SMALL);
					break;
				case 2:
					mPad.changeStroke(TouchDraw.PAINT_BRUSH_WIDTH_MEDIUM);
					break;
				default:
					mPad.changeStroke(TouchDraw.PAINT_BRUSH_WIDTH_LARGE);
					break;
				}
				mPad.invalidate();
			}

		}

	}
}
