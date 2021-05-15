package com.summermessenger

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import java.text.SimpleDateFormat
import java.time.Clock
import java.util.*

class MainActivity : AppCompatActivity() {
    private val _users = arrayOf(
            User("real_doer", "Dmytro"),
            User("nillado", "Albert")
    );
    private val _messages = arrayListOf(
            Message(_users.find { user -> user.id == "real_doer" }!!, "Привіт!", "19:43"),
            Message(_users.find { user -> user.id == "nillado" }!!, "Здоров, друже!", "19:44"),
            Message(_users.find { user -> user.id == "nillado" }!!, "оаоаоаоа", "19:44"),
            Message(_users.find { user -> user.id == "real_doer" }!!, "Опа, опа! Бім, бом!", "19:45")
    );
    private lateinit var _messageStack: TextView
    private lateinit var _multiText: EditText
    private lateinit var _sendBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        _messageStack = findViewById(R.id.msg_trash)
        _multiText = findViewById(R.id.message_send)
        _sendBtn = findViewById(R.id.send_button)
        _sendBtn.setOnClickListener { v ->
            val sender = _users.random()
            val msgText = _multiText.text.toString()
            val currentTime = Calendar.getInstance().time
            val newMessage = Message(sender, msgText, SimpleDateFormat("HH:MM").format(currentTime));
            _messages.add(newMessage)
            readMessages()
        }
        readMessages();
    }

    fun readMessages() {
        _messageStack.text = ""
        for (message in _messages){
            _messageStack.append("${message.timeStamp}: ${message.sender.displayName} -> ${message.text}\n");
        }
    }
}