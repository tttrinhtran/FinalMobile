package com.example.finalproject.Section;

import static com.example.finalproject.Constants.KEY_COLLECTION_SECTION;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.R;

import java.util.ArrayList;

public class SectionScreen extends AppCompatActivity {

    Button btnSection;
    Button btnMySection;
    TextView sectionKind;
    ArrayList<Section> sectionList;
ArrayList<String> tmp;
    ArrayList<Section> mySectionList;
    FirebaseFirestoreController<Section> sectionFirebaseFirestoreController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
//        tabSetup();
        getData();
    }
    void tabSetup()
    {
        sectionKind=findViewById(R.id.section_kind);
        btnSection=findViewById(R.id.Section_Tab);
        btnSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionKind.setText("Section");
                sectionSetup();
            }
        });

        btnMySection=findViewById(R.id.MySection_Tab);
        btnMySection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectionKind.setText("My Section");
                mySectionSetUp();
            }
        });
    }
    void getData()
    {

        tmp=new ArrayList<>();
        tmp=sectionFirebaseFirestoreController.retrieveAllDocumentsIDOfaCollection( KEY_COLLECTION_SECTION );
        for(int i=0; i<tmp.size(); i++)
        {
            if (sectionList==null)
            {
                sectionList=new ArrayList<>();
            }
            sectionList.add(sectionFirebaseFirestoreController.retrieveObjectsFirestoreByID(KEY_COLLECTION_SECTION, tmp.get(i)));
       }

    }

    private void mySectionSetUp() {


    }

    private void sectionSetup() {



    }

}