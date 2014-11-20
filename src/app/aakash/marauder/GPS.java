package app.aakash.marauder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;


public class GPS extends Service implements LocationListener{
	private final Context mcontext;
	Location loc;
	LocationManager lm;
	double lat, lon;
	public GPS(Context context){
		mcontext=context;
		getloc();
		lat=loc.getLatitude();
		lon=loc.getLongitude();
	}
	public Location getloc(){
		try{
			lm=(LocationManager) mcontext.getSystemService(LOCATION_SERVICE);
			if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
				loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000*60*10, 100, this);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return loc;
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
