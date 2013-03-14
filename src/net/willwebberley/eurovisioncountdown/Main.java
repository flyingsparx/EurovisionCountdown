package net.willwebberley.eurovisioncountdown;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

@SuppressLint("NewApi") public class Main extends Activity {

	private Date today, euro;
	private TextView weeks, days;
	
	private final String finalDate = "18/05/2013";
	
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*
         * Hide the action bar so the activity can take up the whole window
         * (inside try-catch as this caused force-closes on some devices)
         */
        try{
        	System.out.println("Trying to hide action bar...");
        	ActionBar actionBar = getActionBar();
        	actionBar.hide();
        }
        catch(Exception e){
        	System.err.println(e);
        }
        
       setDate();
        
    }
    
    /*
     * Calculate difference between today and the date of the final in integer weeks and days.
     * (Passes the absolute day difference too so updater knows if day is negative)
     */
    private void setDate(){
    	weeks = ((TextView)findViewById(R.id.weeks));
        days = ((TextView)findViewById(R.id.days));
                
        try{
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        	euro = (Date)sdf.parse(finalDate);
        	today = new Date(System.currentTimeMillis());
        }
        catch(Exception e){
        	System.err.println(e);
        }
        
    	DateTime dateTime1 = new DateTime(today);
        DateTime dateTime2 = new DateTime(euro);
        
        int dayDiff = Days.daysBetween(dateTime1.toDateMidnight() , dateTime2.toDateMidnight() ).getDays();
        int weekDiff =  Weeks.weeksBetween(dateTime1.toDateMidnight(), dateTime2.toDateMidnight()).getWeeks();
        
        int weekInt = dayDiff / 7;
        int dayInt = dayDiff % 7;
        
        updateUI(weekDiff, dayInt, dayDiff);        
    }
    
    /*
     * Update UI with new week and day integers. Various rules defined for different difference outcomes.
     */
    private void updateUI(int weekInt, int dayInt, int dayDiff){
    	if(weekInt == 1){
        	weeks.setText(weekInt+" week");
        }
        if(weekInt > 1 || weekInt == 0){
        	weeks.setText(weekInt+" weeks");
        }
        if(dayInt == 1){
        	days.setText(dayInt+" day");
        }
        if(dayInt > 1 || dayInt == 0){
        	days.setText(dayInt+" days");
        }
        
        if(weekInt == 0){
        	weeks.setText(dayInt+" days");
        	((TextView)findViewById(R.id.days)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.andLabel)).setVisibility(View.INVISIBLE);
        }
        if(dayDiff < 0){
        	weeks.setText("The 2013 final is over :(");
        	((TextView)findViewById(R.id.days)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.andLabel)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.remainingLabel)).setVisibility(View.INVISIBLE);
        }
        if(dayInt == 1 && weekInt == 0){
        	weeks.setText("The final is tomorrow!");
        	((TextView)findViewById(R.id.days)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.andLabel)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.remainingLabel)).setVisibility(View.INVISIBLE);
        }        
        if(weekInt == 0 && dayInt == 0){
        	weeks.setText("The 2013 final is today!");
        	((TextView)findViewById(R.id.days)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.andLabel)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.remainingLabel)).setVisibility(View.INVISIBLE);
        }
        String styledText = "Visit <span style=\"color:blue\"><u>eurovision.tv</u></span> for more info";
        ((TextView)findViewById(R.id.link)).setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
    }
    
    /*
     * Method to handle the click of the eurovision.tv link. Opens link in browser.
     */
    public void openLink(View view){
    	System.out.println("here");
    	String url = "http://www.eurovision.tv";
    	Intent i = new Intent(Intent.ACTION_VIEW);
    	i.setData(Uri.parse(url));
    	startActivity(i); 
    }
    
    /*
     * Method to handle click of the about button. Starts the About activity.
     */
    public void showAbout(View view){
    	Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }    
    
    /*
     * The About button (to start About activity) will also be available through the menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.aboutMenu:
                showAbout(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
}
