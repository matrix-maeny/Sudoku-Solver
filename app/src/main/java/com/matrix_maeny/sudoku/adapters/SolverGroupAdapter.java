package com.matrix_maeny.sudoku.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matrix_maeny.sudoku.R;
import com.matrix_maeny.sudoku.activities.SudokuSolver2Activity;
import com.matrix_maeny.sudoku.activities.SudokuSolver3Activity;
import com.matrix_maeny.sudoku.activities.SudokuSolverActivity;
import com.matrix_maeny.sudoku.models.SolverGroupModel;

import java.util.ArrayList;

public class SolverGroupAdapter extends RecyclerView.Adapter<SolverGroupAdapter.viewHolder> {
    ArrayList<SolverGroupModel> list;
    Context context;

    public SolverGroupAdapter(ArrayList<SolverGroupModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.solver_group_model, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SolverGroupModel model = list.get(position);
        holder.solverTypeName.setText(model.getSolverTypeName());

        holder.solverTypeLayout.setOnClickListener(v -> {
            changeActivity(holder.getAdapterPosition());
        });
    }

    private void changeActivity(int n) {
        switch (n) {
            case 0:
                // 9x9
                context.startActivity(new Intent(context.getApplicationContext(), SudokuSolverActivity.class));
                break;
            case 1:
                //12x12
                context.startActivity(new Intent(context.getApplicationContext(), SudokuSolver2Activity.class));
                break;
            case 2:
                //  15x15
                context.startActivity(new Intent(context.getApplicationContext(), SudokuSolver3Activity.class));
                break;

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView solverTypeName;
        LinearLayout solverTypeLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            solverTypeLayout = itemView.findViewById(R.id.solverTypeLayout);
            solverTypeName = itemView.findViewById(R.id.solverName);
        }
    }
}
