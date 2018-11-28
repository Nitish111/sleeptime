package nitish.androidprojects.com.sleep;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class MyBackgroundService extends JobService {


    private myTask doThisTask;

    @Override
    public boolean onStartJob(JobParameters params) {

        doThisTask = new myTask();
        doThisTask.execute();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (doThisTask != null)
        {
            doThisTask.cancel(true);

        }
        return true;
    }

    private class myTask extends AsyncTask<Void, Void, Void> {

        SensorManager sensorManager;
        public myTask()
        {
            sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        }
        @Override
        protected Void doInBackground(final Void... params) {

            /*PhoneUnlockedReceiver  receiver = new PhoneUnlockedReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_USER_PRESENT);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(receiver, filter);*/
            Handler handler = new Handler(Looper.getMainLooper());
            final boolean post = handler.post(new Runnable() {

                @Override
                public void run() {

                    Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    if (proximitySensor == null) {
                        Log.e("TAG", "Proximity sensor not available.");
                    }


                    final SensorEventListener proximitySensorListener = new SensorEventListener() {

                        @Override
                        public void onSensorChanged(SensorEvent event) {

                            Log.i("TYPE_ACCELEROMETER Z", event.values[2] + "");
                            SharedPreference preference= SharedPreference.getPreferences(getApplicationContext());
                            float absValue = Math.abs(event.values[2]);
                            if(preference.getFloatData(SharedPreference.Z_DATA)==100)
                            {
                                // capture time start
                                preference.saveStringData(SharedPreference.LAST_SLEEP_HOURS,"");
                                preference.setLongData(SharedPreference.START_TIME, System.currentTimeMillis());
                                preference.setFloatData(SharedPreference.Z_DATA, absValue);
                            }
                            else
                            {
                                float g = preference.getFloatData(SharedPreference.Z_DATA)+0.5f ;
                                float l = preference.getFloatData(SharedPreference.Z_DATA)-0.5f ;
                                if((absValue<g && absValue>l) /*&& !isScreenOn(getApplicationContext())*/)
                                {
                                    // capture time end
                                    preference.setLongData(SharedPreference.END_TIME, System.currentTimeMillis());
                                    long diff =  preference.getLongData(SharedPreference.END_TIME) - preference.getLongData(SharedPreference.START_TIME);

                                    if(TimeUnit.MILLISECONDS.toMinutes(diff)>=1)
                                    {
                                        Date date = new Date(diff);
                                        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                                        String dateFormatted = formatter.format(date);


                                        preference.saveStringData(SharedPreference.LAST_SLEEP_HOURS,dateFormatted);
                                    }
                                    else{

                                        preference.saveStringData(SharedPreference.LAST_SLEEP_HOURS,"");
                                    }

                                }
                                else
                                {
                                    preference.setFloatData(SharedPreference.Z_DATA, 100);
                                    preference.setLongData(SharedPreference.START_TIME,0);
                                    preference.setLongData(SharedPreference.END_TIME,0);
                                    if(!TextUtils.isEmpty(preference.getStringData(SharedPreference.LAST_SLEEP_HOURS)))
                                    {
                                        ArrayList<String> sleeps = preference.getStringArray(SharedPreference.LAST_SLEEP_HOURS_LIST);
                                        sleeps.add(preference.getStringData(SharedPreference.LAST_SLEEP_HOURS));
                                        preference.saveStringArray(SharedPreference.LAST_SLEEP_HOURS_LIST,sleeps);
                                        preference.saveStringData(SharedPreference.LAST_SLEEP_HOURS,"");
                                    }


                                }
                            }
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int accuracy) {

                        }
                    };

                    sensorManager.registerListener(
                            proximitySensorListener,
                            proximitySensor, 30000000
                    );


                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }

       /* public class PhoneUnlockedReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
                    Log.d("", "Phone unlocked");
                }else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                    Log.d("", "Phone locked");
                }
            }
        }*/
    }

   /* public boolean isScreenOn(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            boolean screenOn = false;
            for (Display display : dm.getDisplays()) {
                if (display.getState() != Display.STATE_OFF) {
                    screenOn = true;
                }
            }
            return screenOn;
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //noinspection deprecation
            return pm.isScreenOn();
        }
    }*/



}
