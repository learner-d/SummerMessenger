package com.summermessenger.ui.register

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.summermessenger.databinding.ActivityRegisterBinding
import com.summermessenger.util.ext.replaceFragment

class RegisterActivity : AppCompatActivity() {
    private lateinit var mViewModel: RegisterViewModel
    private lateinit var mBinding: ActivityRegisterBinding
    // Відстежувач результату реєстрації
    private val mRegisterResultObserver = Observer<RegisterResult> {
        val registerResult = it
        if (registerResult.error != null) {
            // показати помилку
            Toast.makeText(this, "Помилка реєстрації :/ ${registerResult.error}", Toast.LENGTH_LONG).show()
            Log.e(TAG, registerResult.error.toString())
            return@Observer
        }
        if (registerResult.registerState == ERegisterState.CreatedAccount) { // користувача створено
            if (registerResult.success != null) {
                showUserDataFormFragment() // показати форму заповнення даних
                return@Observer
            }
        }
        else if (registerResult.registerState == ERegisterState.Registered) { // реєстрацію завершено
            // успішна реєстрація
            Toast.makeText(this, "Успішно зареєстровано !", Toast.LENGTH_LONG).show()
            setResult(RESULT_OK)
            //Complete and destroy login activity once successful
            finish()
            return@Observer
        }
        // TODO: видалити користувача з Firebase, якщо не вдалося завершити реєстрацію
        // Непередбачуваний результат
        throw IllegalStateException()
    }
    var mResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        // показати фрагмент "емаіл і пароль"
        showRegisterEmailFragment()

        setContentView(mBinding.root)

        mViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        mViewModel.registerResult.observe(this, mRegisterResultObserver)
    }

    override fun onStop() {
        super.onStop()
        mViewModel.registerResult.removeObserver(mRegisterResultObserver)
    }

    private fun showRegisterEmailFragment() {
        replaceFragment(mBinding.fvRegisterMainContent, RegisterEmailFragment(), false)
    }

    private fun showUserDataFormFragment() {
        replaceFragment(mBinding.fvRegisterMainContent, RegisterUserDataFormFragment(), false)
    }
}