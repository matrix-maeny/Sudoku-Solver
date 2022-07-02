package com.matrix_maeny.sudoku.models;

public class SolverGroupModel {
    String solverTypeName;

    public SolverGroupModel(String solverTypeName) {
        this.solverTypeName = solverTypeName;
    }

    public String getSolverTypeName() {
        return solverTypeName;
    }

    public void setSolverTypeName(String solverTypeName) {
        this.solverTypeName = solverTypeName;
    }
}
