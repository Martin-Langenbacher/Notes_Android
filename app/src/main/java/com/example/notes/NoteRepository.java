package com.example.notes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        // Bemerkung: Abstract method, which we created in NoteDatabase.class
        // Normally, we cannot call abstract methods, but since we build our db-instance with the
        // builder (in NoteDatabase), it autogenerates all the necessary code for this method
        allNotes = noteDao.getAllNotes();
    }

    // Room will automatically execute the DB operation that returns the live data on the background
    // thread, so we don't have to take care of that... but for our other database operation (e.g. insert, update, ...)
    // we have to execute the code on the background thread ourself, because Room does not allow DB operation
    // on the main thread - since this could freeze our App.


    // the next 5 methods are the API, which the repository exposes to the outside ! (eg. call insert...)
    public void insert(Note note){
        new InsertNoteAsyncTask(noteDao).execute(note); // where we pass our note, because we want to insert...

    }

    public void update(Note note){
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note){
        new DeleteNoteAsyncTask(noteDao).execute(note);

    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute(); // in this case, we don't have to pass anything
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;

    }


    // Class for AsyncTask Insert
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;
        // we need this noteDao to do DB operations


        // Since the class is static, we cannot access the noteDao of our repository directly,
        // se we have to pass it our a constructor: -->

        // Constructor
        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao; // noteDao which get passed...

        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]); // 0 ==> the first index
            return null;
        }
    }


    // Class for AsyncTask Update
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        // Constructor
        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }


    // Class for AsyncTask Delete One Note
    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        // Constructor
        private DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }


    // Class for AsyncTask Delete All Notes
    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        // Constructor
        private DeleteAllNotesAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }



}
