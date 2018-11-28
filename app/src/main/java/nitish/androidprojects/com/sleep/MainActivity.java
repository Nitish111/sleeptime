package nitish.androidprojects.com.sleep;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {


    private JobScheduler jobScheduler;
    private TextView sleep_hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jobScheduler = (JobScheduler) (MainActivity.this).getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName((MainActivity.this), MyBackgroundService.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName).setMinimumLatency(0).setOverrideDeadline(0)
                .build();
        jobScheduler.schedule(jobInfo);

        sleep_hours = findViewById(R.id.sleep_hours);
        setTime();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTime();
    }

    private void setTime() {
        ArrayList<String> time = SharedPreference.getPreferences(this).getStringArray(SharedPreference.LAST_SLEEP_HOURS_LIST);
        String text = "";
        for (int i = 0; i < time.size(); i++) {
            text += "\n" + time.get(i);
        }
        sleep_hours.setText(TextUtils.isEmpty(text) ? "00:00:00" : text);
    }
}
