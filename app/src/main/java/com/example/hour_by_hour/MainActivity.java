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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener {

    private MaterialCalendarView _widget;
    private HashMap<CalendarDay,ArrayList<Task>> _days;
    private ArrayList<Task> _taskList;
    RecyclerView _recyclerView;
    private CalendarDay _calendarDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        _taskList = new ArrayList<>();
        initializeRecyclerView(R.id.recycler_view, _taskList);

        Toolbar myToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);
        _widget = findViewById(R.id.calendarView);
        _widget.setOnDateChangedListener(this);

        _days = new HashMap<>();
        _calendarDay = null;

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            CalendarDay calendarDay = extras.getParcelable(getString(R.string.EXTRA_CALENDAR_DAY));
            ArrayList<Task> tasks = extras.getParcelable(getString(R.string.EXTRA_TASK));

            _days.put(calendarDay, tasks);
        }

        //Gson g = new Gson();
        //SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //String dataString = getResources().getString(R.string.saved_data_info);
        //for ease of testing
        //days = g.fromJson(dataString, HashMap.class);
    }

    private void initializeRecyclerView(int view, ArrayList<Task> tasks){
        _recyclerView = findViewById(view);
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
        switch (item.getItemId()) {
            case (R.id.action_add_new_event):
                Intent intent = new Intent(this, EditTask.class);
                if(_calendarDay != null){
                    intent.putExtra(getString(R.string.EXTRA_CALENDAR_DAY), _calendarDay);
                }
                startActivity(intent);
                return true;

            case (R.id.change_day_view):
                //TODO display day event info
                setContentView(R.layout.day_view);
                initializeRecyclerView(R.id.day_recycler_view, _taskList);
                Toolbar toolbar = findViewById(R.id.toolbar_day_view);
                setSupportActionBar(toolbar);
                return true;

            case (R.id.change_month_view):
                setContentView(R.layout.main_activity);
                initializeRecyclerView(R.id.recycler_view, _taskList);
                toolbar = findViewById(R.id.toolbar_main);
                setSupportActionBar(toolbar);
                _widget = findViewById(R.id.calendarView);
                _widget.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                return true;

            case (R.id.change_week_view):
                setContentView(R.layout.main_activity);
                initializeRecyclerView(R.id.recycler_view, _taskList);
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
        String dataJSON = g.toJson(getString(R.string.saved_data_info));

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_data_info), dataJSON);
        editor.apply();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        _taskList = _days.get(date);
        RecyclerView.Adapter mAdapter = new TaskAdapter(_taskList);
        _recyclerView.setAdapter(mAdapter);
        _calendarDay = date;
    }
}
