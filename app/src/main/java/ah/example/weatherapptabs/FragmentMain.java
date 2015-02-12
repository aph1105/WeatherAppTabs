package ah.example.weatherapptabs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dell on 08/02/2015.
 */
public class FragmentMain extends Fragment{


    private TabHost mTabHost;

    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;

    Handler handler;

    public FragmentMain(){
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        cityField = (TextView)rootView.findViewById(R.id.city_field);
        updatedField = (TextView)rootView.findViewById(R.id.updated_field);
        detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)rootView.findViewById(R.id.weather_icon);

        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(),"fonts/weathericons-regular-webfont.ttf");
        updateWeatherData(new CityPreferences(getActivity()).getCity());


    }

    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
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

    private void renderWeather(JSONObject json){
        try{
            cityField.setText(json.getJSONObject("city").getString("name").toUpperCase(Locale.US)+ ","+ json.getJSONObject("city").getString("country"));
            JSONObject details = json.getJSONArray("list").getJSONObject(0);
            JSONObject temp = details.getJSONObject("temp");
            JSONArray weather = details.getJSONArray("weather");
            String description = new String();
            int icon=0;
            for(int i=0;i<weather.length();i++){
                JSONObject obj =weather.getJSONObject(i);
                description = obj.getString("description");
                icon = obj.getInt("id");
            }

            detailsField.setText(description.toUpperCase(Locale.US));




            currentTemperatureField.setText("Min: "+String.format("%.2f",temp.getDouble("min"))+"°C"+"Max: "+String.format("%.2f",temp.getDouble("max"))+"°C");



            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(details.getLong("dt")*1000));
            updatedField.setText("Last update: "+updatedOn);

            System.out.println("Last update: "+updatedField.toString());

            setWeatherIcon(icon);
        }catch(Exception e){
            Log.e("WeatherApp", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId){
        int id = actualId / 100;
        String icon = "";

        switch(id) {
            case 1 :icon = getActivity().getString(R.string.weather_clear_sky);
                break;
            case 2 : icon = getActivity().getString(R.string.weather_thunder);
                break;
            case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                break;
            case 7 : icon = getActivity().getString(R.string.weather_foggy);
                break;
            case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                break;
            case 6 : icon = getActivity().getString(R.string.weather_snowy);
                break;
            case 5 : icon = getActivity().getString(R.string.weather_rainy);
                break;
        }

        weatherIcon.setText(icon);
    }

    public void changeCity(String city){
        updateWeatherData(city);
    }
}
