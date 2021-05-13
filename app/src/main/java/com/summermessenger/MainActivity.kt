package com.summermessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val _users = arrayOf(
            User("real_doer", "Dmytro"),
            User("nillado", "Albert")
    );
    private val _messages = arrayOf(
            Message(_users.find { user -> user.id == "real_doer" }!!, "Привіт!", "19:43"),
            Message(_users.find { user -> user.id == "nillado" }!!, "Здоров, друже!", "19:44"),
            Message(_users.find { user -> user.id == "nillado" }!!, "оаоаоаоа", "19:44"),
            Message(_users.find { user -> user.id == "real_doer" }!!, "Опа, опа! Бім, бом!", "19:45")
    );
    private lateinit var _messageStack: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _messageStack = findViewById(R.id.msg_trash)
        readMessages();
    }

    fun readMessages() {
        for (message in _messages){
            _messageStack.append("${message.timeStamp}: ${message.sender.displayName} -> ${message.text}\n");
        }
    }
}