package ru.jollydroid.sensorsdemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


/*

http://developer.android.com/guide/topics/sensors/sensors_overview.html
http://developer.android.com/guide/topics/sensors/sensors_motion.html
http://developer.android.com/guide/topics/sensors/sensors_position.html
http://developer.android.com/guide/topics/sensors/sensors_environment.html

 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private LayoutInflater inflater;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflater = getLayoutInflater();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        /*
        можно запросить список сенсоров определенного типа
        sensorList = sensorManager.getSensorList(Sensor.TYPE_GRAVITY);

        а также довериться системе и попросить дефолтный сенсор этого типа
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        не забывайте проверять, что getSensorList вернул не пустой список, а
        getDefaultSensor -- не null
        */
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(new SensorAdapter());
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(DetailActivity.getStartIntent(this, position));
    }

    public class SensorAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sensorList.size();
        }

        @Override
        public Sensor getItem(int position) {
            return sensorList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Sensor s = getItem(position);
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_sensor, parent, false);
            }

            ((TextView)view.findViewById(R.id.name)).setText(s.getName());
            ((TextView)view.findViewById(R.id.vendor)).setText(s.getVendor());
            if (Build.VERSION.SDK_INT >= 20) {
                ((TextView) view.findViewById(R.id.type))
                        .setText(s.getStringType() + "(" + s.getType() + ")");
            } else {
                ((TextView) view.findViewById(R.id.type))
                        .setText("" + s.getType());
            }

            return view;
        }
    }
}
