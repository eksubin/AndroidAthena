package com.libgdx.subin.trial2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends Activity {

    // hello i made few changes
    //how to avoid push to the g

    String sun,returnString2;

    TextView hoo;
    public static final String KEY_121 = "http://athena.nitc.ac.in/navaneeth_b140143cs/test.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hoo = (TextView)findViewById(R.id.name);
        try {
            sun=getServerData(KEY_121);
            hoo.setText(sun);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    ////////////////////////////////////////////////////
    private String getServerData(String returnString) {

        final InputStream[] is = {null};

        final String[] result = {""};
        //the year data to send
        final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id","1"));


        //http post

        Thread thread = new Thread() {
            @Override
            public void run() {
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(KEY_121);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    is[0] = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is[0],"iso-8859-1"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is[0].close();
                    result[0] =sb.toString();
                    try{
                        JSONArray jArray = new JSONArray(result[0]);
                        for(int i=0;i<jArray.length();i++){
                            JSONObject json_data = jArray.getJSONObject(i);
                            Log.i("log_tag","id: "+json_data.getInt("id")+
                                            ", name: "+json_data.getString("name")
                            );
                            //Get an output to the screen
                            returnString2 = ("\n\t" + jArray.getJSONObject(i));
                        }
                    }catch(JSONException e){
                        Log.e("log_tag", "Error parsing data "+e.toString());
                    }

                }catch(Exception e){
                    Log.e("log_tag", "Error in http connection " + e.toString());
                }
            }
        };

        thread.start();

        //convert response to string
        try{

        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }
        //parse json data

        return returnString2;
    }
}
