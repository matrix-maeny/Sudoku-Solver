package com.matrix_maeny.sudoku.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.matrix_maeny.sudoku.R;
import com.matrix_maeny.sudoku.databases.SoundDataBase;

import java.util.Currency;

public class SoundDialog extends AppCompatDialogFragment {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch soundSwitch;

    private SoundDataBase soundDataBase = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ContextThemeWrapper wrapper = new ContextThemeWrapper(getContext(), androidx.appcompat.R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.settings_dialog, null);
        builder.setView(view);

        soundSwitch = view.findViewById(R.id.soundsSwitch);
        soundDataBase = new SoundDataBase(getContext());
        Cursor cursor = soundDataBase.getData();
        String state = "enabled";
        try {
            cursor.moveToNext();
            state = cursor.getString(1);
        }catch (Exception ignored){}

        if (state.equals("enabled")) soundSwitch.setChecked(true);
        else soundSwitch.setChecked(false);

        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (soundDataBase.updateData("MatrixSound", "enabled")) {
                    Toast.makeText(wrapper, "Sounds enabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(wrapper, "Error enabling sounds", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (soundDataBase.updateData("MatrixSound", "disabled")) {
                    Toast.makeText(wrapper, "Sounds disabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(wrapper, "Error disabling sounds", Toast.LENGTH_SHORT).show();
                }
            }
            soundDataBase.close();
        });
        return builder.create();
    }


}
