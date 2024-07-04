package com.dubr0vin.diary.models;

public class Color {
    public int r, g, b;

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return r == color.r && g == color.g && b == color.b;
    }

    public int getAndroidColor(){
        return android.graphics.Color.rgb(r,g,b);
    }

    public String toString(){
        return r + "," + g + "," + b;
    }

    public static Color White = new Color(255,255,255);
    public static Color Black = new Color(0,0,0);
}
