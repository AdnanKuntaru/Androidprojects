package com.example.android.notekeeper;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by USER on 12/14/2018.
 */
public class NoteBackupService extends IntentService {

    public static final String EXTRA_COURSE_ID = "com.example.android.notekeeper.extra.COURSE_ID";

    public NoteBackupService() {
        super("NoteBackupService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String backupCourseId = intent.getStringExtra(EXTRA_COURSE_ID);
            NoteBackup.doBackup(this, backupCourseId);
        }
    }

}

















