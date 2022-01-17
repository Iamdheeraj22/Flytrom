package com.flytrom.learning.room_db.repository

import android.app.Application
import com.flytrom.learning.beans.response_beans.custom_module.CustomModuleBean
import com.flytrom.learning.beans.response_beans.others.SolveMCQBean
import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean
import com.flytrom.learning.beans.response_beans.random_question.RandomQuestionBean
import com.flytrom.learning.beans.response_beans.subject.SubjectBean
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean
import com.flytrom.learning.beans.response_beans.test_bean.TestBean
import com.flytrom.learning.beans.response_beans.videos.VideoBean
import com.flytrom.learning.room_db.db.AppDatabase
import com.flytrom.learning.utils.Constants
import com.flytrom.learning.utils.Coroutines

class FlytromDBRepositoryKotlin(application: Application) {

    private var database: AppDatabase = AppDatabase.getDatabase(application)

    /*** INSERTING DATA */

    fun insertSubjects(list: List<SubjectBean>) {
        Coroutines.io {
            database.daoAccess().insertSubjects(list)
        }
    }

    fun insertVideos(list: List<VideoBean>) {
        Coroutines.io {
            database.daoAccess().insertVideos(list)
        }
    }

    fun insertSingleVideo(videoBean: VideoBean) {
        Coroutines.io {
            database.daoAccess().insertSingleVideo(videoBean)
        }
    }

    fun insertSubjectChapters(list: List<ChapterBean>) {
        Coroutines.io {
            database.daoAccess().insertSubjectChapters(list)
        }
    }

    fun insertTests(list: List<TestBean>) {
        Coroutines.io {
            database.daoAccess().insertTests(list)
        }
    }

    fun insertQuestions(list: List<QuestionBean>) {
        Coroutines.io {
            database.daoAccess().insertQuestions(list)
        }
    }

    fun insertCustomModule(list: List<CustomModuleBean>) {
        Coroutines.io {
            database.daoAccess().insertCustomModule(list)
        }
    }

    fun insertRandomQuestion(randomQuestionBean: RandomQuestionBean) {
        Coroutines.io {
            database.daoAccess().insertRandomQuestion(randomQuestionBean)
        }
    }

    /*** DELETING DATA */

    fun clearRoomDatabase() {
        Coroutines.io {
            database.daoAccess().deleteAll()
        }
    }

    fun deleteLocalCustomModule() {
        Coroutines.io {
            database.daoAccess().deleteCustomModule()
        }
    }

    fun deleteDownloadVideo(id: Int) {
        Coroutines.io {
            database.daoAccess().deleteDownloadVideo(id, Constants.SAVED)
        }
    }

    /*** UPDATING DATA */

    fun setChapterDownloaded(chapterId: Int) {
        Coroutines.io {
            database.daoAccess().setChapterDownloaded("Yes", chapterId)
        }
    }

    fun updateDownloadStatusOfVideo(videoBean: VideoBean) {
        Coroutines.io {
            database.daoAccess().updateVideoDownloadStatus(videoBean.downloadStatus, videoBean.id)
        }
    }

    fun updateDownloadStatusOfTest(testBean: TestBean) {
        Coroutines.io {
            database.daoAccess().updateTestDownloadStatus(testBean.is_downloaded, testBean.id)
        }
    }

    fun updateDownloadStatusOfChapter(chapterBean: ChapterBean) {
        Coroutines.io {
            database.daoAccess().updateChapterDownloadStatus(chapterBean.is_downloaded, chapterBean.id)
        }
    }

    fun updateSecondAndStatusOfVideo(videoBean: VideoBean) {
        Coroutines.io {
            database.daoAccess().updateSecondAndStatusOfVideo(videoBean.status, videoBean.seconds, videoBean.id)
        }
    }

    fun updateSolvedMCQInLocalTest(list: List<SolveMCQBean>, testId: Int) {
        Coroutines.io {
            database.daoAccess().updateSolvedMCQInLocalTest(list, testId)
        }
    }

    fun updateSolvedMCQInLocalChapter(list: List<SolveMCQBean>, testId: Int) {
        Coroutines.io {
            database.daoAccess().updateSolvedMCQInLocalChapter(list, testId)
        }
    }
}