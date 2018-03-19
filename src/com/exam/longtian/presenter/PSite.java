package com.exam.longtian.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.interfac.ISite;
import com.exam.longtian.sort.CharacterParser;
import com.exam.longtian.sort.SiteInfoComparators;
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
	public static void site_list(Context context, final String orderType, final ISite iSite){

		OkHttpUtil.get(context, API.site_list, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				if(success){

					JSONArray jsonArray = (JSONArray) data;
					List<SiteInfo> siteList = new ArrayList<SiteInfo>();//中集合，包括所有的站点
					List<SiteInfo> jzhList = new ArrayList<SiteInfo>();//江浙沪站点集合，根据首字母排序

					int len = jsonArray.length();
					for(int i=0; i<len; i++){

						JSONObject jsonObject = jsonArray.optJSONObject(i);
						SiteInfo siteInfo = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<SiteInfo>(){}.getType());

						String strPinYin = (CharacterParser.getInstance().getSelling(siteInfo.getSiteName()).toUpperCase());
						siteInfo.setPinYin(strPinYin);

						/* 上海--provinceCode--310000
						 * 江苏--provinceCode--320000
						 * 浙江--provinceCode--330000
						 * */
						
						//录单类型，只显示浙江地区网点
						if(PresenterUtil.ORDER_TYPE_INPUT.equals(orderType)){

							if(!siteInfo.getProvinceCode().equals("330000")){
								continue;
							}
						}
						
						//杭州分拨中心放在第一个
						if(siteInfo.getSiteName().equals("杭州分拨")){
							siteList.add(0, siteInfo);
						}

						//浙江省内网点的单独处理，显示在最前面并按照首字母排序
						else if(siteInfo.getProvinceCode().equals("330000")){
							jzhList.add(siteInfo);
						}

						else{
							siteList.add(siteInfo);
						}

					}

					//对江浙沪站点按名称排序
					Collections.sort(jzhList, new SiteInfoComparators());

					len = siteList.size();
					boolean flag = false;
					if(len > 0){
						flag = siteList.get(0).getSiteName().equals("杭州分拨") ? true:false;
					}

					len = jzhList.size();
					for(int i=0; i<len; i++){

						//如果第一条是杭州分拨
						if(flag){
							siteList.add(i+1, jzhList.get(i));
						}else{
							siteList.add(i, jzhList.get(i));
						}

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

	/**
	 * 上海--provinceCode--310000
	 * 江苏--provinceCode--320000
	 * 浙江--provinceCode--330000
	 */
	public static void sortData(SiteInfo siteInfo){


	}
}
