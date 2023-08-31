package com.example.finalproject;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FirebaseController<T> {
    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private Class<T> type;

    // Instructor for getting type of T.
    public FirebaseController(Class<T> type) {
        this.type = type;
    }

    // this function used to add to the "new data" to the "collection", the new data document is marked by "new ID".
    public boolean addToFirestore (String collection, String newID, T newData){
        return db.collection(collection).document(newID)
                .set(newData).isSuccessful();
    }

    public ArrayList<T> retrieveAllObjectsOfaCollection (String collection){
        CollectionReference _collection = db.collection(collection);
        Task<QuerySnapshot> future = _collection.get();
        ArrayList<T> list = new ArrayList<>();

        // loop for waiting the future is completely retrieved from firestore
        while(!future.isComplete()){}

        if(future.isSuccessful()){
            for (QueryDocumentSnapshot document : future.getResult()) {
                T obj = document.toObject(type);
                list.add(obj);
            }
        }
        return list;
    }

    // Retrieving by ID. ID is also the name of the document, in the other words, this function get the corresponding document.
    // This firestore assumes every document ID are unique. For example, there is only one user whose ID 01.
    public T retrieveObjectsFirestoreByID (String collection, String id){
        T tObject = null;
        DocumentReference _documentShot = db.collection(collection).document(id);
        Task _documentTask = _documentShot.get();
        while(!_documentTask.isComplete()){}
        DocumentSnapshot _documentSnapShot = (DocumentSnapshot) _documentTask.getResult();
        if(_documentSnapShot != null) tObject = _documentSnapShot.toObject(type);
        return tObject;
    }
}
