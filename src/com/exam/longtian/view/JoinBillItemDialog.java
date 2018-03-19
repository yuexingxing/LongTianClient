package com.exam.longtian.view;

import java.util.List;
import com.exam.longtian.R;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.JoinBillInfo;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 带回调的单选框对话框
 * 
 * @author yxx
 * 
 *         2015-12-3
 */
public class JoinBillItemDialog {

	public static boolean isShow = false;
	public static Dialog builder;

	public JoinBillItemDialog() {

	}

	// 弹窗结果回调函数
	public static abstract class JoinBillItemDialogCallBack {
		public abstract void callback(int pos);
	}

	/**
	 * @param context
	 * @param strTitle
	 * @param flag
	 *            点击外部是否可以关闭对话框
	 * @param list
	 * @param resultCallBack
	 */
	public static void showDialog(final Context context, String strTitle, boolean flag, List<JoinBillInfo> list, final JoinBillItemDialogCallBack resultCallBack) {

		if (builder != null) {
			builder.dismiss();
		}

		View view = LayoutInflater.from(context).inflate(R.layout.dialog_item_joinbill, null);

		TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_item);
		ListView listView = (ListView) view.findViewById(R.id.listView_dialog);

		tvTitle.setText(strTitle);

		try {

			CommonAdapter<JoinBillInfo> commonAdapter = new CommonAdapter<JoinBillInfo>(context, list, R.layout.layout_single_item_joinbill) {
				
				@Override
				public void convert(ViewHolder helper, JoinBillInfo item) {

					helper.setText(R.id.tv_dialog_single_item_joinbill_1, item.getHandoverId());
					helper.setText(R.id.tv_dialog_single_item_joinbill_2, item.getDriverName());
				}
			};

			listView.setAdapter(commonAdapter);

			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					resultCallBack.callback(arg2);
					builder.dismiss();
				}
			});

			builder = new Dialog(context, R.style.dialogSupply);
			builder.setContentView(view);

			if (flag == true) {
				builder.setCanceledOnTouchOutside(true);
			}
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
