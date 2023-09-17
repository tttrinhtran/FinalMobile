package com.example.finalproject;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

// Note: Username is UNIQUE and be the ID of corresponding document on firestore.
public class User implements Serializable {

    String _UserName;
    String _UserPassword;
    String _UserFirstname;
    String _UserLastname;
    String _UserDoB;
    String _UserJoinDate;
    String _UserNickName;
    String _UserPhone;
    boolean _UserActive;
    String _UserBio;
    ArrayList<String> _UserHobbies;
    String _UserSchool;
    String _UserSpecialization;
    String _UserAddress;
    ArrayList<String> _UserFriend;
    ArrayList<String> _UserWaitingList;
    public float _UserMinAge = 18;
    public float _UserMaxAge = 50;
    public float _UserDistancePref = 5000;



    public User(){}

    public User(String _UserName, String _UserPassword, String _UserFirstname, String _UserLastname, String _UserDoB, String _UserJoinDate, String _UserNickName, String _UserPhone, boolean _UserActive, String _UserBio, ArrayList<String> _UserHobbies, String _UserSchool, String _UserSpecialization) {
        this._UserName = _UserName;
        this._UserPassword = _UserPassword;
        this._UserFirstname = _UserFirstname;
        this._UserLastname = _UserLastname;
        this._UserDoB = _UserDoB;
        this._UserJoinDate = _UserJoinDate;
        this._UserNickName = _UserNickName;
        this._UserPhone = _UserPhone;
        this._UserActive = _UserActive;
        this._UserBio = _UserBio;
        this._UserHobbies = _UserHobbies;
        this._UserSchool = _UserSchool;
        this._UserSpecialization = _UserSpecialization;
    }

    @Override
    public String toString() {
        return "User{" +
                "_UserName='" + _UserName + '\'' +
                ", _UserPassword='" + _UserPassword + '\'' +
                ", _UserFirstname='" + _UserFirstname + '\'' +
                ", _UserLastname='" + _UserLastname + '\'' +
                ", _UserDoB='" + _UserDoB + '\'' +
                ", _UserJoinDate='" + _UserJoinDate + '\'' +
                ", _UserNickName='" + _UserNickName + '\'' +
                ", _UserPhone='" + _UserPhone + '\'' +
                ", _UserActive=" + _UserActive +
                ", _UserBio='" + _UserBio + '\'' +
                ", _UserHobbies='" + _UserHobbies + '\'' +
                ", _UserSchool='" + _UserSchool + '\'' +
                ", _UserFriend='" + _UserFriend + '\'' +
                ", _UserWaitingList='" + _UserWaitingList + '\'' +
                ", _UserSpecialization='" + _UserSpecialization + '\'' +
                ", _UserAddress='" + _UserAddress + '\'' +
                ", _UserMinAge='" + _UserMinAge + '\'' +
                ", _UserMaxAge='" + _UserMaxAge + '\'' +
                ", _UserDistancePref='" + _UserDistancePref + '\'' +
                '}';
    }

    public String get_UserName() {
        return _UserName;
    }

    public void set_UserName(String _UserName) {
        this._UserName = _UserName;
    }

    public String get_UserPassword() {
        return _UserPassword;
    }

    public void set_UserPassword(String _UserPassword) {
        this._UserPassword = _UserPassword;
    }

    public String get_UserFirstname() {
        return _UserFirstname;
    }

    public void set_UserFirstname(String _UserFirstname) {
        this._UserFirstname = _UserFirstname;
    }

    public String get_UserLastname() {
        return _UserLastname;
    }

    public void set_UserLastname(String _UserLastname) {
        this._UserLastname = _UserLastname;
    }

    public String get_UserDoB() {
        return _UserDoB;
    }

    public void set_UserDoB(String _UserDoB) {
        this._UserDoB = _UserDoB;
    }

    public String get_UserJoinDate() {
        return _UserJoinDate;
    }

    public void set_UserJoinDate(String _UserJoinDate) {
        this._UserJoinDate = _UserJoinDate;
    }

    public String get_UserNickName() {
        return _UserNickName;
    }

    public void set_UserNickName(String _UserNickName) {
        this._UserNickName = _UserNickName;
    }

    public String get_UserPhone() {
        return _UserPhone;
    }

    public void set_UserPhone(String _UserPhone) {
        this._UserPhone = _UserPhone;
    }

    public boolean is_UserActive() {
        return _UserActive;
    }

