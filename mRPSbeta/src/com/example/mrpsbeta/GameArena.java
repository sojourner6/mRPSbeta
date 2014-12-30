package com.example.mrpsbeta;

//import java.sql.Date;
import java.util.ArrayList;

//import com.example.newdraw.R;

import android.support.v7.app.ActionBarActivity;
//import android.text.format.Time;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
//import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class GameArena extends ActionBarActivity implements OnTouchListener {

	ArrayList <Point> pt;
	ArrayList <Integer> dX;
	ArrayList <Integer> dY;
	ImageView imageView;
	Bitmap bitmap;
	Canvas canvas;
	Paint paint;
	String uChoiceStr="X", bChoiceStr="X";
	float downx = 0, downy = 0, upx = 0, upy = 0;
	int cell=0, noVertices;
	int standardDev=9;
	String uid,seq,age,gender,uChoice,bChoice,uResult;
	String coordinates="";	
	//Latest
	int curveTol=50;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_arena);
		
		imageView = (ImageView) this.findViewById(R.id.imageView1);
		
		uid=getIntent().getExtras().getString("uName");
		seq=getIntent().getExtras().getString("uSeq");
		age=getIntent().getExtras().getString("uAge");
		gender=getIntent().getExtras().getString("uGender");
		
		pt = new ArrayList<Point>();
		dX = new ArrayList<Integer>();
		dY = new ArrayList<Integer>();
		
		setCanvas();
		
	    canvas.save();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_arena, menu);
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
	
	
	
	
	
	@SuppressLint("ClickableViewAccessibility") // check this
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		Point p;
		float oldX, oldY;
	    switch (action)
	    {
	    case MotionEvent.ACTION_DOWN:
	      downx = event.getX();
	      downy = event.getY();
	      p = new Point((int)downx,(int)downy);
	      pt.add(p);cell++;
	      //System.out.println(String.valueOf(downx)+","+String.valueOf(downy));
	      break;
	    case MotionEvent.ACTION_MOVE:
	    	oldX=pt.get(cell-1).x;
		    oldY=pt.get(cell-1).y;cell++;
	    	p = new Point((int)event.getX(),(int)event.getY());
	    	upx = event.getX();
		    upy = event.getY();
		    pt.add(p);
		    System.out.println(upx+","+upy);
		    canvas.drawLine(oldX, oldY, upx, upy, paint);
		    imageView.invalidate();
		    coordinates+="("+(int)upx+","+(int)upy+")";
	    	//System.out.println((int)upx+","+(int)upy);
	        break;
	    case MotionEvent.ACTION_UP:
	      
	      formDiffArray();
		  determineVertices();
	      formResult();
	      //updateDB();
	      Intent i=new Intent(this,TestScreen.class);
		  //i.putExtra("cxy", coordinates);
		  i.putExtra("userc", uChoice);
		  i.putExtra("botc", bChoice);
		  i.putExtra("result", uResult);
		  startActivity(i);
		  updateDB();
	      noVertices=1;
	      pt.clear();
	      dX.clear();
	      dY.clear();
	      this.finish();
	      //canvas.restore();
	      break;
	    case MotionEvent.ACTION_CANCEL:
	      break;
	    default:
	      break;
	    }
	    return true;
	  }
	
	
	
	
	private void formDiffArray()
	{
		int i,j;
		float x1,x2,y1,y2,diffX,diffY;
		for(i=0;i<pt.size()-1;i++) 
			/* from 1 and not 0 because coordinates at ACTION_DOWN 
			   and initial ACTION_MOVE are almost similar */ 
		{
			
			j=i+1;
			x1=pt.get(i).x; x2=pt.get(j).x;
			y1=pt.get(i).y; y2=pt.get(j).y;
			diffX=x2-x1;
			diffY=y2-y1;
			dX.add((int) diffX);
			dY.add((int) diffY);
			System.out.println(String.valueOf(dX.get(i))+","+String.valueOf(dY.get(i)));
		}
	}
	
	private void determineVertices()
	{
		int i,baseY=pt.get(0).y,diffdX=0,diffdY=0;
		//int baseX=pt.get(0).x, diffdX1=0,diffdY1=0;
		int axis=0,maxDepth=0,maxWidth=0, depth;
		for(i=2;i<dX.size()-1;i++)
		{
			diffdX=Math.abs(dX.get(i+1)-dX.get(i));
			diffdY=Math.abs(dY.get(i+1)-dY.get(i));
			if(axis%2==0)
			{
				if(diffdX>standardDev && diffdY<standardDev)
				{
					System.out.println("axis: "+axis+","+diffdX+","+diffdY);
					noVertices++;
					axis++;
				}
			}
			else
			{
				//diffdY=Math.abs(dY.get(i+1)-dY.get(i));
				if(diffdY>standardDev && diffdX<standardDev)
				{
					System.out.println("axis: "+axis+","+diffdX+","+diffdY);
					noVertices++;
					axis++;
				}
			}	
		}
		for(i=0;i<pt.size();i++)
		{
			depth=Math.abs(pt.get(i).y-baseY);
			if(depth>maxDepth)
			{
				maxDepth=depth;
			}				
		}
		
		//Latest Change
		
		int refX,refY,minX=0,maxX=0,minY=0,maxY=0,absX=0,absY=0,negativeDev=0;
		for(i=0;i<pt.size();i++)
		{
			refX=pt.get(i).x;
			refY=pt.get(i).y;
			
			if(minX>refX)
			{
				minX=refX;
				//maxDepth=depth;
			}
			if(minY>refY)
			{
				minY=refY;
				//maxDepth=depth;
			}
			if(maxX<refX)
			{
				maxX=refX;
				//maxDepth=depth;
			}
			if(maxY<refY)
			{
				maxY=refY;
				//maxDepth=depth;
			}
			negativeDev=Math.abs(pt.get(0).x-minX);
			absX=Math.abs(maxX-minX);
			absY=Math.abs(maxY-minY);	
		}
		System.out.println("minX: "+minX);
		System.out.println("minY: "+minY);
		System.out.println("maxX: "+maxX);
		System.out.println("maxY: "+maxY);
		System.out.println("absX: "+absX);
		System.out.println("absY: "+absY);
		System.out.println("maxWidth: "+maxWidth);
		System.out.println("maxDepth: "+maxDepth);
		System.out.println("curveTol: "+curveTol);
		
		// LAtest End
		
		maxWidth=pt.get(i-1).x-pt.get(0).x;
		System.out.println("width: "+maxWidth+" depth: "+maxDepth);
		if(maxWidth>(10*standardDev) && maxDepth>(5*standardDev))
		{
			System.out.println("Resetting Vertices for Scissors, Vertices: "+noVertices);
			noVertices=99;
		}
		else if (noVertices>=3 && noVertices<10)
		{
			System.out.println("Resetting Vertices for Paper, Vertices: "+noVertices);
			noVertices=4;
		}
		else
		{
			System.out.println("Resetting Vertices for Rock, Vertices: "+noVertices);
			noVertices=1;
		}
			
		
		
		//Latest
		
		/*else if(Math.abs(absX-absY)<=curveTol && negativeDev>curveTol)
		{
			System.out.println("Resetting to Circle, vertices: "+noVertices);
			noVertices=1;
		}*/
		/*if(noVertices>=4 && noVertices<10)
		{
			System.out.println("Resetting Vertices for Paper, Vertices: "+noVertices);
			noVertices=4;
		}
		else
		{
			System.out.println("Resetting Vertices for Rock, Vertices: "+noVertices);
			noVertices=1;
		}*/
			
		System.out.println("Vertices = "+noVertices);
		// Latest End
		
		//noVertices=1;
	}
	
	
	private void formResult()
	{
		int botChoice=0, uChoiceNo=0;
		//botChoice++;
		ArrayList<String> choice=new ArrayList<String>();
		String rock=new String("Rock");
		String paper=new String("Paper");
		String scissor=new String("Scissor");
		
		choice.add(rock); choice.add(paper);choice.add(scissor);
		//Latest
		//if(noVertices<=3)
		if(noVertices==1)
		//Latest end
		{
			uChoiceNo=0; uChoice="R";			
		}
		if(noVertices==99)
		{
			uChoiceNo=2; uChoice="S";
		}
		//Latest
		//if(noVertices>=4 && noVertices<10)
		if(noVertices==4)
		//Latest End
		{
			uChoiceNo=1; uChoice="P";
		}
		botChoice=getBotChoice();
		//Toast.makeText(this,"You have chosen "+choice.get(uChoiceNo), Toast.LENGTH_LONG).show();
		//uChoiceStr="You have chosen "+choice.get(uChoiceNo), Toast.LENGTH_LONG).show();
		//Toast.makeText(this,"The Bot has chosen "+choice.get(botChoice), Toast.LENGTH_LONG).show();
				
		if(botChoice==uChoiceNo)
		{
			//Toast.makeText(this,"Please try again..", Toast.LENGTH_LONG).show();
			uResult="D";
		}		
		if(botChoice==0 && uChoiceNo==1)
		{
			//Toast.makeText(this,"Congratulations! You have WON!", Toast.LENGTH_LONG).show(); 
			uResult="U";
		}
		if(botChoice==0 && uChoiceNo==2)
		{
			//Toast.makeText(this,"Sorry! The Bot has WON!", Toast.LENGTH_LONG).show(); 
			uResult="B";
		}
		if(botChoice==1 && uChoiceNo==0)
		{
			//Toast.makeText(this,"Sorry! The Bot has WON!", Toast.LENGTH_LONG).show(); 
			uResult="B";
		}
		if(botChoice==1 && uChoiceNo==2)
		{
			//Toast.makeText(this,"Congratulations! You have WON!", Toast.LENGTH_LONG).show(); 
			uResult="U";
		}
		if(botChoice==2 && uChoiceNo==0)
		{
			//Toast.makeText(this,"Congratulations! You have WON!", Toast.LENGTH_LONG).show(); 
			uResult="U";
		}
		if(botChoice==2 && uChoiceNo==1)
		{
			//Toast.makeText(this,"Sorry! The Bot has WON!", Toast.LENGTH_LONG).show(); 
			uResult="B";
		}
		
			
	}
	
	
	private int getBotChoice()
	{
		//Time t=new Time();
		//int i=t.second;
		int randNum=((123456+(pt.get(pt.size()-1).x))/pt.get(0).y)%3;
		System.out.println(pt.get(pt.size()-1).x+","+pt.get(0).y+","+randNum);
		switch (randNum)
		{
		case 0: bChoice="R"; break;
		case 1: bChoice="P"; break;
		case 2: bChoice="S"; break;
		default:bChoice="U";
		}
		return randNum;
	}
	
	
	private void setCanvas()
	 {
		 Display currentDisplay = getWindowManager().getDefaultDisplay();
		 @SuppressWarnings("deprecation")
		float dw = currentDisplay.getWidth();
		 @SuppressWarnings("deprecation")
		float dh = currentDisplay.getHeight();
		 bitmap = Bitmap.createBitmap((int) dw, (int) dh,
		 Bitmap.Config.ARGB_8888);
		 canvas = new Canvas(bitmap);
		 imageView.setBackgroundColor(Color.BLACK);
		 paint = new Paint();
		 paint.setDither(true);
		 paint.setColor(Color.YELLOW);
		 paint.setStyle(Style.FILL_AND_STROKE);
		 paint.setStrokeJoin(Paint.Join.ROUND);
		 paint.setStrokeCap(Paint.Cap.ROUND);
		 paint.setStrokeWidth(8);
	     imageView.setImageBitmap(bitmap);		 
	     imageView.setOnTouchListener(this);
	     noVertices=1;
	 }
	
	private void updateDB()
	{
		SQLiteDatabase db = null;
		String rseq;
		String dbPath="\\sdcard\\MRPSDB";
		int i,seqno=0,refseq=0;
		try
		{
			//int seqno=Integer.parseInt(seq);
			//seqno++;
			//seq=String.valueOf(seqno);
			db=openOrCreateDatabase(dbPath,MODE_PRIVATE, null);
			Cursor c=db.rawQuery("select * from RPSM where uid='"+uid+"';", null);
			c.moveToFirst();
			for(i=0;i<c.getCount();i++)
			{
				rseq=c.getString(c.getColumnIndex("seq"));
				seqno=Integer.parseInt(rseq);
				System.out.println("Sequence: "+seqno);
				if(seqno>refseq){ refseq=seqno;}
				c.moveToNext();
			}
			c.close();
			refseq++;
			
			//seq=c.getString(c.getColumnIndex("uid"));
			//int seqno=Integer.parseInt(seq);
			//seqno++;
			seq=String.valueOf(refseq);
			String qry="insert into RPSM values('"+uid+"','"+seq+"','"+age+"','"+gender+"','"
					+uResult+"','"+uChoice+"','"+bChoice+"');";
			System.out.println(qry);
			db.execSQL("insert into RPSM values('"+uid+"','"+seq+"','"+age+"','"+gender+"','"
					+uResult+"','"+uChoice+"','"+bChoice+"');");
			Toast.makeText(this,"Scores updated ...", Toast.LENGTH_SHORT).show();
			db.close();
			//fullData=dbuid+dbuage+dbugndr;
			//Intent i=new Intent(this,HomeScreen.class);
			//i.putExtra("userAge", dbuage);
			//i.putExtra("userID", dbuid);
			//i.putExtra("userGender", dbugndr);
			//startActivity(i);
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
			Toast.makeText(this,"Scores could not be updated ...", Toast.LENGTH_SHORT).show();
			db.close();
		}
		//Toast.makeText(this,"Please go back and start a new Game!", Toast.LENGTH_SHORT).show();
	}
	
	public void onBackPressed()
	{
		//Toast.makeText(this,"Thank You... "+dbuid, Toast.LENGTH_SHORT).show();
		GameArena.this.finish();
	}
	
	
}
