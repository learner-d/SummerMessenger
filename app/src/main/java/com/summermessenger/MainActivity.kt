package com.summermessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.summermessenger.ui.login.LoginActivity
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        val MockUsers = arrayOf(
            User("real_doer", "Dmytro"),
            User("nillado", "Albert")
        );
    }

    val _messages = arrayListOf(
        Message(MockUsers.find { user -> user.id == "real_doer" }!!, "Привіт!", "19:43"),
        Message(MockUsers.find { user -> user.id == "nillado" }!!, "Здоров, друже!", "19:44"),
        Message(MockUsers.find { user -> user.id == "nillado" }!!, "оаоаоаоа", "19:44"),
        Message(
            MockUsers.find { user -> user.id == "real_doer" }!!,
            "Опа, опа! Бім, бом!",
            "19:45"
        )
    );
    private lateinit var _messagesRV: RecyclerView
    private lateinit var _multiText: EditText
    private lateinit var _sendBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(true && false){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        _messagesRV = findViewById(R.id.message_list)
        _messagesRV.layoutManager = LinearLayoutManager(this)
        _messagesRV.adapter = MessagesAdapter(_messages, this)

        _multiText = findViewById(R.id.message_send)
        _sendBtn = findViewById(R.id.send_button)
        _sendBtn.setOnClickListener { v ->
            val sender = MockUsers.random()
            val msgText = _multiText.text.toString()
            val currentTime = Calendar.getInstance().time
            val newMessage = Message(sender, msgText, SimpleDateFormat("HH:MM").format(currentTime));
            _messages.add(newMessage)
            (_messagesRV.adapter as MessagesAdapter).notifyDataSetChanged()
        }

    }
}