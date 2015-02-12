package ah.example.weatherapptabs;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Dell on 06/02/2015.
 */
public class CityPreferences {

    SharedPreferences preferences;

    public CityPreferences(Activity activity){
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    String getCity(){
        return preferences.getString("city","Moscow,RU");
    }

    void setCity(String city){
        preferences.edit().putString("city",city).commit();
    }

}
