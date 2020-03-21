package com.stephantasy.alarmclock.core;

public class LightParams {
    LightMode mode =LightMode.WHITE;
    Color color;
    int brightness = 0;
    boolean gradient = false;
    Color colorFrom;
    Color colorTo;

    public LightParams() {
    }

    public LightParams(Color colorFrom, Color colorTo) {
        this.gradient = true;
        this.color = colorFrom;
        this.colorFrom = colorFrom;
        this.colorTo = colorTo;
    }

    public LightMode getMode() {
        return mode;
    }

    public void setMode(LightMode mode) {
        this.mode = mode;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isGradient() {
        return gradient;
    }

    public void setGradient(boolean gradient) {
        this.gradient = gradient;
    }

    public Color getColorFrom() {
        return colorFrom;
    }

    public void setColorFrom(Color colorFrom) {
        this.colorFrom = colorFrom;
    }

    public Color getColorTo() {
        return colorTo;
    }

    public void setColorTo(Color colorTo) {
        this.colorTo = colorTo;
    }
}
