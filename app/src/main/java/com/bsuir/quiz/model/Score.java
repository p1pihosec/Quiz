package com.bsuir.quiz.model;

public class Score {

    private String name;
    private Long value;
    private String time;

    public Score() {
    }

    public Score(String name, Long value, String time) {
        this.name = name;
        this.value = value;
        this.time = time;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
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

    @Override
    public String toString() {
        return "Email: " + name + ", ques amount: " + value + ", time: " + time;
    }
}
