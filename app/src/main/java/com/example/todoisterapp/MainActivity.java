package com.example.todoisterapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoisterapp.adapter.RecyclerViewAdapter;
import com.example.todoisterapp.adapter.onTodoClickListener;
import com.example.todoisterapp.databinding.ActivityMainBinding;
import com.example.todoisterapp.model.Task;
import com.example.todoisterapp.model.TaskViewModel;
import com.example.todoisterapp.model.sharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
// Todo: Can add one more item in menu for explaining working of app.
public class MainActivity extends AppCompatActivity implements onTodoClickListener {

//    private static final String TAG = "item";
    public RecyclerView recyclerView;
    private TaskViewModel taskViewModel;
    private TextView textViewNull;
    private ActivityMainBinding binding;
    private RecyclerViewAdapter recyclerViewAdapter;
    private BottomSheetFragment bottomSheetFragment;
    private sharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textViewNull = findViewById(R.id.textViewNull);

        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);

        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior =
                BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        sharedViewModel = new ViewModelProvider(this)
                .get(sharedViewModel.class);

        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication())
                .create(TaskViewModel.class);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            if(tasks.size() <= 0)
            {
                textViewNull.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            else
            {
                textViewNull.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewAdapter = new RecyclerViewAdapter(tasks, this);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
        });

        binding.fab.setOnClickListener(view -> showBottomSheet());
    }

    private void showBottomSheet() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            MainActivity.this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoClick(Task task) {
        sharedViewModel.selectItem(task);
        sharedViewModel.setEdit(true);
        showBottomSheet();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onTodoRadioButtonClick(Task task) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Surely you want to delete this Task?");
        dialog.setPositiveButton("YES", (dialog12, which) -> {
            TaskViewModel.deleteTask(task);
            recyclerViewAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Deleted!!!", Toast.LENGTH_SHORT).show();
        });
        dialog.setNegativeButton("NO", (dialog1, which) -> {
        });
        dialog.show();
    }
}