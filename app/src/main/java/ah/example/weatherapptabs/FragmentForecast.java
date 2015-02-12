package ah.example.weatherapptabs;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dell on 08/02/2015.
 */
public class FragmentForecast extends ListFragment {


   Typeface weatherFont;



    private TabHost mTabHost;
    CustomAdapter adapter;
    private ArrayList<HashMap<String,String>> rowItems;
    private List<RowItem> rowItemList;

    Handler handler;

    public FragmentForecast(){
        handler = new Handler();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_forecast,container,false);


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(),"fonts/weathericons-regular-webfont.ttf");
        updateWeatherData(new CityPreferences(getActivity()).getCity());

    }


    private void updateWeatherData(final String city){
        new Thread(){
            public void  run(){
                final JSONObject json = RemoteFetch.getJSON(getActivity(),city);
                if(json == null){
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json) {
        try {

            rowItems = new ArrayList<HashMap<String, String>>();
            rowItemList = new ArrayList<RowItem>();
            JSONArray details = json.getJSONArray("list");


            HashMap<String, String> map = new HashMap<String, String>();
            DateFormat df = DateFormat.getDateTimeInstance();

            for (int i = 0; i < details.length() - 1; i++) {
                JSONObject obj = details.getJSONObject(i);
                JSONObject temp = obj.getJSONObject("temp");
                JSONArray weather = obj.getJSONArray("weather");
                String date = df.format(new Date(obj.getLong("dt") * 1000));
                String description = new String();
                int icon = 0;
                for (int j = 0; j < weather.length(); j++) {
                    JSONObject obj1 = weather.getJSONObject(j);
                    description = obj1.getString("description");
                    icon = obj1.getInt("id");

                }
                RowItem item = new RowItem();
                item.setDate(df.format(new Date(obj.getLong("dt") * 1000)));
                item.setDetails(description.toUpperCase(Locale.US));
                item.setIcon(setWeatherIcon(icon));
                item.setTemp("Min: " + String.format("%.2f", temp.getDouble("min")) + "°C" + "  Max" + String.format("%.2f", temp.getDouble("max")) + "°C");
                item.setHumidity("Humidity: " + obj.getString("humidity") + "%");
                item.setPressure("Pressure: "+obj.getString("pressure")+"hPa");

                map.put("Date: ", item.getDate());
                map.put("Description: ",item.getDetails());
                map.put("Icon",item.getIcon());
                map.put("Temperature: ",item.getTemp());
                map.put("Humidity: ",item.getPressure());
                map.put("Pressure: ",item.getPressure());

                rowItemList.add(item);
                rowItems.add(map);



            }

            adapter = new CustomAdapter(getActivity(), rowItemList);
            setListAdapter(adapter);




        } catch (Exception e) {
            Log.e("WeatherApp", "Error in FragmentForecast");
        }
    }

        private String setWeatherIcon(int actualId){
            int id = actualId / 100;
            String icon = "";

            switch(id) {
                case 1 :icon = getActivity().getString(R.string.weather_clear_sky);
                    return icon;
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    return icon;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    return icon;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    return icon;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    return icon;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    return icon;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    return icon;
                default:  return null;
            }


        }

    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
    }


