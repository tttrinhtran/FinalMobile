package com.example.finalproject;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

// Remind Firebase Firestore Structure: A Collection -> Many Document...

public class FirebaseFirestoreController<T> {
    final private FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private Class<T> type;

    // Instructor for getting type of T.
    public FirebaseFirestoreController(Class<T> type) {
        this.type = type;
    }

    // this function used to add to the "new data" to the "collection", the new data document is marked by "new ID".
    public void addToFirestore(String collection, String newID, T newData) {
        db.collection(collection).document(newID)
                .set(newData);
    }


    // This function is used to retrieve all documents ID of a specific Collection.
    public ArrayList<String> retrieveAllDocumentsIDOfaCollection(String collection) {
        CollectionReference _collection = db.collection(collection);
        Task<QuerySnapshot> _documentTask = _collection.get();
        ArrayList<String> list = new ArrayList<>();

        // loop for waiting the future is completely retrieved from firestore
        while (!_documentTask.isComplete()) {
        }

        // Traversing the list of documents and store them into an ArrayList.
        if (_documentTask.isSuccessful()) {
            for (DocumentSnapshot document : _documentTask.getResult().getDocuments()) {
                list.add(document.getId().toString());
            }
        }
        return list;
    }

    // Retrieving by ID. ID is also the name of the document, in the other words, this function get the corresponding document.
    // This firestore assumes every document ID are unique. For example, there is only one user whose ID 01.
    public T retrieveObjectsFirestoreByID(String collection, String id) {
        T tObject = null;
        DocumentReference _documentShot = db.collection(collection).document(id);
        Task _documentTask = _documentShot.get();
        while (!_documentTask.isComplete()) {
        }
        DocumentSnapshot _documentSnapShot = (DocumentSnapshot) _documentTask.getResult();
        if (_documentSnapShot != null) tObject = _documentSnapShot.toObject(type);
        return tObject;
    }

    // The following function is used to update a specific field of a document, for ex, the isActive atribute of UserName ABC.
    // There are three parameters: Collection, id of Document (i.e., username in case of User class), field for updating (i.e., isActive)
    public <Type> void updateDocumentField(String collection, String id, String field, Type newValue) {
        // Update an existing document
        db.collection(collection).document(id).update(field, newValue);
    }
}
