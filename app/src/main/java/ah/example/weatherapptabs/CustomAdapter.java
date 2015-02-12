package ah.example.weatherapptabs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dell on 08/02/2015.
 */
public class CustomAdapter extends BaseAdapter {

    /** * Holder for the list items. */
    private class ViewHolder{
        TextView date;
        TextView icon;
        TextView temp;
        TextView detail;
        TextView hum;
        TextView press;
    }



    Context context;
    List<RowItem> rowItem;


    CustomAdapter(Context context, List<RowItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;

    }

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem item = (RowItem)getItem(position);
        View viewToUse = null;

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            viewToUse = mInflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.date = (TextView) viewToUse.findViewById(R.id.date_field);
            holder.icon = (TextView) viewToUse.findViewById(R.id.weather_icon);
            holder.detail = (TextView) viewToUse.findViewById(R.id.details_field);
            holder.temp = (TextView) viewToUse.findViewById(R.id.current_temperature_field);
            holder.hum = (TextView) viewToUse.findViewById(R.id.humidity);
            holder.press = (TextView) viewToUse.findViewById(R.id.pressure);
            viewToUse.setTag(holder);
        }else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }


        RowItem row_pos = rowItem.get(position);

        TextView date_field = (TextView) viewToUse.findViewById(R.id.date_field);
        TextView weather_icon = (TextView) viewToUse.findViewById(R.id.weather_icon);
        TextView current_temperature_field = (TextView) viewToUse.findViewById(R.id.current_temperature_field);
        TextView details_field= (TextView) viewToUse.findViewById(R.id.details_field);
        TextView humidity= (TextView) viewToUse.findViewById(R.id.humidity);
        TextView pressure = (TextView) viewToUse.findViewById(R.id.pressure);

        date_field.setText(row_pos.getDate());
        weather_icon.setText(row_pos.getIcon());
        current_temperature_field.setText(row_pos.getTemp());
        details_field.setText(row_pos.getDetails());
        humidity.setText(row_pos.getHumidity());
        pressure.setText(row_pos.getPressure());

        return viewToUse;



    }
}
