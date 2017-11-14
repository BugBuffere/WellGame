package com.leng.utils;

public class Resource {
	
	private String gen;
	
	private static Resource res;
	
	private Resource() {
		super();
		gen = System.getProperty("user.dir") + "\\res";
	}
	
	public static Resource asInstance() {
		if (res == null) {
			synchronized (Resource.class) {
				if (res == null) {
					res = new Resource();
				}
			}
		}
		return res;
	}
	
	public String getRoot(){
		return gen;
	}
	
	public String getFilePath(String fileName){
		return gen + "\\" + fileName;
	}

	public String getImangeResource(String fileName){
		return gen +"\\image\\" + fileName;
	}
	
	public String getFileResource(String fileName){
		return gen + "\\file\\" + fileName;
	}

}
