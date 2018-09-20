package com.example.android.myapplicationupdatelibrary

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.appupdatechecker.AppUpdateChecker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialize update checker
        AppUpdateChecker(this).loginToFirebase()
    }
}
