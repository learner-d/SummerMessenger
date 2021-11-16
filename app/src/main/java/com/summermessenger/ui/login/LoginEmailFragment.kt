package com.summermessenger.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summermessenger.databinding.FragmentLoginEmailBinding
import com.summermessenger.ui.register.RegisterActivity
import com.summermessenger.util.ext.afterTextChanged

class LoginEmailFragment : Fragment() {
    private lateinit var mBinding: FragmentLoginEmailBinding
    private lateinit var mViewModel: LoginViewModel

    private val mTelActivityResultHandler = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != AppCompatActivity.RESULT_OK) {
            return@registerForActivityResult
        }

        if (activity == null)
            return@registerForActivityResult

        // Закрити активіті входу
        requireActivity().setResult(AppCompatActivity.RESULT_OK)
        requireActivity().finish()
    }

    private val mRegisterActivityResultHandler = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != AppCompatActivity.RESULT_OK) {
            return@registerForActivityResult
        }
        // Закрити активіті входу
//        setResult(RESULT_OK)
//        finish()
    }

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
        if (loginResult.error != null) {
            showLoginFailed(loginResult.error)
            return@Observer
        }
        if (loginResult.user != null) {
            updateUiWithUser(loginResult)
            if (activity == null)
                return@Observer

            requireActivity().setResult(Activity.RESULT_OK)
            //Complete and destroy login activity once successful
            requireActivity().finish()
        }
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
            goToTelLoginPage()
        }

        // "Зареєструватися"
        mBinding.lblRegister.setOnClickListener {
            goToRegisterPage()
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


    private fun goToTelLoginPage() {
        val i = Intent(activity as AppCompatActivity, TelLoginActivity::class.java)
        mTelActivityResultHandler.launch(i)
    }

    private fun goToRegisterPage() {
        val i = Intent(activity as AppCompatActivity, RegisterActivity::class.java)
        mRegisterActivityResultHandler.launch(i)
    }

    private fun updateUiWithUser(model: LoginResult) {
        if (model.loginState != ELoginState.LoggedIn)
            return
        val welcome = "Welcome, "
        val displayName = model.user?.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            context,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }
}