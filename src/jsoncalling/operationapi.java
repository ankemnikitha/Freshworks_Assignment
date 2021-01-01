package jsoncalling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class operationapi {

	static operationapi instance;
	public static operationapi apiinstance(){
		if(instance==null){
			 synchronized (operationapi.class) {
				 if(instance==null){
			instance=new operationapi();
				 }
			 }
		
		}
		return instance;
		
	}

	public  void retrieve(String kk,String name){
		JSONParser parser = new JSONParser();
	      try {
	         Object obj = parser.parse(kk);
	         JSONObject jsonObject = (JSONObject)obj;
	         	
	         if (jsonObject.containsKey(name)) {
	        	    //get Value of video
	        	
	        	 Double atime=(Double) jsonObject.get("atime");
	        	 long ctime=(Long) jsonObject.get("ctime");
	        	
	        	 if(checkalive(ctime,atime)){
	        		
	        		 System.out.println(jsonObject.toJSONString()); 
	        	 }else{
	        		 System.out.println("alive time for the key is completed"); 
	        	 }
	        	
//	        	  JSONArray subjects = (JSONArray)jsonObject.get(name);
//			       
//	 	         System.out.println("Subjects:");
//	 	         if(subjects==null){
//	 	        	 System.out.println("empty");
//	 	         }
//	 	         Iterator iterator = subjects.iterator();
//	 	         while (iterator.hasNext()) {
//	 	            System.out.println(iterator.next());
//	 	         }
	        	}
	       
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	        
	}
	
	public  void getdata(String name){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					"course1.json"));
			String line = reader.readLine();
			while (line != null) {
		//	System.out.println(line);
				if(line!= null && !line.equals("") ){
					
				retrieve(line.trim(),name);	
				}
				
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public  void writetodb(String name,float atime,float tenth,float twelth,float ug){
		 JSONObject obj = new JSONObject();
	     
	      JSONArray subjects = new JSONArray();
	      subjects.add("10th percentage:"+tenth);
	      subjects.add("12th percentage:"+twelth);
	      subjects.add("U.G percentage:"+ug);
	      obj.put(name, subjects);
	      long millis=System.currentTimeMillis();  
	      obj.put("ctime",millis);
	      obj.put("atime", atime);
	      try {
	    	  String s= subjects.toJSONString();
	 	     byte[] utf16Bytes;
				utf16Bytes = s.getBytes("UTF-16BE");
				int len=utf16Bytes.length;
				float kk=len/1024;
				
				if(kk<16 ){
					if(maxsize(len)){
					   try (FileWriter file = new FileWriter("course1.json",true)) {
					         file.write(obj.toJSONString());
					         file.write("\r\n");
					         System.out.println("record inserted successfully");
//					         System.out.println("JSON Object write to a File successfully");
//					         System.out.println("JSON Object: " + obj);
					      }catch(Exception e){
					    	  e.printStackTrace();
					      }
					}
					else{
						System.out.println("max size of file reached");
					}
				}else{
					System.out.println("values should be less than 16kb");
				}
			  
				
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	     
		
	    
	 
	}
	synchronized  void enterdetails(int k){
	
	switch(k){
		
		case 1:	System.out.println("enter name to insert");
		break;
		case 2:	System.out.println("enter name to retrieve");;
		break;
		case 3:
			System.out.println("enter name to delete");
			break;
			default:
				System.out.println("enter valid option");
				return;
		
		}
	Scanner sc=new Scanner(System.in);
	
	String name=sc.nextLine();
	if(name.equals("atime") || name.equals("ctime") ){
		System.out.println("enter name other than atime or ctime");
		name=sc.nextLine();
	}
		switch(k){
		
		case 1:	if(!check(name)){
			System.out.println("time for object to be alive in seconds");
			float at=sc.nextFloat();
			
			System.out.println("enter you 10th perntile");
			float tnth=sc.nextFloat();
			System.out.println("enter you 12th perntile");
			float twel=sc.nextFloat();
			System.out.println("enter you u.g perntile");
			float ug=sc.nextFloat();
			writetodb(name,at,tnth,twel,ug);
		}else{
			System.out.println("name already exists");
		}
		break;
		case 2:getdata(name);
		break;
		case 3:System.out.println("enter name to delete");
			deletedata(name);
		break;
	
		default:
			System.out.println("enter valid option");
		}
	
		
	}
	
	public boolean check(String name){
		BufferedReader reader;
		int flag=0;
		try {
			reader = new BufferedReader(new FileReader(
					"course1.json"));
			String line = reader.readLine();
			while (line != null) {
//				System.out.println(line);
				if(line!= null && !line.equals("") ){
					
					JSONParser parser = new JSONParser();
				      try {
				         Object obj = parser.parse(line);
				         JSONObject jsonObject = (JSONObject)obj;
				         	
				         if (jsonObject.containsKey(name)) {
				        	    //get Value of video
				        	flag=1;
				        	 
				        	}
				       
				      } catch(Exception e) {
				         e.printStackTrace();
				      }
				}
				
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag==1?true:false;
	}
	
	public  void deletedata(String name){
		BufferedReader reader;
		String kk="";
		int flag=0;
		int temp=0;
		try {
			reader = new BufferedReader(new FileReader(
					"course1.json"));
			String line = reader.readLine();
			while (line != null) {
//				System.out.println(line);
				if(line!= null && !line.equals("") ){
					
					JSONParser parser = new JSONParser();
				      try {
				         Object obj = parser.parse(line);
				         JSONObject jsonObject = (JSONObject)obj;


				         ///
				         Double atime=(Double) jsonObject.get("atime");
			        	 long ctime=(Long) jsonObject.get("ctime");
			        	
			        	
				         //
				         if (jsonObject.containsKey(name)) {
				        	    //get Value of video
				        	
				        	 if(checkalive(ctime,atime)){
					        		
				        		 flag=1;
				        		 System.out.println("record deleted ");
				        	 }else{
				        		 temp=1;
				        		 kk=kk+line+"\r\n";
				        		// System.out.println("alive time for the key is completed cannot delete"); 
				        	 }
				    
				        	 
				        	}else{
				        		kk=kk+line+"\r\n";
				        	}
				       
				      } catch(Exception e) {
				         e.printStackTrace();
				      }
				}
				
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(flag==0){
			if(temp==1){
				System.out.println("alive time for the key is completed cannot delete"); 
				
				
			}else{
				System.out.println("item not deleted");
					}
			
		}else{
			replace(kk);
			System.out.println("item deleted");
		}
		
	}
	
	public  boolean checkalive(long ctime,double atime){
		long millis=System.currentTimeMillis();  
		double caltime=ctime+(atime*1000);
		if(millis<=caltime){
			//System.out.println(millis);
		return true;	
		}else{
			return false;
		}
		
	}
	public  void replace(String kk){
		   try (FileWriter file = new FileWriter("course1.json")) {
		         file.write(kk);
		   
		         
//		         System.out.println("JSON Object write to a File successfully");
//		         System.out.println("JSON Object: " + obj);
		      }catch(Exception e){
		    	  e.printStackTrace();
		      }
		   
	}
	
	public boolean maxsize(long objsize){
		boolean fileSize =false;
		long max=1073741824;
		Path filePath = Paths.get("course1.json");
		FileChannel fileChannel;
		try {
			fileChannel = FileChannel.open(filePath);
			 fileSize = (fileChannel.size()+objsize)<=max?true:false;
			
			fileChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileSize;
	}
}
