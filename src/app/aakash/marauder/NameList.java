package app.aakash.marauder;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
 
public class NameList extends ListActivity {
	JSONArray persons = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread thread = new Thread(new A());
        thread.start();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username",Login.user));
        Json j = new Json();
        JSONObject jobj= j.makeHttpRequest("http://aakashgps.site88.net/name_list.php", "GET", params);
        ListView lv = getListView();	
        
        try{
			persons = jobj.getJSONArray("person");
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("jobj",e.getMessage());
		}
        
        String names[] = new String[persons.length()] ;
        final JSONObject []jsons = new JSONObject[persons.length()];
        for(int count=0; count < persons.length(); count++){
        	try {
				jsons[count] = persons.getJSONObject(count);
	        	names[count]= jsons[count].getString("name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e("ARRAY", e.getMessage());
			}
        }
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.names, R.id.label, names));
        lv.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> parent , View view, int position , long id){
        		String s = ((TextView) view).getText().toString();
        		Intent i = new Intent(getApplicationContext(), MapActivity.class);
        		i.putExtra("name", s);
        		int j;
        		try{
        			for(j=0; j<persons.length(); j++){
        				if(s.equals(jsons[j].getString("name")))break;
        			}
        			i.putExtra("latitude", jsons[j].getDouble("latitude"));
        			i.putExtra("longitude", jsons[j].getDouble("longitude"));
        			i.putExtra("time", jsons[j].getString("time"));
        			startActivity(i);
        			finish();
        		}catch(Exception e){
        			Log.e("in onclick method", "");
        		}
        	}
        });
    }
    class A extends Thread{
    	public void run(){
    		while(true){
    			bgupdate.update(getApplicationContext());
    			try{
    				Thread.sleep(10*60*1000);
    			}catch(Exception e){}
    		}
    	}
    }
}