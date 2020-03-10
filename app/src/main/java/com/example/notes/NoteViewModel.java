package com.example.notes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    // AndroidViewModel is a sub-Class of ViewModel
    // the difference is in the A.ViewModel we get passed the application and the constructor
    // and we can use it when ever the application context is needed

    // You should never store a context of an activity for a view that references an activity
    // in the ViewModel, because the ViewModel is designed to outlet? an activity. After it is destroyed
    // and if we hold a reference when it is already destroyed, we have a memory leak

    // but you have to pass the context to our repository because we need it there to instanciate our
    // db instances... and that is why we extend AndroidViewModel because then we get passed application
    // and can pass it down to the database

    // -->in the ViewModel we create two member variables...
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;


    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    //
    //
    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

}


