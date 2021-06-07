package com.example.Site_RegistrationApp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    /**
     * @author		Dvir Dadon <dd2640@bs.amalnet.k12.il
     * @version	4.2.1
     * This class help you to pick a date from the calendar and check the date
     */

    /**
     * build your own custom Dialog container.
     * <p>
     * @param savedInstanceState Called when the fragment's activity has been created and this fragment's view hierarchy instantiated
     * @return own custom Dialog container.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity() , (DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);
    }

    /**
     * contains the updated day, month and year
     * <p>
     * @param view the dialog.
     * @param year the year the user choose.
     * @param month the month the user choose.
     * @param dayOfMonth the day of the month that the user choose.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
