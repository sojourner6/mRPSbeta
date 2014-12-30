package com.example.mrpsbeta;

import com.example.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BMain extends ActionBarActivity 
{
	//------------------- Variables ------------------------------------
	
	private static final String serviceName = "mPRSbeta";
	private int CHOSENINDEX;
	private BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
	private BluetoothDevice bd;
	BluetoothSocket bSocket;
	//Message msg;
	private ArrayList<BluetoothDevice> bdset;// = new ArrayAdapter<BluetoothDevice>();
	private ArrayAdapter<String> aa;// = new ArrayAdapter(getApplicationContext(), 0);
	private Intent in = new Intent();
	UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	String TAG0 = "OnCreate: ";
	private String chosenDevice;
	private String chosenDeviceMac;
	ArrayList<String> deviceName;
	ArrayList<String> deviceMAC;
	private int BROADCASTREGISTERSTAT = 0; 
	//private Handler mh;
	
	//----- components ---------
	//private Button bTurnOn = (Button)findViewById(R.id.bTurnOn);
	//private Button bVisible = (Button)findViewById(R.id.bVisible);
	//private Button bListDev = (Button)findViewById(R.id.bListDev);
	//private Button bTurnOff = (Button)findViewById(R.id.bTurnOff);
	private ListView listDev;
	private TextView progressText;
	private ProgressBar progressBar;
			
	//----- activity codes ------
	private static final int REQUEST_ENABLE_BT = 1;
	private static final int REQUEST_VISIBLE_BT = 2;
	//private static final int REQUEST_LIST_BT = 3;
	//private static final int REQUEST_DISABLE_BT = 4;
	
	
	public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int SOCKET_CONNECTED = 50;
    public static final int DATA_RECEIVED = 51;
    String user,seq,age,gender;
	
	private final BroadcastReceiver br = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        String TAGBroadcast = "Broadcast Receiver: ";
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action))
	        {
	            // Get the BluetoothDevice object from the Intent
	        	try
	        	{
	        		bd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        		bdset.add(bd);
	        		aa.add(bd.getName() + "^" + bd.getAddress() + "^" + bd.getBondState());
	        		// Add the name and address to an array adapter to show in a ListView
	        		Log.i(TAGBroadcast, " --------- Device Found -------- ");
	        		deviceName.add(bd.getName());
	        		deviceMAC.add(bd.getAddress());
	        		//mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	        	}
	        	catch(Exception e) 	{e.printStackTrace();}
	        }
	        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
	        {
	        	Log.i(TAGBroadcast, " ^^^^^^ Discovery Started ^^^^^ ");
	        }
	        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
	        {
	        	Log.i(TAGBroadcast, " ^^^^^^ Discovery Finished ^^^^^ ");
	        	listDev.setAdapter(aa);
	        	progressText.setText("");
	        	
	        }
	    }
	};
	
	/*
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case AcceptThread.STATE_CONNECTED:
                    break;
                case AcceptThread.STATE_CONNECTING:
                    break;
                case AcceptThread.STATE_LISTEN:
                	break;
                case AcceptThread.STATE_NONE:
                    break;
                }
                break;
            case MESSAGE_WRITE:
            	byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                Toast.makeText(getApplicationContext(), "You played "
                        + writeMessage, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_READ:
            	byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
            	Toast.makeText(getApplicationContext(), "Opponent played "
                        + readMessage, Toast.LENGTH_SHORT).show();
                break;
            /*
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            */    
           //}
       // }
    //};
    

	//------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		user=getIntent().getExtras().getString("userID");
		seq=getIntent().getExtras().getString("userSeq");
		age=getIntent().getExtras().getString("userAge");
		gender=getIntent().getExtras().getString("userGender");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bmain);
		//aa = new ArrayAdapter(getApplicationContext(), 0);
		aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);
		listDev = (ListView)findViewById(R.id.listDev);
		progressText = (TextView)findViewById(R.id.progressText);
		//progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		deviceName = new ArrayList<String>();
		deviceMAC = new ArrayList<String>();
		bdset = new ArrayList<BluetoothDevice>();
		
		
		listDev.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(ba.isDiscovering())	ba.cancelDiscovery();

	            // Get the device MAC address, which is the last 17 chars in the View
	            String info = ((TextView) view).getText().toString();
	            System.out.println("================ posn ====== "+String.valueOf(position));
	    		String delimiter = "[\\^]";
	            //String address = info.substring(info.length() - 17);
	    		String[] devValues = new String[10]; 
	    		devValues =	info.split(delimiter);
	    		chosenDevice = devValues[0];
	    		chosenDeviceMac = devValues[1];
	    		CHOSENINDEX = position;

			}
		});		
		
	}
	
	//----------------------------------------------------
	
	
	//----------------------------------------------------
	
	public void selfTurnOn(View v)
	{
		if(ba == null)
		{
			Toast.makeText(getApplicationContext(), "The Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
			finish();
		}
		else
		{ 
			if(!ba.isEnabled()){in = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); startActivityForResult(in, REQUEST_ENABLE_BT);	}
			else Toast.makeText(getApplicationContext(), "Bluetooth already enabled", Toast.LENGTH_SHORT).show();
		}

		
	}
	
	//----------------------------------------------------
	
	public void listDevInRange(View v)
	{
		//Set<BluetoothDevices> bPaired = ba.st
		//DisplayThread dTh = new DisplayThread("Scanning...");
		//dTh.run();
		progressText.setText("Scanning...");
		registerReceiver(br, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)); BROADCASTREGISTERSTAT++;
		registerReceiver(br, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)); BROADCASTREGISTERSTAT++;
		registerReceiver(br, new IntentFilter(BluetoothDevice.ACTION_FOUND)); BROADCASTREGISTERSTAT++;
		if(!ba.isDiscovering())	
		{
			bdset.clear();
			ba.startDiscovery();
		}
	}
	
	//----------------------------------------------------
	
	public void selfTurnOff(View v)
	{
		if(ba == null)
		{
			Toast.makeText(getApplicationContext(), "The Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
			finish();
		}
		else
		{
			if(ba.isEnabled())
			{
				ba.disable();
//				in = new Intent(BluetoothAdapter.STATE_TURNING_OFF);
//				startActivityForResult(in, REQUEST_ENABLE_BT);
			}
			else Toast.makeText(getApplicationContext(), "Bluetooth already turned Off", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	//--------------------------------------------------------
	
	public void setVisible(View v)
	{
		in = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		startActivityForResult(in, REQUEST_VISIBLE_BT);
	}
	
	//--------------------------------------------------------
	/*
	public void listDevClicked(View v)
	{
		System.out.println("(((((((((((( Starting Server ))))))))))))");
		AcceptThread server = new AcceptThread(ba, serviceName, MY_UUID);
		server.start();
	}
	*/
	
	//--------------------------------------------------------
	
	
	public void hostServer(View v)
	{
		final Boolean mServerMode = true;
		String TAG = "HostServer: ";
		if(ba.isDiscovering())	ba.cancelDiscovery();

        // Get the device MAC address, which is the last 17 chars in the View
        //String info = ((TextView) v).getText().toString();
        //String address = info.substring(info.length() - 17);
        // Create the result Intent and include the MAC address
		
        //Log.i(TAG0, "Item Clicked ---------------");
        Log.i(TAG, chosenDevice + " " + chosenDeviceMac);
        //AcceptThread serverThread = new AcceptThread(ba, serviceName, MY_UUID);
        
//        Handler mh1 = new Handler()
//        {
//        	@Override
//        	public void handleMessage(Message msg)
//        	{
//        		System.out.println("++++ Handler activated ++++");
//        		bSocket = (BluetoothSocket) msg.obj;
//        		//waitRead(bSocket);
//        		//progressBar.setProgress(msg.arg1);
//        	}
//        };
        
        Handler mh1 = new Handler() 
        {
            public void handleMessage(Message msg) 
            {
              switch (msg.what) 
              {
                case SOCKET_CONNECTED: 
                {
                	System.out.println("Socket Connected********************");
                	
                	
                		//int seqno=Integer.parseInt(seq);
                		//seqno++;
                		//seq=String.valueOf(seqno);
                		
                	takeToGame();
                	
//                  ReadWriteThread mBluetoothConnection = (ReadWriteThread) msg.obj;
//                  if (!mServerMode)
//                    mBluetoothConnection.write("this is a message".getBytes());
//                  break;
                }
                case DATA_RECEIVED: 
                {
                	
                	//ReadWriteThread mBluetoothConnection = (ReadWriteThread) msg.obj;
                	String data = (String) msg.obj;
                	System.out.println("%%%%%%%% DATA RECEIVED: "+data); //tv.setText(data);
                	
                	//if (mServerMode)
                		//mBluetoothConnection.write(data.getBytes());
                }
              }
            }
        };
        
//        AcceptThread serverThread = new AcceptThread(ba, serviceName, MY_UUID, mh);
        
        //Thread serverThread = new Thread(new AcceptThread(ba, serviceName, MY_UUID, mh));
        AcceptThread serverThread = new AcceptThread(ba, serviceName, MY_UUID, mh1);
        serverThread.start();
        
 	}
	
	//--------------------------------------------------------
	
	
	public void takeToGame()
	{
		Intent i= new Intent(this, GameArena.class);
		i.putExtra("uName", user);
		i.putExtra("uSeq", seq);
		i.putExtra("uAge", age);
		i.putExtra("uGender", gender);
		startActivity(i);
	}
	
//	public void waitRead(BluetoothSocket bS)
//	{
//		final Boolean mServerMode = true;
//		System.out.println("++++ Read activated ++++");
////		Toast.makeText(getApplicationContext(), "Player has joined the game", Toast.LENGTH_SHORT).show();
////		Handler mh2 = new Handler()
////        {
////        	@Override
////        	public void handleMessage(Message msg)
////        	{
////        		System.out.println("++++ Read activated ++++ data: " + msg);
////        		//progressBar.setProgress(msg.arg1);
////        		//rwThread.;
////        	}
////        };
////        
//        Handler mHandler = new Handler() 
//        {
//            public void handleMessage(Message msg) 
//            {
//              switch (msg.what) 
//              {
//                case SOCKET_CONNECTED: 
//                {
//                	
//                  ReadWriteThread mBluetoothConnection = (ReadWriteThread) msg.obj;
//                  if (!mServerMode)
//                    mBluetoothConnection.write("this is a message".getBytes());
//                  break;
//                }
//                case DATA_RECEIVED: 
//                {
//                	
//                	//ReadWriteThread mBluetoothConnection = (ReadWriteThread) msg.obj;
//                	String data = (String) msg.obj;
//                	System.out.println("%%%%%%%% DATA RECEIVED: "+data); //tv.setText(data);
//                	if (mServerMode)
//                		mBluetoothConnection.write(data.getBytes());
//                }
//              }
//            }
//        };
//        
//        
//        
//        ReadWriteThread rwThread = new ReadWriteThread(bS, mh2);
//        rwThread.start();
//	}
//	
	//--------------------------------------------------------
	
	
    /* Call this from the main activity to shutdown the connection */
//    public void cancel() {
//        try {
//            mmSocket.close();
//        } catch (IOException e) { }
//    }
	
	
	//---------------------------------------------------------
	
	public void connectServer(View v)
	{
		final Boolean mServerMode = false;
		String TAG = "HostServer: ";
		if(ba.isDiscovering())	ba.cancelDiscovery();

        // Get the device MAC address, which is the last 17 chars in the View
        //String info = ((TextView) v).getText().toString();
        //String address = info.substring(info.length() - 17);
        // Create the result Intent and include the MAC address
		
        //Log.i(TAG0, "Item Clicked ---------------");
        Log.i(TAG, chosenDevice + " " + chosenDeviceMac);
        
        
        final Handler mHandler = new Handler() 
        {
            public void handleMessage(Message msg) 
            {
              switch (msg.what) 
              {
                case SOCKET_CONNECTED: 
                {
                	
                  //ReadWriteThread mBluetoothConnection = (ReadWriteThread) msg.obj;
                  BluetoothSocket mBluetoothConnection = (BluetoothSocket) msg.obj;
                  Handler htmp = new Handler();
                  ReadWriteThread rw = new ReadWriteThread(mBluetoothConnection, htmp);
                  
                  takeToGame();
                  
                  if (!mServerMode)
                    rw.write("this is a message".getBytes());
                  break;
                }
                case DATA_RECEIVED: 
                {
                	
                	ReadWriteThread mBluetoothConnection = (ReadWriteThread) msg.obj;
                	String data = (String) msg.obj;
                	System.out.println("%%%%%%%% DATA RECEIVED: "+data); //tv.setText(data);
                	if (mServerMode)
                		mBluetoothConnection.write(data.getBytes());
                }
              }
            }
        };
        
        
        
        ConnectThread connThread = new ConnectThread(bdset.get(CHOSENINDEX), ba, serviceName, MY_UUID, mHandler);
        connThread.start();
        /*
        Intent intent = new Intent();
        intent.putExtra("deviceaddr", address);
        // Set result and finish this Activity
        setResult(Activity.RESULT_OK, intent);
        finish();
        */
        
        /*
        Handler mh2 = new Handler()
        {
        	@Override
        	public void handleMessage(Message msg)
        	{
        		System.out.println("++++ Read activated ++++");
        		progressBar.setProgress(msg.arg1);
        		//rwThread.;
        	}
        };
        
        ReadWriteThread rwThread = new ReadWriteThread(bSocket, mh2);
        rwThread.start();
        String sendText = "I joined";
        byte[] sendBytes = sendText.getBytes();
        rwThread.write(sendBytes);
		*/
        
        
        
	}
	
	//-----------------------------------------------------------
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bmain, menu);
		return true;
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//===========================================================
	
	protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
	    //super.onActivityResult(requestCode, resultCode, resultIntent);
		String TAGonActivityResult = "_OnActivityResult: ";
	    switch(requestCode) {
	    case REQUEST_ENABLE_BT:
	        if(resultCode==RESULT_OK) {
	            Log.i(TAGonActivityResult, "Bluetooth successfully enabled by request");
	        }
	        else {
	            Log.i(TAGonActivityResult, "Bluetooth was not enabled. Finishing.");
	            finish();
	        }
	        break;
	    case REQUEST_VISIBLE_BT:
	    	Log.i(TAGonActivityResult, String.valueOf(resultCode));
	    	if(resultCode == 0)Log.i(TAGonActivityResult, "Bluetooth Discovery On failed");
	    	break;
	    }//END switch
	}//END onActivityResult
	
	//===========================================================
	
	public void onBackPressed()
	{
		this.finish();
		if(BROADCASTREGISTERSTAT != 0)
		{
			unregisterReceiver(br);
			BROADCASTREGISTERSTAT = 0;
		}
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		
		if(BROADCASTREGISTERSTAT != 0)
		{
			unregisterReceiver(br);
			BROADCASTREGISTERSTAT = 0;
		}
	}
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% CLIENT THREAD %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
//	public class DisplayThread extends Thread
//	{
//		String showText;
//		int count=0;
//		DisplayThread(String msg)
//		{
//			showText = msg;
//		}
//		public void run()
//		{
//			while(Thread.currentThread().isAlive())
//			{
//				progressText.setText(showText+"... "+count++);
//			}
//		}
//	}
	/*
	public class AcceptThread extends Thread
	{
		private final BluetoothServerSocket mmServerSocket;
		 
	    public AcceptThread() {
	        // Use a temporary object that is later assigned to mmServerSocket,
	        // because mmServerSocket is final
	        BluetoothServerSocket tmp = null;
	        try {
	            // MY_UUID is the app's UUID string, also used by the client code
	            tmp = ba.listenUsingRfcommWithServiceRecord("mRPSbeta", MY_UUID);
	        } catch (IOException e) { }
	        mmServerSocket = tmp;
	    }
	 
	    public void run()
	    {
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true)
	        {
	            try
	            {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) 
	            {
	                break;
	            }
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	                //manageConnectedSocket(socket);
	                try
	                {
						mmServerSocket.close();
					} catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                break;
	            }
	        }
	    }
	 
	    /** Will cancel the listening socket, and cause the thread to finish 
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) { }
	    }
	}
	*/
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% CLIENT THREAD END %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
	
	
	
//	public class AcceptThread implements Runnable
//	public class AcceptThread extends Thread
//	{
//		private final BluetoothServerSocket mmServerSocket;
//		private BluetoothAdapter mBluetoothAdapter;
//		//private Handler mh;
////		private Message message;
//		BluetoothSocket socket = null;
//		private String NAME;
//		private UUID MY_UUID;
//		Handler h;
//		private static final String TAG1 = "AcceptThread> SocketCreate: ";
//		private static final String TAG2 = "AcceptThread> SocketAccept: ";
//		private static final String TAG3 = "AcceptThread> SocketClose: ";
//		private static final String TAG4 = "AcceptThread> ThreadCancel: ";
//		
//		
//		public static final int STATE_NONE = 0;       // we're doing nothing
//	    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
//	    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
//	    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
//	    
//		 
//	    public AcceptThread(BluetoothAdapter btmp, String name, UUID myuuid, Handler handler) 
//	    {
//	    	mBluetoothAdapter = btmp;
//	    	NAME = name;
//	    	MY_UUID = myuuid;
//	    	h = handler;
//	    	//mh = handler;
//	        // Use a temporary object that is later assigned to mmServerSocket,
//	        // because mmServerSocket is final
//	        BluetoothServerSocket tmp = null;
//	        try 
//	        {
//	            // MY_UUID is the app's UUID string, also used by the client code
//	            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
//	        } catch (IOException e) 
//	        {
//	        	Log.i(TAG1, "Error"); e.printStackTrace();
//	        }
//	        mmServerSocket = tmp;
//	    }
//	    //-----------------------------------------------------------------
//	    
//	    /*
//	    private void manageConnectedSocket() 
//	    {
//	    	  ConnectedThread transferThread = new ConnectedThread(mBluetoothSocket, mHandler);
//	    	  mHandler.obtainMessage(
//	    	DataTransferActivity.SOCKET_CONNECTED, conn)
//	    								.sendToTarget();
//	    	  conn.start();
//	    }
//	    */
//	    
//	    
//	    //-----------------------------------------------------------------
//	    
//	    
//		public BluetoothSocket getBluetoothSocket() { return socket; } 
//	 
//	    public void run() 
//	    {
//	    	//Handler h1 = new Handler();
//	    	
//	    	for(int i=1;i<100;i++)
//        	{
//	    		Message message = Message.obtain();
//        		message.arg1 = i;
//        		h.sendMessage(message);
//        	}
//	        //BluetoothSocket socket = null;
//	        // Keep listening until exception occurs or a socket is returned
//	        while (true) 
//	        {
//	        	
//	            try 
//	            {
//	                socket = mmServerSocket.accept();
//	            } 
//	            catch (IOException e) 
//	            {
//	                Log.i(TAG2, "Error");
//	                e.printStackTrace();
//	            	break;
//	            }
//	            // If a connection was accepted
//	            if (socket != null) 
//	            {
//	                // Do work to manage the connection (in a separate thread)
//	                //manageConnectedSocket(socket);
//	                try 
//	                {
//						mmServerSocket.close();
//					} catch (IOException e) 
//					{
//						Log.i(TAG3, "Error");
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//	                break;
//	            }
//	        }
//	    }
//	 
//	    /** Will cancel the listening socket, and cause the thread to finish */
//	    public void cancel() 
//	    {
//	        try 
//	        {
//	            mmServerSocket.close();
//	        } catch (IOException e)
//	        { Log.i(TAG4, "Error"); e.printStackTrace(); }
//	    }
//	}
	
	
}
