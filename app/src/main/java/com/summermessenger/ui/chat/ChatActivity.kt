package com.summermessenger.ui.chat

//import com.summermessenger.data.LoginDataSource.Companion.MockUsers
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.summermessenger.R
import com.summermessenger.data.model.Chat
import com.summermessenger.data.model.Message
import com.summermessenger.data.repository.MainRepository
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var _inputMethodManager: InputMethodManager

    private lateinit var mChat: Chat
    private val mMessages = ArrayList<Message>()
    private lateinit var mMessagesAdapter: MessagesAdapter

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

            val sender = MainRepository.usersRepository.loggedInUser!!
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
    }

    override fun onStart() {
        super.onStart()
        //Завантажуємо дані з Firebase
        loadData()
    }

    private fun loadData(){
        lifecycleScope.launchWhenStarted {
            mChat = MainRepository.chatsRepository.getChat(1)
            mMessages.addAll(mChat.messages)
            mMessagesAdapter.notifyDataSetChanged()
        }
    }

    private fun hideKeyboard(){
        _inputMethodManager.hideSoftInputFromWindow(_multiText.windowToken, 0)
    }
}