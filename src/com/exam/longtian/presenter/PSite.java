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
	 * ��ȡ����վ��
	 * @param context
	 * @param callback
	 */
	public static void site_list(Context context, final String orderType, final ISite iSite){

		OkHttpUtil.get(context, API.site_list, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				if(success){

					JSONArray jsonArray = (JSONArray) data;
					List<SiteInfo> siteList = new ArrayList<SiteInfo>();//�м��ϣ��������е�վ��
					List<SiteInfo> jzhList = new ArrayList<SiteInfo>();//���㻦վ�㼯�ϣ���������ĸ����

					int len = jsonArray.length();
					for(int i=0; i<len; i++){

						JSONObject jsonObject = jsonArray.optJSONObject(i);
						SiteInfo siteInfo = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<SiteInfo>(){}.getType());

						String strPinYin = (CharacterParser.getInstance().getSelling(siteInfo.getSiteName()).toUpperCase());
						siteInfo.setPinYin(strPinYin);

						/* �Ϻ�--provinceCode--310000
						 * ����--provinceCode--320000
						 * �㽭--provinceCode--330000
						 * */
						
						//¼�����ͣ�ֻ��ʾ�㽭��������
						if(PresenterUtil.ORDER_TYPE_INPUT.equals(orderType)){

							if(!siteInfo.getProvinceCode().equals("330000")){
								continue;
							}
						}
						
						//���ݷֲ����ķ��ڵ�һ��
						if(siteInfo.getSiteName().equals("���ݷֲ�")){
							siteList.add(0, siteInfo);
						}

						//�㽭ʡ������ĵ���������ʾ����ǰ�沢��������ĸ����
						else if(siteInfo.getProvinceCode().equals("330000")){
							jzhList.add(siteInfo);
						}

						else{
							siteList.add(siteInfo);
						}

					}

					//�Խ��㻦վ�㰴��������
					Collections.sort(jzhList, new SiteInfoComparators());

					len = siteList.size();
					boolean flag = false;
					if(len > 0){
						flag = siteList.get(0).getSiteName().equals("���ݷֲ�") ? true:false;
					}

					len = jzhList.size();
					for(int i=0; i<len; i++){

						//�����һ���Ǻ��ݷֲ�
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
	 * �Ϻ�--provinceCode--310000
	 * ����--provinceCode--320000
	 * �㽭--provinceCode--330000
	 */
	public static void sortData(SiteInfo siteInfo){


	}
}
