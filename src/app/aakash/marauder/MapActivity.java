package app.aakash.marauder;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MapActivity extends FragmentActivity {
	private GoogleMap m=null;
	Button b;
	Location l1=null, loc;
	double lat=0,lon=0;
	CameraPosition cameraPosition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		pointMe();
		b=(Button) findViewById(R.id.btnloc);
		b.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), NameList.class);
				
				startActivity(i);
				finish();
			}
		});
		Intent intent = getIntent();
		lat= intent.getDoubleExtra("latitude" , 0);
		lon = intent.getDoubleExtra("longitude", 0);
		String time = intent.getStringExtra("time");
		String name = intent.getStringExtra("name");
		Log.e("name", name);
		Log.e("lat", Double.toString(lat));
		Log.e("lon", Double.toString(lon));
		Log.e("time", time);
		LatLng lng = new LatLng(lat,lon);
		m.addMarker(new MarkerOptions().position(lng).title(name+"~("+time+")").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		loc = new Location(l1);
		loc.setLatitude(lat);
		loc.setLongitude(lon);
		Json json = new Json();
		try {
			String url = Json.getUrl(l1, loc);
			String j = json.getJSON(url);
           final JSONObject jobj = new JSONObject(j);
           JSONArray routeArray = jobj.getJSONArray("routes");
           JSONObject routes = routeArray.getJSONObject(0);
           JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
           String encodedString = overviewPolylines.getString("points");
           List<LatLng> list = decodePoly(encodedString);
           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                m.addPolyline(new PolylineOptions().add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,dest.longitude)).width(8).color(Color.BLUE).geodesic(true));
            }
		} 
		catch (JSONException e) {
			Log.e("JSON Exception", "MapActivity");
		}
		m.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
	}
	private void pointMe(){
		GPS g= new GPS(getApplicationContext());
		if(m==null)m = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		if(m!=null){
			m.setBuildingsEnabled(true);
			l1=g.getloc();
			LatLng l=new LatLng(l1.getLatitude(),l1.getLongitude());
			m.addMarker(new MarkerOptions().position(l).title("My location"));
			cameraPosition = new CameraPosition.Builder().target(l).tilt(60).zoom(18).build();
			
		}
		else{
			Toast.makeText(this, "Error in retrieving maps", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	private List<LatLng> decodePoly(String encoded) {
	    List<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;
	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;
	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;
	        LatLng p = new LatLng( (((double) lat / 1E5)),(((double) lng / 1E5) ));
	        poly.add(p);
	    }
	    return poly;
	}
}
