package com.example.mrpsbeta;


import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
//import android.text.Editable;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class LogScreen extends ActionBarActivity {

	String dbPath="\\sdcard\\MRPSDB",dbuid,dbseq,dbuage,dbugndr="",dbures,dbuchoice,dbopchoice;
	SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_screen, menu);
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
	
	

	@SuppressLint("NewApi")
	public void takeToHome(View v) throws Exception
	{
		int exceptionFlag=0;
		EditText euname=(EditText)findViewById(R.id.editText1);
		EditText eage=(EditText)findViewById(R.id.editText2);
		//String uname=euname.getText().toString();
		//String age=eage.getText().toString();
		//String gender="M/F";
		RadioButton r1=(RadioButton)findViewById(R.id.radio0);
		RadioButton r2=(RadioButton)findViewById(R.id.radio1);
		//Toast.makeText(this,"Gender:"+gender, Toast.LENGTH_SHORT).show();
		
		//String dbPath="\\sdcard\\MRPSDB",dbuid,dbseq,dbuage,dbugndr="",dbures,dbuchoice,dbopchoice;//fullData;
		db=openOrCreateDatabase(dbPath,MODE_PRIVATE, null);
		db.execSQL("Create table if not exists RPSM(uid varchar(10) not null," +
				"seq varchar(10) not null, " +
				"uage varchar(3) not null," +
				" ugndr varchar(1) not null," +
				"ures varchar(1) not null, uchoice varchar(1) not null," +
				" opchoice varchar(1) not null, PRIMARY KEY (uid,seq));");
		dbuid=euname.getText().toString();
		dbuage=eage.getText().toString();
		System.out.println("Input Age: "+dbuage);
		try
		{
			if(dbuid.equals(""))
			{
				exceptionFlag++;
				throw new IOException("No Username");
			//throw new Exception("No value in Age");
			}	
			if(dbuage.equals(""))
			{
				exceptionFlag++;
				throw new IOException("No Age");
			//throw new Exception("No value in Age");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(this,"Please enter Username/Age", Toast.LENGTH_SHORT).show();
		}
		if(exceptionFlag==0)
		{
		if(r1.isChecked())
		{
			dbugndr="M";
		}
		else if(r2.isChecked())
		{
			dbugndr="F";
		}
		dbures="";
		dbuchoice="";
		dbopchoice="";
		Cursor c=db.rawQuery("select * from RPSM where uid = '"+dbuid+"';", null);
		try
		{
			//Cursor c=db.rawQuery("select * from RPSM where uid = '"+dbuid+"';", null);
			if(c.getCount()>0)
			{
				c.moveToFirst();
				dbseq=c.getString(c.getColumnIndex("seq"));
				//int seqno=Integer.parseInt(dbseq);
				//dbseq=String.valueOf(seqno);
				c.close();
				db.close();
				Toast.makeText(this," ReLogging in..."+dbuid, Toast.LENGTH_SHORT).show();
				startHomeScreen();
			}
			else
			{
				dbseq="0";
				try
				{
					db.execSQL("insert into RPSM values('"+dbuid+"','"+dbseq+"','"+dbuage+"','"
							+dbugndr+"','"+dbures+"'" +
							",'"+dbuchoice+"','"+dbopchoice+"');");
					db.close();
					Toast.makeText(this,"Logging in... "+dbuid, Toast.LENGTH_SHORT).show();
					startHomeScreen();
				}
				catch (Exception e)
				{
					Toast.makeText(this,"Re-enter user data", Toast.LENGTH_SHORT).show();
					c.close();
					db.close();
				}
			}
		}

		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.toString());
			Toast.makeText(this,"Reenter user data", Toast.LENGTH_SHORT).show();
			db.close(); c.close();
		}
		c.close();
		}
		//Toast.makeText(this,"User: "+dbuid+" Logging in...", Toast.LENGTH_SHORT).show();
		//db.close();
			//fullData=dbuid+dbuage+dbugndr;
		/*Intent i=new Intent(this,HomeScreen.class);
		i.putExtra("userAge", dbuage);
		i.putExtra("userID", dbuid);
		i.putExtra("userSeq", dbseq);
		i.putExtra("userGender", dbugndr);
		startActivity(i);*/


		//db.close();
		//fullData=dbuid+dbuage+dbugndr;
		//Intent i=new Intent(this,HomeScreen.class);
		//i.putExtra("username", fullData);
		//i.putExtra("userID", dbuid);
		//startActivity(i);
		/*Toast.makeText(this,"Logging in ...", Toast.LENGTH_SHORT).show();
		EditText textname = (EditText)findViewById(R.id.t_uName);
		EditText textage = (EditText)findViewById(R.id.t_uAge);
		EditText textgender = (EditText)findViewById(R.id.t_uGender);
		
		openDB();
		String ins_name = textname.toString();
		String ins_age = textage.toString();
		String ins_gender = textgender.toString();
		addUserData(ins_name,ins_age,ins_gender);
		
		startActivity(new Intent(this, GameScr.class));*/
	}
	
	private void startHomeScreen()
	{
		Intent i=new Intent(this,HomeScreen.class);
		i.putExtra("userAge", dbuage);
		i.putExtra("userID", dbuid);
		i.putExtra("userSeq", dbseq);
		i.putExtra("userGender", dbugndr);
		startActivity(i);
	}
	
	
	@Override
	public void onBackPressed()
	{
		Toast.makeText(this,"Thank You... "+dbuid, Toast.LENGTH_SHORT).show();
		LogScreen.this.finish();
	}
	
}

