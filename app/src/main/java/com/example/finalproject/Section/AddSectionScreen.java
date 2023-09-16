package com.example.finalproject.Section;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddSectionScreen extends AppCompatActivity {

    EditText _name;
    EditText _descript;
    EditText _date;
    ImageView _backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_section_screen);

        fetchUI();
    }

    private void fetchUI() {
        _name = (EditText) findViewById(R.id.AddSectionName);
        _descript = (EditText) findViewById(R.id.AddSectionDescript);
        _date = (EditText) getDate();
        _backArrow = (ImageView) findViewById(R.id.AddSectionBackIcon);
    }

    private EditText getDate() {
        final EditText tmp = (EditText) findViewById(R.id.AddSectionDate);

        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddSectionScreen.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Format the selected date and set it to the EditText
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                tmp.setText(sdf.format(selectedDate.getTime()));
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
        return tmp;
    }
}