package com.matrix_maeny.sudoku.javaclasses;

import java.util.ArrayList;

public class Solver {

    int[][] gameBoard;          // this consists of the total sudoku solved data
    ArrayList<ArrayList<Object>> emptyBox;      // contains empty box indexes after solving

    int selectedRow;            // holds the selected row and columns
    int selectedColumn;
    int selectedMatrixRow;
    int selectedMatrixColumn;

    public Solver() {
        selectedColumn = -1;        // initially setting them to -1
        selectedRow = -1;
        selectedMatrixRow = -1; // this is for whole matrix
        selectedMatrixColumn = -1; // this too

        gameBoard = new int[9][9];  // allocating array of 9/9

        for (int r = 0; r < 9; r++) {           // setting them to zero.. off-course it allocates but for safety..
            for (int c = 0; c < 9; c++) {
                gameBoard[r][c] = 0;
            }
        }

        emptyBox = new ArrayList<>(); // creating empty list
    }

    public int getSelectedMatrixRow() {
        return selectedMatrixRow;
    }

    public void setSelectedMatrixRow(int selectedMatrixRow) {
        this.selectedMatrixRow = selectedMatrixRow;
    }

    public int getSelectedMatrixColumn() {
        return selectedMatrixColumn;
    }

    public void setSelectedMatrixColumn(int selectedMatrixColumn) {
        this.selectedMatrixColumn = selectedMatrixColumn;
    }

    public int getSelectedRow() { // for setting the row
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) { // this is for setting the row
        this.selectedRow = selectedRow;
    }

    public int getSelectedColumn() { // for getting the column
        return selectedColumn;
    }

    public void setSelectedColumn(int selectedColumn) { // for setting teh column
        this.selectedColumn = selectedColumn;
    }

    // method for setting empty box indexes
    public void setEmptyBoxIndexes() {

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (gameBoard[r][c] == 0) {
                    emptyBox.add(new ArrayList<>());
                    emptyBox.get(emptyBox.size() - 1).add(r); // adding that particular row and column
                    emptyBox.get(emptyBox.size() - 1).add(c);
                }
            }
        }
    }

    // for setting the number positions when you click the buttons
    public void setNumberPos(int num) {
        if (selectedRow != -1 && selectedColumn != -1) {
            if (gameBoard[selectedRow - 1][selectedColumn - 1] == num) { // when you double click the button, the button gonna empty the box
                gameBoard[selectedRow - 1][selectedColumn - 1] = 0;
            } else {
                gameBoard[selectedRow - 1][selectedColumn - 1] = num; // or else if it is the single click... then set the number
            }
        }
    }


    public int[][] getGameBoard() { // for getting the gameBoard. consists of solved data
        return gameBoard;
    }

    public ArrayList<ArrayList<Object>> getEmptyBoxIndexes() { // for getting emptyBox indexes
        return emptyBox;
    }


    // solving game

    // this method is for checking whether the row and column contains the same numbers or not
    private boolean check(int row, int col) {

        if (this.gameBoard[row][col] > 0) {

            for (int i = 0; i < 9; i++) {
                if (this.gameBoard[i][col] == this.gameBoard[row][col] && row != i) {
                    return false;
                }

                if (this.gameBoard[row][i] == this.gameBoard[row][col] && col != i) {
                    return false;
                }
            }

            int boxRow = row / 3;
            int boxCol = col / 3;

            for (int r = boxRow * 3; r < (boxRow * 3) + 3; r++) {
                for (int c = boxCol * 3; c < (boxCol * 3) + 3; c++) {

                    if (this.gameBoard[r][c] == this.gameBoard[row][col] && row != r && col != c) {
                        return false;
                    }
                }
            }
        }
        return true;
    }



    public boolean solve2() {
        int row = -1, col = -1;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (this.gameBoard[r][c] == 0) {
                    row = r;
                    col = c;
                    break;
                }
            }
        }

        if (row == -1) return true;

        for (int i = 1; i < 10; i++) {
            this.gameBoard[row][col] = i;


            if (check(row, col)) {
                if (solve2()) {
                    return true;
                }
            }
            this.gameBoard[row][col] = 0;

        }

        return false;
    }


    // for re-setting the entire game

    public void resetBoard() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                gameBoard[r][c] = 0;
            }
        }

        emptyBox = new ArrayList<>();
    }


}
