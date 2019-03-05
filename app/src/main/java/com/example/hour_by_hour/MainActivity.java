package com.example.hour_by_hour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);

        Toolbar myToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);
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
                setContentView(R.layout.activity_day_view);
                return true;

            case(R.id.change_month_view):
                setContentView(R.layout.activity_month_view);
                return true;

            case(R.id.change_week_view):
                setContentView(R.layout.activity_week_view);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // used exclusively to move to edit task while implementation isn't complete
    public void onClickTest(View view) {
        Intent intent = new Intent(this, EditTask.class);
        startActivity(intent);
    }

}
