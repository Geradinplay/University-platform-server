package com.example.entity;

import java.time.LocalTime;

public interface Schedulable {
    LocalTime getStartTime();
    LocalTime getEndTime();
    Integer getDay();
}