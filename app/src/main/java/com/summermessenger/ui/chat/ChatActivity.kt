package com.summermessenger.ui.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.summermessenger.ui.login.LoginActivity
import androidx.recyclerview.widget.RecyclerView
import com.summermessenger.R
import com.summermessenger.data.LoginDataSource.Companion.MockUsers
import com.summermessenger.data.MainRepository
import com.summermessenger.data.model.Message
import java.util.*

class ChatActivity : AppCompatActivity() {
    lateinit var _inputMethodManager: InputMethodManager

    val _messages = arrayListOf(
            Message(MockUsers.find { user -> user.id == "real_doer" }!!, "Привіт!", Calendar.getInstance().time),
            Message(MockUsers.find { user -> user.id == "nillado" }!!, "Здоров, друже!", Calendar.getInstance().time),
            Message(MockUsers.find { user -> user.id == "nillado" }!!, "оаоаоаоа", Calendar.getInstance().time),
            Message(
                    MockUsers.find { user -> user.id == "real_doer" }!!,
                    "Опа, опа! Бім, бом!",
                    Calendar.getInstance().time
            )
    );
    private lateinit var _messagesRV: RecyclerView
    private lateinit var _multiText: EditText
    private lateinit var _sendBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        _inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        _messagesRV = findViewById(R.id.message_list)
        _messagesRV.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        _messagesRV.adapter = MessagesAdapter(_messages, this)

        _multiText = findViewById(R.id.message_send)
        _sendBtn = findViewById(R.id.send_button)
        _sendBtn.setOnClickListener { v ->
            hideKeyboard()

            val sender = MainRepository.loginRepository.user!!
            val msgText = _multiText.text.toString()
            val currentTime = Calendar.getInstance().time
            val newMessage = Message(sender, msgText, currentTime)
            _messages.add(newMessage)
            (_messagesRV.adapter as MessagesAdapter).notifyDataSetChanged()

            _multiText.text.clear()
        }

    }

    private fun hideKeyboard(){
        _inputMethodManager.hideSoftInputFromWindow(_multiText.getWindowToken(), 0)
    }
}