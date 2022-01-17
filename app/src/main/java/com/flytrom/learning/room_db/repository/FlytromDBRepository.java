package com.flytrom.learning.room_db.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.flytrom.learning.beans.response_beans.custom_module.CustomModuleBean;
import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean;
import com.flytrom.learning.beans.response_beans.random_question.RandomQuestionBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.beans.response_beans.test_bean.TestBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.room_db.db.AppDatabase;
import com.flytrom.learning.utils.Constants;

import java.util.List;

public class FlytromDBRepository {

    private AppDatabase database;

    public FlytromDBRepository(Application application) {
        database = AppDatabase.getDatabase(application);
    }

    /*** GETTING DATA */

    public LiveData<List<SubjectBean>> getSubjects(String orderType) {
        return database.daoAccess().getSubjects(orderType);
    }

    public LiveData<List<CustomModuleBean>> getCustomModuleFromRoom() {
        return database.daoAccess().getCustomModuleFromRoom();
    }

    public LiveData<List<QuestionBean>> getBookmarked() {
        return database.daoAccess().getBookmarked("QB", "Test");
    }

    public LiveData<List<QuestionBean>> getFreeQuestions(String type) {
        return database.daoAccess().getFreeQuestions(type);
    }

    public LiveData<List<TestBean>> getSubjectTests(String subject) {

        return database.daoAccess().getSubjectTests(subject, Constants.PAID);
    }

    public LiveData<List<TestBean>> getFreeTests(String type) {

        return database.daoAccess().getFreeTests(type);
    }

    public LiveData<List<QuestionBean>> getTestQuestions(String id) {
        return database.daoAccess().getTestQuestions(id);
    }

    public LiveData<List<QuestionBean>> getCustomModuleQuestions(String id) {
        return database.daoAccess().getCustomModuleQuestions(id);
    }

    public LiveData<List<QuestionBean>> getQBankChapterQuestions(String questionCategoryChapter, int mChapterId) {
        return database.daoAccess().getQBankChapterQuestions(questionCategoryChapter, mChapterId);
    }

    public LiveData<List<VideoBean>> getVideos(String type) {
        return database.daoAccess().getVideos(type);
    }

    public LiveData<List<VideoBean>> getParticularSavedVideoById(int videoId) {
        return database.daoAccess().getParticularSavedVideoById(videoId, Constants.SAVED);
    }

    public LiveData<List<VideoBean>> getSubjectVideos(String category) {
        return database.daoAccess().getSubjectVideos(category, Constants.PAID);
    }

    public LiveData<List<ChapterBean>> getSubjectChapters(String question_category) {
        return database.daoAccess().getSubjectChapters(question_category, Constants.PAID);
    }

    public LiveData<List<ChapterBean>> getFreeChapters() {
        return database.daoAccess().getFreeChapters(Constants.FREE);
    }

    public LiveData<List<RandomQuestionBean>> getRandomQuestion() {
        return database.daoAccess().getRandomQuestion();
    }
}
