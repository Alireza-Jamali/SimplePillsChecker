package com.aeza.simpletaskchecker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Switch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;


/**
 * Created by Seyyed Alireza Jamali on 1/24/2018.
 */

public class Configurations extends AppCompatActivity {
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    LinearLayout inputsWrapper;

    public Configurations context;

    boolean language;
    boolean isDoubleTap;
    boolean isStatic;
    boolean isStack;

    static final String INPUTS_DELIMITER = "D73t9d";
    static final String ORANGE_STATE = "D73t8d";
    static final String GREEN_STATE = "D73t7d";
    static final String TIME_STATE = "D73t6d";
    static final String NO_STATE = "D73t0d";
    static final String ORANGE_COLOR = "#FFFF6F00";
    static final String GREEN_COLOR = "#FF8BC34A";
    static final String RED_COLOR = "#FFE53935";
    static final String TIME_COLOR = "#FFE040FB";

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.configs);
        context = Configurations.this;

        prefs = getSharedPreferences(getPackageName() + ".prefs", MODE_PRIVATE);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        inputsWrapper = findViewById(R.id.inputs_wrapper);

        SeekBar seeker = findViewById(R.id.seeker_bar);
        seeker.setMax(10);
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int childCount = inputsWrapper.getChildCount();
                progress -= childCount;
                if (progress != -1) {
                    for (int i = 0; i < progress; i++) {
                        EditText editText = new EditText(inputsWrapper.getContext());
                        editText.setHint(language ? R.string.en_name : R.string.fa_name);
                        editText.setBackgroundColor(Color.TRANSPARENT);
                        inputsWrapper.addView(editText);
                    }
                } else {
                    inputsWrapper.removeViewAt(childCount - 1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() != inputsWrapper.getChildCount()) {
                    int progress = seekBar.getProgress();
                    while (progress != inputsWrapper.getChildCount()) {
                        inputsWrapper.removeViewAt(inputsWrapper.getChildCount() - 1);
                    }
                }
            }
        });
        Switch languageSw = findViewById(R.id.language_switch);
        languageSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    language = true;
                    ((Switch) findViewById(R.id.two_step_switch)).setText(R.string.en_two_step_confirm);
                    ((Switch) findViewById(R.id.stack_switch)).setText(R.string.en_stack);
                    ((Switch) findViewById(R.id.time_switch)).setText(R.string.en_time_based);
                    ((Button) findViewById(R.id.cancel)).setText(R.string.en_cancel);
                    ((Button) findViewById(R.id.confirm)).setText(R.string.en_confirm);
                    ((RadioButton) findViewById(R.id.radioButton1)).setText(R.string.en_dynamic);
                    ((RadioButton) findViewById(R.id.radioButton2)).setText(R.string.en_static);
                    ((EditText) findViewById(R.id.time_start_text)).setHint(R.string.en_time_start_text);
                    ((EditText) findViewById(R.id.time_deadline_text)).setHint(R.string.en_time_deadline_text);
                    prefs.edit().putBoolean(mAppWidgetId + "language", true).apply();
                } else {
                    language = false;
                    ((Switch) findViewById(R.id.two_step_switch)).setText(R.string.fa_two_step_confirm);
                    ((Switch) findViewById(R.id.stack_switch)).setText(R.string.fa_stack);
                    ((Switch) findViewById(R.id.time_switch)).setText(R.string.fa_time_based);
                    ((Button) findViewById(R.id.cancel)).setText(R.string.fa_cancel);
                    ((Button) findViewById(R.id.confirm)).setText(R.string.fa_confirm);
                    ((RadioButton) findViewById(R.id.radioButton1)).setText(R.string.fa_dynamic);
                    ((RadioButton) findViewById(R.id.radioButton2)).setText(R.string.fa_static);
                    ((EditText) findViewById(R.id.time_start_text)).setHint(R.string.fa_time_start_text);
                    ((EditText) findViewById(R.id.time_deadline_text)).setHint(R.string.fa_time_deadline_text);
                    prefs.edit().putBoolean(mAppWidgetId + "language", false).apply();
                }
            }
        });

        Switch timeSw = findViewById(R.id.time_switch);
        timeSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    findViewById(R.id.time_start_text).setVisibility(View.VISIBLE);
                    findViewById(R.id.time_deadline_text).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.time_start_text).setVisibility(View.GONE);
                    findViewById(R.id.time_deadline_text).setVisibility(View.GONE);
                }
                prefs.edit().putBoolean(mAppWidgetId + "isTime", isChecked).apply();
            }
        });
    }

    public void confirm(View view) {
        LinearLayout inputsWrapper = findViewById(R.id.inputs_wrapper);
        String[] inputs = new String[inputsWrapper.getChildCount()];

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, Provider.class));
        for (int i = 0; i < inputsWrapper.getChildCount(); i++) {
            EditText input = ((EditText) inputsWrapper.getChildAt(i));
            inputs[i] = NO_STATE + input.getText().toString();
            for (int id : widgetIds) {
                for (String name : prefs.getString(id + "", "").split(INPUTS_DELIMITER)) {
                    if (inputs[i].equals(name)) {
                        input.setError(getString(language ? R.string.en_name_already_in_use : R.string.fa_name_already_in_use));
                        return;
                    }
                }
            }
        }

        prefs.edit().putString(mAppWidgetId + "", TextUtils.join(INPUTS_DELIMITER, inputs)).apply();

        RadioGroup radioGroup = findViewById(R.id.config_radioGroup);
        int checkedRadioBtn = radioGroup.getCheckedRadioButtonId();
        isStatic = checkedRadioBtn == R.id.radioButton2;
        prefs.edit().putBoolean(mAppWidgetId + "isStatic", isStatic).apply();

        Switch doubleTapSw = findViewById(R.id.two_step_switch);
        isDoubleTap = doubleTapSw.isChecked();
        prefs.edit().putBoolean(mAppWidgetId + "isDoubleTap", isDoubleTap).apply();

        Switch stackSw = findViewById(R.id.stack_switch);
        isStack = stackSw.isChecked();
        prefs.edit().putBoolean(mAppWidgetId + "isStack", isStack).apply();

        EditText timeStartText = findViewById(R.id.time_start_text);
        prefs.edit().putString(mAppWidgetId + "timeStartText", timeStartText.getText().toString()).apply();

        EditText timeDeadlineText = findViewById(R.id.time_deadline_text);
        prefs.edit().putString(mAppWidgetId + "timeDeadlineText", timeDeadlineText.getText().toString()).apply();

        initAppWidgets(context, appWidgetManager, mAppWidgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void cancel(View view) {
        onBackPressed();
    }


    public static void initAppWidgets(Context context, final AppWidgetManager appWidgetManager,
                                      final int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName() + ".prefs", Context.MODE_PRIVATE);
        String[] data = prefs.getString(appWidgetId + "", "").split(INPUTS_DELIMITER);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
        ArrayList<BroadcastReceiver> localBrList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            int btnId = R.id.btn_a + i;
            views.setTextViewText(btnId, data[i].substring(6));
            views.setTextColor(btnId,
                    data[i].startsWith(NO_STATE) ? Color.BLACK :
                            data[i].startsWith(GREEN_STATE) ?
                                    Color.parseColor(GREEN_COLOR) : Color.parseColor(ORANGE_COLOR)
            );
            views.setViewVisibility(btnId, View.VISIBLE);

            boolean isTime = prefs.getBoolean(appWidgetId + "isTime", false);
            if (isTime) {
                boolean isAllGreen = true;
                for (String name : data) {
                    if (!name.startsWith(GREEN_STATE)) {
                        isAllGreen = false;
                        break;
                    }
                }
                try {
                    String timeStartText = prefs.getString(appWidgetId + "timeStartText", "06:00");
                    String timeDeadlineText = prefs.getString(appWidgetId + "timeDeadlineText", "18:00");
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Calendar calendar = Calendar.getInstance();
                    Date now = sdf.parse(calendar.get(HOUR_OF_DAY) + ":" + calendar.get(MINUTE));
                    if (!isAllGreen) {
                        if (now.after(sdf.parse(timeStartText))) {
                            boolean hasTimeState = false;
                            for (int k = 0; k < data.length; k++) {
                                if (data[k].startsWith(TIME_STATE)) {
                                    hasTimeState = true;
                                    if (now.after(sdf.parse(timeDeadlineText))) {
                                        views.setTextColor(R.id.btn_a + k, Color.parseColor(RED_COLOR));
                                        data[k] = TIME_STATE + data[k].substring(6);
                                        boolean lan = prefs.getBoolean(appWidgetId + "language", false);

                                        NotificationCompat.Builder mBuilder =
                                                new NotificationCompat.Builder(context)
                                                        .setSmallIcon(R.drawable.ic_stat_name)
                                                        .setContentTitle(lan ? context.getString(R.string.en_missed_task) : context.getString(R.string.fa_missed_task))
                                                        .setContentText(lan ? data[i] + context.getString(R.string.en_isnt_checked) : data[i].substring(6));
                                        NotificationManager mNotificationManager =
                                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                        mNotificationManager.notify(758, mBuilder.build());
                                    }
                                    break;
                                }
                            }
                            boolean isTimeTaskChecked = prefs.getBoolean(appWidgetId + "isTimeTaskChecked", false);
                            if (!hasTimeState && !isTimeTaskChecked) {
                                int j = 0;
                                boolean skipLastBtn = false;
                                while (data[j].startsWith(GREEN_STATE)) {
                                    if (j < data.length - 1) {
                                        j++;
                                    } else {
                                        skipLastBtn = true;
                                        break;
                                    }
                                }
                                if (!skipLastBtn) {
                                    views.setTextColor(R.id.btn_a + j, Color.parseColor(TIME_COLOR));
                                    data[j] = TIME_STATE + data[j].substring(6);
                                }
                            }
                        } else {
                            prefs.edit().putBoolean(appWidgetId + "isTimeTaskChecked", false).apply();
                        }
                    } else if (data.length > 0 && !now.after(sdf.parse(timeStartText))) {
                        for (int k = 0; k < data.length; k++) {
                            int tempBtnId = R.id.btn_a + k;
                            String btn = NO_STATE + data[k].substring(6);
                            views.setTextColor(tempBtnId, Color.BLACK);
                            data[k] = btn;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                prefs.edit().putString(appWidgetId + "", TextUtils.join(INPUTS_DELIMITER, data)).apply();
            }

            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    SharedPreferences prefs = context.getSharedPreferences(context.getPackageName() + ".prefs", Context.MODE_PRIVATE);

                    boolean isDoubleTap = prefs.getBoolean(appWidgetId + "isDoubleTap", false);
                    boolean isStatic = prefs.getBoolean(appWidgetId + "isStatic", false);
                    boolean isStack = prefs.getBoolean(appWidgetId + "isStack", false);
                    boolean isTime = prefs.getBoolean(appWidgetId + "isTime", false);

                    String[] data = prefs.getString(appWidgetId + "", "").split(INPUTS_DELIMITER);
                    int currentBtnIndex = intent.getIntExtra("btn_index", 0);
                    int btnId = intent.getIntExtra("btn_id", 0);
                    String btnName = data[currentBtnIndex];
                    switch (btnName.substring(0, 6)) {
                        case TIME_STATE: {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            Calendar calendar = Calendar.getInstance();
                            String timeStartText = prefs.getString(appWidgetId + "timeStartText", "06:00");
                            try {
                                Date now = sdf.parse(calendar.get(HOUR_OF_DAY) + ":" + calendar.get(MINUTE));
                                if (!now.before(sdf.parse(timeStartText))) {
                                    data[currentBtnIndex] = NO_STATE + data[currentBtnIndex].substring(6);
                                    prefs.edit().putBoolean(appWidgetId + "isTimeTaskChecked", true).apply();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        case NO_STATE: {
                            if (isStack) {
                                if (isDoubleTap) {
                                    String temp = data[currentBtnIndex];
                                    System.arraycopy(data, 0, data, 1, currentBtnIndex);
                                    data[0] = ORANGE_STATE + temp.substring(6);
                                } else {
                                    int moveUpTurns = (data.length - 1) - currentBtnIndex;
                                    String temp = data[currentBtnIndex];
                                    System.arraycopy(data, currentBtnIndex + 1, data, currentBtnIndex, moveUpTurns);
                                    data[data.length - 1] = GREEN_STATE + temp.substring(6);
//                                    if (isTime) {
//                                        views.setTextColor(R.id.btn_a, Color.parseColor(ORANGE_COLOR));
//                                        String next = ORANGE_STATE + data[0].substring(6);
//                                        views.setTextViewText(R.id.btn_a, next.substring(6));
//                                        data[0] = next;
//                                    }
                                }
                                for (int i = 0; i < data.length; i++) {
                                    int tempBtnId = R.id.btn_a + i;
                                    views.setTextViewText(tempBtnId, data[i].substring(6));
                                    views.setTextColor(tempBtnId,
                                            data[i].startsWith(NO_STATE) ? Color.BLACK :
                                                    data[i].startsWith(GREEN_STATE) ?
                                                            Color.parseColor(GREEN_COLOR) : Color.parseColor(ORANGE_COLOR)
                                    );
                                }
                            } else {
                                views.setTextColor(btnId, Color.parseColor(isDoubleTap ? ORANGE_COLOR : GREEN_COLOR));
                                String newBtnName = (isDoubleTap ? ORANGE_STATE : GREEN_STATE) + btnName.substring(6);
                                views.setTextViewText(btnId, newBtnName.substring(6));
                                data[currentBtnIndex] = newBtnName;
//                                if (isTime && !isDoubleTap && currentBtnIndex != (data.length - 1)) {
//                                    boolean hasOrange = false;
//                                    for (String name : data) {
//                                        if (name.startsWith(ORANGE_STATE)) {
//                                            hasOrange = true;
//                                            break;
//                                        }
//                                    }
//                                    if (!hasOrange) {
//                                        int i = 1;
//                                        boolean skipLastBtn = false;
//                                        while (data[currentBtnIndex + i].startsWith(GREEN_STATE)) {
//                                            if (currentBtnIndex + i < data.length - 1) {
//                                                i++;
//                                            } else {
//                                                skipLastBtn = true;
//                                                break;
//                                            }
//                                        }
//                                        if (!skipLastBtn) {
//                                            views.setTextColor(btnId + i, Color.parseColor(ORANGE_COLOR));
//                                            data[currentBtnIndex + i] = ORANGE_STATE + data[currentBtnIndex + i].substring(6);
//                                        }
//                                    }
//                                }
                            }
                            prefs.edit().putString(appWidgetId + "", TextUtils.join(INPUTS_DELIMITER, data)).apply();
                        }
                        break;
                        case ORANGE_STATE: {
                            views.setTextColor(btnId, Color.parseColor(GREEN_COLOR));
                            String newBtnName = GREEN_STATE + btnName.substring(6);
                            views.setTextViewText(btnId, newBtnName.substring(6));
                            data[currentBtnIndex] = newBtnName;
                            if (isStack) {
                                int moveUpTurns = (data.length - 1) - currentBtnIndex;
                                String temp = data[currentBtnIndex];
                                System.arraycopy(data, currentBtnIndex + 1, data, currentBtnIndex, moveUpTurns);
                                data[data.length - 1] = GREEN_STATE + temp.substring(6);
                                for (int i = 0; i < data.length; i++) {
                                    int tempBtnId = R.id.btn_a + i;
                                    views.setTextViewText(tempBtnId, data[i].substring(6));
                                    views.setTextColor(tempBtnId,
                                            data[i].startsWith(NO_STATE) ? Color.BLACK :
                                                    data[i].startsWith(GREEN_STATE) ?
                                                            Color.parseColor(GREEN_COLOR) : Color.parseColor(ORANGE_COLOR)
                                    );
                                }
//                                if (isTime) {
//                                    boolean isAllGreen = true;
//                                    for (String name : data) {
//                                        if (!name.startsWith(GREEN_STATE)) {
//                                            isAllGreen = false;
//                                            break;
//                                        }
//                                    }
//                                    if (!isAllGreen) {
//                                        views.setTextColor(R.id.btn_a, Color.parseColor(ORANGE_COLOR));
//                                        data[0] = ORANGE_STATE + data[0].substring(6);
//                                        prefs.edit().putString(appWidgetId + "", TextUtils.join(INPUTS_DELIMITER, data)).apply();
//                                    }
//                                }
                            }
//                            else if (isTime && currentBtnIndex != (data.length - 1)) {
//                                boolean hasOrange = false;
//                                for (String name : data) {
//                                    if (name.startsWith(ORANGE_STATE)) {
//                                        hasOrange = true;
//                                        break;
//                                    }
//                                }
//                                if (!hasOrange) {
//                                    int i = 1;
//                                    boolean skipLastBtn = false;
//                                    while (data[currentBtnIndex + i].startsWith(GREEN_STATE)) {
//                                        if (currentBtnIndex + i < data.length - 1) {
//                                            i++;
//                                        } else {
//                                            skipLastBtn = true;
//                                            break;
//                                        }
//                                    }
//                                    if (!skipLastBtn) {
//                                        views.setTextColor(btnId + i, Color.parseColor(ORANGE_COLOR));
//                                        data[currentBtnIndex + i] = ORANGE_STATE + data[currentBtnIndex + i].substring(6);
//                                    }
//                                }
//                            }
                            prefs.edit().putString(appWidgetId + "", TextUtils.join(INPUTS_DELIMITER, data)).apply();
                        }
                        break;
                        case GREEN_STATE: {
                            views.setTextColor(btnId, isStatic ? Color.parseColor(GREEN_COLOR) : Color.BLACK);
                            String newBtnName = (isStatic ? GREEN_STATE : NO_STATE) + btnName.substring(6);
                            views.setTextViewText(btnId, newBtnName.substring(6));
                            data[currentBtnIndex] = newBtnName;
                            prefs.edit().putString(appWidgetId + "", TextUtils.join(INPUTS_DELIMITER, data)).apply();

                            if (isStatic) {
                                boolean isAllGreen = true;
                                for (String name : data) {
                                    if (!name.startsWith(GREEN_STATE)) {
                                        isAllGreen = false;
                                        break;
                                    }
                                }
                                if (isAllGreen && data.length > 0) {
                                    for (int i = 0; i < data.length; i++) {
                                        int tempBtnId = R.id.btn_a + i;
                                        String btn = ((i == 0 && isTime) ? ORANGE_STATE : NO_STATE) + data[i].substring(6);
                                        views.setTextViewText(tempBtnId, btn.substring(6));
                                        views.setTextColor(tempBtnId, (i == 0 && isTime) ? Color.parseColor(ORANGE_COLOR) : Color.BLACK);
                                        data[i] = btn;
                                    }
                                    prefs.edit().putString(appWidgetId + "", TextUtils.join(INPUTS_DELIMITER, data)).apply();
                                }
                            }
                        }
                        break;
                    }
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }

            };

            localBrList.add(broadcastReceiver);

            Intent btnIntent = new Intent(data[i]);

            btnIntent.putExtra("btn_index", i);
            btnIntent.putExtra("btn_id", btnId);

            context.getApplicationContext().registerReceiver(broadcastReceiver, new IntentFilter(data[i]));

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                    appWidgetId, btnIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(btnId, pendingIntent);

        }
        Provider.broadcastList.put(appWidgetId, localBrList);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }


}
