package com.summermessenger.ui.chat.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.summermessenger.R

class ChatMessage : Fragment() {

    companion object {
        fun newInstance() = ChatMessage()
    }

    private lateinit var viewModel: ChatMessageViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chat_message_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatMessageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}