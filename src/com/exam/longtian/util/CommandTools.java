package com.exam.longtian.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.exam.longtian.MyApplication;


/** 
 * 工具类
 *
 * @date 2016-5-20 下午2:04:19
 * 
 */
public class CommandTools {

	private static Dialog singleDialog = null;
	private static TelephonyManager telephonemanage;
	private static Toast toast;

	// 弹窗结果回调函数
	public static abstract class CommandToolsCallback {
		public abstract void callback(int position);
	}

	public static String getTimes() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date).toString();
	}

	/**
	 * 将bitmap转换成base64字符
	 * 
	 * @param bitmap
	 * @return base64 字符
	 */
	public static String bitmap2String(Bitmap bitmap, int bitmapQuality) {

		String string = null;
		try {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, bitmapQuality, bStream);
			byte[] bytes = bStream.toByteArray();
			string = Base64.encodeToString(bytes, Base64.NO_WRAP);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return string;
	}

	public static String bitmapToBase64(Bitmap bitmap) {  

		String result = null;  
		ByteArrayOutputStream baos = null;  
		try {  
			if (bitmap != null) {  
				baos = new ByteArrayOutputStream();  
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  

				baos.flush();  
				baos.close();  

				byte[] bitmapBytes = baos.toByteArray();  
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);  
			}  
		} catch (IOException e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				if (baos != null) {  
					baos.flush();  
					baos.close();  
				}  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return result;  
	}  

	/**
	 * 获取PDA SN
	 * 
	 * @param context
	 * @return
	 */
	public static String getMIME(Context context) {
		telephonemanage = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			String sn = telephonemanage.getDeviceId();
			return sn;
		} catch (Exception e) {
			Logs.e("MIME", e.getMessage());
			return "00000000000";
		}
	}

	/**
	 * 弹出toast提示 防止覆盖
	 * 
	 * @param msg
	 */
	public static void showToast(String msg) {

		if (toast == null) {
			toast = Toast.makeText(MyApplication.getInstance(), msg + "", Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	/**
	 * 强制关闭软键盘
	 */
	public static void hidenKeyboars(Context context, View view) {

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getTime() {

		Date nowdate = new Date(); // 当前时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(nowdate);
	}

	/**
	 * 获取当前上下文
	 * @return
	 */
	public static Activity getGlobalActivity(){

		Class<?> activityThreadClass;
		try {
			activityThreadClass = Class.forName("android.app.ActivityThread");
			Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
			Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
			activitiesField.setAccessible(true);
			Map<?, ?> activities = (Map<?, ?>) activitiesField.get(activityThread);
			for(Object activityRecord:activities.values()){
				Class<? extends Object> activityRecordClass = activityRecord.getClass();
				Field pausedField = activityRecordClass.getDeclaredField("paused");
				pausedField.setAccessible(true);
				if(!pausedField.getBoolean(activityRecord)) {
					Field activityField = activityRecordClass.getDeclaredField("activity");
					activityField.setAccessible(true);
					Activity activity = (Activity) activityField.get(activityRecord);
					return activity;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据手机的分辨率�? dp 的单�? 转成�? px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率�? px(像素) 的单�? 转成�? dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	// 获取版本号
	public static String getVersionName() {

		try {
			PackageManager pm = MyApplication.getInstance().getPackageManager();
			String versionName = "";
			try {
				PackageInfo packageInfo = pm.getPackageInfo(
						MyApplication.getInstance().getPackageName(), 0);
				versionName = packageInfo.versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			return versionName;
		} catch (Exception e) {
			return "1.0.0";
			// TODO: handle exception
		}

	}

	/**
	 * 获取版本
	 * @param context
	 * @return
	 */
	public static int getVersionCode() {

		Context context = MyApplication.getInstance();
		int versionCode = 1;
		try {
			PackageManager pm = context.getPackageManager();

			try {
				PackageInfo packageInfo = pm.getPackageInfo(
						context.getPackageName(), 0);
				versionCode = packageInfo.versionCode;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			return versionCode;
		} catch (Exception e) {
			return versionCode;
		}

	}

	/**
	 * 弹出信息，需要手动关
	 * 
	 * @param context
	 * @param strMsg
	 */
	public static void showDialog(final Context context, final String strMsg) {

		Activity mActivity = (Activity) context;
		if (mActivity.isFinishing()) {
			Logs.v("zd", "当前activity界面已关闭，不能显示对话?");
			return;
		}

		if(singleDialog != null){
			singleDialog.dismiss();
			singleDialog = null;
		}

		singleDialog = new AlertDialog.Builder(context)
		.setTitle("提示")
		.setMessage(strMsg + "")
		.setPositiveButton("关闭", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
			}
		}).show();

	}

	/**
	 * 弹出信息，需要手动关
	 * 
	 * @param context
	 * @param strMsg
	 */
	public static void showHotFixDialog(final Context context, String btn, final String strMsg, final CommandToolsCallback callback) {

		Activity mActivity = (Activity) context;
		if (mActivity == null || mActivity.isFinishing()) {
			Logs.v("zd", "当前activity界面已关闭，不能显示对话?");
			return;
		}

		if(singleDialog != null){
			singleDialog.dismiss();
			singleDialog = null;
		}

		singleDialog = new AlertDialog.Builder(context)
		.setTitle("提示")
		.setMessage(strMsg + "")
		.setCancelable(false)
		.setPositiveButton(btn, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
				callback.callback(0);
			}
		}).show();

	}


	/**
	 * 弹出确认取消框
	 * @param context
	 * @param strMsg
	 */
	static Builder dialog;
	public static void showChooseDialog(final Context context, final String strMsg, final CommandToolsCallback callback) {

		Activity mActivity = (Activity) context;
		if (mActivity.isFinishing()) {
			return;
		}

		dialog = new AlertDialog.Builder(context);

		dialog.setTitle("提示");
		dialog.setMessage(strMsg + "");
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialoginterface, int i){

				callback.callback(0);
			}
		});

		dialog.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				callback.callback(-1);
			}
		});

		dialog.setCancelable(false);//对话框外部不可点击

		dialog.show();
	}

	/**
	 * 获取主键 UUID
	 * @return
	 */
	public static String getUUID(){

		return java.util.UUID.randomUUID().toString();
	}

	/** 
	 * @param path 图片路径 
	 * @param targetSize 缩放后期待的长边(图片长和宽大的那一个边)的长度 
	 * @param targetW 期待的缩放后宽度 
	 * @param targetH 期待的缩放后高度 
	 * @return 
	 */  
	public static Bitmap resetBitmap(Bitmap bitMap){  

		int width = bitMap.getWidth();
		int height = bitMap.getHeight();

		// 设置想要的大小 
		int newWidth = width * 800 / height;
		int newHeight = 800;

		if(width > height){

			newWidth = 800;
			newHeight = height * 800 / width;
		}

		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		// 得到新的图片
		bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);

		return bitMap;
	}  

	/** 
	 * 压缩图片（质量压缩） 
	 * @param bitmap 
	 */  
	public static File compressImage(Bitmap bitmap) {  

		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
		int options = 100;  
		while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩  
			baos.reset();//重置baos即清空baos  
			options -= 10;//每次都减少10  
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
			long length = baos.toByteArray().length;  
		}  
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");  
		Date date = new Date(System.currentTimeMillis());  
		String filename = format.format(date);  
		File file = new File(Environment.getExternalStorageDirectory(),filename+".png");  
		try {  
			FileOutputStream fos = new FileOutputStream(file);  
			try {  
				fos.write(baos.toByteArray());  
				fos.flush();  
				fos.close();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		}  
		//		recycleBitmap(bitmap);  
		return file;  
	}  

	public static void recycleBitmap(Bitmap... bitmaps) {  

		if (bitmaps==null) {  
			return;  
		}  
		for (Bitmap bm : bitmaps) {  
			if (null != bm && !bm.isRecycled()) {  
				bm.recycle();  
			}  
		}  
	}  

	/**
	 * 验证手机是否合法
	 * 
	 * @param mobiles
	 *            传入的手机号
	 * @return true 合法 false 不合法
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][34587]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	/**
	 * 对象转byte
	 * @param obj
	 * @return
	 */
	public static byte[] ObjectToByte(Object obj) {  

		byte[] bytes = null;  
		try {  
			// object to bytearray  
			ByteArrayOutputStream bo = new ByteArrayOutputStream();  
			ObjectOutputStream oo = new ObjectOutputStream(bo);  
			oo.writeObject(obj);  

			bytes = bo.toByteArray();  

			bo.close();  
			oo.close();  
		} catch (Exception e) {  
			System.out.println("translation" + e.getMessage());  
			e.printStackTrace();  
		}  
		return bytes;  
	} 

	public static void requestFolcus(EditText edt){

		edt.setFocusable(true);  
		edt.setFocusableInTouchMode(true); 
	}

	/**
	 * string转成bitmap
	 * 
	 * @param st
	 */
	public static Bitmap convertStringToIcon(String st)
	{
		// OutputStream out;
		Bitmap bitmap = null;
		try
		{
			// out = new FileOutputStream("/sdcard/aa.jpg");
			byte[] bitmapArray;
			bitmapArray = Base64.decode(st, Base64.DEFAULT);
			bitmap =
					BitmapFactory.decodeByteArray(bitmapArray, 0,
							bitmapArray.length);
			// bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			return bitmap;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}


