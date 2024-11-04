package com.group_name.connectfourgame.types;

public class PointDouble implements Cloneable {
   public double x;
   public double y;
   
   public PointDouble(final double p_x, final double p_y) {
      x = p_x; y = p_y;
   }

   @Override
   public PointDouble clone() {
      return new PointDouble(x, y);
   }

   @Override
   public String toString() {
      return Double.toString(x)+','+y;
   }

   public double get(final int index) {
      if(index == 0)
         return x;
      return y;
   }

   public PointDouble subtract(final double sub) {
      return new PointDouble(x-sub, y-sub);
   }

   public PointDouble divide(final double divisor) {
      return new PointDouble(x/divisor, y/divisor);
   }

   public double distance(final PointInt other) {
      final double a = other.x - x;
      final double b = other.y - y;
      return Math.sqrt(a*a+b*b);
   }

   public double distance(final PointDouble other) {
      final double a = x - other.x;
      final double b = y - other.y;
      return Math.sqrt(a*a+b*b);
   }
}
