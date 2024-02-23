package me.sialim.danganronparoleplayv2;

public class Utils {
    public static boolean isWithinRadius(double x1,double y1,double z1,double x2,double y2,double z2,int radius)
    {
        double dis=Math.pow(x1-x2,2)+Math.pow(y1-y2,2)+Math.pow(z1-z2,2);
        double rad=Math.pow(radius,2);
        return dis<=rad;
    }
}

