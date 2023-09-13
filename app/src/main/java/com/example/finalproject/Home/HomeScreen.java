package com.example.finalproject.Home;

import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.FriendsScreen;
import com.example.finalproject.Home.Active.ActiveListAdapter;
import com.example.finalproject.LocationUpdatePeriodicallyService;
import com.example.finalproject.NavBar;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<Integer> list;
    User user;
    ArrayList<User> activeFriend;
    FirebaseFirestoreController<User> userFirebaseController;
    private TextView titleText;
    cardSwipeAdapter adapterSwipe;
    CardStackLayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userFirebaseController=new FirebaseFirestoreController<>(User.class);
        setContentView(R.layout.activity_home_screen);
        activeFriend=new ArrayList<>();
        setUp();
        updateActiveStatus();
        ActiveList();
        swipe();
        NavBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent serviceIntent = new Intent(HomeScreen.this, LocationUpdatePeriodicallyService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onPause() {
        Intent serviceIntent = new Intent(HomeScreen.this, LocationUpdatePeriodicallyService.class);
        stopService(serviceIntent);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Intent serviceIntent = new Intent(HomeScreen.this, LocationUpdatePeriodicallyService.class);
        startService(serviceIntent);
        super.onStop();
    }
    void updateActiveStatus()
    {
       user.set_UserActive(true);
        userFirebaseController.updateDocumentField("Users",user.get_UserName(),"_UserActive", user.is_UserActive());
    }

    private void swipe()
    {
        final int[] curPos = new int[1];

        CardStackView cardStackView=findViewById(R.id.HomeScreenSwipeItem);
        manager=new CardStackLayoutManager(HomeScreen.this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
                curPos[0] =manager.getTopPosition();
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right){
                    checkSwipeRight(activeFriend.get(curPos[0]));
                    Toast.makeText(HomeScreen.this, "Direction Right", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Left){
                    Toast.makeText(HomeScreen.this, "Direction Left", Toast.LENGTH_SHORT).show();
                    checkSwipeLeft(activeFriend.get(curPos[0]));
                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.HomeScreenUserName);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.HomeScreenUserName);
                activeFriend.get(position);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }
        });
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapterSwipe = new cardSwipeAdapter((Context) this, activeFriend);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapterSwipe);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
    }
    void checkSwipeRight(User matchUser)
    {
        if(user.get_UserWaitingList().contains(matchUser.get_UserName()))
        {
            user.add_Friend(matchUser.get_UserName());
            userFirebaseController.updateDocumentField("Users",user.get_UserName(),"_UserFriend", user.get_UserFriend());

        }
        else
        {
            matchUser.add_WaitingList(user.get_UserName());
            userFirebaseController.updateDocumentField("Users",matchUser.get_UserName(),"_UserWaitingList", matchUser.get_UserWaitingList());
        }
    }
    void checkSwipeLeft(User leftUser)
    {
        if(user.get_UserWaitingList().contains(leftUser.get_UserName()))
        {
            user.remove_WaitingList(leftUser.get_UserName());

        }

    }

    void setUp()
    {
        getUser();
        titleText=(TextView) findViewById(R.id.HomeScreenTitleText);
        String temp="Hello! " + user.get_UserName();
        titleText.setText(temp);

    }
    void ActiveList()
    {
        getActiveFriend();
        RecyclerView recyclerView=findViewById(R.id.HomeScreenFriendRecyclerView);
        ActiveListAdapter adapter= new ActiveListAdapter(activeFriend, (Context) this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
    void getUser()
    {
        SharedPreferenceManager<User> sharedPreferenceManager = new SharedPreferenceManager<>(User.class, this);
        user = sharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);
    }
    private void getActiveFriend()
    {
        ArrayList<String> temp= user.get_UserFriend();
        if (temp != null) {
            for (int i = 0; i < temp.size(); i++) {

                User tmp = userFirebaseController.retrieveObjectsFirestoreByID(KEY_COLLECTION_USERS, temp.get(i));
                if (activeFriend == null) {
                    activeFriend = new ArrayList<>();
                }
                activeFriend.add(tmp);

            }
        }

    }

    private void NavBar()
    {
        ImageView home;
        ImageView section;
        ImageView friend;
        ImageView profile;


        home= findViewById(R.id.NaviBarHomeIcon); home.setImageResource(R.drawable.home_icon_fill);
        section= findViewById(R.id.NaviBarSectionIcon);
        friend= findViewById(R.id.NaviBarFriendIcon);
        profile = findViewById(R.id.NaviBarProfile);

        section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, FriendsScreen.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
