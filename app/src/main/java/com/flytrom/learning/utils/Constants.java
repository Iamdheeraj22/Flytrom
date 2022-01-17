package com.flytrom.learning.utils;

import android.os.Environment;

import com.flytrom.learning.R;

public class Constants {

    public static final String BASE_URL = "https://learningapp.flytrom.com/api/";
    public static final String BASE_URL_MSG91 = "https://api.msg91.com/api/v5/";
    public static final String USERNAME = "flytrom";
    public static final String PASSWORD = "flytrom123!@#";
    public static final String WHATS_APP_URL = "https://api.whatsapp.com/send?phone=919941737737";
    public static final String MEDIA_URL = "https://learningapp.flytrom.com/media/";
    public static final String PRIVACY_POLICY_URL = "http://3.17.254.50/testfly/terms.pdf";
    public static final String APP_DB_NAME = "Flytrom";
    public static final String FREE = "Free";
    public static final String PAID = "Paid";
    public static final String CASH_FREE_TRANSACTION_STAGE = "TEST";//TEST or PROD
    public static final int FREE_VIDEOS_MENU_ID = 501;
    public static final int SAVED_VIDEOS_MENU_ID = 502;
    public static final int SPLASH_TIME = 700;
    public static final String NOTIFICATION = "NOTIFICATION";
    public static final int FREE_QUESTIONS_MENU_ID = 503;
    public static final String DEVICETYPE = "ANDROID";
    public static final String APP_PASSWORD = "3333";
    public static final String SAVED = "Saved";
    public static final int SUCCESS_CODE = 200;
    public static final int NO_DATA = 202;
    public static final String CASHFREE_APP_ID = "13673ef1aac4b3cdd2554200e37631";
    public static final String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0"};
    public static final String LOCAL_IMAGE_PATH = Environment.getExternalStorageDirectory().toString() + "/ElllementsExample/";
    public static final String KEY_USER_ID = "userid";
    public static final String KEY_SESSION_ID = "sessionid";
    public static final String ENCRYPT_KEY = "c1b232e5058928c7";
    public static final String DEVICE_TYPE = "1";
    public static final String QUOTE_CATEGORY = "love";
    public static boolean SHOW_LOG = true;
    public static final String SEPARATOR = ",";
    public static final int TEST_ITEM = 501;
    public static final int TEST_DETAILS = 502;
    public static final int CHAPTER_ITEM = 601;
    public static final int CHAPTER_DETAILS = 602;
    public static final int VIDEO_ITEM = 701;
    public static final int VIDEO_DETAILS = 702;
    public static final String VIDEO_URL = "video_url";
    public static final String[] SIDE_MENU_TITLES = {"App Settings", "Buy Plan", "Pilot Training", "Other Pilots Apps", "About Us", "Contact Us", "Report Piracy", "FAQ", "T&C"};

    public static final int[] SIDE_MENU_ICONS = {R.drawable.ic_settings, R.drawable.ic_explore, R.drawable.ic_flytorm_notes, R.drawable.ic_other_pilots_apps,
            R.drawable.ic_about_us, R.drawable.ic_contect_us, R.drawable.ic_report_piracy, R.drawable.ic_faq, R.drawable.ic_t_c};
    public static final String[] options = {"NK cells", "All nucleated cells", "B cells,dendritic cell", "Platelets"};
    public static final String[] allSubjectsNames = {"Air Regulation", "Air Navigation", "Aviation Meterology", "Radio Aids(ATPL)",
            "Air Regulation", "Air Navigation", "Aviation Meterology", "Radio Aids(ATPL)"};

    public static final int[] allSubjectIcons = {R.drawable.ic_instruments, R.drawable.ic_radio_aids, R.drawable.bg_button_primary_blue, R.drawable.ic_regulations,
            R.drawable.ic_instruments, R.drawable.ic_radio_aids, R.drawable.ic_met, R.drawable.ic_regulations};

    public static final String[] subscribePlansNames = {"ATPM ALL SUBJECTS", "ATPL NAVIGATION"};

    public static final String[] subscribePlansPrice = {"Rs. 12000", "Rs. 5000"};

