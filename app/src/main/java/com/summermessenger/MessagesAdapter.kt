package com.summermessenger

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessagesAdapter(msgList: List<Message>, context: Context)
    : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {
    private val _msgList = msgList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_message_fragment, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return _msgList.size;
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.sender.text = _msgList[position].sender.displayName
        holder.messageText.text = _msgList[position].text
        holder.messageTime.text = _msgList[position].timeStamp
        if (MainActivity.MockUsers.indexOf(_msgList[position].sender) % 2 == 0)
            ;
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sender: TextView = view.findViewById(R.id.sender)
        val messageText: TextView = view.findViewById(R.id.messageText)
        val messageTime: TextView = view.findViewById(R.id.timeStamp)

    }
}