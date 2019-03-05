package com.example.hour_by_hour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class EditTask extends AppCompatActivity {

    @Override
    // create a new instance with a toolbar
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar myToolbar = findViewById(R.id.toolbar_edit_event);
        setSupportActionBar(myToolbar);
    }

    @Override
    // create the options available for the toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_event, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    // when either the add or delete button is selected, this will be called
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_event:
                // User clicked on adding the selected event information
                createEvent();
                return true;

            case R.id.action_delete_event:
                // User clicked on deleting the event currently being worked on
                deleteEvent();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * create a new event with the information provided in the inputs and returns
     * to the main activity afterward.
     */
    void createEvent() {
        CharSequence message = "Event Created";
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * delete the event that is currently being edited, if there this is an existing
     * event, delete from the list and return to the main activity. Otherwise, just
     * return to the main activity.
     */
    void deleteEvent() {
        CharSequence message = "Event Deleted";
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
