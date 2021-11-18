package com.summermessenger.ui.register

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summermessenger.data.model.User
import com.summermessenger.databinding.ActivityRegisterBinding
import com.summermessenger.util.ext.replaceFragment
import com.summermessenger.util.ext.showToast

enum class ERegisterAction { None, RegisterWithEmail, FillUserData }
class RegisterActivity : AppCompatActivity() {
    companion object {
        const val PARAM_REGISTER_ACTION = "PARAM_REGISTER_ACTION"
        const val PARAM_REGISTERING_USER = "PARAM_REGISTERING_USER"
        const val RESULT_REGISTERED_USER = "RESULT_REGISTERED_USER"
    }

    private lateinit var mViewModel: RegisterViewModel
    private lateinit var mBinding: ActivityRegisterBinding
    // Відстежувач результату реєстрації
    private val mRegisterResultObserver = Observer<RegisterResult> {
        if (it.error != null) {
            // показати помилку
            Toast.makeText(this, "Помилка реєстрації :/ ${it.error}", Toast.LENGTH_LONG).show()
            Log.e(TAG, it.error.toString())
            return@Observer
        }
        if (it.registerState == ERegisterState.NeedDataFilling) { // користувача створено
            if (it.user != null) {
                showUserDataFormFragment() // показати форму заповнення даних
                return@Observer
            }
        }
        else if (it.registerState == ERegisterState.Registered && it.user != null) { // реєстрацію завершено
            // успішна реєстрація
            Toast.makeText(this, "Успішно зареєстровано !", Toast.LENGTH_LONG).show()
            // Повернути інформацію про зареєстрованого користувача
            setResult(RESULT_OK, Intent().apply { putExtra(RESULT_REGISTERED_USER, it.user) })
            //Complete and destroy login activity once successful
            finish()
            return@Observer
        }
        // TODO: видалити користувача з Firebase, якщо не вдалося завершити реєстрацію
        // Непередбачуваний результат
        throw IllegalStateException()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        mViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        // Отримати дію реєстрації
        val registerActionParam = intent.extras?.get(PARAM_REGISTER_ACTION)
        val registerAction = if (registerActionParam != null)
            registerActionParam as ERegisterAction
        else
            ERegisterAction.RegisterWithEmail

        when (registerAction) {
            ERegisterAction.RegisterWithEmail -> {
                showRegisterEmailFragment()
            }
            ERegisterAction.FillUserData -> {
                val registeringUser = intent.extras?.get(PARAM_REGISTERING_USER) as User?
                if (registeringUser != null) {
                    mViewModel.requestUserDataFilling(registeringUser)
                }
                else {
                    showToast("'${PARAM_REGISTERING_USER}' is null")
                    finish()
                    return
                }
            }
            else -> {}
        }
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        mViewModel.registerResult.observe(this, mRegisterResultObserver)
    }

    private fun showRegisterEmailFragment() {
        replaceFragment(mBinding.fvRegisterMainContent, RegisterEmailFragment(), false)
    }

    private fun showUserDataFormFragment() {
        replaceFragment(mBinding.fvRegisterMainContent, RegisterUserDataFormFragment(), false)
    }
}