package com.dyhx.kdtask;

import com.dyhx.kdtask.db.model.DayTaskMd;
import com.dyhx.kdtask.db.model.HourTaskMd;
import com.dyhx.kdtask.db.model.MinuteTaskMd;

/**
 * Created by YeRen on 2020/7/29.
 * Describe:
 */
public class TaskModel {

    private DayTaskMd dayTaskMd;
    private HourTaskMd hourTaskMd;
    private MinuteTaskMd minuteTaskMd;
    private String timeMark;

    public DayTaskMd getDayTaskMd() {
        return dayTaskMd;
    }

    public void setDayTaskMd(DayTaskMd dayTaskMd) {
        this.dayTaskMd = dayTaskMd;
    }

    public HourTaskMd getHourTaskMd() {
        return hourTaskMd;
    }

    public void setHourTaskMd(HourTaskMd hourTaskMd) {
        this.hourTaskMd = hourTaskMd;
    }

    public MinuteTaskMd getMinuteTaskMd() {
        return minuteTaskMd;
    }

    public void setMinuteTaskMd(MinuteTaskMd minuteTaskMd) {
        this.minuteTaskMd = minuteTaskMd;
    }

    public String getTimeMark() {
        return timeMark;
    }

    public void setTimeMark(String timeMark) {
        this.timeMark = timeMark;
    }
}
