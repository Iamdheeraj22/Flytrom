package com.flytrom.learning.model.progressModel;

public class ProgressData {
    public String subject;
    public SubjectDetails subject_details;
    public int test_total;
    public int test_completed;
    public String test_accuracy;
    public String test_completion_percent;
    public int qb_total;
    public int qb_completed;
    public String qb_accuracy;
    public String qb_completion_percent;
    public int video_total;
    public int video_completed;
    public String video_completion_percent;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public SubjectDetails getSubject_details() {
        return subject_details;
    }

    public void setSubject_details(SubjectDetails subject_details) {
        this.subject_details = subject_details;
    }

    public int getTest_total() {
        return test_total;
    }

    public void setTest_total(int test_total) {
        this.test_total = test_total;
    }

    public int getTest_completed() {
        return test_completed;
    }

    public void setTest_completed(int test_completed) {
        this.test_completed = test_completed;
    }

    public String getTest_accuracy() {
        return test_accuracy;
    }

    public void setTest_accuracy(String test_accuracy) {
        this.test_accuracy = test_accuracy;
    }

    public String getTest_completion_percent() {
        return test_completion_percent;
    }

    public void setTest_completion_percent(String test_completion_percent) {
        this.test_completion_percent = test_completion_percent;
    }

    public int getQb_total() {
        return qb_total;
    }

    public void setQb_total(int qb_total) {
        this.qb_total = qb_total;
    }

    public int getQb_completed() {
        return qb_completed;
    }

    public void setQb_completed(int qb_completed) {
        this.qb_completed = qb_completed;
    }

    public String getQb_accuracy() {
        return qb_accuracy;
    }

    public void setQb_accuracy(String qb_accuracy) {
        this.qb_accuracy = qb_accuracy;
    }

    public String getQb_completion_percent() {
        return qb_completion_percent;
    }

    public void setQb_completion_percent(String qb_completion_percent) {
        this.qb_completion_percent = qb_completion_percent;
    }

    public int getVideo_total() {
        return video_total;
    }

    public void setVideo_total(int video_total) {
        this.video_total = video_total;
    }

    public int getVideo_completed() {
        return video_completed;
    }

    public void setVideo_completed(int video_completed) {
        this.video_completed = video_completed;
    }

    public String getVideo_completion_percent() {
        return video_completion_percent;
    }

    public void setVideo_completion_percent(String video_completion_percent) {
        this.video_completion_percent = video_completion_percent;
    }
}
