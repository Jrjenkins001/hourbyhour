package com.example.hour_by_hour;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.hour_by_hour.Notification.CHANNEL_1_ID;
import static com.example.hour_by_hour.Notification.CHANNEL_2_ID;
import static java.util.Collections.sort;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener {
    private MaterialCalendarView _widget;
    private HashMap<String, ArrayList<Task>> _days;
    private ArrayList<Task> _taskList;
    private RecyclerView _recyclerView;
    private CalendarDay _calendarDay;
    private NotificationManagerCompat notificationManger;
    private EditText editTextTitle;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        _taskList = new ArrayList<>();
        _widget = findViewById(R.id.calendarView);
        _widget.setOnDateChangedListener(this);
        _widget.setCurrentDate(CalendarDay.today());

        _calendarDay = null;
        _days = getSavedDays(this);


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            CalendarDay calendarDay = extras.getParcelable(getString(R.string.EXTRA_CALENDAR_DAY));
            Task task = extras.getParcelable(getString(R.string.EXTRA_TASK));

            if(calendarDay != null) {
                ArrayList<Task> taskItems = _days.get(calendarDay.toString());
                if(taskItems == null) {
                    taskItems = new ArrayList<>();
                }

                if(task != null) {
                    if(task.getRepeating().equals(getString(R.string.none_array))){
                        taskItems.add(task);

                        sort(taskItems);

                        _days.put(calendarDay.toString(), taskItems);
                    } else {
                        RepeatableEvents re = new RepeatableEvents();
                        re.getStartingInfo(this, task);
                        re.execute();
                        _days = getSavedDays(this);
                    }
                }
                _taskList = _days.get(calendarDay.toString());
                _widget.setCurrentDate(calendarDay);
            }
        }

        initializeRecyclerView(_taskList);

        Toolbar myToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        notificationManger = NotificationManagerCompat.from(this);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextMessage = findViewById(R.id.edit_text_message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toolbar toolbar;
        switch (item.getItemId()) {
            case (R.id.action_add_new_event):
                Intent intent = new Intent(this, EditTask.class);

                if(_calendarDay != null){
                    intent.putExtra(getString(R.string.EXTRA_CALENDAR_DAY), _calendarDay);
                    intent.putExtra(getString(R.string.EXTRA_TASK), _days.get(_calendarDay.toString()));
                }

                startActivity(intent);
                return true;

            case (R.id.change_to_do_list):
                Intent intentToDo = new Intent(this, ToDoListActivity.class);
                startActivity(intentToDo);
                return true;

            case (R.id.change_month_view):
                setContentView(R.layout.main_activity);
                toolbar = findViewById(R.id.toolbar_main);
                setSupportActionBar(toolbar);
                _widget = findViewById(R.id.calendarView);
                _widget.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                _widget.setOnDateChangedListener(this);
                initializeRecyclerView(new ArrayList<Task>());
                return true;

            case (R.id.change_week_view):
                setContentView(R.layout.main_activity);
                toolbar = findViewById(R.id.toolbar_main);
                setSupportActionBar(toolbar);
                _widget = findViewById(R.id.calendarView);
                _widget.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
                _widget.setOnDateChangedListener(this);
                initializeRecyclerView(new ArrayList<Task>());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(_days.size() > 0) {
            putSavedDays(this, _days);
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        _taskList = _days.get(date.toString());
        RecyclerView.Adapter mAdapter = new TaskAdapter(_taskList);
        _recyclerView.setAdapter(mAdapter);
        _calendarDay = date;
    }

    public void sendOnChannel1(View view) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Intent notifyIntent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notifyIntent,0);

        Intent broadcastIntent = new Intent (this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .build();

        notificationManger.notify(1,notification);
    }

    public void sendOnChannel2(View view) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManger.notify(2,notification);

    }

    /**
     * Returns from Shared Preferences the HashMap with the list of days
     * and all the tasks within those days
     * @param context used to access shared preferences
     * @return a list of days and the tasks related
     */
    static HashMap<String, ArrayList<Task>> getSavedDays(@NonNull Context context) {
        HashMap<String, ArrayList<Task>> days;
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.saved_data_info), Context.MODE_PRIVATE);
        String dataString = sharedPref.getString(context.getString(R.string.saved_preferences_json), "NULL");


        if (dataString != null && !dataString.equals("NULL")) {
            Type type = new TypeToken<HashMap<String, ArrayList<Task>>>(){}.getType();
            try {
                days = gson.fromJson(dataString, type);
            } catch (JsonSyntaxException e) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear().apply();
                days = new HashMap<>();
            }
        } else {
            days = new HashMap<>();
        }

        return days;
    }

    /**
     * Writes the list of days and events to SharedPreferences in JSON
     * @param context location of the shared preferences
     * @param days the list of days to be saved
     */
    static void putSavedDays(@NonNull Context context, HashMap<String, ArrayList<Task>> days) {
        Gson g = new Gson();
        Type type = new TypeToken<HashMap<String, ArrayList<Task>>>(){}.getType();
        String dayJSON = g.toJson(days, type);

        SharedPreferences sharedPref =
                context.getSharedPreferences(context.getString(R.string.saved_data_info), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.saved_preferences_json), dayJSON);
        editor.apply();
    }

    /**
     * Initializes the RecyclerView with the new information
     * @param tasks the list to be shown
     */
    private void initializeRecyclerView(ArrayList<Task> tasks){
        _recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new TaskAdapter(tasks);
        _recyclerView.setAdapter(mAdapter);
    }

    /**
     * If a task is made to be repeatable, this class will be called to allow for
     * the event to be made the required amount of times. This will work in the
     * background.
     */
    static class RepeatableEvents extends AsyncTask<Void, Void, HashMap<String, ArrayList<Task>>> {
        private final int NUM_REPEATING_DAILY = 365;
        private final int NUM_REPEATING_MONTHLY = 12;
        private final int NUM_REPEATING_YEARLY = 5;
        private final int NUM_REPEATING_WEEKLY = 52;

        Task task;
        WeakReference<Context> context;
        HashMap<String, ArrayList<Task>> _days;

        /**
         * Used to instantiate the class
         * @param context a reference for the comparisons
         * @param task the task to be repeated
         */
        void getStartingInfo(Context context, Task task){
            this.context = new WeakReference<>(context);
            this.task = task;
        }

        @Override
        protected HashMap<String, ArrayList<Task>> doInBackground(Void ... aVoid) {
            int repetitions;
            _days = MainActivity.getSavedDays(context.get());

            if(task.repeating.equals(context.get().getString(R.string.yearly_array))){
                repetitions = NUM_REPEATING_YEARLY;
            } else if (task.repeating.equals(context.get().getString(R.string.monthly_array))){
                repetitions = NUM_REPEATING_MONTHLY;
            } else if (task.repeating.equals(context.get().getString(R.string.weekly_array))) {
                repetitions = NUM_REPEATING_WEEKLY;
            } else if (task.repeating.equals(context.get().getString(R.string.daily_array))){
                repetitions = NUM_REPEATING_DAILY;
            } else {
                repetitions = 0;
            }

            for(int i = 0; i < repetitions; i++){
                ArrayList<Task> tasks = _days.get(task.getStartDate().toString());
                if(tasks == null){
                    tasks = new ArrayList<>();
                }

                tasks.add(task);
                sort(tasks);
                _days.put(task.getStartDate().toString(), tasks);

                task = task.getNextRepeating(context.get());
            }

            return _days;
        }

        @Override
        protected void onPostExecute(HashMap<String, ArrayList<Task>> _days) {
            MainActivity.putSavedDays(context.get(), _days);
            Toast.makeText(context.get(), "Completed adding events", Toast.LENGTH_LONG).show();
            super.onPostExecute(_days);
        }
    }
}