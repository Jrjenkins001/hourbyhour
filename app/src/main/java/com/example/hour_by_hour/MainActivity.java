package com.example.hour_by_hour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MaterialCalendarView widget;
    private ArrayList<Task> taskList;
    private ArrayList<Day> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // exclusively for testing
        //TODO remove this
        days = Day.createDays(1);


        initializeRecyclerView(R.id.recycler_view);

        Toolbar myToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);
        widget = findViewById(R.id.calendarView);


        Gson g = new Gson();
        //SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //String dataString = getResources().getString(R.string.saved_data_info);
        // for ease of testing
        // dayList = g.fromJson(dataString, ArrayList.class);

        taskList = new ArrayList<>();
        taskList.add(new Task());
    }

    private void initializeRecyclerView(int view){
        RecyclerView recyclerView = findViewById(view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter mAdapter = new TaskAdapter(days.get(0).dayTasks);
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
                setContentView(R.layout.day_view);
                initializeRecyclerView(R.id.day_recycler_view);
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

        //SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putString(getString(R.string.saved_data_info), dataJSON);
    }
}
