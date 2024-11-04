package com.group_name.connectfourgame.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayList2d<T> implements Iterable<T> {
   private final ArrayList<T> mData;
   private final int col_size;

   public static <T> ArrayList2d<T> asEmpty(final int rows, final int cols) {
      ArrayList2d<T> list = new ArrayList2d<>(rows, cols);
      for(int i = 0; i < rows*cols; ++i)
         list.add(null);
      return list;
   }

   public static <T> ArrayList2d<T> asFill(final T value, final int cols, final int rows) {
      ArrayList2d<T> list = new ArrayList2d<>(rows, cols);
      for(int i = 0; i < rows*cols; ++i)
         list.add(value);
      return list;
   }

   public ArrayList2d(final int rows, final int cols) {
      col_size = cols;
      mData = new ArrayList<>(rows*cols);
   }

   public ArrayList2d(final ArrayList<T> list, final int cols) {
      col_size = cols;
      mData = list;
   }

   public T get(final int x, final int y) {
      return mData.get(y*col_size + x);
   }

   public T get(final PointInt pos) {
      return mData.get(pos.y*col_size + pos.x);
   }

   public List<T> getCols(int index) {
      final int rowLength = getRowLength();
      ArrayList<T> res = new ArrayList<>(rowLength);
      for(int i = 0; i < rowLength; ++i) {
         res.add(mData.get(index));
         index += col_size;
      }
      return res;
   }

   public List<T> getRows(final int index) {
      return mData.subList(index*col_size, (index+1)*col_size);
   }

   public void set(final int x, final int y, final T val) {
      mData.set(y*col_size + x, val);
   }

   public void set(final T val, final PointInt pos) {
      mData.set(pos.y*col_size + pos.x, val);
   }

   public final ArrayList<T> getRawArrayList() {
      return mData;
   }

   public final int getColLength() {
      return col_size;
   }

   public final int getRowLength() {
      return mData.size() / col_size;
   }

   public final int getTotalCells() {
      return mData.size();
   }

   @Override
   public Iterator<T> iterator() {
      return mData.iterator();
   }

   public void add(final T data) {
      mData.add(data);
   }

   public PointInt getDimension() {
      return new PointInt(col_size, mData.size()/col_size);
   }

   public PointInt getAppendPosition() {
      return new PointInt(mData.size()%col_size, mData.size()/col_size);
   }

   public void addNull() {
      mData.add(null);
   }
}
