package com.summermessenger.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summermessenger.databinding.ActivityTelLoginBinding
import com.summermessenger.util.ext.afterTextChanged

class LoginTelFragment : Fragment() {
    private lateinit var mViewModel: LoginViewModel
    private lateinit var mBinding: ActivityTelLoginBinding

    private val mTelLoginFormStateObserver = Observer<TelLoginFormState> {
        val loginState = it ?: return@Observer

        mBinding.btnRequestCode.isEnabled = loginState.isPhoneNumValid
        // disable login button unless both username / password is valid
        mBinding.btnLoginTel.isEnabled = loginState.isMsgCodeValid

        if (loginState.phoneNumError != null) {
            mBinding.phoneNumber.error = getString(loginState.phoneNumError)
        }
        if (loginState.msgCodeError != null) {
            mBinding.msgCode.error = getString(loginState.msgCodeError)
        }
    }

    private val mTelLoginResultObserver = Observer<LoginResult> {
        mBinding.loading.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = ActivityTelLoginBinding.inflate(layoutInflater)
        mViewModel = ViewModelProvider(requireActivity(), LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        mBinding.phoneNumber.afterTextChanged {
            mViewModel.telLoginDataChanged(
                mBinding.phoneNumber.text.toString(),
                mBinding.msgCode.text.toString()
            )
        }

        mBinding.btnRequestCode.setOnClickListener {
            mBinding.phoneNumber.isEnabled = false
            mViewModel.requestMsgCode(mBinding.phoneNumber.text.toString(), requireActivity())
            mBinding.msgCode.visibility = View.VISIBLE
            mBinding.btnLoginTel.visibility = View.VISIBLE
            //loading.visibility = View.VISIBLE
        }

        mBinding.btnLoginTel.setOnClickListener {
            mViewModel.verifyMsgCode(mBinding.msgCode.text.toString())
        }

        mBinding.msgCode.apply {
            afterTextChanged {
                mViewModel.telLoginDataChanged(
                    mBinding.phoneNumber.text.toString(),
                    mBinding.msgCode.text.toString()
                )
            }
        }

        // Повернути 'View' фрагмента
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        mViewModel.telLoginFormState.observe(viewLifecycleOwner, mTelLoginFormStateObserver)
        mViewModel.loginResult.observe(viewLifecycleOwner, mTelLoginResultObserver)
    }
}
