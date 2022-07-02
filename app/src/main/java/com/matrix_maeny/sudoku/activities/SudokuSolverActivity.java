package com.matrix_maeny.sudoku.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.matrix_maeny.sudoku.MainActivity;
import com.matrix_maeny.sudoku.R;
import com.matrix_maeny.sudoku.databases.SoundDataBase;
import com.matrix_maeny.sudoku.javaclasses.MatrixChecker;
import com.matrix_maeny.sudoku.javaclasses.Solver;
import com.matrix_maeny.sudoku.javaclasses.SudokuBoard;

import java.util.Objects;

public class SudokuSolverActivity extends AppCompatActivity {

    private SudokuBoard gameBoard;
    private Solver solver;
    private MatrixChecker checker;
    private AppCompatButton solveBtn;

    private boolean pressed = false;
    private Thread thread = null;
    private final Handler handler = new Handler();
    private MediaPlayer player = null;

    boolean soundsEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_solver);

        Objects.requireNonNull(getSupportActionBar()).hide(); // hiding the toolbar..
        gameBoard = findViewById(R.id.sudokuBoard);
        solveBtn = findViewById(R.id.solve_clear_Btn);

        solver = gameBoard.getSolver();
        checker = new MatrixChecker(SudokuSolverActivity.this);

        setTheSounds();
    }


    MediaPlayer.OnCompletionListener playerListener = MediaPlayer::release;

    private void setTheSounds() {
        SoundDataBase soundDataBase = new SoundDataBase(SudokuSolverActivity.this);
        Cursor cursor = soundDataBase.getData();

        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            String state = cursor.getString(1);

            if (state.equals("disabled")) {
                soundsEnabled = false;
            }else{
                soundsEnabled = true;
            }
        }else{
            soundsEnabled = true;
        }
    }

    private void invalidate() {
        gameBoard.invalidate();
    }

    private void tempToast(String m, int time) {
        if (time == 0) {
            Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, m, Toast.LENGTH_LONG).show();

        }

    }

    private void setTheValues(int number) {
        pressed = false;
        if (thread != null && thread.isAlive()) {
            tempToast("Please wait while solving", 0);
            return;
        }

        if (solver.getSelectedRow() == -1) {
            tempToast("Please select a certain box", 1);
            return;
        }
        if (soundsEnabled) {
            playButtonSound();
        }

        checker.setNumberPos(number, solver.getSelectedRow(), solver.getSelectedColumn());

        if (checker.check(solver.getSelectedRow(), solver.getSelectedColumn())) {
            solver.setNumberPos(number);
            invalidate();
        } else {
            solver.setNumberPos(0);

            checker.setNumberPos(0, solver.getSelectedRow(), solver.getSelectedColumn());

        }
    }

    public void Btn1(View view) {
        setTheValues(1);
    }


    public void Btn2(View view) {
        setTheValues(2);

    }

    public void Btn3(View view) {
        setTheValues(3);

    }

    public void Btn4(View view) {
        setTheValues(4);

    }

    public void Btn5(View view) {
        setTheValues(5);

    }

    public void Btn6(View view) {
        setTheValues(6);

    }

    public void Btn7(View view) {
        setTheValues(7);

    }

    public void Btn8(View view) {
        setTheValues(8);

    }

    public void Btn9(View view) {
        setTheValues(9);

    }

    public void BtnX(View view) {
        pressed = false;

        if (thread != null && thread.isAlive()) {
            tempToast("Please wait while solving", 0);
            return;
        }

        if (solveBtn.getText().toString().equals(getString(R.string.clear)))
            BtnSolve(null);
        else setTheValues(0);

        if (soundsEnabled) {
            playXSound();
        }

    }

    public void BtnSolve(View view) {
        if (thread != null && thread.isAlive()) {
            tempToast("Please wait while solving", 0);
            return;
        }

        String txt = solveBtn.getText().toString();
        if (txt.equals(getString(R.string.solve))) {
            solveBtn.setText(R.string.clear);
            solver.setEmptyBoxIndexes();
            tempToast("Solving...", 0);
            SolverThread solverThread = new SolverThread();
            thread = new Thread(solverThread);

            if (soundsEnabled) {
                playCalculationSound();
            }
            thread.start();

        } else {
            if (soundsEnabled) {
                playXSound();
            }
            solveBtn.setText(R.string.solve);
            solver.resetBoard();
            checker.resetBoard();
        }
        invalidate();
    }


    class SolverThread implements Runnable {
        public void run() {
            if (solver.solve2()) {
                handler.post(() -> tempToast("Completed", 1));
            } else {
                handler.post(() -> tempToast("It has no solution", 1));
            }
            stopSound();
        }
    }


    private void playCalculationSound() {
        stopSound();

        player = MediaPlayer.create(SudokuSolverActivity.this, R.raw.calculation_sound2);
        player.setOnCompletionListener(playerListener);
        player.setLooping(true);
        player.start();
    }

    private void playXSound() {
        stopSound();

        player = MediaPlayer.create(SudokuSolverActivity.this, R.raw.click_sound3);
        player.setOnCompletionListener(playerListener);
        player.setLooping(false);
        player.start();
    }

    private void playButtonSound() {
        stopSound();

        player = MediaPlayer.create(SudokuSolverActivity.this, R.raw.click_sound2);
        player.setOnCompletionListener(playerListener);
        player.setLooping(false);
        player.start();
    }

    private void stopSound() {
        if (player != null) {
            try {
                player.stop();
            } catch (Exception ignored) {
            } finally {
                player.release();
            }

        }
    }

    @Override
    public void onBackPressed() {

        if (pressed) {
            super.onBackPressed();
            stopSound();
        } else {
            tempToast("press again to exit", 0);
            if (thread != null && !thread.isAlive())
                try {
                    stopSound();
                } catch (Exception ignored) {

                }
            pressed = true;
        }

    }
}