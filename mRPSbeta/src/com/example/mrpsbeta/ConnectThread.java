package com.example.mrpsbeta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.os.Handler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class ConnectThread extends Thread
{
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private String NAME;
	private UUID MY_UUID;
	Handler mHandler;
 
    public ConnectThread(BluetoothDevice device, BluetoothAdapter btmp, String serviceName, UUID myuuid, Handler handler)
    {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mBluetoothAdapter = btmp;
        mHandler = handler;
        mmDevice = device;
        NAME = serviceName;
        MY_UUID = myuuid;
        NAME = mmDevice.getName();
        
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
            manageConnectedSocket();
        }
        catch (IOException connectException) 
        {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
        //manageConnectedSocket(mmSocket);
    }
 
    /** Will cancel an in-progress connection, and close the socket */
    private void manageConnectedSocket() 
    {
    	  ReadWriteThread conn = new ReadWriteThread(mmSocket, mHandler);
    	  Message msg = Message.obtain();
    	  msg.what = BMain.SOCKET_CONNECTED;
    	  msg.obj = mmSocket;
    	  mHandler.sendMessage(msg);
    	  //mHandler.obtainMessage(BMain.SOCKET_CONNECTED, conn).sendToTarget();
    	  conn.start();
    }
    
    
    //================================================================================================
    
//    public class ReadWriteThread extends Thread 
//    {
//    	private BluetoothSocket mBluetoothSocket;
//    	private Handler mHandler;
//    	private InputStream mInStream;
//        private OutputStream mOutStream;
//    	  ReadWriteThread(BluetoothSocket socket, Handler handler)
//    	  {
//    	    super();
//    	    mBluetoothSocket = socket;
//    	    mHandler = handler;
//    	    try
//    	    {
//    	      mInStream = mBluetoothSocket.getInputStream();
//    	      mOutStream = mBluetoothSocket.getOutputStream();
//    	    } catch (IOException e) { e.printStackTrace(); }
//    	  }
//    	  
//    	  public void run() 
//    	  {
//    		    byte[] buffer = new byte[1024];
//    		    int bytes;
//    		    while (true) {
//    		      try {
//    		    	  Message msg = Message.ob
//    		        bytes = mInStream.read(buffer);
//    		        String data = new String(buffer, 0, bytes);
//    		        mHandler.obtainMessage(BMain.MESSAGE_READ, data).sendToTarget();
//    		      } catch (IOException e) { break;  }
//    		    }
//    	  }
//    	  public void write(byte[] bytes) 
//    	  {
//    		    try {
//    		      mOutStream.write(bytes);
//    		    } catch (IOException e) { e.printStackTrace(); }
//    		  
//    	  }
//    }
    
    //================================================================================================
    
    
    
    
    
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}