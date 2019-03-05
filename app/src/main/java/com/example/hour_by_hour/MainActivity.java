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

import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class MainActivity extends AppCompatActivity {

    private MaterialCalendarView widget;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //mAdapter = new TaskAdapter();
        recyclerView.setAdapter(mAdapter);
        Toolbar myToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);
        widget = findViewById(R.id.calendarView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case (R.id.action_add_new_event):
                Intent intent = new Intent(this, EditTask.class);
                startActivity(intent);
                return true;

            case(R.id.change_day_view):
                //TODO display day event info
                return true;

            case(R.id.change_month_view):
                widget.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
                return true;

            case(R.id.change_week_view):
                widget.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
