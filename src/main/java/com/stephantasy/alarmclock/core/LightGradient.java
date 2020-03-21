package com.stephantasy.alarmclock.core;

public class LightGradient {

    /**
     * Send a color where each color component (r, g, b) is calculated. The value is a percentage defined by Brightness between From and To
     * Ex: From = {0, 100, 0} ; To = {0, 200, 0} ; Brightness = 50% => {0, 150, 0}
     * @param brightness Brightness in percentage
     * @param colorFrom The first clolr of the gradient
     * @param colorTo The last color of the gradient
     * @return A color based on the percentage of the brightness
     */
    public static Color getColor(int brightness, Color colorFrom, Color colorTo){
        int delta1 = colorTo.color1 - colorFrom.color1 ;
        int c1 = colorFrom.color1 + (int)(delta1 * (brightness/100.0));

        int delta2 = colorTo.color2 - colorFrom.color2;
        int c2 = colorFrom.color2 + (int)(delta2 * (brightness/100.0));

        int delta3 = colorTo.color3 - colorFrom.color3;
        int c3 = colorFrom.color3 + (int)(delta3 * (brightness/100.0));

        return new Color(c1, c2, c3);
    }

    public static void main(String[] args) {
        Color cFrom = new Color(50, 100, 200);
        Color cTo = new Color(100, 0, 240);

        Color test0 = getColor(0, cFrom, cTo);
        Color test1 = getColor(25, cFrom, cTo);
        Color test2 = getColor(50, cFrom, cTo);
        Color test3 = getColor(75, cFrom, cTo);
        Color test4 = getColor(100, cFrom, cTo);
    }

}
