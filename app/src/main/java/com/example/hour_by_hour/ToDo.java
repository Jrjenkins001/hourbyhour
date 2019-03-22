package com.example.hour_by_hour;

public class ToDo implements Comparable<ToDo>{
    private boolean _completed;
    private String _name;

    ToDo(String _name) {
        set_name(_name);
    }

    void set_completed(boolean _completed){
        this._completed = _completed;
    }

    boolean get_completed() {
        return _completed;
    }

    void set_name(String _name) {
        this._name = _name;
    }

    String get_name() {
        return _name;
    }

    @Override
    public int compareTo(ToDo o) {
        return _name.compareTo(o.get_name());
    }
}
