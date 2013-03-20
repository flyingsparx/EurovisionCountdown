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

public class Main extends Activity {

	private Date today, euro;
	private TextView weeks, days;
	
	private final String finalDate = "18/05/2013";
	
	private final String infoDate = "18th May 2013";
	private final String infoVenue = "Malmö, Sweden";
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
        int weekInt =  Weeks.weeksBetween(dateTime1.toDateMidnight(), dateTime2.toDateMidnight()).getWeeks();
        
        int dayInt = dayDiff % 7;
        
        updateUI(weekInt, dayInt, dayDiff);        
    }
    
    /*
     * Update UI with new week and day integers. Various rules defined for different difference outcomes.
     */
    private void updateUI(int weekInt, int dayInt, int dayDiff){
    	if(weekInt == 1){
        	weeks.setText(weekInt+" week");
        	((TextView)findViewById(R.id.totalDays)).setVisibility(View.INVISIBLE);
        }
        if(weekInt >1){
        	weeks.setText(weekInt+" weeks");
        }
        if(weekInt > 0){
        	((TextView)findViewById(R.id.totalDays)).setVisibility(View.VISIBLE);
        	((TextView)findViewById(R.id.totalDays)).setText("That's "+dayDiff+" days!");
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
        	((TextView)findViewById(R.id.totalDays)).setVisibility(View.INVISIBLE);
        }
        if(dayDiff < 0){
        	weeks.setText("The 2013 final is over");
        	((TextView)findViewById(R.id.totalDays)).setText("Please wait for the app update for information on the 2014 final");
        	((TextView)findViewById(R.id.weeks)).setVisibility(View.VISIBLE);
        	((TextView)findViewById(R.id.days)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.totalDays)).setVisibility(View.VISIBLE);
        	((TextView)findViewById(R.id.andLabel)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.remainingLabel)).setVisibility(View.INVISIBLE);
        }
        if(dayInt == 1 && weekInt == 0){
        	weeks.setText("The final is tomorrow!");
        	((TextView)findViewById(R.id.days)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.andLabel)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.remainingLabel)).setVisibility(View.INVISIBLE);
        }        
        if(dayInt == 0 && weekInt == 0){
        	weeks.setText("The 2013 final is today!");
        	((TextView)findViewById(R.id.days)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.andLabel)).setVisibility(View.INVISIBLE);
        	((TextView)findViewById(R.id.remainingLabel)).setVisibility(View.INVISIBLE);
        }
        String styledText = "Visit <span style=\"color:blue\"><u>eurovision.tv</u></span> for more info";
        ((TextView)findViewById(R.id.link)).setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        
        String styledText2 = "<b>Date:</b> "+infoDate;
        String styledText3 = "<b>Venue:</b> "+infoVenue;
        ((TextView)findViewById(R.id.infoDate)).setText(Html.fromHtml(styledText2), TextView.BufferType.SPANNABLE);
        ((TextView)findViewById(R.id.infoVenue)).setText(Html.fromHtml(styledText3), TextView.BufferType.SPANNABLE);
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
