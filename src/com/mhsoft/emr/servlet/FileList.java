package com.mhsoft.emr.servlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileList {

	private static FileList instance = null;
	
	private List<String> list = null;
	
	private FileList() {
		// TODO Auto-generated constructor stub
		this.list = new ArrayList<String>();
	}
	
	public static FileList getInstance() {
		if(null==instance)
			instance = new FileList();
		return instance;
	}
	
	public void add(File file) {
		this.list.add(file.getAbsoluteFile().toString());
	}
	
	public void addArray(File [] files) {
		for(int i=0;null!=files&&i<files.length;i++){
			this.add(files[i]);
		}
	}
	
	public List<String> getList() {
		return this.list;
	}
}
