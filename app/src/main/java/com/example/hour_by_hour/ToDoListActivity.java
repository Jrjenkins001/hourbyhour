package com.example.hour_by_hour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements AddNewToDoDialogFragment.AddNewToDoDialogListener{
    List<ToDo> _toDoList;
    RecyclerView.Adapter mAdapter;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_do_list);

        gson = new Gson();

        _toDoList = new ArrayList<>();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        if(sharedPref.contains(getString(R.string.SAVE_DATA_TO_DO_LIST))) {
             String toDoString = sharedPref.getString(getString(R.string.SAVE_DATA_TO_DO_LIST), "Empty");
             if(toDoString != null && !toDoString.equals("Empty")) {
                 _toDoList = gson.fromJson(toDoString, new TypeToken<List<ToDo>>(){}.getType());
             }
        }

        initializeRecyclerView(_toDoList);

        Toolbar myToolbar = findViewById(R.id.toolbar_to_do_list);
        setSupportActionBar(myToolbar);
    }

    private void initializeRecyclerView(List<ToDo> toDo){
        RecyclerView recyclerView = findViewById(R.id.to_do_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ToDoAdapter(toDo);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_to_do_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        switch (menu.getItemId()) {
            case (R.id.month_view_to_do):
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case (R.id.add_to_do):
                // alert dialog
                showAddToDoDialog();
                return true;

            case (R.id.remove_selected):
                for(int i = _toDoList.size() - 1; i >= 0; i--) {
                    if(_toDoList.get(i).get_completed()){
                        _toDoList.remove(i);
                        Log.i("ToDoListActivity", "Removed Item");
                    }

                    initializeRecyclerView(_toDoList);
                }
                return true;

            default:
                // shouldn't get here at all
                return false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gson = new Gson();
        String toDoJSON = gson.toJson(_toDoList);

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.SAVE_DATA_TO_DO_LIST),toDoJSON);
        editor.apply();
    }

    /**
     * Create an instance of the dialog fragment and show it
     */
    public void showAddToDoDialog() {
        DialogFragment dialog = new AddNewToDoDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddNewToDo");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name) {
        _toDoList.add(new ToDo(name));
        initializeRecyclerView(_toDoList);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    public void set_toDoList(List<ToDo> _toDoList) {
        this._toDoList = _toDoList;
    }
}
