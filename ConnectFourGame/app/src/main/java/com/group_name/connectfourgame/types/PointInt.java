package com.group_name.connectfourgame.types;

public class PointInt implements Cloneable {
   public int x;
   public int y;

   public PointInt() {
      x = 0; y = 0;
   }
   public PointInt(final int p_x, final int p_y) {
      x = p_x; y = p_y;
   }

   @Override
   public PointInt clone() {
      return new PointInt(x, y);
   }

   @Override
   public String toString() {
      return Integer.toString(x)+','+y;
   }

   public int get(final int index) {
      if(index == 0)
         return x;
      return y;
   }

   public PointDouble divide(final double divisor) {
      return new PointDouble(x/divisor, y/divisor);
   }

   public PointInt subtract(final int sub) {
      return new PointInt(x-sub, y-sub);
   }

   public double distance(final PointInt other) {
      final double a = x - other.x;
      final double b = y - other.y;
      return Math.sqrt(a*a+b*b);
   }

   public double distance(final PointDouble other) {
      final double a = other.x - x;
      final double b = other.y - y;
      return Math.sqrt(a*a+b*b);
   }
}
