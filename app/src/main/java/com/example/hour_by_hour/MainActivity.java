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
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;

import static java.util.Collections.sort;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener {

    private MaterialCalendarView _widget;
    private SparseArray<ArrayList<Task>> _days;
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
        _days = new SparseArray<>();
        _calendarDay = null;

        /*Gson gson = new Gson();
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.saved_data_info), Context.MODE_PRIVATE);
        String dataString = sharedPref.getString(getString(R.string.saved_preferences_json), "NULL");
        Log.i("MainActivity", dataString);

        if(!dataString.equals("NULL")) {
            _days = gson.fromJson(dataString, new TypeToken<SparseArray<ArrayList<Task>>>() {
            }.getType());
        }*/

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            CalendarDay calendarDay = extras.getParcelable(getString(R.string.EXTRA_CALENDAR_DAY));
            Task task = extras.getParcelable(getString(R.string.EXTRA_TASK));

            if(calendarDay != null) {
                Log.i("MainActivity", "Calendar is not null");

                ArrayList<Task> tasks = new ArrayList<>();

                Log.i("MainActivity",(_days.get(calendarDay.hashCode()) != null) ? "True" : "False");
                if(_days.size() > 0 || _days.get(calendarDay.hashCode()) != null) {
                    Log.i("MainActivity", "There is a list already");
                    tasks = _days.get(calendarDay.hashCode());
                }

                tasks.add(task);
                _days.put(calendarDay.hashCode(), tasks);

                sort(_days.get(calendarDay.hashCode()));
                _taskList = _days.get(calendarDay.hashCode());
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
                    intent.putExtra(getString(R.string.EXTRA_TASK), _days.get(_calendarDay.hashCode()));
                }
                startActivity(intent);
                return true;

            case (R.id.change_to_do_list):
                Intent intentToDo = new Intent(this, toDoListActivity.class);
                startActivity(intentToDo);
                return true;

            case (R.id.change_month_view):
                setContentView(R.layout.main_activity);
                initializeRecyclerView(_taskList);
                toolbar = findViewById(R.id.toolbar_main);
                setSupportActionBar(toolbar);
                _widget = findViewById(R.id.calendarView);
                _widget.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                return true;

            case (R.id.change_week_view):
                setContentView(R.layout.main_activity);
                initializeRecyclerView(_taskList);
                toolbar = findViewById(R.id.toolbar_main);
                setSupportActionBar(toolbar);
                _widget = findViewById(R.id.calendarView);
                _widget.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onPause() {
        super.onPause();

        Gson g = new Gson();
        String dayJSON = g.toJson(_days);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.saved_data_info), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_preferences_json), dayJSON);
        editor.apply();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        _taskList = _days.get(date.hashCode());
        RecyclerView.Adapter mAdapter = new TaskAdapter(_taskList);
        _recyclerView.setAdapter(mAdapter);
        _calendarDay = date;
    }
}
