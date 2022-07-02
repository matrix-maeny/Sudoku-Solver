package com.matrix_maeny.sudoku.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.matrix_maeny.sudoku.R;
import com.matrix_maeny.sudoku.databases.SoundDataBase;
import com.matrix_maeny.sudoku.javaclasses.MatrixChecker2;
import com.matrix_maeny.sudoku.javaclasses.Solver2;
import com.matrix_maeny.sudoku.javaclasses.SudokuBoard2;

import java.util.Objects;

public class SudokuSolver2Activity extends AppCompatActivity {

    private SudokuBoard2 gameBoard;
    private Solver2 solver;
    private MatrixChecker2 checker;
    private AppCompatButton solveBtn;

    private String message;
    private boolean pressed = false;
    private Thread thread = null;
    private final Handler handler = new Handler();
    private MediaPlayer player = null;

    boolean soundsEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_solver2);

        Objects.requireNonNull(getSupportActionBar()).hide();

        gameBoard = findViewById(R.id.sudokuBoard2);
        solveBtn = findViewById(R.id.solve_clear_Btn2);

        solver = gameBoard.getSolver();
        checker = new MatrixChecker2(SudokuSolver2Activity.this);

        setTheSounds();

    }

    MediaPlayer.OnCompletionListener playerListener = MediaPlayer::release;

    private void setTheSounds() {
        SoundDataBase soundDataBase = new SoundDataBase(SudokuSolver2Activity.this);
        Cursor cursor = soundDataBase.getData();

        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            String state = cursor.getString(1);

            if (state.equals("disabled")) {
                soundsEnabled = false;
            } else {
                soundsEnabled = true;
            }
        } else {
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

    public void Btn12(View view) {
        setTheValues(1);
    }


    public void Btn22(View view) {
        setTheValues(2);

    }

    public void Btn32(View view) {
        setTheValues(3);

    }

    public void Btn42(View view) {
        setTheValues(4);

    }

    public void Btn52(View view) {
        setTheValues(5);

    }

    public void Btn62(View view) {
        setTheValues(6);

    }

    public void Btn72(View view) {
        setTheValues(7);

    }

    public void Btn82(View view) {
        setTheValues(8);

    }

    public void Btn92(View view) {
        setTheValues(9);

    }

    public void Btn102(View view) {
        setTheValues(10);

    }

    public void Btn112(View view) {
        setTheValues(11);

    }

    public void Btn122(View view) {
        setTheValues(12);

    }

    public void BtnX2(View view) {
        pressed = false;
        if (thread != null && thread.isAlive()) {
            tempToast("Please wait while solving", 0);
            return;
        }

        if (solveBtn.getText().toString().equals(getString(R.string.clear)))
            BtnSolve2(null);
        else setTheValues(0);
        if (soundsEnabled) {
            playXSound();
        }

    }

    public void BtnSolve2(View view) {
        pressed = false;

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
                message = "Completed";
            } else {
                message = "It has no solution";
            }
            handler.post(() -> tempToast(message, 1));
            stopSound();
        }
    }

    private void playCalculationSound() {
        stopSound();

        player = MediaPlayer.create(SudokuSolver2Activity.this, R.raw.calculation_sound2);
        player.setOnCompletionListener(playerListener);
        player.setLooping(true);
        player.start();
    }

    private void playXSound() {
        stopSound();

        player = MediaPlayer.create(SudokuSolver2Activity.this, R.raw.click_sound3);
        player.setOnCompletionListener(playerListener);
        player.setLooping(false);
        player.start();
    }

    private void playButtonSound() {
        stopSound();

        player = MediaPlayer.create(SudokuSolver2Activity.this, R.raw.click_sound2);
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