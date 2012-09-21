package vn.mbm.phimp.me;

import java.util.ArrayList;

import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.ViewSwitchListener;

import vn.mbm.phimp.me.image.CropImage;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class PhimpMeGallery extends Activity{
	private ViewFlow viewFlow;
	static Cursor cursor;
	private static int columnIndex;
	public static ArrayList<String> filepath = new ArrayList<String>();
	private String mImagePath="";
	public static String url;
	ProgressDialog pro_gress;
	private static ImageButton btnShareBT,btnUpload,btnEdit; 
	public static Bitmap bmp = null;
	public static Bitmap bmp1 = null;
	
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.phimpme_gallery_view);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		btnShareBT=(ImageButton)findViewById(R.id.btnShareBT);
		btnUpload=(ImageButton)findViewById(R.id.btnUpLoad);
		btnEdit=(ImageButton)findViewById(R.id.btnEdit);
		Intent intent=getIntent();
		Bundle extra=intent.getExtras();
		if(extra!=null){
			mImagePath = extra.getString("image-path");
		}
		
		

		/*
		 * get photo from memory
		 */
		String[] projection = { MediaStore.Images.Media.DATA };		
		cursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				projection, null, null, null);
		columnIndex = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		for (int i = 0; i < cursor.getCount(); i++) {
			if (cursor.moveToNext())
				filepath.add(cursor
						.getString(columnIndex));
		}
		if (filepath.size() > 0) {
			final ArrayList<String> array_file;
			if (filepath.size() <= 6) {
				array_file = new ArrayList<String>(
						filepath.size());
				for (int i = 0; i < filepath.size(); i++) {
					array_file.add(filepath.get(i));
				}

			} else {
				array_file = new ArrayList<String>(
						6);
				for (int i = 0; i < filepath.size(); i++) {
					array_file.add(filepath.get(i));
				}				
			}
			
			//set adapter for viewflow
			viewFlow.setAdapter(new ImageAdapter(PhimpMeGallery.this,array_file,mImagePath), 1);
			
		}
		url=mImagePath;
		btnShareBT.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(PhimpMeGallery.this, SendFileActivity.class);
				intent.putExtra("image-path", url);
				intent.putExtra("aspectX", 0);
				intent.putExtra("aspectY", 0);
				intent.putExtra("scale", true);
				intent.putExtra("activityName", "PhimpMeGallery");
				startActivityForResult(intent, 1);
				
			}
		});
		
		btnUpload.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				
				AlertDialog.Builder builder=new AlertDialog.Builder(PhimpMeGallery.this);
				builder.setMessage("This photo have been add to list upload photo !");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {		
						Upload.imagelist+=url+"#";
					}
				});
				builder.show();
			}
		});
		
		btnEdit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Log.i("PhimpMeGallery","url edit : "+url);				
				Intent intent = new Intent();
				intent.setClass(PhimpMeGallery.this, CropImage.class);
				intent.putExtra("image-path", url);			
				intent.putExtra("aspectX", 0);
				intent.putExtra("aspectY", 0);
				intent.putExtra("scale", true);
				intent.putExtra("activityName", "PhimpMeGallery");
				startActivityForResult(intent, 1);
			}
		});
		
		viewFlow.setOnViewSwitchListener(new ViewSwitchListener() {		
			public void onSwitched(View view, int position) {		
				if(position==1){
					url=mImagePath;
				}else{
					url= filepath.get(position);
				}				
				Log.i("PhimpMeGallery","test_switch : "+position);		
				Log.i("PhimpMeGallery","url : "+url+" at position :"+position);									
				
			}
		});
	}
	
	/*@Override
	public void onBackPressed(){		
		try{
			
			Log.i("PhimpMeGallery","onBackPressed");
			if(bmp!=null && !bmp.isRecycled()){
				bmp.recycle();
				Log.i("PhimpMeGallery","recycle bimap");
			}else{
				Log.i("PhimpMeGallery","bmp is recycled");
			}
				
			if(bmp1!=null && !bmp1.isRecycled()){
				bmp1.recycle();
				Log.i("PhimpMeGallery","recycle bimap1");
			}else{
				Log.i("PhimpMeGallery","bmp1 is recycled");
			}
		}catch(Exception e){	
			e.printStackTrace();
		}
		super.onBackPressed();
	}*/
}
