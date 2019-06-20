package com;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class pingThread implements Runnable{
	public boolean status;
	private String path = "/home/pc1/Desktop/checkping/";
	Thread t = new Thread(this);
	private long First;
	private long Last;
	private boolean flags;
	private String host = "132.176.134.87";
	private long starttime;
	private long stoptime;
	public void Start(){
		status = true;
		flags = false;
		starttime = new GregorianCalendar().getTimeInMillis();
		t.start();
	}
	public void run(){
		while(status == true){
			if(!flags){
				flags = true;
				First = new GregorianCalendar().getTimeInMillis();
			}if(flags){
				Last = new GregorianCalendar().getTimeInMillis();
				Double time = (Math.abs(Last-First)/1000.0);
				if(time >= 60){
					Calendar cal = Calendar.getInstance();
					if(pingHost(host, 100)) writeFile(path+"checkPing", cal.getTime()+" is Connected\n", true);
					else{
						stoptime = new GregorianCalendar().getTimeInMillis();
						String content = "Total time: "+(Math.abs(stoptime-starttime)/1000.0);
						writeFile(path+"Checkping", cal.getTime()+" is Disconnect\n"+content+"\n", true);
						status = false;
					}
					flags = false;
				}
			}
		}
	}
	public boolean pingHost(String host, int timeout) {
		boolean reachable = false;
		try{
			reachable = InetAddress.getByName(host).isReachable(100);
		}catch(Exception e){
			e.printStackTrace();
		}
		return reachable;
	}
	public  boolean writeFile(String path,String content,boolean type){
		BufferedWriter bw = null;
		FileWriter fw = null;
		String Path =new File(path).getParent();
		if(!new File(Path).exists()){
			new File(Path).mkdirs();
		}
		
		try {
			fw = new FileWriter(path,type);
			bw = new BufferedWriter(fw);
			bw.write(content);
		} catch (IOException e) {
			System.out.println("IO Exception");
			return false;
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		return true;
	}
}
