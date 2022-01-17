package com.flytrom.learning.interfaces;

public interface OnTaskCompleted {
    default void onTaskCompleted(int position){}
    default void onTaskCompletedPdf(String position){}
}
