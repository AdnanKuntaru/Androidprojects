package com.example.android.notekeeper;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by USER on 12/9/2018.
 */

public final class NoteKeeperProviderContract {

    private  NoteKeeperProviderContract(){}
    public static final String AUTHORITY = "com.example.android.notekeeper.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);



    protected interface CourseColumn{
        public static final String COLUMN_COURSE_TITLE = "course_title";
        public static final String COLUMN_COURSE_ID = "courses_id";
    }
    protected interface NotesColumns{

        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_COURSE_TEXT = "note_text";

    }

    protected interface CoursesIdColumns{
        public static final String COLUMN_COURSE_ID = "courses_id";
    }

    public static final class Courses implements BaseColumns, CoursesIdColumns, CourseColumn {
        public static final String PATH = "courses";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
    }

    public static final class Notes implements BaseColumns, CoursesIdColumns, NotesColumns{
        public static final String PATH = "note";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
        public static final String PATH_EXPANDED = "notes_expanded";
        public static final Uri CONTENT_EXPANDED_URI = Uri.withAppendedPath(AUTHORITY_URI,PATH_EXPANDED);
        public static final String COLUMN_NOTE_TEXT = "";
    }

}
