package com.example.finalproject.Section;

import static com.example.finalproject.Constants.KEY_COLLECTION_SECTION;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class SectionDetail extends BaseActivity {

    private Section section;
    private User user;
    FirebaseFirestoreController<Section> sectionirebaseFirestoreController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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
        tvHost.setText("Host:  " + section.get_SectionHost());
        tvDate.setText(section.get_SectionDate().toString());
        tvDes.setText(section.get_SectionDescript());

        if(check_mySection(section) == true)
        {
            joinBtn.setText("Join Section");
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                    String sectionDate = section.get_SectionDate();
                    String sectionTime = section.get_SectionHour();

                    LocalDateTime sectionDateAndTime = getSectionTime(sectionDate, sectionTime);

                    if(currentDateTime.isAfter(sectionDateAndTime)) {
                        if(section._SectionParticipate.contains(section._SectionHost) || user.get_UserName().equals(section._SectionHost)) {
                            forwardToRoom(SectionDetail.this, section._SectionName, "123");
                        } else Toast.makeText(SectionDetail.this, "Waiting for Host to start the Section",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SectionDetail.this, "Section has not openned yet", Toast.LENGTH_SHORT).show();
                    }
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
                finish();
            }
        });
    }

    private static LocalDateTime getSectionTime(String sectionDate, String sectionTime) {
        ArrayList<Integer> time = new ArrayList<Integer>(0); // {dd,mm,yyyy,hh,mm,ss}

        StringBuilder temp = new StringBuilder();

        for (char i : sectionDate.toCharArray()){
            if(i == '/'){
                time.add(Integer.parseInt(temp.toString()));
                temp = new StringBuilder();
            }else {
                temp.append(i);
            }
        }
        time.add(Integer.parseInt(temp.toString())); // add for the last element since dd/mm/yyyy there is no way
        // to identify yyyy inside the loop
        temp = new StringBuilder();

        for (char i : sectionTime.toCharArray()){
            if(i == ':'){
                time.add(Integer.parseInt(temp.toString()));
                temp = new StringBuilder();
            }else {
                temp.append(i);
            }
        }
        time.add(Integer.parseInt(temp.toString())); // add for the last element since hh:mm:ss there is no way
        // to identify ss inside the loop


        int remain_attribute_of_time = time.size();
        for (int i = 0 ; i < 6 - remain_attribute_of_time; i++) time.add(0);

        LocalDateTime sectionDateAndTime = LocalDateTime.of(time.get(2), time.get(1), time.get(0), time.get(3), time.get(4), time.get(5));
        return sectionDateAndTime;
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
        i.putExtra("nickname",user.get_UserNickName());
        startActivity(i);
    }
}