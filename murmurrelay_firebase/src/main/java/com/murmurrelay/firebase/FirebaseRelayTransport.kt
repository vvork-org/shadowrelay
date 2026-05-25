package com.murmurrelay.firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.murmurrelay.core.transport.RelayTransport
import com.murmurrelay.core.MurmurRelayResult

class FirebaseRelayTransport : RelayTransport {
    private val db = FirebaseDatabase.getInstance().reference

    override fun sendMessage(
        roomId: String,
        encryptedPayload: String,
        onComplete: (MurmurRelayResult) -> Unit
    ) {
        db.child("murmurrelay")
            .child(roomId)
            .push()
            .setValue(encryptedPayload)
            .addOnSuccessListener {
                onComplete(MurmurRelayResult.Success)
            }
            .addOnFailureListener { e ->
                onComplete(MurmurRelayResult.Error(e.message ?: "Unknown error"))
            }
    }

    override fun observeMessages(
        roomId: String,
        onMessage: (String) -> Unit
    ) {
        db.child("murmurrelay")
            .child(roomId)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    snapshot.getValue(String::class.java)?.let(onMessage)
                }

                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {}

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}