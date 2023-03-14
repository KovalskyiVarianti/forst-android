package com.example.forst_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.forst_android.ui.main.MainFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        Timber.d("Hello from ${this.localClassName}")
    }
}