package com.summermessenger.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summermessenger.databinding.ActivityLoginBinding
import com.summermessenger.ui.register.RegisterActivity
import com.summermessenger.util.ext.afterTextChanged

class LoginActivity : AppCompatActivity() {
//    private val getData = GetData();

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mViewModel: LoginViewModel

    private lateinit var username:EditText
    private lateinit var password:EditText
    private lateinit var login:Button
    private lateinit var loading:ProgressBar

    private val mTelActivityResultHandler = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != RESULT_OK) {
            return@registerForActivityResult
        }
        // Закрити активіті входу
        setResult(RESULT_OK)
        finish()
    }

    private val mRegisterActivityResultHandler = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != RESULT_OK ) {
            return@registerForActivityResult
        }
        // Закрити активіті входу
        setResult(RESULT_OK)
        finish()
    }

    private val mLoginFormStateObserver = Observer<LoginFormState> {
        val loginState = it ?: return@Observer

        // disable login button unless both username / password is valid
        login.isEnabled = loginState.isDataValid

        if (loginState.usernameError != null) {
            username.error = getString(loginState.usernameError)
        }
        if (loginState.passwordError != null) {
            password.error = getString(loginState.passwordError)
        }
    }

    private val mLoginResultObserver = Observer<LoginResult> {
        val loginResult = it ?: return@Observer
        loading.visibility = View.GONE
        if (loginResult.error != null) {
            showLoginFailed(loginResult.error)
            return@Observer
        }
        if (loginResult.user != null) {
            updateUiWithUser(loginResult)
            setResult(Activity.RESULT_OK)
            //Complete and destroy login activity once successful
            finish()
        }
    }

    private fun startLogin() {
        mViewModel.login(username.text.toString(), password.text.toString())
    }

    private fun updateUiWithUser(model: LoginResult) {
        if (model.loginState != ELoginState.LoggedIn)
            return
        val welcome = "Welcome, "
        val displayName = model.user?.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
                applicationContext,
                "$welcome $displayName",
                Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun goToTelLoginPage() {
        val i = Intent(this, TelLoginActivity::class.java)
        mTelActivityResultHandler.launch(i)
    }

    private fun goToRegisterPage() {
        val i = Intent(this, RegisterActivity::class.java)
        mRegisterActivityResultHandler.launch(i)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
//        lifecycle.addObserver();
        setContentView(mBinding.root)

        username = mBinding.username
        password = mBinding.password
        login = mBinding.login
        loading = mBinding.loading

        mViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        username.afterTextChanged {
            mViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                mViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        startLogin()
                }
                false
            }
        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            startLogin()
        }

        // Натискання "Увійти за ном. телефону"
        mBinding.lblLoginTel.setOnClickListener {
            goToTelLoginPage()
        }
        // Натискання "Зареєструватися"
        mBinding.lblRegister.setOnClickListener {
            goToRegisterPage()
        }
    }

    override fun onStart() {
        super.onStart()
        mViewModel.loginFormState.observe(this, mLoginFormStateObserver)
        mViewModel.loginResult.observe(this, mLoginResultObserver)
    }

    override fun onStop() {
        super.onStop()
        mViewModel.loginFormState.removeObserver(mLoginFormStateObserver)
        mViewModel.loginResult.removeObserver(mLoginResultObserver)
    }
}