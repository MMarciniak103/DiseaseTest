package com.mmarciniak.diseasetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        test_btn.setOnClickListener {
            val intent =  Intent(this, TestActivity::class.java)
            startActivity(intent)
        }

        stats_btn.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

        test2_btn.setOnClickListener {
            val intent = Intent(this,Test2Activity::class.java)
            startActivity(intent)
        }
    }
}