package com.stephantasy.alarmclock.core;

import javax.persistence.Embeddable;

@Embeddable
public class Recurrence {
    private RecurrenceType recurrenceType;
    private boolean[] days;

    public Recurrence(RecurrenceType recurrenceType, boolean[] days) {
        this.recurrenceType = recurrenceType;
        this.days = days;
    }

    public Recurrence() {
        this.recurrenceType = RecurrenceType.Once;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(RecurrenceType recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }
}
