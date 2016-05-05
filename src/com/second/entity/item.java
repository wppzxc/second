package com.second.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

// 商品条目

public class item extends BmobObject {

	public String itemTITLE;
	public String itemCLASS;
	BmobFile item_iv_img;
	
	public BmobFile getItem_iv_img() {
		return item_iv_img;
	}
	public void setItem_iv_img(BmobFile item_iv_img) {
		this.item_iv_img = item_iv_img;
	}
	public String getItemTITLE() {
		return itemTITLE;
	}
	public void setItemTITLE(String itemTITLE) {
		this.itemTITLE = itemTITLE;
	}
	public String getItemCLASS() {
		return itemCLASS;
	}
	public void setItemCLASS(String itemCLASS) {
		this.itemCLASS = itemCLASS;
	}

}
