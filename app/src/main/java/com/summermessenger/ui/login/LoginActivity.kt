package com.summermessenger.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.summermessenger.R
import com.summermessenger.util.ext.afterTextChanged
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
//    private val getData = GetData();

    private lateinit var loginViewModel: LoginViewModel

    lateinit var username:EditText
    lateinit var password:EditText
    lateinit var login:Button
    lateinit var loading:ProgressBar

    val loginFormStateObserver = Observer<LoginFormState> {
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

    val loginResultObserver = Observer<LoginResult> {
        val loginResult = it ?: return@Observer
        loading.visibility = View.GONE
        if (loginResult.error != null) {
            showLoginFailed(loginResult.error)
        }
        if (loginResult.success != null) {
            updateUiWithUser(loginResult.success)
            setResult(Activity.RESULT_OK)
            //Complete and destroy login activity once successful
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        lifecycle.addObserver();
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        loading = findViewById(R.id.loading)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
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

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                startLogin()
            }
        }
    }

    fun startLogin() {
        GlobalScope.launch {
            loginViewModel.login(username.text.toString(), password.text.toString())
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = "Welcome, "
        val displayName = model.displayName
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

    val TEL_LOGIN_CODE = 502
    fun goToTelLoginPage_OnClick(view: View) {
        goToTelLoginPage()
    }

    fun goToTelLoginPage(){
        val i = Intent(this, TelLoginActivity::class.java)
        startActivityForResult(i, TEL_LOGIN_CODE)
    }

    override fun onStart() {
        super.onStart()
        loginViewModel.loginFormState.observe(this, loginFormStateObserver)
        loginViewModel.loginResult.observe(this, loginResultObserver)
    }

    override fun onStop() {
        super.onStop()
        loginViewModel.loginFormState.removeObserver(loginFormStateObserver)
        loginViewModel.loginResult.removeObserver(loginResultObserver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TEL_LOGIN_CODE) {
            if (resultCode != RESULT_OK) {
                goToTelLoginPage()
                return
            }
            setResult(RESULT_OK)
            finish()
        }
    }
}