package com.example.finalproject.Section;

import static com.example.finalproject.Constants.KEY_COLLECTION_SECTION;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddSectionScreen extends AppCompatActivity {

    private EditText _name;
    private EditText _descript;
    private EditText _date;
    private EditText _hour;
    private ImageView _backArrow;
    private TextView _addSectionButton;

    private FirebaseFirestoreController<Section> sectionDatabase;

    private User user;

    Section section;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_section_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        fetchUI();
        getData();
        section = new Section();
        sectionDatabase = new FirebaseFirestoreController<>(Section.class);

        _addSectionButton.setOnClickListener(view -> {
            boolean isComplete = CreateNewSection();
            if(isComplete){
                sectionDatabase.addToFirestore(KEY_COLLECTION_SECTION, section.get_SectionName(), section);
                Toast.makeText(AddSectionScreen.this, "Add new section successfully", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(AddSectionScreen.this, SectionScreen.class);
                startActivity(i);
            }
        });

        _backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void getData() {
        SharedPreferenceManager<User> sharedPreferenceManager = new SharedPreferenceManager<>(User.class, this);
        user = sharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);
    }


    private boolean CreateNewSection(){
        String name = _name.getText().toString();
        String descript = _descript.getText().toString();
        String date = _date.getText().toString();
        String hour = _hour.getText().toString();

        if(name.isEmpty() || descript.isEmpty() || date.isEmpty() || hour.isEmpty()){
            Toast.makeText(AddSectionScreen.this, "Please fulfill the require information", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            section.set_SectionName(name);
            section.set_SectionDescript(descript);
            section.set_SectionDate(date);
            section.set_SectionHour(hour);
            section.set_SectionHost(user.get_UserName());
            return true;
        }
    }

    private void fetchUI() {
        _name = (EditText) findViewById(R.id.AddSectionName);
        _descript = (EditText) findViewById(R.id.AddSectionDescript);
        _date = (EditText) getDate();
        _hour = (EditText) getHour();
        _backArrow = (ImageView) findViewById(R.id.AddSectionBackIcon);
        _addSectionButton = (TextView) findViewById(R.id.AddSectionConfirmButton);
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
    private EditText getHour(){
        final EditText tmp = (EditText) findViewById(R.id.AddSectionTime);
        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog _picker;
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                boolean is24HourView = true;

                _picker = new TimePickerDialog(AddSectionScreen.this, android.R.style.Theme_Holo_Light_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                String formattedTime = String.format("%02d:%02d", i, i1);
                                tmp.setText(formattedTime);
                            }
                        }, hourOfDay, minute, is24HourView);

                _picker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                _picker.setTitle("Set a time");
                _picker.show();
            }
        });
        return tmp;
    }
}