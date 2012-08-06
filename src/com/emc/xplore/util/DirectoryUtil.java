package com.emc.xplore.util;

import java.io.File;
import java.util.ArrayList;

public class DirectoryUtil {
	ArrayList<File> fileResult= new ArrayList<File>(); 
	ArrayList<String> pathResult= new ArrayList<String>(); 
	ArrayList<String> collectionIndex = new ArrayList<String>();
	final static String INDEX_FOLDER = "LI-";
	
	public ArrayList<String> getAllCollectionIndex(File index) {
		addAllCollectionIndex(index);
		return collectionIndex;
	}
	
	public void addAllCollectionIndex(File file) {
		if(file.isDirectory()){
			if(file.getName().startsWith(INDEX_FOLDER)){
				collectionIndex.add(file.getAbsolutePath());
			}else{
				File[] files = file.listFiles();
				int length = files.length;
				for(int i =0; i< length; i++){
					addAllCollectionIndex(files[i]);
				}
			}
		}
	}
	
	public ArrayList<File> getAllFiles(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			int length = files.length;
			for(int i =0; i< length; i++){
				getAllFiles(files[i]);
			}
		}else{
			fileResult.add(file);
		}	
		return fileResult;
	}
	
	public ArrayList<String> getAllFilePath(String filePath){
		File file =new File(filePath);
		if(file.isDirectory()){
			File[] files = file.listFiles();
			int length = files.length;
			for(int i =0; i< length; i++){
				getAllFilePath(files[i].getAbsolutePath());
			}
		}else{
			pathResult.add(file.getAbsolutePath());
		}	
		return pathResult;
	}


}
