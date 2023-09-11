package com.example.finalproject.Home;

import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.Home.Active.ActiveListAdapter;
import com.example.finalproject.R;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.SwipeAdapter;
import com.example.finalproject.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.library.Koloda;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {
    private static final String TAG = "MainActivity";
//    private SwipeAdapter adapter;
    private List<Integer> list;
     User user;
     ArrayList<User> activeFriend;

//    Koloda koloda;
    FirebaseFirestoreController<User> userFirebaseController;
    private TextView titleText;
     cardSwipeAdapter adapterSwipe;
    CardStackLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setUp();
        ActiveList();
        swipe();
    }

    private void swipe()
    {
//        koloda = findViewById(R.id.HomeScreenSwipeItem);
//        adapter = new SwipeAdapter(this, activeFriend);
//        koloda.setAdapter(adapter);
        CardStackView cardStackView=findViewById(R.id.HomeScreenSwipeItem);
        manager=new CardStackLayoutManager(HomeScreen.this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right){
                    Toast.makeText(HomeScreen.this, "Direction Right", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Top){
                    Toast.makeText(HomeScreen.this, "Direction Top", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Left){
                    Toast.makeText(HomeScreen.this, "Direction Left", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Bottom){
                    Toast.makeText(HomeScreen.this, "Direction Bottom", Toast.LENGTH_SHORT).show();
                }

//
//                if (manager.getTopPosition() == adapterSwipe.getItemCount() - 5){
//                    paginate();
//                }

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
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }
        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapterSwipe = new cardSwipeAdapter(this, activeFriend);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapterSwipe);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
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
        userFirebaseController=new FirebaseFirestoreController<>(User.class);
        ArrayList<String> temp= user.get_UserFriend();
        for(int i=0; i< temp.size(); i++)
        {

            User tmp=userFirebaseController.retrieveObjectsFirestoreByID(KEY_COLLECTION_USERS, temp.get(i));
            if (activeFriend==null) {
                activeFriend=new ArrayList<>();
            }
            activeFriend.add(tmp);

        }



    }

}