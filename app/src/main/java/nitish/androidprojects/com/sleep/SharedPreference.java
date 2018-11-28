package nitish.androidprojects.com.sleep;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;



import java.util.ArrayList;

public class SharedPreference {
    public static final String Z_DATA = "Z_DATA";
    public static final String START_TIME = "START_TIME";
    public static final String END_TIME = "END_TIME";
    public static final String LAST_SLEEP_HOURS = "LAST_SLEEP_HOURS";
    public static final String LAST_SLEEP_HOURS_LIST = "LAST_SLEEP_HOURS_LIST";
    public static final String JOB_ID = "jobId";
    private SharedPreferences sharedPref = null;
    private SharedPreferences.Editor editor = null;

    public static final String SHARED_PREF = "com";
     private static SharedPreference OBJECT;

    private Context context;

    private SharedPreference(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }


    public static SharedPreference getPreferences(Context context) {
        if (OBJECT == null) {
            OBJECT = new SharedPreference(context);
        }
        return OBJECT;
    }

    public void saveStringData(String key, String data) {

        editor.putString(key, data);
        editor.commit();

    }

    public String getStringData(String key) {

        return sharedPref.getString(key, "");
    }

    public void saveIntData(String key, int data) {
        editor.putInt(key, data);
        editor.commit();
    }


    public int getIntData(String key) {
        return sharedPref.getInt(key, -1);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }


    public void saveIntArray(String key, int[] array) {
        String data = new Gson().toJson(array);
        saveStringData(key, data);
    }

    public int[] getIntArray(String key) {
        String data = getStringData(key);
        if (data == null || data.equals("")) {
            return null;
        }
        return new Gson().fromJson(data, int[].class);
    }

    public void saveStringArray(String key, ArrayList<String> array) {
        String data = new Gson().toJson(array);
        saveStringData(key, data);
    }

    public ArrayList<String> getStringArray(String key) {
        String data = getStringData(key);
        if (data == null || data.equals("")) {
            return new ArrayList<String>();
        }
        return new Gson().fromJson(data, ArrayList.class);
    }

    public boolean getBoolean( String key) {
       return sharedPref.getBoolean(key,false);
    }
    public void setBoolean( String key, boolean b) {
        editor.putBoolean(key, b);
        editor.commit();
    }

    public float getFloatData(String zData) {

        return sharedPref.getFloat(zData, 100);
    }

    public void setFloatData(String zData, float value) {
        editor.putFloat(zData, value);
        editor.commit();
    }

    public long getLongData(String zData) {

        return sharedPref.getLong(zData, 100);
    }

    public void setLongData(String zData, long value) {
        editor.putLong(zData, value);
        editor.commit();
    }
}

