package com.summermessenger.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summermessenger.data.model.User
import com.summermessenger.databinding.FragmentRegisterFormUserdataBinding
import com.summermessenger.util.ext.showToast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 */
// TODO: додати перевірку полів
class RegisterUserDataFormFragment : Fragment() {
    private lateinit var mBinding: FragmentRegisterFormUserdataBinding
    private lateinit var mViewModel: RegisterViewModel

    private var registeringUser: User? = null
    private val mRegisterResultObserver = Observer<RegisterResult> {
        if (it.error == null && it.registerState == ERegisterState.CreatedAccount)
            registeringUser = it.success
        else
            registeringUser = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = FragmentRegisterFormUserdataBinding.inflate(inflater)
        mViewModel = ViewModelProvider(activity as AppCompatActivity, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        // Натискання кнопки "Далі"
        mBinding.btnNext.setOnClickListener {
            if (registeringUser == null) {
                showToast("No user data has found !", Toast.LENGTH_LONG)
                return@setOnClickListener
            }
            //TODO: перевірка полів
            mViewModel.fillUserData(registeringUser!!.userId, mBinding.etDisplayName.text.toString(),
                mBinding.etNickname.text.toString())
        }
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        mViewModel.registerResult.observe(viewLifecycleOwner, mRegisterResultObserver)
    }

    override fun onStop() {
        super.onStop()
        mViewModel.registerResult.removeObserver(mRegisterResultObserver)
    }
}