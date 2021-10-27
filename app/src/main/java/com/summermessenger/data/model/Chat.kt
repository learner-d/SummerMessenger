package com.summermessenger.data.model

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

data class Chat(val participants:ArrayList<User>,
                val messages: ArrayList<Message>){
    companion object{
        suspend fun load(chatDoc: DocumentSnapshot) : Chat {
            val cdParticipantRefs = chatDoc["participants"] as List<DocumentReference>
            val cdMessageRefs = chatDoc["messages"] as List<DocumentReference>

            val participants = ArrayList<User>(cdParticipantRefs.size)
            for (cdParticipantRef in cdParticipantRefs){
                val cdParticipant = cdParticipantRef.get().await()
                participants.add(User.load(cdParticipant!!))
            }

            val messages = ArrayList<Message>(cdMessageRefs.size)
            for (cdMessageRef in cdMessageRefs){
                //TODO add ref checking
                val cdMessage = cdMessageRef.get().await()
                messages.add(Message.load(cdMessage))
            }
            return Chat(participants, messages)
        }
    }
}