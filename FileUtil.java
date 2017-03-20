package com.ds4u.project.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

	public static void main(String[] args) {
	
		//Input zip file
		File zipFile = new File("D:\\Project\\traversDemo.zip");
		
		//output Folder
		File outputFile = new File("D:\\Project\\traversDemo");

		FileUtil.unZipIt(zipFile, outputFile);
		
		//Array list to be take all files object which is traversed
		List<File> list = new ArrayList<File>();
		
		FileUtil.traverseFile(outputFile, list);
		
		for(File file : list){
			System.out.println(file.getAbsolutePath());
		}
	}
	
	/**
	 * File traversing
	 * @param f
	 * @param listFile
	 */
	public static void traverseFile(File f, List<File> listFile){
		if(!f.isDirectory()){
			
			listFile.add(f);
		}else{
			File[] files = f.listFiles();
			for(File file : files){
				if(file.isDirectory()){
					traverseFile(file, listFile);
				}else{
					listFile.add(file);
				}
			}
		}
	}
	
	
	/**
	 * Unzip files
	 * @param zipFile
	 * @param outputFolder
	 */
	public static void unZipIt(File zipFile, File outputFolder){

	     byte[] buffer = new byte[1024];

	     try{

	    	//create output directory is not exists
	    	File folder = outputFolder;
	    	if(!folder.exists()){
	    		folder.mkdir();
	    	}

	    	//get the zip file content
	    	ZipInputStream zis =
	    		new ZipInputStream(new FileInputStream(zipFile));
	    	//get the zipped file list entry
	    	
	    	
	    	ZipEntry ze=zis.getNextEntry();
	    	while(ze !=null){

	    		
	    		
	    	   String fileName = ze.getName();
	           File newFile = new File(outputFolder + File.separator + fileName);

	           System.out.println("file unzip : "+ newFile.getAbsoluteFile());

	            //create all non exists folders
	            //else you will hit FileNotFoundException for compressed folder
	            new File(newFile.getParentFile().getAbsolutePath()).mkdirs();
	            //FileTraverseUtil.createAllParentDirectory(outputFolder, newFile.getParentFile());
	            
	            if(!ze.isDirectory()){
	    			
	    		
		            FileOutputStream fos = new FileOutputStream(newFile);
	
		            int len;
		            while ((len = zis.read(buffer)) > 0) {
		       		fos.write(buffer, 0, len);
		            }
	
		            fos.close();
	            }
		        ze = zis.getNextEntry();
	            
	    	}

	        zis.closeEntry();
	    	zis.close();

	    	System.out.println("Done");

	    }catch(IOException ex){
	       ex.printStackTrace();
	    }
	   }

}