    public static final String[] QUESTIONS_FROM = {"All QBank MCQs", "Bookmarked QBank MCQs", "Incorrect QBank MCQs",
            "Attempted QBank MCQs", "Un attempted QBank MCQs"};

    public static final String[] MODES = {"Exam :", "Regular :"};

    public static final String[] MODES_TO_SHOW = {"Exam", "Regular"};

    public static final String[] DIFFICULTY_LEVEL = {"All", "Easy", "Medium", "Hard"};

    public static final String[] CONTENT_STATUS = {"All", "Paused", "Completed", "Unattempted"};

    public static final String[] DesignationNames = {"Captain", "First Officer", "Student Pilot" , "Engineer" , "Cabin Crew" , "Other" };

    public static final String[] allModeDescription = {"You can review all answers after the completion of the module.",
            "You can see answer and explanation after solving particular Question."};

    public static final String[] abcValues = {"A. ", "B. ", "C. ", "D. ", "E. ", "F. ", "G. ", "H. ", "I. ", "J. ", "K. ", "L. ", "M. ", "N. "};

    public static String[] TERMS_HEADLINES = {
            "Acceptance of Terms",
            "Eligibility",
            "Services of Flytrom learning",
            "Subscriber Account, Password and Security",
            "Third Parties",
            "Payment Terms",
            "Disclaimer as regards Study materials",
            "Online Subscription and Access Terms",
            "Terms of Use",
            "System Requirements",
            "Modifications to Subscription",
            "Technical Support and Access",
            "Disclaimer of Warranties and Liabilities",
            "Disclaimer of Services",
            "Data protection",
            "Prohibited Conduct",
            "Preservation/Disclosure",
            "Security Components",
            "Proprietary Rights",
            "Flytrom Learning and Third Parties",
            "Trademark, Copyright and Restriction",
            "Terms and Termination",
            "Exclusions and Limitations",
            "Indemnity",
            "Additional Terms",
            "Grievances"};

    public static String[] RESET_HEADLINES = {"Bookmark", "QBank", "Tests"};

    public static String[] RESET_DESCRIPTION = {
            "Reset all your Bookmarks",
            "Clear all your Qbank Progress and start afresh",
            "Clear all your Tests Progress and start afresh", "Tests"};

    public static String[] FAQ_TITLES = {"General FAQs", null, null, null, null, null, null, null, null, null,
            "Video Section", null, null, null, null,
            "Notes Section", null, null,
            "QBank Section", null, null, null, null,
            "Test Section", null, null, null,
            "Help and Support", null, null};

    public static String[] FAQ_HEADLINES = {
            "Advantage Of Flytrom Learning App",
            "Can I use Flytrom Learning App in more than one device ?",
            "I have already joined classroom coaching, should I join Flytrom Learning Pro ?",
            "How can I ask of any Doubts that i have in a chapter, topic or question ?",
            "What is the refund policy ?",
            "What will happen to my pro plan if i change or lose my phone ?",
            "How to pay and is price inclusive of Taxes ?",
            "Can i Change my plan after purchasing ?",
            "I have purchased pro plan but its not showing in the app ?",
            "What is the duration of my subscription (Pro) plan ?",
            "Can I see videos multiple times ?",
            "Can I watch Videos offline ?",
            "Can I watch Videos on Laptop or PC ?",
            "Subtitles of video lecture available ?",
            "My video is not loading or loading very slow ?",
            "Will notes cover all the subjects and chapters ?",
            "Is it going to be hard copy or soft copy of notes ?",
            "Notes text is too small, can i zoom in ?",
            "How to reset MCQs in the app ?",
            "How to reset Bookmarks in the app ?",
            "How to report error in an MCQ ?",
            "Can I solve Question bank multiple times ?",
            "How to use Custom Test ?",
            "Can I retake a test ?",
            "How to reset Tests in the app ?",
            "How do I assess my preparation level with test scores?",
            "Is Test Series available via Postal Course ?",
            "How to reach Us ?",
            "Do we have any whatsapp or facebook groups ?",
            "Can i reach teaching faculty Directly ?"};
}


/*"How to reset Bookmark in the app ?",
            "How to report error in an MCQ ?",*/