package com.summermessenger.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summermessenger.data.model.User
import com.summermessenger.databinding.ActivityLoginBinding
import com.summermessenger.ui.register.ERegisterAction
import com.summermessenger.ui.register.RegisterActivity
import com.summermessenger.util.ext.replaceFragment
import com.summermessenger.util.ext.showToast

class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mViewModel: LoginViewModel

    private val mRegisterActivityResultHandler = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        // TODO: обробити результат реєстрації
        if (it.resultCode != RESULT_OK) {
            // TODO: improve safety
            // Перезапустити процес завершення реєстрації
            if (mViewModel.loginResult.value?.loginState == ELoginState.NeedToCompleteRegistration
                    && mViewModel.loginResult.value?.user != null)
                invokeRegistration(ERegisterAction.FillUserData, mViewModel.loginResult.value?.user)

            //TODO: перезапустити процес входу при провалі
        }
        else {
            val registeredUser = it.data?.extras?.get(RegisterActivity.RESULT_REGISTERED_USER) as User?
            if (registeredUser == null) {
                showToast("registeredUser = null")
                return@registerForActivityResult
            }
            mViewModel.addLoggedInUser(registeredUser, true)
        }
    }

    private val mLoginResultObserver = Observer<LoginResult> {
        val loginResult = it ?: return@Observer

        if (loginResult.errorStrId != null) {
            showLoginFailed(getString(loginResult.errorStrId))
            return@Observer
        }
        else if (loginResult.errorStr != null) {
            showLoginFailed(loginResult.errorStr)
            return@Observer
        }
        when (loginResult.loginState) {
            ELoginState.NeedToRegister -> {
                invokeRegistration(ERegisterAction.RegisterWithEmail)
                return@Observer
            }
            ELoginState.NeedToCompleteRegistration -> {
                invokeRegistration(ERegisterAction.FillUserData, loginResult.user)
                return@Observer
            }
            ELoginState.TelCodeSent -> {
                return@Observer
            }
            ELoginState.LoggedIn -> {
                if (loginResult.user != null) {
                    updateUiWithUser(loginResult)

                    setResult(Activity.RESULT_OK)
                    //Complete and destroy login activity once successful
                    finish()
                    return@Observer
                }
            }
            ELoginState.LoggedOut -> {
                return@Observer
            }
            else -> {}
        }

        throw IllegalStateException("Unpredicted behaviour")
    }

    private val mShowingLoginFragmentObserver = Observer<ELoginFragment> {
        when (it) {
            ELoginFragment.EmailLoginFragment -> showLoginEmailFragment()
            ELoginFragment.TelLoginFragment -> showLoginTelFragment()
            else -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        mViewModel.loginResult.observe(this, mLoginResultObserver)
        mViewModel.showingLoginFragment.observe(this, mShowingLoginFragmentObserver)
    }

    private fun updateUiWithUser(model: LoginResult) {
        if (model.loginState != ELoginState.LoggedIn)
            return
        val welcome = "Welcome, "
        val displayName = model.user?.displayName
        // TODO : initiate successful logged in experience
        showToast("$welcome $displayName", Toast.LENGTH_LONG)
    }

    private fun showLoginFailed(errorString: String) {
        showToast(errorString, Toast.LENGTH_SHORT)
    }

    private fun invokeRegistration(registerAction: ERegisterAction, user: User? = null) {
        val registerIntent = Intent(applicationContext, RegisterActivity::class.java)
        if (!(user == null && registerAction == ERegisterAction.FillUserData))
            registerIntent.putExtra(RegisterActivity.PARAM_REGISTER_ACTION, registerAction)
        if (user != null)
            registerIntent.putExtra(RegisterActivity.PARAM_REGISTERING_USER, user)
        mRegisterActivityResultHandler.launch(registerIntent)
    }

    private fun showLoginEmailFragment() {
        replaceFragment(mBinding.fcvLoginContainerMain, LoginEmailFragment())
    }

    private fun showLoginTelFragment() {
        replaceFragment(mBinding.fcvLoginContainerMain, LoginTelFragment())
    }
}