package com.example.finalproject.Section;

import static com.example.finalproject.Constants.KEY_COLLECTION_SECTION;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.FriendsScreen;
import com.example.finalproject.Home.HomeScreen;
import com.example.finalproject.Profile.ProfileScreen;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;

import java.io.Serializable;
import java.util.ArrayList;

public class SectionScreen extends AppCompatActivity implements sectionListInterface {

    private Button btnSection;
    private Button btnMySection;
    private TextView sectionKind;
    private ImageView addSectionButton;
    private ArrayList<Section> sectionList;
    private ArrayList<Section> mySectionList;
    private  ArrayList<Section> recyclerViewList;
    private FirebaseFirestoreController<Section> sectionFirebaseFirestoreController;
    RecyclerView SectionRecyclerView;
    sectionListAdap sectionAdap;
    private User user;
    private int tab = 1; // 1 = friend's section, 2 = my section




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        sectionFirebaseFirestoreController = new FirebaseFirestoreController<>(Section.class);
        getData();
        tabSetup();
        navBar();
    }

    private void navBar() {
        ImageView home;
        ImageView section;
        ImageView friend;
        ImageView profile;


        home = findViewById(R.id.NaviBarHomeIcon);
        section= findViewById(R.id.NaviBarSectionIcon); section.setImageResource(R.drawable.section_icon_fill);
        friend= findViewById(R.id.NaviBarFriendIcon);
        profile = findViewById(R.id.NaviBarProfile);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SectionScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SectionScreen.this, FriendsScreen.class);
                Bundle b = ActivityOptions.makeSceneTransitionAnimation(SectionScreen.this).toBundle();
                startActivity(intent, b);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SectionScreen.this, ProfileScreen.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sectionFirebaseFirestoreController = new FirebaseFirestoreController<>(Section.class);
        clear();
        getData();
        tabSetup();
    }
    void clear()
    {
        sectionList.clear();
        mySectionList.clear();
        recyclerViewList.clear();
    }

    void tabSetup()
    {
        sectionKind = findViewById(R.id.section_kind);
        btnSection = findViewById(R.id.Section_Tab);
        btnMySection = findViewById(R.id.MySection_Tab);
        addSectionButton = findViewById(R.id.SectionScreenAddSectionButton);

        sectionRecycler();
        btnSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               btnSection.setTextColor(ContextCompat.getColor(SectionScreen.this, R.color.primary_blue));
               btnMySection.setTextColor(ContextCompat.getColor(SectionScreen.this, R.color.secondary_text));
               btnSection.setBackgroundResource(R.drawable.border_tab);
               btnMySection.setBackgroundResource(android.R.color.transparent);
               sectionRecycler();
            }
        });

        btnMySection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSection.setTextColor(ContextCompat.getColor(SectionScreen.this, R.color.secondary_text));
                btnMySection.setTextColor(ContextCompat.getColor(SectionScreen.this, R.color.primary_blue));
                btnSection.setBackgroundResource(android.R.color.transparent);
                btnMySection.setBackgroundResource(R.drawable.border_tab);
                mySectionRecycler();
            }
        });

        addSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SectionScreen.this, AddSectionScreen.class);
                startActivity(i);
            }
        });
    }
    void getData()
    {
        //get user
        SharedPreferenceManager<User> sharedPreferenceManager = new SharedPreferenceManager<>(User.class, this);
        user = sharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);
         //get all section
        ArrayList<String> tmp;
        tmp = new ArrayList<>();
        tmp = sectionFirebaseFirestoreController.retrieveAllDocumentsIDOfaCollection( KEY_COLLECTION_SECTION );
        for(int i=0; i<tmp.size(); i++)
        {
            if (sectionList == null)
            {
                sectionList = new ArrayList<>();
            }
            sectionList.add(sectionFirebaseFirestoreController.retrieveObjectsFirestoreByID(KEY_COLLECTION_SECTION, tmp.get(i)));
       }
        if(mySectionList == null)
        {
            mySectionList = new ArrayList<>();
        }
        ArrayList<Section> sectionsToRemove = new ArrayList<>();
        for(int i = 0; i < sectionList.size(); i++)
        {
           if(check_mySection(sectionList.get(i)) == true)
           {
               mySectionList.add(sectionList.get(i));
               sectionsToRemove.add(sectionList.get(i));
           }
        }

        sectionList.removeAll(sectionsToRemove);
    }
    boolean check_mySection(Section section)
    {
        if(section.get_SectionParticipate() == null)
        {
            section._SectionParticipate = new ArrayList<>();
        }
        if((section.get_SectionHost().equals(user.get_UserName()))||(section.get_SectionParticipate().contains(user.get_UserName())))
        {
            return true;
        }
        return false;

    }
    private void recyclerView(ArrayList<Section> recyclerViewList)
    {
        SectionRecyclerView = findViewById(R.id.section_recycleView);
        sectionAdap = new sectionListAdap(recyclerViewList, (Context) SectionScreen.this, this);
        SectionRecyclerView.setAdapter(sectionAdap);
        SectionRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void mySectionRecycler()
    {
        sectionKind.setText("My Section");
        recyclerViewList = mySectionList;
        recyclerView(recyclerViewList);


    }
    private void sectionRecycler()
    {
        sectionKind.setText("Section");
        recyclerViewList = sectionList;
        recyclerView(recyclerViewList);
    }


    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(this, SectionDetail.class);
         i.putExtra("section", (Serializable) recyclerViewList.get(position));
        startActivity(i);
    }
}