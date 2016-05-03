package ru.jollydroid.sensorsdemo;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tse on 27/04/16.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String POSITION_EXTRA = "pos";

    private Sensor sensor;
    private SensorManager sensorManager;
    private TextView accuracyView;
    private TextView valuesView;


    public static Intent getStartIntent(Context context, int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(POSITION_EXTRA, position);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensor = sensorList.get(getIntent().getIntExtra(POSITION_EXTRA, 0));

        Log.d("happy", sensor.toString());

        ((TextView) findViewById(R.id.string)).setText(
                sensor.toString());
        ((TextView) findViewById(R.id.name)).setText(
                sensor.getName());
        ((TextView) findViewById(R.id.vendor)).setText(
                sensor.getVendor());
        if (Build.VERSION.SDK_INT >= 20) {
            ((TextView) findViewById(R.id.type)).setText(
                    sensor.getStringType() + "(" + sensor.getType() + ")");
        } else {
            ((TextView) findViewById(R.id.type)).setText(
                    "" + sensor.getType());
        }

        accuracyView = (TextView) findViewById(R.id.accuracy);
        valuesView = (TextView) findViewById(R.id.values);

    }

    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            StringBuilder sb = new StringBuilder();
            for (float value : event.values) {
                sb.append(value).append("\n");
            }

            valuesView.setText(sb.toString());
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            accuracyView.setText("" + accuracy);
        }

    };

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(listener);
    }



}
