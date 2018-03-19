package com.exam.longtian.sort;

import java.util.Comparator;
import com.exam.longtian.entity.SiteInfo;

/** 
 * ’æµ„≈≈–Ú
 * 
 * @author yxx
 *
 * @date 2018-3-1 …œŒÁ10:00:51
 * 
 */
public class SiteInfoComparators implements Comparator<SiteInfo> {

	public int compare(SiteInfo o1, SiteInfo o2) {
		if (o1.getPinYin().equals("@")
				|| o2.getPinYin().equals("#")) {
			return -1;
		} else if (o1.getPinYin().equals("#")
				|| o2.getPinYin().equals("@")) {
			return 1;
		} else {
			return o1.getPinYin().compareTo(o2.getPinYin());
		}
	}

}
