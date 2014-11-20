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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	Button b,b1;
	Animation a;
	EditText e1,e2;
	ProgressDialog pDialog;
	public static String user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		a=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide);
		b=(Button) findViewById(R.id.btnregister);
		b1=(Button) findViewById(R.id.btnlogin);
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Validate().execute();
			}
		});
		e1=(EditText) findViewById(R.id.fielduser);
		e2=(EditText) findViewById(R.id.fieldpwd);
		e1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				e1.setText(null);
			}
		});
		e2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				e2.setText(null);
			}
		});
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Register.class);
				startActivity(i);
				overridePendingTransition(R.anim.upin,R.anim.upout);
				finish();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	class Validate extends AsyncTask<String, String, String>{
		Location l1;
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Connecting..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String a=e1.getText().toString();
			String b=e2.getText().toString();
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("username",a));
			parameters.add(new BasicNameValuePair("password",b));
			
			Json parser = new Json();
			JSONObject j= parser.makeHttpRequest("http://aakashgps.site88.net/validate_login.php", "POST" , parameters);
			try {
		        int success = j.getInt("success");
		        if (success == 1) {
		            Log.e("Done!",j.getString("message"));
		            Intent i = new Intent(getApplicationContext(), NameList.class);
					startActivity(i);
					user = e1.getText().toString();
					finish();
		        } else {
		           	CharSequence c = j.getString("message");
		            Log.e("SQL FAULT", j.getString("message"));
		            Toast.makeText(getApplicationContext(), c, Toast.LENGTH_LONG).show();
		        }
		    }catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		 protected void onPostExecute(String file_url) {
	            // dismiss the dialog after getting all products
	            pDialog.dismiss();
		 }
		
	}
}
