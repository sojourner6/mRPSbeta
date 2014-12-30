package com.example.mrpsbeta;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends ActionBarActivity {
	String user,seq,age,gender;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		TextView tWelcome=(TextView)findViewById(R.id.textView2);
		user=getIntent().getExtras().getString("userID");
		seq=getIntent().getExtras().getString("userSeq");
		age=getIntent().getExtras().getString("userAge");
		gender=getIntent().getExtras().getString("userGender");
		tWelcome.setText("Welcome, "+user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
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
	
	
	
	
	public void takeToGameArena(View v)
	{
		//int seqno=Integer.parseInt(seq);
		//seqno++;
		//seq=String.valueOf(seqno);
		Intent i=new Intent(this, GameArena.class);
		i.putExtra("uName", user);
		i.putExtra("uSeq", seq);
		i.putExtra("uAge", age);
		i.putExtra("uGender", gender);
		startActivity(i);
	}
	
	
	public void takeToMultiplayer(View v)
	{
		//int seqno=Integer.parseInt(seq);
		//seqno++;
		//seq=String.valueOf(seqno);
		Intent i=new Intent(this, BMain.class);
		i.putExtra("uName", user);
		i.putExtra("uSeq", seq);
		i.putExtra("uAge", age);
		i.putExtra("uGender", gender);
		startActivity(i);
	}
	
	
	
	public void toStatScreen(View v)
	{
		Intent i=new Intent(this,StatScreen.class);
		startActivity(i);
	}
	
	
	public void takeToTutorial(View v)
	{
		Intent i=new Intent(this,GameTutorial.class);
		startActivity(i);
	}

	
	@Override
	public void onBackPressed()
	{
		Toast.makeText(this,"Logging out "+user, Toast.LENGTH_SHORT).show();
		HomeScreen.this.finish();
	}
	
}
