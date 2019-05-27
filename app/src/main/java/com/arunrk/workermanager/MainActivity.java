package com.arunrk.workermanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_SEND = "send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data = new Data.Builder()
                .putString(KEY_SEND, "Hi Im sending message")
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        final OneTimeWorkRequest request = new OneTimeWorkRequest
                .Builder(MyWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        findViewById(R.id.btnClick)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WorkManager.getInstance()
                                .enqueue(request);

                    }
                });

        final TextView textView = findViewById(R.id.textView);

        WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {

                        if (workInfo != null) {
                            if (workInfo.getState().isFinished()) {
                                String output = workInfo.getOutputData().getString(MyWorker.KEY_RECEIVE);
                                textView.append("\n" + output);
                            }
                            String status = workInfo.getState().name();
                            textView.append("\n" + status);
                        }


                    }
                });
    }
}
