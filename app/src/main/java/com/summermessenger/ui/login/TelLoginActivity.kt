package com.summermessenger.ui.login

import android.app.Activity
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

class TelLoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tel_login)

        val phoneNumber = findViewById<EditText>(R.id.phone_number)
        val msgCode = findViewById<EditText>(R.id.msg_code)
        val btnRequestCode = findViewById<Button>(R.id.btn_request_code)
        val btnLoginTel = findViewById<Button>(R.id.btn_login_tel)
        val loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.telLoginFormState.observe(
            this,
            Observer {
                val loginState = it ?: return@Observer

                btnRequestCode.isEnabled = loginState.isPhoneNumValid
                // disable login button unless both username / password is valid
                btnLoginTel.isEnabled = loginState.isMsgCodeValid

                if (loginState.phoneNumError != null) {
                    phoneNumber.error = getString(loginState.phoneNumError)
                }
                if (loginState.msgCodeError != null) {
                    msgCode.error = getString(loginState.msgCodeError)
                }
            }
        )

        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        phoneNumber.afterTextChanged {
            loginViewModel.telLoginDataChanged(
                phoneNumber.text.toString(),
                msgCode.text.toString()
            )
        }

        btnRequestCode.setOnClickListener {
            phoneNumber.isEnabled = false
            loginViewModel.applyPhoneNum(phoneNumber.text.toString(), this)
            msgCode.visibility = View.VISIBLE
            btnLoginTel.visibility = View.VISIBLE
            //loading.visibility = View.VISIBLE
        }

        msgCode.apply {
            afterTextChanged {
                loginViewModel.telLoginDataChanged(
                    phoneNumber.text.toString(),
                    msgCode.text.toString()
                )
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
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
}
