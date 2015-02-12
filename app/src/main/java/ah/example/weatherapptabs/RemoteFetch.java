package ah.example.weatherapptabs;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dell on 06/02/2015.
 */
public class RemoteFetch {

    private static final String OPEN_WEATHER_REPORT_API = "http://api.openweathermap.org/data/2.5/forecast/daily?id=524901&cnt=14&APPID=xxxxx ";

    public static JSONObject getJSON(Context context, String city){
        try{
            URL url = new URL(String.format(OPEN_WEATHER_REPORT_API,city));
            HttpURLConnection connection =  (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", context.getString(R.string.open_weather_reports_app_id));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if(data.getInt("cod")!= 200){
                return null;
            }
            return data;
        }catch(Exception e){
            Log.e("WeatherApp", "Error in RemoteFetch");
            return null;
        }
    }
}


