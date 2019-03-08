package com.example.hour_by_hour;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MaterialCalendarView widget;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Task> taskList;
    ArrayList<Day> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        days = Day.createDays(3);

        initializeRecyclerView(R.id.recycler_view);

        Toolbar myToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);
        widget = findViewById(R.id.calendarView);

        Gson g = new Gson();
        //SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String dataString = getResources().getString(R.string.saved_data_info);
        // for ease of testing
        // taskList = g.fromJson(dataString, ArrayList.class);

        taskList = new ArrayList<>();
        taskList.add(new Task());
    }

    private void initializeRecyclerView(int view){
        recyclerView = findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new TaskAdapter(days.get(0).dayTasks);
        recyclerView.setAdapter(mAdapter);
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
                startActivity(intent);
                return true;

            case (R.id.change_day_view):
                //TODO display day event info
                initializeRecyclerView(R.id.day_recycler_view);
                setContentView(R.layout.day_view);
                Toolbar toolbar = findViewById(R.id.toolbar_day_view);
                setSupportActionBar(toolbar);
                return true;

            case (R.id.change_month_view):
                setContentView(R.layout.main_activity);
                initializeRecyclerView(R.id.recycler_view);
                toolbar = findViewById(R.id.toolbar_main);
                setSupportActionBar(toolbar);
                widget = findViewById(R.id.calendarView);
                widget.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                return true;

            case (R.id.change_week_view):
                setContentView(R.layout.main_activity);
                initializeRecyclerView(R.id.recycler_view);
                toolbar = findViewById(R.id.toolbar_main);
                setSupportActionBar(toolbar);
                widget = findViewById(R.id.calendarView);
                widget.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onPause() {
        super.onPause();

        Gson g = new Gson();
        String dataJSON = g.toJson(taskList);
        Log.i("MAIN", dataJSON);
        Log.e("ON_PAUSE", "IN ON PAUSE");

        //SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putString(getString(R.string.saved_data_info), dataJSON);
    }
}
