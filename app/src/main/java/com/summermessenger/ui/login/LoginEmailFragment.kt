package com.summermessenger.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summermessenger.databinding.FragmentLoginEmailBinding
import com.summermessenger.util.ext.afterTextChanged

class LoginEmailFragment : Fragment() {
    private lateinit var mBinding: FragmentLoginEmailBinding
    private lateinit var mViewModel: LoginViewModel

    // Отримано результат перевірки форми
    private val mLoginFormStateObserver = Observer<LoginFormState> {
        val loginState = it ?: return@Observer

        // disable login button unless both username / password is valid
        mBinding.login.isEnabled = loginState.isDataValid

        if (loginState.usernameError != null) {
            mBinding.username.error = getString(loginState.usernameError)
        }
        if (loginState.passwordError != null) {
            mBinding.password.error = getString(loginState.passwordError)
        }
    }

    private val mLoginResultObserver = Observer<LoginResult> {
        val loginResult = it ?: return@Observer
        mBinding.loading.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Ініціалізувати об'єкт прив'язки
        mBinding = FragmentLoginEmailBinding.inflate(inflater)
        // Ініціалізувати 'ViewModel'
        mViewModel = ViewModelProvider(activity as AppCompatActivity, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        // Поле "Ел. пошта"
        mBinding.username.afterTextChanged {
            // Перевірка полів після їхнього редагування
            mViewModel.loginDataChanged(
                mBinding.username.text.toString(),
                mBinding.password.text.toString()
            )
        }

        // Поле "Пароль"
        mBinding.password.apply {
            // Перевірка полів після їхнього редагування
            afterTextChanged {
                mBinding.username.text.toString()
                mBinding.password.text.toString()
            }
            // Кнопка "Увійти" на клавіатурі
            setOnEditorActionListener { v, actionId, event ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        startLogin()
                }
                false
            }
        }

        // Кнопка "Увійти"
        mBinding.login.setOnClickListener {
            startLogin()
        }

        // "Увійти за номером телефону"
        mBinding.lblLoginTel.setOnClickListener {
            mViewModel.showLoginFragment(ELoginFragment.TelLoginFragment)
        }

        // "Зареєструватися"
        mBinding.lblRegister.setOnClickListener {
            mViewModel.requestRegistrationActivity()
        }

        // Повернути 'View' фрагменту
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        mViewModel.loginFormState.observe(this, mLoginFormStateObserver)
        mViewModel.loginResult.observe(this, mLoginResultObserver)
    }

    private fun startLogin() {
        mBinding.loading.visibility = View.VISIBLE
        mViewModel.login(mBinding.username.text.toString(), mBinding.password.text.toString())
    }
}