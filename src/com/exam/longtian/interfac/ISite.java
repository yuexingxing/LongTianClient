package com.exam.longtian.interfac;

import java.util.List;

import com.exam.longtian.entity.SiteInfo;

public interface ISite {

	public void success(List<SiteInfo> list);
	
	public void failed();
}
