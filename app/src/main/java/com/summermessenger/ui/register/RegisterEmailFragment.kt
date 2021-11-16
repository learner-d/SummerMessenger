package com.summermessenger.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.summermessenger.databinding.FragmentRegisterEmailBinding

class RegisterEmailFragment : Fragment() {
    private lateinit var mViewModel: RegisterViewModel
    private lateinit var mBinding: FragmentRegisterEmailBinding
    private lateinit var mEtEmailAddress: EditText
    private lateinit var mEtPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRegisterEmailBinding.inflate(inflater)

        mEtEmailAddress = mBinding.editTextTextEmailAddress
        mEtPassword = mBinding.editTextTextPassword

        mViewModel = ViewModelProvider(activity as AppCompatActivity, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        // натискання "Реєстрація"
        mBinding.btnDoRegister.setOnClickListener {
            // TODO: показати індикатор завантаження
            Toast.makeText(context, "Реєструємось, зачекайте, будь ласка ...", Toast.LENGTH_LONG).show()
            mViewModel.createUser(mEtEmailAddress.text.toString(), mEtPassword.text.toString())
        }
        return mBinding.root
    }
}

