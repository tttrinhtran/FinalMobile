package com.example.finalproject.Home;

import static com.example.finalproject.Constants.FIRESTORE_LOCATION_KEY;
import static com.example.finalproject.Constants.KEY_COLLECTION_USERS;
import static com.example.finalproject.Constants.KEY_SHARED_PREFERENCE_USERS;
import static com.example.finalproject.Constants.LOCATION_UPDATE_STATUS;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Constants;
import com.example.finalproject.FirebaseCloudStorageManager;
import com.example.finalproject.FirebaseFirestoreController;
import com.example.finalproject.FirestoreGeoHashQueries;
import com.example.finalproject.FriendsScreen;
import com.example.finalproject.Home.Active.ActiveListAdapter;
import com.example.finalproject.Listeners.CardListener;
import com.example.finalproject.LocationUpdatePeriodicallyService;
import com.example.finalproject.Message.ChatActivity;
import com.example.finalproject.Position;
import com.example.finalproject.Profile.ProfileScreen;
import com.example.finalproject.R;
import com.example.finalproject.Section.SectionScreen;
import com.example.finalproject.SharedPreferenceManager;
import com.example.finalproject.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class HomeScreen extends AppCompatActivity implements CardListener {
    private FirestoreGeoHashQueries firestoreGeoHashQueries;
    private FirebaseFirestoreController<Position> positionfirebaseFirestoreController;
    private static final String TAG = "HomeScreen";
    User user;
    ArrayList<User> activeFriend;
    ArrayList<User> activeUsers;
    FirebaseFirestoreController<User> userFirebaseController;
    private SharedPreferenceManager<Boolean> isLocationUpdatedSharedPreference;

    private TextView titleText;
    cardSwipeAdapter adapterSwipe;
    CardStackLayoutManager manager;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private CardStackView cardStackView;
    private RecyclerView recyclerView;
    private ImageView home;
    private ImageView section;
    private ImageView friend;
    private ImageView profile;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        HomeScren_UIElementsSetup();

        databaseManagerInit();

        startLocationService();

        setUp();
        NavBar();

        Handler handler = new Handler(Looper.getMainLooper());
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });

                while (!isLocationUpdatedSharedPreference.retrieveSerializableObjectFromSharedPreference(LOCATION_UPDATE_STATUS)){}
                if(isLocationUpdatedSharedPreference.retrieveSerializableObjectFromSharedPreference(LOCATION_UPDATE_STATUS)) {
                    Position pos = null;
                    pos = positionfirebaseFirestoreController.retrieveObjectsFirestoreByID(FIRESTORE_LOCATION_KEY, user.get_UserName());
                    firestoreGeoHashQueries.QueryForLocationFireStore(user, pos, 500, activeUsers);
                    // activeUsers.removeAll(user.get_UserFriend());
                    activeUsers.removeIf( u -> user.get_UserFriend().contains(u.get_UserName()) );
                }
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        updateActiveStatus();
                        swipe();
                        progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                });
            }
        });
        ActiveList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startLocationService() {
        Intent serviceIntent = new Intent(HomeScreen.this, LocationUpdatePeriodicallyService.class);

        isLocationUpdatedSharedPreference.clearObject(LOCATION_UPDATE_STATUS);
        isLocationUpdatedSharedPreference.storeSerializableObjectToSharedPreference(false, LOCATION_UPDATE_STATUS);

        startService(serviceIntent);
    }

    private void databaseManagerInit() {
        userFirebaseController = new FirebaseFirestoreController<>(User.class);
        activeFriend = new ArrayList<>();
        activeUsers = new ArrayList<>();

        firestoreGeoHashQueries = new FirestoreGeoHashQueries();
        positionfirebaseFirestoreController = new FirebaseFirestoreController<>(Position.class);
        isLocationUpdatedSharedPreference = new SharedPreferenceManager<>(Boolean.class,HomeScreen.this);
    }

    private void HomeScren_UIElementsSetup(){
        swipeRefreshLayout = findViewById(R.id.HomeScreenRefreshLayout);
        progressBar = findViewById(R.id.HomeScreenProgressBar);
        cardStackView = findViewById(R.id.HomeScreenSwipeItem);
        titleText = (TextView) findViewById(R.id.HomeScreenTitleText);
        recyclerView = findViewById(R.id.HomeScreenFriendRecyclerView);
        home = findViewById(R.id.NaviBarHomeIcon);
        section = findViewById(R.id.NaviBarSectionIcon);
        friend = findViewById(R.id.NaviBarFriendIcon);
        profile = findViewById(R.id.NaviBarProfile);
        avatar = findViewById(R.id.HomeScreenAvatarTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d( "Vicluu", "onDestroy: " + "Home destroy");
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
        stopService(serviceIntent);
        Log.d( "Vicluu", "onStop: " + "Home stop");
        super.onStop();
    }

    void updateActiveStatus() {
        user.set_UserActive(true);
        userFirebaseController.updateDocumentField("Users", user.get_UserName(), "_UserActive", user.is_UserActive());
    }

    private void swipe() {
        final int[] curPos = new int[1];
        manager = new CardStackLayoutManager(HomeScreen.this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
                curPos[0] = manager.getTopPosition();
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right) {
                    checkSwipeRight(activeUsers.get(curPos[0]));
                    // Toast.makeText(HomeScreen.this, "Direction Right", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Left) {
                    // Toast.makeText(HomeScreen.this, "Direction Left", Toast.LENGTH_SHORT).show();
                    checkSwipeLeft(activeUsers.get(curPos[0]));
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
                activeUsers.get(position);
                Log.d(TAG, "onCardDisappeared: " + position + ", nama: " + tv.getText());
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
        manager.setOverlayInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                return 10.0f;
            }
        });
        adapterSwipe = new cardSwipeAdapter( activeUsers, this);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapterSwipe);
        // adapterSwipe.setOnItemClickListener(this);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

