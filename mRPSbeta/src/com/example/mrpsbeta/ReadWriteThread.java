package com.example.mrpsbeta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ReadWriteThread extends Thread 
{
	private BluetoothSocket mBluetoothSocket;
	private Handler mHandler;
	private InputStream mInStream;
    private OutputStream mOutStream;
	  ReadWriteThread(BluetoothSocket socket, Handler handler)
	  {
	    super();
	    mBluetoothSocket = socket;
	    mHandler = handler;
	    try
	    {
	      mInStream = mBluetoothSocket.getInputStream();
	      mOutStream = mBluetoothSocket.getOutputStream();
	    } catch (IOException e) { e.printStackTrace(); }
	  }
	  
	  public void run() 
	  {
		    byte[] buffer = new byte[1024];
		    int bytes;
		    while (true) 
		    {
		      try 
		      {
		    	  bytes = mInStream.read(buffer);
			      String data = new String(buffer, 0, bytes);
		    	  Message msg = Message.obtain();
		    	  msg.what = BMain.DATA_RECEIVED;
		    	  msg.obj = data;
		    	  mHandler.sendMessage(msg);
		        
		        
		       // mHandler.obtainMessage(BMain.MESSAGE_READ, data).sendToTarget();
		      } catch (IOException e) { break;  }
		    }
	  }
	  
	  
	  public void write(byte[] bytes) 
	  {
		    try {
		      mOutStream.write(bytes);
		    } catch (IOException e) { e.printStackTrace(); }
		  
	  }
}


