package com.example.mrpsbeta;

import java.io.IOException;
import java.util.UUID;
import android.os.Handler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Message;
import android.util.Log;

public class AcceptThread extends Thread
{
	private final BluetoothServerSocket mmServerSocket;
	private BluetoothAdapter mBluetoothAdapter;
	private Handler mh;
	private Message message;
	BluetoothSocket socket = null;
	private String NAME;
	private UUID MY_UUID;
	private static final String TAG1 = "AcceptThread> SocketCreate: ";
	private static final String TAG2 = "AcceptThread> SocketAccept: ";
	private static final String TAG3 = "AcceptThread> SocketClose: ";
	private static final String TAG4 = "AcceptThread> ThreadCancel: ";
	
	
	public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    

    	  public AcceptThread(BluetoothAdapter btmp, String name, UUID myuuid, Handler handler) 
    	  {
    		  
    		    mBluetoothAdapter = btmp;
    	    	NAME = name;
    	    	MY_UUID = myuuid;
    	    	mh = handler;
    	        // Use a temporary object that is later assigned to mmServerSocket,
    	        // because mmServerSocket is final
    	        BluetoothServerSocket tmp = null;
    	        try 
    	        {
    	            // MY_UUID is the app's UUID string, also used by the client code
    	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
    	        } catch (IOException e) 
    	        {
    	        	Log.i(TAG1, "Error"); e.printStackTrace();
    	        }
    	        mmServerSocket = tmp;
    	  
//    	     try {
//    	        mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("mRPSbeta", BMain);
//    	     } catch (IOException e) {}
    	  }
    	  
    	  public void run() 
    	  {
    		    while (true) 
    		    {
    		      try 
    		      {
    		        socket = mmServerSocket.accept();
    		        System.out.println("%%%%%%%%%%%%% Socket Value: " + socket.toString());
    		        manageConnectedSocket(socket, mh);
    		        mmServerSocket.close();
    		        break;
    		      } 
    		      catch (IOException e) 
    		      { e.printStackTrace(); }
    		    }
    	  }
    	  
    	  
    	  private void manageConnectedSocket(BluetoothSocket socket, Handler handler) 
    	    {
    		  	  BluetoothSocket mmSocket = socket;
    		  	  Handler mHandler = handler;
    	    	  ReadWriteThread conn = new ReadWriteThread(mmSocket, mHandler);
    	    	  Message msg = Message.obtain();
    	    	  msg.what = BMain.SOCKET_CONNECTED;
    	    	  msg.obj = mmSocket;
    	    	  mHandler.sendMessage(msg);
    	    	  System.out.println("%%%%%%%%%%%%% Socket Value: " + conn.toString());
    	    	  //mHandler.obtainMessage(BMain.SOCKET_CONNECTED, conn).sendToTarget();
    	    	  conn.start();
    	    } 
    	  
    	  

}
    
    
    
//    public AcceptThread(BluetoothAdapter btmp, String name, UUID myuuid, Handler handler) 
//    {
//    	mBluetoothAdapter = btmp;
//    	NAME = name;
//    	MY_UUID = myuuid;
//    	mh = handler;
//        // Use a temporary object that is later assigned to mmServerSocket,
//        // because mmServerSocket is final
//        BluetoothServerSocket tmp = null;
//        try 
//        {
//            // MY_UUID is the app's UUID string, also used by the client code
//            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
//        } catch (IOException e) 
//        {
//        	Log.i(TAG1, "Error"); e.printStackTrace();
//        }
//        mmServerSocket = tmp;
//    }
//    //-----------------------------------------------------------------
//    
//    /*
//    private void manageConnectedSocket() 
//    {
//    	  ConnectedThread transferThread = new ConnectedThread(mBluetoothSocket, mHandler);
//    	  mHandler.obtainMessage(
//    	DataTransferActivity.SOCKET_CONNECTED, conn)
//    								.sendToTarget();
//    	  conn.start();
//    }
//    */
//    
//    
//    //-----------------------------------------------------------------
//    
//    
//	//public BluetoothSocket getBluetoothSocket() { return socket; } 
// 
//    public void run() 
//    {
//    	
//        //BluetoothSocket socket = null;
//       // Keep listening until exception occurs or a socket is returned
//        while (true) 
//        {
//        	/*
//        	for(int i=1;i<100;i++)
//        	{
//        		message.arg1 = i;
//        		mh.sendMessage(message);
//        	}
//        	*/
//            try 
//            {
//                socket = mmServerSocket.accept();
//                manageConnectedSocket();
//            } 
//            catch (IOException e) 
//            {
//                Log.i(TAG2, "Error");
//                e.printStackTrace();
//            	break;
//            }
//            // If a connection was accepted
//            if (socket != null) 
//            {
//                // Do work to manage the connection (in a separate thread)
//                //manageConnectedSocket(socket);
//                try 
//                {
//                	System.out.println("++++++++ Closing Socket +++++++++++");
//					mmServerSocket.close();
//					Message msg = Message.obtain();
//                    
//                    msg.obj = socket;
//                    
//                    mh.sendMessage(msg);
//				} catch (IOException e) 
//				{
//					Log.i(TAG3, "Error");
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//                break;
//            }
//        }
//    }
// 
//    /** Will cancel the listening socket, and cause the thread to finish */
//    public void cancel() 
//    {
//        try 
//        {
//            mmServerSocket.close();
//        } catch (IOException e)
//        { Log.i(TAG4, "Error"); e.printStackTrace(); }
//    }
//}
//
//
