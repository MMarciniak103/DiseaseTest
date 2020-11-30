package com.mmarciniak.diseasetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.email_input
import kotlinx.android.synthetic.main.activity_login.password_input
import kotlinx.android.synthetic.main.activity_login.register_button
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        login_button.setOnClickListener {
            if (email_input.text.trim().toString().isNotEmpty() && password_input.text.trim()
                    .toString().isNotEmpty()
            ) {
                login(email_input.text.trim().toString(),password_input.text.trim().toString())
            }
            else {
                Toast.makeText(this,"You must provide both email and password", Toast.LENGTH_SHORT).show()
            }
        }

        register_button.setOnClickListener {
            val intent =  Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }


    private fun login(email: String, password: String){
       auth.signInWithEmailAndPassword(email,password)
           .addOnCompleteListener(this){task ->
               if(task.isSuccessful)
               {
                   val intent =  Intent(this, MainActivity::class.java)
                   startActivity(intent)
               }
               else{
                   Toast.makeText(this,"Login failed, please try again...", Toast.LENGTH_SHORT).show()
               }
           }
    }
}