package com.summermessenger.ui.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.summermessenger.ui.login.LoginActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.summermessenger.R
//import com.summermessenger.data.LoginDataSource.Companion.MockUsers
import com.summermessenger.data.MainRepository
import com.summermessenger.data.model.Chat
import com.summermessenger.data.model.Message
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    lateinit var _inputMethodManager: InputMethodManager

    lateinit var mChat: Chat;
    val mMessages = ArrayList<Message>();
    lateinit var mMessagesAdapter: MessagesAdapter;

    private lateinit var _messagesRV: RecyclerView
    private lateinit var _multiText: EditText
    private lateinit var _sendBtn: ImageButton
    private lateinit var _backBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        _inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        _messagesRV = findViewById(R.id.message_list)
        _messagesRV.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        mMessagesAdapter = MessagesAdapter(mMessages, this)
        _messagesRV.adapter = mMessagesAdapter

        _multiText = findViewById(R.id.message_send)
        _sendBtn = findViewById(R.id.send_button)
        _backBtn = findViewById(R.id.back_button_menu)
        _sendBtn.setOnClickListener { v ->
            hideKeyboard()

            val sender = MainRepository.loginRepository.user!!
            val msgText = _multiText.text.toString()
            val currentTime = Calendar.getInstance().time
            val newMessage = Message(sender, msgText, Timestamp(currentTime))
            mMessages.add(newMessage)
            mMessagesAdapter.notifyDataSetChanged()

            _multiText.text.clear()
        }

        _backBtn.setOnClickListener { v ->
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideKeyboard()
        finish()
        //Завантажуємо дані з Firebase
        lifecycleScope.launchWhenStarted {
            loadData()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private suspend fun loadData(){
        mChat = MainRepository.chatsDataSource.loadChat("1")
        mMessages.addAll(mChat.messages)
        mMessagesAdapter.notifyDataSetChanged()
    }

    private fun hideKeyboard(){
        _inputMethodManager.hideSoftInputFromWindow(_multiText.getWindowToken(), 0)
    }
}