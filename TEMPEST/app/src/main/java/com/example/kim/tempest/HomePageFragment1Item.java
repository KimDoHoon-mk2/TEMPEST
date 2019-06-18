package com.example.kim.tempest;

public class HomePageFragment1Item {
    private String id;
    private String name;
    private String time;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public HomePageFragment1Item(){}

    public HomePageFragment1Item(String id, String name, String time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }
}
