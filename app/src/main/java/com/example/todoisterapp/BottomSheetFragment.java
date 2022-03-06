package com.example.todoisterapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoisterapp.model.Priority;
import com.example.todoisterapp.model.Task;
import com.example.todoisterapp.model.TaskViewModel;
import com.example.todoisterapp.model.sharedViewModel;
import com.example.todoisterapp.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/*
 Todo: To check priorityRadioButton in update automatically acc. to data saved
*/
public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    //    private static final String TAG = "Item";
    Calendar date = Calendar.getInstance();   // to manage chips for date
    Calendar calendar = Calendar.getInstance(); // to create date
    private EditText enterTodo;
    private ImageButton calenderButton;
    private ImageButton priorityButton;
    private ImageButton saveButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private CalendarView calendarView;
    private Group calenderGroup;
    private Date dueDate;
    private sharedViewModel sharedViewModel;
    private boolean isEdit;
    private Priority priority;
    private Chip todayChip;
    private Chip nextWeekChip;
    private Chip tomorrowChip;
    private int chipNo;

    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        calendarView = view.findViewById(R.id.calendar_view);
        calenderGroup = view.findViewById(R.id.calendar_group);
        calenderButton = view.findViewById(R.id.today_calendar_button);
        enterTodo = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);

        date.add(Calendar.DAY_OF_YEAR, 0);

        todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);

        return view;
    }

    // Because when we go out of fragment then on resume it will refresh the views
    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getSelectItem().getValue() != null) {
            isEdit = sharedViewModel.isEdit();
            Task task = sharedViewModel.getSelectItem().getValue();
            if (isEdit) {
                if (task.chipNo == 0) {
                    todayChip.setChipStrokeColorResource(R.color.chipStrokeColor);
                } else if (task.chipNo == 1) {
                    tomorrowChip.setChipStrokeColorResource(R.color.chipStrokeColor);
                } else if (task.chipNo == 2) {
                    nextWeekChip.setChipStrokeColorResource(R.color.chipStrokeColor);
                }
                enterTodo.setText(task.getTask());
                priority = task.getPriority();
                dueDate = task.getDueDate();
            } else {
                enterTodo.setText("");
                priority = null;
                dueDate = null;
            }
        }
    }

//    private void editPrioritySetter(View view) {
//        if (priorityRadioGroup.getVisibility() == View.VISIBLE) {
//            RadioButton selectedRadioButton;
//            if (priority == Priority.HIGH) {
//                selectedRadioButton = view.findViewById(R.id.radioButton_high);
////                selectedRadioButton.setChecked(true);
////                Log.d(TAG, "editPrioritySetter: "+priority);
//            } else if (priority == Priority.MEDIUM) {
////                selectedRadioButton = view.findViewById(R.id.radioButton_med);
////                selectedRadioButton.setChecked(true);
////                Log.d(TAG, "editPrioritySetter: "+priority);
//            } else if (priority == Priority.LOW) {
////                selectedRadioButton = view.findViewById(R.id.radioButton_low);
////                selectedRadioButton.setChecked(true);
////                Log.d(TAG, "editPrioritySetter: "+priority);
//            }
//        }
//    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This will have same values as in MainActivity's sharedViewModel object.
        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(sharedViewModel.class);


        calenderButton.setOnClickListener(v -> {
            calenderGroup.setVisibility(
                    calenderGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

            Utils.hideSoftKeyboard(v);
        });

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();
            chipNo = 4;
        });

        priorityButton.setOnClickListener(v -> {
            priorityRadioGroup.setVisibility(priorityRadioGroup.getVisibility() ==
                    View.GONE ? View.VISIBLE : View.GONE);
//            editPrioritySetter(v);
            Utils.hideSoftKeyboard(v);
            priorityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (priorityRadioGroup.getVisibility() == View.VISIBLE) {
                    selectedButtonId = checkedId;
                    selectedRadioButton = view.findViewById(selectedButtonId);
                    if (selectedRadioButton.getId() == R.id.radioButton_high) {
                        priority = Priority.HIGH;
                    } else if (selectedRadioButton.getId() == R.id.radioButton_med) {
                        priority = Priority.MEDIUM;
                    } else if (selectedRadioButton.getId() == R.id.radioButton_low) {
                        priority = Priority.LOW;
                    } else {
                        priority = Priority.LOW;
                    }
                } else {
                    priority = Priority.LOW;
                }
            });
        });

        saveButton.setOnClickListener(v -> {
            String task = enterTodo.getText().toString().trim();
            if (!TextUtils.isEmpty(task) && dueDate != null && priority != null) {
                Task myTask = new Task(task, priority, dueDate,
                        Calendar.getInstance().getTime(),
                        false, chipNo);
                if (isEdit) {
                    Task updateTask = sharedViewModel.getSelectItem().getValue();
                    assert updateTask != null;
                    updateTask.setTask(task);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(priority);
                    updateTask.setDueDate(dueDate);
                    updateTask.setChipNo(chipNo);
                    TaskViewModel.updateTask(updateTask);
                    chipNo = 4;
                    Toast.makeText(getContext(), "Task updated...", Toast.LENGTH_SHORT).show();
                    sharedViewModel.setEdit(false);
                } else {
                    TaskViewModel.insertTask(myTask);
                    chipNo = 4;
                    Toast.makeText(getContext(), "Task created...", Toast.LENGTH_SHORT).show();
                }
                enterTodo.setText("");
                if (this.isVisible()) {
                    this.dismiss();
                }

            } else {
                // Todo: If possible add animation here to shake whole bottomSheetFragment.
                Snackbar.make(
                        Objects.requireNonNull(getDialog()).getWindow().getDecorView(),
                        R.string.snackbarBottomSheet,
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.today_chip) {
            // set date for today
            calendar.clear();
            nextWeekChip.setChipStrokeColorResource(R.color.chipColor);
            tomorrowChip.setChipStrokeColorResource(R.color.chipColor);
            todayChip.setChipStrokeColorResource(R.color.chipStrokeColor);
            chipNo = 0;

            calendar.set(date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH));
            dueDate = calendar.getTime();
        } else if (id == R.id.tomorrow_chip) {
            // 1 day from today
            calendar.clear();
            todayChip.setChipStrokeColorResource(R.color.chipColor);
            nextWeekChip.setChipStrokeColorResource(R.color.chipColor);
            tomorrowChip.setChipStrokeColorResource(R.color.chipStrokeColor);
            chipNo = 1;

            calendar.set(date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH) + 1);
            dueDate = calendar.getTime();
        } else if (id == R.id.next_week_chip) {
            // 7 days from today

            // can also use this line instead of below some code line
            // but this has some flaws that if user click same button many times.
//            calendar.add(date.get(Calendar.DAY_OF_YEAR),7);

            calendar.clear();
            todayChip.setChipStrokeColorResource(R.color.chipColor);
            tomorrowChip.setChipStrokeColorResource(R.color.chipColor);
            nextWeekChip.setChipStrokeColorResource(R.color.chipStrokeColor);
            chipNo = 2;

            calendar.set(date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH) + 7);
            dueDate = calendar.getTime();
        }

    }
}