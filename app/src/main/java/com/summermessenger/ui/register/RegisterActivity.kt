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
        if (registerResult.success != null) {
            // успішна реєстрація
            Toast.makeText(this, "Успішно зареєстровано !", Toast.LENGTH_LONG).show()
            setResult(RESULT_OK)
            //Complete and destroy login activity once successful
            finish()
            return@Observer
        }
        // Непередбачуваний результат
        throw IllegalStateException()
    }
    var mResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val etEmailAddress = mBinding.editTextTextEmailAddress
        val etPassword = mBinding.editTextTextPassword

        mViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        // натискання "Реєстрація"
        mBinding.btnDoRegister.setOnClickListener {
            // TODO: показати індикатор завантаження
            Toast.makeText(this, "Реєструємось, зачекайте, будь ласка ...", Toast.LENGTH_LONG).show()
            mViewModel.registerUser(etEmailAddress.text.toString(), etPassword.text.toString())
        }
    }
    override fun onStart() {
        super.onStart()
        mViewModel.registerResult.observe(this, mRegisterResultObserver)
    }

    override fun onStop() {
        super.onStop()
        mViewModel.registerResult.removeObserver(mRegisterResultObserver)
    }
}