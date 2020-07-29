package com.dyhx.kdtask.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by YeRen on 2020/7/28.
 * Describe:
 */
@Entity
public class DayTaskMd {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private int day;
    private String name;
    private String startTime;
    private String endTime;
    private String describe;
    @Generated(hash = 1261286162)
    public DayTaskMd(Long id, int day, String name, String startTime,
            String endTime, String describe) {
        this.id = id;
        this.day = day;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.describe = describe;
    }
    @Generated(hash = 1698767763)
    public DayTaskMd() {
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
