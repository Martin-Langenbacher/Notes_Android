package com.example.notes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import javax.xml.namespace.QName;


@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    // Methe: singelton
    private static NoteDatabase instance;

    // Methode eigentlich sowas wie getNoteDao()...
    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)  // when instance is first created --> it will excecute on create method and populate our DB
                    .build();
        }
        return instance;
    }


    // how you can populate during the start - so we don't need to start with an empty table...
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){

        // ==> "CTRG + O ... on create... the first time
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
            // the last line will excecute the acing tasks on create --> for the DB we pass our instance variable
        }
    };


    // we need an async task (we pass void for our 3-Typs, because we have nothing in it...
    // 3-Types: Title, Description and Priority...
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        // ... this is possible because on Create is called after the DB was created...
        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            noteDao.insert(new Note("Title 3", "Description 3", 3));
            return null;
        }


    }



}
