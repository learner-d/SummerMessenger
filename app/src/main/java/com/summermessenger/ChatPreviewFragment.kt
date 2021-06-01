package com.summermessenger

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ChatPreviewFragment : Fragment() {

    companion object {
        fun newInstance() = ChatPreviewFragment()
    }

    private lateinit var viewModel: ChatPreviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_preview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatPreviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}