    public void set_UserActive(boolean _UserActive) {
        this._UserActive = _UserActive;
    }

    public String get_UserBio() {
        return _UserBio;
    }

    public void set_UserBio(String _UserBio) {
        this._UserBio = _UserBio;
    }

    public ArrayList<String> get_UserHobbies() {
        return _UserHobbies;
    }

    public void set_UserHobbies(ArrayList<String> _UserHobbies) {
        this._UserHobbies = _UserHobbies;
    }

    public String get_UserSchool() {
        return _UserSchool;
    }

    public void set_UserSchool(String _UserSchool) {
        this._UserSchool = _UserSchool;
    }

    public String get_UserSpecialization() {
        return _UserSpecialization;
    }

    public void set_UserSpecialization(String _UserSpecialization) {
        this._UserSpecialization = _UserSpecialization;
    }

    public ArrayList<String> get_UserFriend() {
        return _UserFriend;
    }

    public void set_UserFriend(ArrayList<String> _UserFriend) {
        this._UserFriend = _UserFriend;
    }

    public ArrayList<String> get_UserWaitingList() {
        return _UserWaitingList;
    }

    public void set_UserWaitingList(ArrayList<String> _UserWaitingList) {
        this._UserWaitingList = _UserWaitingList;
    }

    public String get_UserAddress() {
        return _UserAddress;
    }

    public float get_UserMinAge() {
        return _UserMinAge;
    }

    public float get_UserMaxAge() {
        return _UserMaxAge;
    }

    public float get_UserDistancePref() {
        return _UserDistancePref;
    }
    public void set_UserAddress(String _UserAddress) {
        this._UserAddress = _UserAddress;
    }

    public void set_UserMinAge(float _UserMinAge) {
        this._UserMinAge = _UserMinAge;
    }

    public void set_UserMaxAge(float _UserMaxAge) {
        this._UserMaxAge = _UserMaxAge;
    }

    public void set_UserDistancePref(float _UserDistancePref) {
        this._UserDistancePref = _UserDistancePref;
    }

    public void add_Friend(String _UserFriend)
    {

        // Add new friend into user friend list
        if( this._UserFriend == null )  this._UserFriend = new ArrayList<>();
        this._UserFriend.add(_UserFriend);

        // Initialize conversation
        HashMap<String, Object> conversationMap = new HashMap<>();
        conversationMap.put(Constants.KEY_SENDER_ID, this.get_UserName());
            // Must be filled later
        conversationMap.put(Constants.SENDER_IMAGE, "null");
        conversationMap.put(Constants.KEY_RECEIVER_ID, _UserFriend );
            // Must be filled later
        conversationMap.put(Constants.RECEIVER_IMAGE, "null");
        conversationMap.put(Constants.KEY_LAST_MESSAGE, "Let's say bonjour <3");
        conversationMap.put(Constants.KEY_TIMESTAMP, new Date() );

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String conversationID;

//        String conversationID = _UserFriend + "+" + this.get_UserName();
//
//        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
//                .document(conversationID)
//                .get()
//                .addOnCompleteListener( task -> {
//                    if( task.isSuccessful() ) {
//                        DocumentSnapshot documentSnapshot = task.getResult();
//                        if(!documentSnapshot.exists()) {
//                            database.collection(Constants.KEY_COLLECTION_CONVERSATION)
//                                    .document( this.get_UserName() + "+" + _UserFriend)
//                                    .set(conversationMap);
//                        }
//                    }
//                } );

        if( this.get_UserName().compareTo(_UserFriend) < 0 ) conversationID = this.get_UserName() + "+" + _UserFriend;
        else conversationID = _UserFriend + "+" + this.get_UserName();
        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .document( conversationID )
                .set(conversationMap);
    }
    public void add_WaitingList(String _UserWaiting)
    {

        if(this._UserWaitingList==null)
        {
            this._UserWaitingList=new ArrayList<>();
        }
        if(this._UserWaitingList.contains(_UserWaiting))
        {
            return;
        }

        this._UserWaitingList.add(_UserWaiting);

    }

    public  void remove_WaitingList(String user)
    {
        if(this._UserWaitingList != null && this._UserWaitingList.contains(user)){
            this._UserWaitingList.remove(user);
        }

    }

    public String get_UserAge()

    {   Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int Useryear= Integer.parseInt(this._UserDoB.substring(this._UserDoB.length() - 4));
        return Integer.toString(currentYear-Useryear);
    }
}
