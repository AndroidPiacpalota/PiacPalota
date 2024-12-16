package com.example.piacpalota.u.i;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piacpalota.R;
import com.example.piacpalota.u.i.message.Message;
import com.example.piacpalota.u.i.message.MessagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private ArrayList<Message> messagesList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText messageInput;
    private ListenerRegistration messagesListener;
    private String receiverId; // A címzett ID-ja
    private String receiverName; // A címzett neve

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        // Initialize Firebase components
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // A címzett adatai, például egy másik felhasználó ID-ja és neve
        receiverId = getArguments().getString("receiverId"); // Az aktuális címzett ID
        receiverName = getArguments().getString("receiverName"); // Az aktuális címzett neve

        // RecyclerView setup
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Input field for the message
        messageInput = view.findViewById(R.id.messageInput);

        // Setup the messages list and adapter
        messagesList = new ArrayList<>();
        adapter = new MessagesAdapter(messagesList);
        recyclerView.setAdapter(adapter);

        // Load messages from Firestore
        loadMessages();

        // Send button functionality
        view.findViewById(R.id.sendButton).setOnClickListener(v -> sendMessage());

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadMessages() {
        // Listen to the "messages" collection and load messages for the current user and receiver
        messagesListener = db.collection("messages")
                .whereIn("senderId", new ArrayList<String>() {{
                    add(auth.getCurrentUser().getUid());
                    add(receiverId);
                }})
                .whereIn("receiverId", new ArrayList<String>() {{
                    add(auth.getCurrentUser().getUid());
                    add(receiverId);
                }})
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((QuerySnapshot snapshot, FirebaseFirestoreException e) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Hiba történt", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (snapshot != null) {
                        messagesList.clear();
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            Message message = document.toObject(Message.class);
                            messagesList.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(getContext(), "Üzenet nem lehet üres", Toast.LENGTH_SHORT).show();
            return;
        }

        String senderUid = auth.getCurrentUser().getUid();
        String senderName = auth.getCurrentUser().getDisplayName();

        // Create a new message with the receiver details
        Message newMessage = new Message(messageText, senderUid, senderName, receiverId, receiverName, FieldValue.serverTimestamp());


        // Add message to Firestore
        db.collection("messages")
                .add(newMessage)
                .addOnSuccessListener(documentReference -> {
                    messageInput.setText(""); // Clear the message input field
                    Toast.makeText(getContext(), "Üzenet elküldve", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Hiba történt", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (messagesListener != null) {
            messagesListener.remove(); // Unsubscribe from the Firestore listener
        }
    }
}
