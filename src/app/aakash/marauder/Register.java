package app.aakash.marauder;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	TextView t;
	Location l1;
	Button butt,b2;
	EditText e1,e2,e3;
	static String a,b,c;
	boolean unique=false;
	GPS g;
	OnClickListener listener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		setContentView(R.layout.register);
		t= (TextView) findViewById(R.id.valid);
		t.setTextColor(Color.RED);
		butt= (Button) findViewById(R.id.btnregister);
		e1= (EditText) findViewById(R.id.fieldname);
		e2= (EditText) findViewById(R.id.fielduid);
		b2= (Button) findViewById(R.id.btnvld);
		e3= (EditText) findViewById(R.id.fieldpass);
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				validate();
			}
		});
		g= new GPS(this.getBaseContext());
		butt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				a=e1.getText().toString();
				b=e2.getText().toString();
				c=e3.getText().toString();
				validate();
				if(!unique || a.isEmpty() || b.isEmpty() || c.isEmpty()){
					t.setText("Check if all values are entered..");
					t.setTextColor(Color.RED);
				}
				else if(unique && !a.isEmpty() && !b.isEmpty() && !c.isEmpty()){
					create();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	void create(){
		String a=e1.getText().toString();
		String b=e2.getText().toString();
		String c=e3.getText().toString();
		l1=g.getloc();
		String lat=Double.toString(l1.getLatitude());
		String lon=Double.toString(l1.getLongitude());
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("name",a));
		parameters.add(new BasicNameValuePair("username",b));
		parameters.add(new BasicNameValuePair("password",c));
		parameters.add(new BasicNameValuePair("latitude",lat));
		parameters.add(new BasicNameValuePair("longitude",lon));
		Json parser = new Json();
		JSONObject j= parser.makeHttpRequest("http://aakashgps.site88.net/register.php", "POST" , parameters);
		try {
            int success = j.getInt("success");
            if (success == 1) {
            	Log.e("Done!",j.getString("message"));
            	Toast.makeText(getApplicationContext(), "Username "+b+" created!", Toast.LENGTH_LONG).show();
            	Intent i = new Intent(getApplicationContext(), Login.class);
            	startActivity(i);
            	finish();
            } else {
                Log.e("SQL FAULT", j.getString("message"));
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	void validate(){
		String a=e2.getText().toString();
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("username",a));
		Json parser = new Json();
		JSONObject j= parser.makeHttpRequest("http://aakashgps.site88.net/validate_user.php", "POST" , parameters);
		try {
            int success = j.getInt("success");
            if (success == 1) {
            	Log.e("Done!",j.getString("message"));
            	t.setTextColor(Color.GREEN);
            	t.setText("User available!");
            	unique=true;
            } else {
                Log.e("SQL FAULT", j.getString("message"));
                t.setText(j.getString("message"));
                t.setTextColor(Color.RED);
            	t.setVisibility(TextView.VISIBLE);
            	unique=false;
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}