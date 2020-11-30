package com.mmarciniak.diseasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()


        register_button.setOnClickListener {
            if (email_input.text.trim().toString().isNotEmpty() && password_input.text.trim()
                    .toString().isNotEmpty()
            ) {
                createUser(email_input.text.trim().toString(),password_input.text.trim().toString())
            }
            else {
                Toast.makeText(this,"You must provide both email and password",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful)
                {
                    runOnUiThread{
                        Toast.makeText(this,"Account registered successfully",Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }
                else {
                    runOnUiThread{
                        Toast.makeText(this,"Account registration failed ${task.exception}",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}