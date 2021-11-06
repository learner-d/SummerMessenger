package com.summermessenger.ui.chat

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.summermessenger.R
import com.summermessenger.data.model.Message
import com.summermessenger.data.repository.MainRepository
import java.text.SimpleDateFormat
import java.util.*

class MessagesAdapter(private val msgList: List<Message>, val context: Context)
    : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_message_fragment, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.sender.text = msgList[position].sender.displayName
        holder.messageText.text = msgList[position].text
        holder.messageTime.text = timeFormatter.format(msgList[position].timeStamp.toDate())

        // установка позиції "моїх повідомлень
        val msgGravity: Int = if (msgList[position].sender.username == MainRepository.usersRepository.loggedInUser?.username)
            Gravity.TOP or Gravity.END
        else
            Gravity.TOP or Gravity.START
        holder.messageInfoRoot.layoutParams =
            (holder.messageInfoRoot.layoutParams as LinearLayout.LayoutParams)
                .apply { gravity = msgGravity }
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageInfoRoot: LinearLayout = view.findViewById(R.id.message_info_root)
        val sender: TextView = view.findViewById(R.id.sender)
        val messageText: TextView = view.findViewById(R.id.messageText)
        val messageTime: TextView = view.findViewById(R.id.timeStamp)
    }
}