//        for(User u : activeUsers){
//            Log.d("ACTIVE_USERS", u.get_UserName());
//        }
    }

    void checkSwipeRight(User matchUser) {
        Log.d("MATCH_USER", matchUser.get_UserName());

        if (user.get_UserWaitingList() != null && user.get_UserWaitingList().contains(matchUser.get_UserName())) {
            Log.d("MATCH_USER_CONTAIN", matchUser.get_UserName());

            user.add_Friend(matchUser.get_UserName());
            userFirebaseController.updateDocumentField("Users", user.get_UserName(), "_UserFriend", user.get_UserFriend());

            matchUser.add_Friend(user.get_UserName());
            userFirebaseController.updateDocumentField("Users", matchUser.get_UserName(), "_UserFriend", matchUser.get_UserFriend());

            user.remove_WaitingList(matchUser.get_UserName());
            userFirebaseController.updateDocumentField("Users", user.get_UserName(), "_UserWaitingList", user.get_UserWaitingList());

            // Update shared preference
            SharedPreferenceManager currentInstance = new SharedPreferenceManager<>(User.class, this);
            currentInstance.storeSerializableObjectToSharedPreference(user, Constants.KEY_SHARED_PREFERENCE_USERS);

            // Update activeUsers list
            activeUsers.remove(matchUser);
            adapterSwipe.notifyDataSetChanged();

            Intent i = new Intent(HomeScreen.this, MatchSplashScreen.class);
            i.putExtra("USER_MATCH", matchUser);
            Bundle b = ActivityOptions.makeSceneTransitionAnimation(HomeScreen.this).toBundle();
            startActivity(i, b);
            finish();

        } else {
            Log.d("MATCH_USER_NOT_CONTAINT", matchUser.get_UserName());
            matchUser.add_WaitingList(user.get_UserName());
            userFirebaseController.updateDocumentField("Users", matchUser.get_UserName(), "_UserWaitingList", matchUser.get_UserWaitingList());
        }
    }

    void checkSwipeLeft(User leftUser) {
        Log.d("LEFT_USER", leftUser.get_UserName());
        if (user.get_UserWaitingList() != null && user.get_UserWaitingList().contains(leftUser.get_UserName())) {

            user.remove_WaitingList(leftUser.get_UserName());
            userFirebaseController.updateDocumentField("Users", user.get_UserName(), "_UserWaitingList", user.get_UserWaitingList());

            // Update shared preference
            SharedPreferenceManager currentInstance = new SharedPreferenceManager<>(User.class, this);
            currentInstance.storeSerializableObjectToSharedPreference(user, Constants.KEY_SHARED_PREFERENCE_USERS);
        }
    }

    void setUp() {
        // Setting for Hello Text
        getUser();
        String temp = "Hello, " + user.get_UserFirstname() + "!";
        titleText.setText(temp);

        FirebaseCloudStorageManager firebaseCloudStorageManager = new FirebaseCloudStorageManager();
        firebaseCloudStorageManager.FetchingImageFromFirebase(user, avatar);
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);


        // Setting for refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLocationUpdatedSharedPreference.clearObject(LOCATION_UPDATE_STATUS);
                isLocationUpdatedSharedPreference.storeSerializableObjectToSharedPreference(false, LOCATION_UPDATE_STATUS);

                Handler handler = new Handler(Looper.getMainLooper());
                ExecutorService executorService = Executors.newSingleThreadExecutor();

                progressBar.setVisibility(View.VISIBLE);
                activeUsers.clear();

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        });

                        while (!isLocationUpdatedSharedPreference.retrieveSerializableObjectFromSharedPreference(LOCATION_UPDATE_STATUS)){}
                        if(isLocationUpdatedSharedPreference.retrieveSerializableObjectFromSharedPreference(LOCATION_UPDATE_STATUS)) {
                            Position pos = null;
                            pos = positionfirebaseFirestoreController.retrieveObjectsFirestoreByID(FIRESTORE_LOCATION_KEY, user.get_UserName());
                            firestoreGeoHashQueries.QueryForLocationFireStore(user, pos, 500, activeUsers);
                            activeUsers.removeIf( u -> user.get_UserFriend().contains(u.get_UserName()) );
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ActiveList();
                                adapterSwipe.notifyDataSetChanged();
                                updateActiveStatus();
                                swipe();

                                progressBar.setVisibility(View.INVISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        });
                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    void ActiveList() {
        getActiveFriend();
        ActiveListAdapter adapter = new ActiveListAdapter(activeFriend, (Context) this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    void getUser() {
        SharedPreferenceManager<User> sharedPreferenceManager = new SharedPreferenceManager<>(User.class, this);
        user = sharedPreferenceManager.retrieveSerializableObjectFromSharedPreference(KEY_SHARED_PREFERENCE_USERS);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection(KEY_COLLECTION_USERS).document(user.get_UserName());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                user = snapshot.toObject(User.class);
            }
        });
    }

    private void getActiveFriend() {
        ArrayList<String> temp = user.get_UserFriend();
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

    private void NavBar() {
        home.setImageResource(R.drawable.home_icon_fill);

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, FriendsScreen.class);
                //Bundle b = ActivityOptions.makeSceneTransitionAnimation(HomeScreen.this).toBundle();
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, ProfileScreen.class);
                startActivity(intent);
            }
        });

        section.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, SectionScreen.class);
            Bundle b = ActivityOptions.makeSceneTransitionAnimation(HomeScreen.this).toBundle();
            startActivity(intent, b);
        });
    }
    void hobbies()
    {
        ArrayList<String>hobiesList=user.get_UserHobbies();
        RecyclerView recyclerView=findViewById(R.id.SwipeItemHobbies);
        hobbiesAdapter adapter= new hobbiesAdapter(hobiesList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));

    }

    @Override
    public void onCardClicker( User user ) {
        Intent intent = new Intent( getApplicationContext(), UserBio.class );
        Log.d("Vicluu", "onCardClicker: " + getApplicationContext());
        intent.putExtra( "USER_OBJECT", user );
        startActivity(intent);
    }
}
