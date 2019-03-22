package com.example.hour_by_hour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Collections.sort;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener {

    private MaterialCalendarView _widget;
    //private HashMap<Integer,ArrayList<Task>> _days;
    private HashMap<String, ArrayList<Task>> _days;
    private ArrayList<Task> _taskList;
    RecyclerView _recyclerView;
    private CalendarDay _calendarDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        _taskList = new ArrayList<>();
        _widget = findViewById(R.id.calendarView);
        _widget.setOnDateChangedListener(this);
        _widget.setCurrentDate(CalendarDay.today());

        _calendarDay = null;

        Gson gson = new Gson();
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.saved_data_info), Context.MODE_PRIVATE);
        String dataString = sharedPref.getString(getString(R.string.saved_preferences_json), "NULL");
        Log.i("MainActivity", "log below is dataString from file");
        Log.i("MainActivity", dataString);


        if (dataString != null && !dataString.equals("NULL")) {
            Type type = new TypeToken<HashMap<String, ArrayList<Task>>>(){}.getType();
            try {
                _days = gson.fromJson(dataString, type);
            } catch (JsonSyntaxException e) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear().apply();
                _days = new HashMap<>();
                Log.e("MainActivity", e.getMessage());
                Log.e("MainActivity", "ERROR: COULD NOT READ FROM FILE");
                Log.e("MainActivity", dataString);
            }
        } else {
            _days = new HashMap<>();
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            CalendarDay calendarDay = extras.getParcelable(getString(R.string.EXTRA_CALENDAR_DAY));
            Task task = extras.getParcelable(getString(R.string.EXTRA_TASK));

            if(calendarDay != null) {
                //ArrayList<Task> taskItems = _days.get(calendarDay.hashCode());
                ArrayList<Task> taskItems = _days.get(calendarDay.toString());
                if(taskItems == null) {
                    Log.i("MainActivity", "Null List");
                    taskItems = new ArrayList<>();
                }

                if(task != null) {
                    Log.i("MainActivity","We have a task " + task.getName());

                    taskItems.add(task);

                    sort(taskItems);

                    Log.i("MainActivity", calendarDay.hashCode() + "Day");
                    //_days.put(calendarDay.hashCode(), taskItems);
                    _days.put(calendarDay.toString(), taskItems);
                }

                //_taskList = _days.get(calendarDay.hashCode());
                _taskList = _days.get(calendarDay.toString());
                _widget.setCurrentDate(calendarDay);
            } else {
                Log.d ("MainActivity", "The calendar day is not transferring properly");
            }
        }

        initializeRecyclerView(_taskList);

        Toolbar myToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);
    }

    private void initializeRecyclerView(ArrayList<Task> tasks){
        _recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new TaskAdapter(tasks);
        _recyclerView.setAdapter(mAdapter);
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
                    //intent.putExtra(getString(R.string.EXTRA_TASK), _days.get(_calendarDay.hashCode()));
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

            case (R.id.remove_shared_prefernces):
                //TODO remove after all testing
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.saved_data_info), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear().apply();
                _days = new HashMap<>();
                Toast toast = Toast.makeText(this, "Deleted File", Toast.LENGTH_SHORT);
                toast.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onPause() {
        super.onPause();

        if(_days.size() > 0) {
            Gson g = new Gson();
            Type type = new TypeToken<HashMap<String, ArrayList<Task>>>(){}.getType();
            String dayJSON = g.toJson(_days, type);
            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.saved_data_info), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.saved_preferences_json), dayJSON);
            editor.apply();
            Log.i("MainActivity", "Task file should be created");
        }

        Log.i("MainActivity", "The size is " + _days.size());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //_taskList = _days.get(date.hashCode());
        _taskList = _days.get(date.toString());
        Log.i("MainActivity", date.toString());
        RecyclerView.Adapter mAdapter = new TaskAdapter(_taskList);
        _recyclerView.setAdapter(mAdapter);
        _calendarDay = date;
    }
}
