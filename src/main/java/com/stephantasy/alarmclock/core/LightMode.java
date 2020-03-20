package com.stephantasy.alarmclock.core;

public enum LightMode {
    WHITE(2),
    RGB(3);

    private final int value;
    LightMode(final int newValue) {
        value = newValue;
    }
    public int getValue() { return value; }
}