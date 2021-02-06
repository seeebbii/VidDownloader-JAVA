package com.Codeminers.allInOne.free.videodownloader.statussaver.model.whatsapp;

import android.graphics.Bitmap;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WhatsppVideos implements Serializable{

	String path="";
	boolean selected = false;
	long size=0;
	Bitmap bitmap;
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
