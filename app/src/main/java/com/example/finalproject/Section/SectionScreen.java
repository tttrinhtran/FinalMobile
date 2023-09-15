package com.example.finalproject.Section;

import static com.example.finalproject.Constants.KEY_COLLECTION_SECTION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.R;
import com.example.finalproject.User;

import java.io.Serializable;
import java.util.ArrayList;

public class SectionScreen extends AppCompatActivity implements sectionListInterface {

    private Button btnSection;
    private Button btnMySection;
    private TextView sectionKind;
    private ArrayList<Section> sectionList;
    private ArrayList<String> tmp;
    private ArrayList<Section> mySectionList;
    private FirebaseFirestoreController<Section> sectionFirebaseFirestoreController;
    RecyclerView recyclerView;
 sectionListAdap sectionAdap;

    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        sectionFirebaseFirestoreController = new FirebaseFirestoreController<>(Section.class);
        getData();
        tabSetup();

    }
    void tabSetup()
    {
        sectionKind=findViewById(R.id.section_kind);
        btnSection=findViewById(R.id.Section_Tab);
        sectionRecycler();
        btnSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sectionRecycler();
            }
        });

        btnMySection=findViewById(R.id.MySection_Tab);
        btnMySection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySectionRecycler();

            }
        });
    }
    void getData()
    {
        tmp = new ArrayList<>();
        tmp = sectionFirebaseFirestoreController.retrieveAllDocumentsIDOfaCollection( KEY_COLLECTION_SECTION );
        for(int i=0; i<tmp.size(); i++)
        {
            if (sectionList==null)
            {
                sectionList=new ArrayList<>();
            }
            sectionList.add(sectionFirebaseFirestoreController.retrieveObjectsFirestoreByID(KEY_COLLECTION_SECTION, tmp.get(i)));
       }

        if(mySectionList==null)
        {
            mySectionList=new ArrayList<>();
        }
        for(int i=0; i<sectionList.size(); i++)
        {
//            if(sectionList.get(i).get_SectionHost()=="abc")
//            {
//                Section temp=sectionList.get(i);
//                mySectionList.add(temp);
//                sectionList.remove(i);
//            }
        }
    }
    private void recyclerView(ArrayList<Section> sectionList)
    {
        recyclerView= findViewById(R.id.section_recycleView);

        sectionAdap=new sectionListAdap(sectionList, (Context) SectionScreen.this, this);
        recyclerView.setAdapter(sectionAdap);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void mySectionRecycler()
    {
        sectionKind.setText("My Section");
        recyclerView(mySectionList);

    }
    private void sectionRecycler()
    {
        sectionKind.setText("Section");
        recyclerView(sectionList);
    }

    @Override
    public void onItemClick(int position) {
        Intent i =new Intent(this, SectionDetail.class);
        startActivity(i);
    }
}