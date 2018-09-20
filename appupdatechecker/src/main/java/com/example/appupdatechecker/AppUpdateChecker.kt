package com.example.appupdatechecker

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AppUpdateChecker(private var context: Context?) {
    private var mDialog: AlertDialog? = null


    fun loginToFirebase() {


        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInAnonymously().addOnCompleteListener { setAPpUpdateCheckerListener() }

    }

    /**
     ************* check app update ap version update *******************
     */

    fun setAPpUpdateCheckerListener() {


        val mDataRef = FirebaseDatabase.getInstance().getReference().child("versionchecker").child("android")

        mDataRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError?) {
                Log.e("error", error.toString())

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                dataSnapshot?.let {


                    val android = dataSnapshot.getValue(Android::class.java)
                    val latestVersionCode = android?.android_version_code ?: 0
                    // android.util.Log.e("value", latestVersionCode.toString() + "" + Gson().toJson(android))
                    //  Log.e("version",latestVersionCode.toString());
                    if (latestVersionCode > getVersionCode()) {

                        mDialog?.let {

                            showUpdateDilaog(android?.force_update ?: false)
                        } ?: showUpdateDilaog(android?.force_update ?: false)


                    } else {
                        mDialog?.dismiss()

                    }


                }
            }

        })

    }

    /**
     * *********** get app current version code ************
     */
    private fun getVersionCode(): Int {
        return try {
            val pInfo = context?.packageManager?.getPackageInfo(context?.packageName, 0)

            pInfo?.versionCode ?: 0
        } catch (e: PackageManager.NameNotFoundException) {
            0
        }

    }

    /**
     ************* show update dialog **************
     */
    fun showUpdateDilaog(status: Boolean) {

        if(mDialog?.isShowing==true)

        {
            mDialog?.dismiss()
        }

        context?.let {
            // Initialize a new instance of
            val builder = android.support.v7.app.AlertDialog.Builder(it)

            // Set the alert dialog title
            builder.setTitle(it.getString(R.string.update_available))

            // Display a message on alert dialog
            builder.setMessage(R.string.update_app_message)

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("UPDATE") { _, _ ->
                mDialog?.dismiss()
                redirectStore()
            }


            //check is update force or normal
            if (!status) {
                // Display a negative button on alert dialog
                builder.setNegativeButton("LATER") { _, _ ->
                    mDialog?.dismiss()
                }
            }
            else
            {
                builder.setCancelable(false)
            }


            // Finally, make the alert dialog using builder
            mDialog= builder.create()

            // Display the alert dialog on app interface
            mDialog?.show()
        }


    }

    /**
     *************** redirect user to play store to update app***********
     */
    private fun redirectStore() {
        val PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id="+context?.packageName
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }


}