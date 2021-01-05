package com.cykj.bean;

public class Lines {
    private String id;
    private String name;
    private Object location;
    private String start_stop;
    private String end_stop;

    public Lines() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public String getStart_stop() {
        return start_stop;
    }

    public void setStart_stop(String start_stop) {
        this.start_stop = start_stop;
    }

    public String getEnd_stop() {
        return end_stop;
    }

    public void setEnd_stop(String end_stop) {
        this.end_stop = end_stop;
    }
}
