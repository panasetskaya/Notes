package com.example.notes2;

public class Note {
    private int id;
    private String title;
    private String desc;
    private int dayOfWeek;
    private int priority;

    public Note(int id, String title, String desc, int dayOfWeek, int priority) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.dayOfWeek = dayOfWeek;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getPriority() {
        return priority;
    }

    public static String getDayAsString(int position) {
        switch (position) {
            case 1:
                return "понедельник";
            case 2:
                return "вторник";
            case 3:
                return "среда";
            case 4:
                return "четверг";
            case 5:
                return "пятница";
            case 6:
                return "суббота";
            default:
                return "воскресенье";
        }
    }
}
