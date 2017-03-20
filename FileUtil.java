package com.project.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


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
	
	public static String uploadFile(MultipartFile file) throws IOException{
		
			String path = getRootPath();
			String filePath =null;
		
			byte[] bytes = file.getBytes();
			File directoryPath = new File(path);
			System.out.println("path: "+path);
			
			if(!directoryPath.exists()){
				directoryPath.mkdir();
			}
		
			String rootPath = directoryPath.getAbsolutePath();
			filePath = "/" + (new Date()).getTime() + "-"
					+ file.getOriginalFilename();
			
			File uploadFile = new File(rootPath + filePath);
			FileOutputStream out = new FileOutputStream(uploadFile);
			BufferedOutputStream outputStream = new BufferedOutputStream(out);
			outputStream.write(bytes);
			outputStream.close();
			
			return filePath;
		
	}
	
	public static void downloadFile(HttpServletRequest request,
			HttpServletResponse response, String fileName) throws IOException{
		
		String path = getRootPath();	
	
		File file = new File(path);
		String rootPath = file.getAbsolutePath();
		
		response.setContentType("application/octet-stream");
		PrintWriter out;
		try {
			out = response.getWriter();
		
		String filename = rootPath+fileName;
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		//String filepath = message.getAbb();
		FileInputStream fileInputStream = new FileInputStream(filename);
		int i;
		while ((i = fileInputStream.read()) != -1) {
			out.write(i);
		}
		fileInputStream.close();
		out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
