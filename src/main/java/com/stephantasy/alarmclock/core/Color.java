package com.stephantasy.alarmclock.core;

public class Color {

    int color1;
    int color2;
    int color3;

    public Color(int color1, int color2, int color3) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
    }

    /**
     *
     * @return CW or Red value
     */
    public int getColor1() {
        return color1;
    }

    /**
     * CW or Red
     * @param color1: The value of the color
     */
    public void setColor1(int color1) {
        this.color1 = color1;
    }

    /**
     *
     * @return Green value
     */
    public int getColor2() {
        return color2;
    }


    /**
     * Green
     * @param color2: The value of the color
     */
    public void setColor2(int color2) {
        this.color2 = color2;
    }

    /**
     *
     * @return Blue value
     */
    public int getColor3() {
        return color3;
    }

    @Override
    public String toString() {
        return "Color{" +
                "color1=" + color1 +
                ", color2=" + color2 +
                ", color3=" + color3 +
                '}';
    }


    /**
     * Blue
     * @param color3: The value of the color
     */
    public void setColor3(int color3) {
        this.color3 = color3;
    }


}
