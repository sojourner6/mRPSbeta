package com.example.mrpsbeta;

import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
//import android.widget.Toast;

public class StatScreen extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stat_screen);
		
		fetchCursor();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stat_screen, menu);
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
	
	public void fetchCursor()
	{
		String cumData="";
		SQLiteDatabase db;
		String dbPath="\\sdcard\\MRPSDB";
		db=openOrCreateDatabase(dbPath,MODE_PRIVATE, null);
		Cursor c=db.rawQuery("select * from RPSM where ures<>''", null);
		//Cursor c=db.rawQuery("select * from MRPSM", null);
		int count = c.getCount(), j;
		c.moveToFirst();
		for (j=0;j<count;j++)
		{			
			if(c.getString(c.getColumnIndex("ures"))==" ")
			{
				c.moveToNext();
			}
			else
			{
			
			cumData+=c.getString(c.getColumnIndex("uid"))+":"+c.getString(c.getColumnIndex("uage"))+
					":"+c.getString(c.getColumnIndex("ugndr"))+":"+c.getString(c.getColumnIndex("ures"))
					+":"+c.getString(c.getColumnIndex("uchoice"))+":"+c.getString(c.getColumnIndex("opchoice"))+'\n';
			c.moveToNext();
			}
		}
		c.close();
		db.close();
		if(cumData=="")
		{
			cumData="No games played so far :(";
		}
		TextView tstat=(TextView)findViewById(R.id.textView2);
		tstat.setMovementMethod(new ScrollingMovementMethod());
		//yourTextView.setMovementMethod(new ScrollingMovementMethod())
		tstat.setText(cumData);
	}
	
	
	@Override
	public void onBackPressed()
	{
		StatScreen.this.finish();
	}
	
}
