package com.example.android.myapplicationupdatelibrary

import android.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.apprating.AppRating
import com.example.appupdatechecker.AppUpdateChecker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialize update checker
       // AppUpdateChecker(this).loginToFirebase()


        //rating dialog
        AppRating.show(this)

    }
}
