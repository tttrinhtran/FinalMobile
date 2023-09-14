package com.example.finalproject.Section;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class Section extends AppCompatActivity {

    Button btnSection;
    Button btnMySection;
    TextView sectionKind;
    ArrayList<SectionModel> sectionList;

    ArrayList<SectionModel> mySectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
    }
    void tabSetup()
    {
        sectionKind=findViewById(R.id.section_kind);
        btnSection=findViewById(R.id.Section_Tab);
        btnSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionKind.setText("Section");
            }
        });

        btnMySection=findViewById(R.id.MySection_Tab);
        btnMySection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionKind.setText("My Section");
            }
        });
    }
}