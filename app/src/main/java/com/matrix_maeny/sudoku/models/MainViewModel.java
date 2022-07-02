package com.matrix_maeny.sudoku.models;

public class MainViewModel {
    private final int modelIcon;
    private final String modelName;

    public MainViewModel(int modelIcon, String modelName) {
        this.modelIcon = modelIcon;
        this.modelName = modelName;
    }


    public int getModelIcon() {
        return modelIcon;
    }

    public String getModelName() {
        return modelName;
    }
}
