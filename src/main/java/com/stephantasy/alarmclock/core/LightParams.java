package com.stephantasy.alarmclock.core;

public class LightParams {
    LightMode mode =LightMode.WHITE;
    int color1 = 0;
    int color2 = 0;
    int color3 = 0;
    int brightness = 0;

    public LightMode getMode() {
        return mode;
    }

    public void setMode(LightMode mode) {
        this.mode = mode;
    }

    public int getColor1() {
        return color1;
    }

    public void setColor1(int color1) {
        this.color1 = color1;
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
    }

    public int getColor3() {
        return color3;
    }

    public void setColor3(int color3) {
        this.color3 = color3;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
}
