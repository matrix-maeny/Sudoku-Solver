package com.matrix_maeny.sudoku.javaclasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.matrix_maeny.sudoku.R;
import com.matrix_maeny.sudoku.activities.SudokuSolverActivity;
import com.matrix_maeny.sudoku.databases.SoundDataBase;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Objects;

public class SudokuBoard extends View {

    private final int bordersColor;
    private final int cellFillColor;
    private final int cellHighlightedColor;
    private final int letterColor;
    private final int letterSolvedColor;

    private final Solver solver = new Solver();

    private final Paint bordersColorPaint = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellHighlightedColorPaint = new Paint();
    private final Paint letterColorPaint = new Paint();
    private final Rect letterColorBounds = new Rect();

    private int cellSize;
    private int matrixSize;

    private MediaPlayer player = null;
    boolean soundsEnabled = true;

    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTheSounds();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SudokuBoard, 0, 0); // getting the array set

        try {
            bordersColor = a.getInteger(R.styleable.SudokuBoard_bordersColor, 0); // assigning the color value seated from view
            cellFillColor = a.getInteger(R.styleable.SudokuBoard_cellFillColor, 0);
            cellHighlightedColor = a.getInteger(R.styleable.SudokuBoard_cellHighlightedColor, 0);
            letterColor = a.getInteger(R.styleable.SudokuBoard_letterColor, 0);
            letterSolvedColor = a.getInteger(R.styleable.SudokuBoard_letterSolvedColor, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());
        cellSize = dimension / 9;
        matrixSize = dimension / 3;

        setMeasuredDimension(dimension, dimension);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        bordersColorPaint.setStyle(Paint.Style.STROKE);
        bordersColorPaint.setStrokeWidth(8);
        bordersColorPaint.setColor(bordersColor);
        bordersColorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL);
        cellFillColorPaint.setAntiAlias(true);
        cellFillColorPaint.setColor(cellFillColor);

        cellHighlightedColorPaint.setStyle(Paint.Style.FILL);
        cellHighlightedColorPaint.setAntiAlias(true);
        cellHighlightedColorPaint.setColor(cellHighlightedColor);

        letterColorPaint.setStyle(Paint.Style.FILL);
        letterColorPaint.setAntiAlias(true);
        letterColorPaint.setColor(letterColor);

        colorCell(canvas, solver.getSelectedRow(), solver.getSelectedColumn(), solver.getSelectedMatrixRow(), solver.getSelectedMatrixColumn());
        canvas.drawRect(0, 0, getWidth(), getWidth(), bordersColorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);

    }

    @Override
    public boolean onFilterTouchEventForSecurity(@NonNull MotionEvent event) {
        boolean isValid = false;

        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            if (soundsEnabled) {
                playButtonSound();
            }
            solver.setSelectedRow((int) Math.ceil(y / cellSize));
            solver.setSelectedColumn((int) Math.ceil(x / cellSize));
            solver.setSelectedMatrixRow((int) Math.ceil(y / matrixSize));
            solver.setSelectedMatrixColumn((int) Math.ceil(x / matrixSize));
            isValid = true;
        }

        return isValid;
    }

    private void drawNumbers(Canvas canvas) {
        letterColorPaint.setTextSize((float) (cellSize / 1.5));

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (solver.gameBoard[r][c] != 0) {
                    String txt = Integer.toString(solver.gameBoard[r][c]);

                    drawLetter(canvas, txt, r, c);

                }
            }
        }// end of for loop

        letterColorPaint.setColor(letterSolvedColor); // for filling empty boxes

        for (ArrayList<Object> letter : solver.getEmptyBoxIndexes()) {
            int r = (int) letter.get(0);
            int c = (int) letter.get(1);

            String txt = Integer.toString(solver.gameBoard[r][c]);

            drawLetter(canvas, txt, r, c);
        }
    }

    private void drawLetter(@NonNull Canvas canvas, String txt, int r, int c) {
        float height, width;

        letterColorPaint.getTextBounds(txt, 0, txt.length(), letterColorBounds);
        height = letterColorBounds.height();
        width = letterColorPaint.measureText(txt);

        canvas.drawText(txt, (c * cellSize) + (cellSize - width) / 2,
                (r * cellSize + cellSize) - (cellSize - height) / 2,
                letterColorPaint);
    }

    private void drawThinLine() {
        bordersColorPaint.setStyle(Paint.Style.STROKE);
        bordersColorPaint.setStrokeWidth(1);
        bordersColorPaint.setColor(bordersColor);

    }

    private void drawThickLine() {
        bordersColorPaint.setStyle(Paint.Style.STROKE);
        bordersColorPaint.setStrokeWidth(6);
        bordersColorPaint.setColor(bordersColor);
    }

    private void drawBoard(Canvas canvas) {

        // drawing columns
        for (int c = 0; c < 10; c++) {

            if (c % 3 == 0) drawThickLine();
            else drawThinLine();

            canvas.drawLine(cellSize * c, 0, cellSize * c, getWidth(), bordersColorPaint);
        }

        for (int r = 0; r < 10; r++) {
            if (r % 3 == 0) {
                drawThickLine();
            } else drawThinLine();

            canvas.drawLine(0, cellSize * r, getWidth(), cellSize * r, bordersColorPaint);
        }
    }

    private void colorCell(Canvas canvas, int row, int column, int matrixRow, int matrixColumn) {
        if (solver.getSelectedRow() != -1 && solver.getSelectedColumn() != -1) {

            canvas.drawRect((column - 1) * cellSize, 0, column * cellSize, cellSize * 9, cellHighlightedColorPaint);
//
            canvas.drawRect(0, (row - 1) * cellSize, cellSize * 9, row * cellSize, cellHighlightedColorPaint);

            canvas.drawRect((matrixColumn - 1) * matrixSize, (matrixRow - 1) * matrixSize, (matrixColumn) * matrixSize, (matrixRow) * matrixSize, cellHighlightedColorPaint);

            float height, width;
            height = (row - 1) * cellSize + (cellSize / 2);
            width = (column - 1) * cellSize + (cellSize / 2);

            float radius = ((float) cellSize / 2 - 5);
            canvas.drawCircle(width, height, radius, cellFillColorPaint);


        }
        invalidate();
    }

    public Solver getSolver() {
        return this.solver;
    }

    private void setTheSounds() {
        SoundDataBase soundDataBase = new SoundDataBase(getContext().getApplicationContext());
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

    private void playButtonSound() {
        stopSound();

        player = MediaPlayer.create(this.getContext().getApplicationContext(), R.raw.select_click2);

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


}
