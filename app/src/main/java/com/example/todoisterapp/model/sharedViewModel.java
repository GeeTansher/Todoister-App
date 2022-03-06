package com.example.todoisterapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class sharedViewModel extends ViewModel {
    // MutableLiveData is used to easily change when moving around throughout the process
    // It is LiveData which publicly exposes setValue() and getValue() methods
    private final MutableLiveData<Task> selectedItem = new MutableLiveData<>();

    private boolean isEdit;

    public void selectItem(Task task) {
        selectedItem.setValue((task));
    }

    public LiveData<Task> getSelectItem() {
        return selectedItem;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
