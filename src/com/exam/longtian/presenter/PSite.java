package com.exam.longtian.presenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.interfac.ISite;
import com.exam.longtian.util.API;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PSite {

	/**
	 * 获取所有站点
	 * @param context
	 * @param callback
	 */
	public static void site_list(Context context, final ISite iSite){

		OkHttpUtil.get(context, API.site_list, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				if(success){

					JSONArray jsonArray = (JSONArray) data;
					List<SiteInfo> siteList = new ArrayList<SiteInfo>();

					for(int i=0; i<jsonArray.length(); i++){

						JSONObject jsonObject = jsonArray.optJSONObject(i);
						SiteInfo siteInfo = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<SiteInfo>(){}.getType());

						siteList.add(siteInfo);
					}

					for(int i=0; i<siteList.size(); i++){

						SiteInfo info = siteList.get(i);
						Log.v("zd", info.getSiteName());
					}
					
					if(iSite != null){
						iSite.success(siteList);
					}
				}else{
					iSite.failed();
				}
			}

		});
	}
}
