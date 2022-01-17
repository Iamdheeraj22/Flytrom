package com.flytrom.learning.room_db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.beans.response_beans.bookmark.BookmarkedQuestionBean;
import com.flytrom.learning.beans.response_beans.custom_module.CustomModuleBean;
import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean;
import com.flytrom.learning.beans.response_beans.random_question.RandomQuestionBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.beans.response_beans.test_bean.TestBean;
import com.flytrom.learning.room_db.converters.AllTypeConverters;

import java.util.List;

@Dao
public interface DaoAccess {

    /*** insert */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubjects(List<SubjectBean> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookmarked(List<BookmarkedQuestionBean> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTests(List<TestBean> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCustomModule(List<CustomModuleBean> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestions(List<QuestionBean> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideos(List<VideoBean> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleVideo(VideoBean videoBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubjectChapters(List<ChapterBean> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFreeChapters(List<ChapterBean> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRandomQuestion(RandomQuestionBean model);

    /*** get queries */

    @Query("SELECT * from Subjects WHERE orderType =:orderType")
    LiveData<List<SubjectBean>> getSubjects(String orderType);

    @Query("SELECT * from CustomModule")
    LiveData<List<CustomModuleBean>> getCustomModuleFromRoom();

    @Query("SELECT * from Questions WHERE type=:type")
    LiveData<List<QuestionBean>> getFreeQuestions(String type);

    @Query("SELECT * from Questions WHERE b_type=:b_type_qb or b_type=:b_type_test")
    LiveData<List<QuestionBean>> getBookmarked(String b_type_qb, String b_type_test);

    @Query("SELECT * from Videos WHERE type =:type")
    LiveData<List<VideoBean>> getVideos(String type);

    @Query("SELECT * from Videos WHERE id=:videoId AND type =:type")
    LiveData<List<VideoBean>> getParticularSavedVideoById(int videoId, String type);

    @Query("SELECT * from Videos WHERE category =:category AND type=:type")
    LiveData<List<VideoBean>> getSubjectVideos(String category, String type);

    @Query("SELECT * from Chapters WHERE question_category =:category AND type=:type")
    LiveData<List<ChapterBean>> getSubjectChapters(String category, String type);

    @Query("SELECT * from Chapters WHERE type =:type")
    LiveData<List<ChapterBean>> getFreeChapters(String type);

    @Query("SELECT * from Tests WHERE subject =:subject AND type=:type")
    LiveData<List<TestBean>> getSubjectTests(String subject, String type);

    @Query("SELECT * from Tests WHERE type =:type")
    LiveData<List<TestBean>> getFreeTests(String type);

    @Query("SELECT * from Questions WHERE testId = :testId")
    LiveData<List<QuestionBean>> getTestQuestions(String testId);

    @Query("SELECT * from Questions WHERE customModuleId = :customModuleId")
    LiveData<List<QuestionBean>> getCustomModuleQuestions(String customModuleId);

    @Query("SELECT * from Questions WHERE questionCategoryChapter = :questionCategoryChapter AND chapterId =:mChapterId")
    LiveData<List<QuestionBean>> getQBankChapterQuestions(String questionCategoryChapter, int mChapterId);

    @Query("SELECT * from RandomQuestion")
    LiveData<List<RandomQuestionBean>> getRandomQuestion();

    /*** update */

    @Query("UPDATE Chapters SET is_downloaded=:value WHERE id=:chapterId")
    void setChapterDownloaded(String value, int chapterId);

    @Query("UPDATE Videos SET downloadStatus=:status WHERE id=:videoId")
    void updateVideoDownloadStatus(String status, int videoId);

    @Query("UPDATE Tests SET is_downloaded=:status WHERE id=:testId")
    void updateTestDownloadStatus(String status, int testId);

    @Query("UPDATE Chapters SET is_downloaded=:status WHERE id=:chapterId")
    void updateChapterDownloadStatus(String status, int chapterId);

    @Query("UPDATE Videos SET status=:status,seconds=:seconds WHERE id=:videoId")
    void updateSecondAndStatusOfVideo(String status, int seconds, int videoId);

    @TypeConverters(value = AllTypeConverters.class)
    @Query("UPDATE Tests SET savedData=:solveMCQBean WHERE id=:testId")
    void updateSolvedMCQInLocalTest(List<SolveMCQBean> solveMCQBean, int testId);

    @TypeConverters(value = AllTypeConverters.class)
    @Query("UPDATE Chapters SET savedData=:solveMCQBean WHERE id=:chapterId")
    void updateSolvedMCQInLocalChapter(List<SolveMCQBean> solveMCQBean, int chapterId);

    /*** delete */
    @Query("DELETE FROM Subjects")
    void deleteAll();

    @Query("DELETE FROM CustomModule")
    void deleteCustomModule();

    @Query("DELETE FROM Videos WHERE id=:id AND type=:type")
    void deleteDownloadVideo(int id, String type);

}
