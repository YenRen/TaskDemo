package com.dyhx.kdtask.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by YeRen on 2020/7/29.
 * Describe:
 */
@Entity
public class MinuteTaskMd {
    @Id(autoincrement = true)
    private Long id;
    private int day;
    private int hour;
    private int minute;
    private String name;
    private String startTime;
    private String endTime;
    private String describe;
    @Generated(hash = 913177443)
    public MinuteTaskMd(Long id, int day, int hour, int minute, String name,
            String startTime, String endTime, String describe) {
        this.id = id;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.describe = describe;
    }
    @Generated(hash = 1663726897)
    public MinuteTaskMd() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getDay() {
        return this.day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getHour() {
        return this.hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinute() {
        return this.minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStartTime() {
        return this.startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getDescribe() {
        return this.describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
