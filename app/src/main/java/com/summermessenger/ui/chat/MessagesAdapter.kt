package com.summermessenger.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.summermessenger.R
import com.summermessenger.data.LoginDataSource
import com.summermessenger.data.model.Message

class MessagesAdapter(val msgList: List<Message>, val context: Context)
    : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_message_fragment, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return msgList.size;
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.sender.text = msgList[position].sender.displayName
        holder.messageText.text = msgList[position].text
        holder.messageTime.text = msgList[position].timeStamp
        if (LoginDataSource.MockUsers.indexOf(msgList[position].sender) % 2 == 0)
            ;
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sender: TextView = view.findViewById(R.id.sender)
        val messageText: TextView = view.findViewById(R.id.messageText)
        val messageTime: TextView = view.findViewById(R.id.timeStamp)

    }
}