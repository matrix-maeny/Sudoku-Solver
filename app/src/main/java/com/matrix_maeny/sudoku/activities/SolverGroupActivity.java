package com.matrix_maeny.sudoku.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.matrix_maeny.sudoku.MainActivity;
import com.matrix_maeny.sudoku.R;
import com.matrix_maeny.sudoku.adapters.SolverGroupAdapter;
import com.matrix_maeny.sudoku.models.SolverGroupModel;

import java.util.ArrayList;
import java.util.Objects;

public class SolverGroupActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SolverGroupAdapter adapter;
    ArrayList<SolverGroupModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver_group);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Sudoku solver");
        recyclerView = findViewById(R.id.solverGroupRecyclerView);

        list = new ArrayList<>();
        list.add(new SolverGroupModel(getString(R.string._9x9)));
        list.add(new SolverGroupModel(getString(R.string._12x12)));
        list.add(new SolverGroupModel(getString(R.string._15x15)));
        adapter = new SolverGroupAdapter(list,SolverGroupActivity.this);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(SolverGroupActivity.this,2);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

    }
}