package com.exam.longtian.activity.sign;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/** 
 * 签收扫描
 * 
 * @author yxx
 *
 * @date 2017-12-1 下午2:39:41
 * 
 */
public class SignScanActivity extends BaseActivity {

	@ViewInject(R.id.sign_scan_billcode) EditText edtBillcode;
	@ViewInject(R.id.sign_scan_name) EditText edtName;
	@ViewInject(R.id.sign_scan_signer) EditText edtSinger;
	@ViewInject(R.id.sign_scan_remark) EditText edtRemark;

	@ViewInject(R.id.sign_scan_icon) ImageView imgIcon;

	private static final int TAKE_PICTURE = 0x000001;
	private static final int TAKE_FOLDER = 0x000002;

	private Bitmap mBitmap;
	private String billCode;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_sign_scan);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("签收");
		edtName.setText(MyApplication.mUser.getEmpName());
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {

			mBitmap = (Bitmap) data.getExtras().get("data");
			mBitmap = CommandTools.resetBitmap(mBitmap);
			imgIcon.setImageBitmap(mBitmap);//想图像显示在ImageView视图上，private ImageView img;
		} else if (requestCode == TAKE_FOLDER && resultCode == RESULT_OK) {

			Uri selectedImage = data.getData();
			String[] filePathColumns = {MediaStore.Images.Media.DATA};
			Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String imagePath = c.getString(columnIndex);
			mBitmap = BitmapFactory.decodeFile(imagePath);
			mBitmap = CommandTools.resetBitmap(mBitmap);
			c.close();

			imgIcon.setImageBitmap(mBitmap);
		}
	}

	/**
	 * 拍照
	 * @param v
	 */
	public void takePhoto(View v){

		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	/**
	 * 相册
	 * @param v
	 */
	public void album(View v){

		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, TAKE_FOLDER);
	}

	/**
	 * 清除图片
	 * @param v
	 */
	public void clearPhoto(View v){

		mBitmap = null;
		imgIcon.setImageBitmap(null);
	}

	public void uploadImg(View v){

		billCode = edtBillcode.getText().toString();
		if(TextUtils.isEmpty(billCode)){
			CommandTools.showToast("运单号不能为空");
			return;
		}


		if(mBitmap == null){
			CommandTools.showToast("请先拍照或选择照片");
			return;
		}

		final File file = CommandTools.compressImage(mBitmap);
		PresenterUtil.uploadImg(file, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				JSONObject jsonObjectData = (JSONObject) data;

				JSONObject jsonObject = new JSONObject();
				try {

					jsonObject.put("billCode", billCode);
					jsonObject.put("serverId", jsonObjectData.optInt("serviceId"));
					jsonObject.put("path", jsonObjectData.optString("path"));
					jsonObject.put("virtualPath", jsonObjectData.optString("virPath"));
					jsonObject.put("fileName", jsonObjectData.optString("picName"));
					jsonObject.put("fileSize", jsonObjectData.optInt("size"));

					jsonObject.put("imageType", "");
					jsonObject.put("lockEmpGcode", "");
					jsonObject.put("lockTime", "");
					jsonObject.put("unlockEmpGcode", "");
					jsonObject.put("uploadEmpGcode", "");
					jsonObject.put("uploadPath", "");
					jsonObject.put("uploadSiteGcode", "");
					jsonObject.put("uploadTime", "");

					PresenterUtil.waybillImage_add(SignScanActivity.this, jsonObject, new ObjectCallback() {

						@Override
						public void callback(boolean success, String message, String code, Object data) {
							// TODO Auto-generated method stub
							clearPhoto(null);
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
		});
	}

	/**
	 * 提交
	 * @param v
	 */
	public void commit(View v){

		billCode = edtBillcode.getText().toString();
		if(TextUtils.isEmpty(billCode)){
			CommandTools.showToast("运单号不能为空");
			return;
		}

		String singer = edtSinger.getText().toString();
		if(TextUtils.isEmpty(singer)){
			CommandTools.showToast("签收人不能为空");
			return;
		}

		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("billCode", billCode);
			jsonObject.put("dataSourcePcode", "P42");//数据来源 PDA固定值
			jsonObject.put("dispEmpGcode", MyApplication.mUser.getEmpGcode());
			jsonObject.put("marchineCode", CommandTools.getMIME(this));
			jsonObject.put("regEmpGcode", MyApplication.mUser.getEmpGcode());
			jsonObject.put("regSiteGcode", MyApplication.mUser.getOwnSiteGcode());
			jsonObject.put("remark", edtRemark.getText().toString());
			jsonObject.put("signSiteGcode", MyApplication.mUser.getOwnSiteGcode());
			jsonObject.put("signSiteName", MyApplication.mUser.getEmpName());
			jsonObject.put("signTime", CommandTools.getTime());
			jsonObject.put("signer", singer);
			jsonObject.put("uploadTime", CommandTools.getTime());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		PresenterUtil.sign_add(this, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				CommandTools.showToast(message);
				if(success){
					edtBillcode.setText("");
					edtRemark.setText("");
				}
			}
		});
	}
}
