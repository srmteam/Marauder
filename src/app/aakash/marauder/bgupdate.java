package app.aakash.marauder;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.content.Context;
import android.location.Location;
import android.util.Log;

public class bgupdate{
	public static void update(Context context){
		//Boolean bool = true;
		//while(bool){	
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username",Login.user));
			GPS g = new GPS(context);
			Location l = g.getloc();
			params.add(new BasicNameValuePair("latitude",Double.toString(l.getLatitude())));
			params.add(new BasicNameValuePair("longitude",Double.toString(l.getLongitude())));
			Json j = new Json();
			JSONObject jobj = j.makeHttpRequest("http://aakashgps.site88.net/update.php", "POST", params);
			try {
	            int success = jobj.getInt("success");
	            if (success == 1) {
	            	Log.e("service done!",jobj.getString("message"));
	            } else {
	                Log.e("service SQL FAULT", jobj.getString("message"));
	            }
			}catch(Exception e){
				e.printStackTrace();
			}
		//}
	}
}
