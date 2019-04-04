package com.example.hour_by_hour;

/**
 * A simple object used to view different items needed to be done
 */
class ToDo implements Comparable<ToDo>{
    private boolean _completed;
    private String _name;

    /**
     * initializes name and sets completed to false
     * @param _name new name of object
     */
    ToDo(String _name) {
        set_name(_name);
        set_completed(false);
    }

    void set_completed(boolean _completed){
        this._completed = _completed;
    }

    boolean get_completed() {
        return _completed;
    }

    private void set_name(String _name) {
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
