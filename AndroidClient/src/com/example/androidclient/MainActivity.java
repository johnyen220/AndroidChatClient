//Created by John Yen on 10/01/2014.
//Copyright 2014 John Yen All rights reserved.
// please run chatserver.py on your terminal, connect your terminal by ip!
// For example see bollow How to run chat server! 
// your terminal name->  python /Users/ca_mob03/Downloads/chatserver.py 
// iOS client please go to --> https://github.com/funkyboy/Socket-Based-iOS-Application-and-Server
package com.example.androidclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

	public class MainActivity extends Activity {

	 static EditText iamEditText,msgEditText,ipEditText;
	 static TextView tv2;
	 static Button connectBtn ;
	 static String str1="0",str2="0";
	 ListView msgListView;
	 ProgressDialog dialog;
	 Button loginBtn,sendBtn;
	 String server;
	 Socket socket;
	 InputStream in;
	 OutputStream out;
	 private ArrayAdapter<String> listAdapter;
	 ArrayList<String> list = new ArrayList<String>();
	 int servPort=5678;
	    @SuppressWarnings("unchecked")
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        ipEditText = (EditText)findViewById(R.id.ipEditText);
	        iamEditText = (EditText) findViewById(R.id.iamEditText);
	        msgEditText = (EditText) findViewById(R.id.msgEditText);
	        tv2 = (TextView) findViewById(R.id.show02);
	        connectBtn = (Button) findViewById(R.id.connectBtn);
	        loginBtn = (Button) findViewById(R.id.loginBtn);
	        sendBtn=(Button)findViewById(R.id.sendBtn);
	        msgListView=(ListView)findViewById(R.id.msgListView);
	        connectBtn.setOnClickListener(new OnClickListener() 
	        {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					server=ipEditText.getText().toString();
					Log.v("TAG",server);
					ConnectThread connectThread=new ConnectThread();
					connectThread.start();
				}});  
	        loginBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Loginthread loginThread=new Loginthread();
					loginThread.start();
				}
			});
	        sendBtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Sendthread sendThread=new Sendthread();
					sendThread.start();
				}});
	    }

	    class ConnectThread extends Thread{
		     public void run() {
		      try{
			      System.out.println("Waitting to connect......");
			      
			      socket=new Socket(server,servPort);
			      InputStream in=socket.getInputStream();
			      OutputStream out=socket.getOutputStream();
			      System.out.println("Connected!!");
			      Log.v("TAG","Connected");
			      runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv2.setText("Socket Connected!!");
						
					}
				  });
			      while(socket.isConnected()){
						in=socket.getInputStream();
						int lenth=in.available();
						if(lenth>0){
					      byte[] rebyte = new byte[lenth];
					      in.read(rebyte);
					      str2 =new String(new String(rebyte,"UTF-8"));
			    	  		  runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									list.add(str2);
									ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
								    msgListView.setAdapter(adapter);
								}
						});
						}
			    	  		sleep(100);
			      }
		      }catch(Exception e)
			      {
			       System.out.println("Error: "+e.getMessage());
			       runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						 tv2.setText("Connect fail!");
					}
				});
			      
			      }
		      }
		    }
	    class Sendthread extends Thread{
		     public void run() {
		      try{
	          OutputStream out=socket.getOutputStream();
		     
		    	  //String str1=null;	  
		      
		      str1 = "msg:"+msgEditText.getText().toString();
		      byte[] sendstr = str1.getBytes("UTF8");
		      System.arraycopy(str1.getBytes(), 0, sendstr, 0, str1.length());
		      out.write(sendstr);
	         runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					msgEditText.setText("");
				}
			});
		      }catch(Exception e)
		      {
		       System.out.println("Error: "+e.getMessage());//
		      }
		      }
		    }
	    class Loginthread extends Thread{
		     public void run() {
		      try{
			          OutputStream out=socket.getOutputStream();
			          String str1;
				      str1 = "iam:"+iamEditText.getText().toString();
				      byte[] sendstr = new byte[str1.length()];
				      System.arraycopy(str1.getBytes(), 0, sendstr, 0, str1.length());
				      out.write(sendstr);
				      runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
						}
					});
				   }catch(Exception e)
				   {
				       System.out.println("Error: "+e.getMessage());//
				   }
				      }
		    }

}
