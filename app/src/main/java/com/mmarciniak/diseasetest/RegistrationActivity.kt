package com.mmarciniak.diseasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.register_view.*

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_view)

        register_button.setOnClickListener {
            if (email_input.text.trim().toString().isNotEmpty() && password_input.text.trim()
                    .toString().isNotEmpty()
            ) {

            }
            else {

            }
        }
    }
}