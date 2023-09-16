package com.example.finalproject.Section;

import static com.example.finalproject.Constants.KEY_COLLECTION_SECTION;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;
import com.example.finalproject.openvcall.model.ConstantApp;
import com.example.finalproject.openvcall.model.CurrentUserSettings;
import com.example.finalproject.openvcall.ui.BaseActivity;
import com.example.finalproject.openvcall.ui.CallActivity;
import com.example.finalproject.openvcall.ui.SectionRoomVideoChatScreen;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class SectionDetail extends BaseActivity {

    private Section section;
    private User user;
    FirebaseFirestoreController<Section> sectionirebaseFirestoreController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_detail);
        sectionirebaseFirestoreController=new FirebaseFirestoreController<>(Section.class);
        getData();
        setUp();

    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    void getData()
    {
        SharedPreferenceManager<User> sharedPreferenceManager = new SharedPreferenceManager<>(User.class, this);
        user = sharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            section=getIntent().getSerializableExtra("section", Section.class);
        }
    }

    void setUp()
    {
        TextView tvTitle = findViewById(R.id.SectionBioName);
        TextView tvHost = findViewById(R.id.SectionBioHost);
        TextView tvHour = findViewById(R.id.SectionBioHour);
        TextView tvDate = findViewById(R.id.SectionBioDate);
        TextView tvDes = findViewById(R.id.SectionBioDescription);
        TextView joinBtn = findViewById(R.id.SectionBioJoinBtn);
        ImageButton backBtn = findViewById(R.id.SectionBioBackBtn);

        tvTitle.setText(section.get_SectionName());
        tvHost.setText(section.get_SectionHost());
        tvDate.setText(section.get_SectionDate().toString());
        tvDes.setText(section.get_SectionDescript());

        if(check_mySection(section) == true)
        {
            joinBtn.setText("Join Section");
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forwardToRoom(SectionDetail.this, section._SectionName, "123");
                }
            });
        }
        else {
            joinBtn.setText("Add section");
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinSection();
                    finish();
                }
            });
        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
void hostUpdate()
{
}
    void joinSection()
    {
        section.add_SectionParticipate(user.get_UserName());
        sectionirebaseFirestoreController.updateDocumentField(KEY_COLLECTION_SECTION, section.get_SectionName(), "_SectionParticipate",section._SectionParticipate);
    }

    boolean check_mySection(Section section)
    {
        if(section.get_SectionParticipate()==null)
        {
            section._SectionParticipate=new ArrayList<>();
        }
        if(section.get_SectionHost().equals(user.get_UserName())||section.get_SectionParticipate().contains(user.get_UserName()))
        {
            return true;
        }
        return false;
    }

    public void forwardToRoom(Context context, String channel, String encryption) {

        vSettings().mChannelName = channel;

        vSettings().mEncryptionKey = encryption;

        Intent i = new Intent(context, CallActivity.class);
        i.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channel);
        i.putExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY, encryption);
        startActivity(i);
    }
}