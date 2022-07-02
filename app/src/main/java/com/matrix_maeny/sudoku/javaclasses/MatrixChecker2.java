package com.matrix_maeny.sudoku.javaclasses;

import android.content.Context;
import android.widget.Toast;

public class MatrixChecker2 {

    int matrix[][];
    Context context;

    public MatrixChecker2(Context context) {
        this.context = context;
        matrix = new int[12][12];

        for (int r = 0; r < 12; r++) {           // setting them to zero.. off-course it allocates but for safety..
            for (int c = 0; c < 12; c++) {
                matrix[r][c] = 0;
            }
        }

    }

    public boolean check(int row, int col) {
        if (this.matrix[row - 1][col - 1] > 0) {

            for (int i = 0; i < 12; i++) {
                if (this.matrix[i][col - 1] == this.matrix[row - 1][col - 1] && (row - 1) != i) {
                    tempToast("Already exists in same Column", 0);
                    this.matrix[row - 1][col - 1] = 0;
                    return false;
                }

                if (this.matrix[row - 1][i] == this.matrix[row - 1][col - 1] && (col - 1) != i) {
                    tempToast("Already exists in same Row", 0);

                    this.matrix[row - 1][col - 1] = 0;

                    return false;
                }
            }

            int boxRow = (row - 1) / 3;
            int boxCol = (col - 1) / 4;

            for (int r = boxRow * 3; r < (boxRow * 3) + 3; r++) {
                for (int c = boxCol * 4; c < (boxCol * 4) + 4; c++) {

                    if (this.matrix[r][c] == this.matrix[row - 1][col - 1] && (row - 1) != r && (col - 1) != c) {
                        tempToast("Already exists within the Box", 1);

                        this.matrix[row - 1][col - 1] = 0;
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setNumberPos(int num, int selectedRow, int selectedColumn) {
        if (selectedRow != -1 && selectedColumn != -1) {
            if (matrix[selectedRow - 1][selectedColumn - 1] == num) { // when you double click the button, the button gonna empty the box
                matrix[selectedRow - 1][selectedColumn - 1] = 0;
            } else {
                matrix[selectedRow - 1][selectedColumn - 1] = num; // or else if it is the single click... then set the number
            }
        }
    }

    private void tempToast(String m, int time) {
        if (time == 0) {
            Toast.makeText(context, m, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, m, Toast.LENGTH_LONG).show();

        }

    }

    public void resetBoard() {
        for (int r = 0; r < 12; r++) {
            for (int c = 0; c < 12; c++) {
                matrix[r][c] = 0;
            }
        }


    }




}
