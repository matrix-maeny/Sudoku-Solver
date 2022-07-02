package com.matrix_maeny.sudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.matrix_maeny.sudoku.activities.AboutActivity;
import com.matrix_maeny.sudoku.adapters.SolverGroupAdapter;
import com.matrix_maeny.sudoku.databases.SoundDataBase;
import com.matrix_maeny.sudoku.dialogs.SoundDialog;
import com.matrix_maeny.sudoku.models.SolverGroupModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<SolverGroupModel> list;
    SolverGroupAdapter adapter = null;
    RecyclerView recyclerView;
    SoundDataBase soundDataBase = null;

    SoundDialog dialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();
        adapter = new SolverGroupAdapter(list,MainActivity.this);

        list.add(new SolverGroupModel(getString(R.string._9x9)));
        list.add(new SolverGroupModel(getString(R.string._12x12)));
        list.add(new SolverGroupModel(getString(R.string._15x15)));
        GridLayoutManager linearLayoutManager = new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

       setTheSound();


    }

    private void setTheSound(){
        soundDataBase = new SoundDataBase(MainActivity.this);
        Cursor cursor = soundDataBase.getData();
        if(cursor.getCount() == 0){
            if(!soundDataBase.insertData("MatrixSound","enabled")){
                Toast.makeText(this, "Error creating sound database: contact Matrix", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.settings:
                showSoundDialog();
                break;
            case R.id.about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
                // go to about activity
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSoundDialog(){
        dialog = new SoundDialog();
        dialog.show(getSupportFragmentManager(),"Sound dialog");
    }
}