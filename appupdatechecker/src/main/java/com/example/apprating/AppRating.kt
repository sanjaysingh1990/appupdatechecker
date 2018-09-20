package com.example.apprating

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.example.appupdatechecker.R


class AppRating : DialogFragment() {
   override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        return AlertDialog.Builder(context!!)
                .setTitle(R.string.rate_title)
                .setMessage(R.string.rate_message)
                .setPositiveButton(R.string.rate_positive) { _, _ ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context?.packageName)))
                    getSharedPreferences(context!!).edit().putBoolean(DISABLED, true).apply()
                    dismiss()
                }
                .setNeutralButton(R.string.rate_remind_later, { _, _ -> dismiss() })
                .setNegativeButton(R.string.rate_never) { _, _ ->
                    getSharedPreferences(context!!).edit().putBoolean(DISABLED, true).apply()
                    dismiss()
                }.create()
    }
    companion object {
        private val LAUNCHES_UNTIL_PROMPT = 10
        private val DAYS_UNTIL_PROMPT = 3
        private val MILLIS_UNTIL_PROMPT = DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000
        private val PREF_NAME = "APP_RATER"
        private val LAST_PROMPT = "LAST_PROMPT"
        private val LAUNCHES = "LAUNCHES"
        private val DISABLED = "DISABLED"

        fun show(context: Context?) {
            var shouldShow = false
            val sharedPreferences = getSharedPreferences(context!!)
            val editor = sharedPreferences.edit()
            val currentTime = System.currentTimeMillis()
            var lastPromptTime = sharedPreferences.getLong(LAST_PROMPT, 0)
            if (lastPromptTime == 0L) {
                lastPromptTime = currentTime
                editor.putLong(LAST_PROMPT, lastPromptTime)
            }

            if (!sharedPreferences.getBoolean(DISABLED, false)) {
                val launches = sharedPreferences.getInt(LAUNCHES, 0) + 1
                if (launches > LAUNCHES_UNTIL_PROMPT) {
                    if (currentTime > lastPromptTime + MILLIS_UNTIL_PROMPT) {
                        shouldShow = true
                    }
                }
                editor.putInt(LAUNCHES, launches)
            }

            if (shouldShow) {
                editor.putInt(LAUNCHES, 0).putLong(LAST_PROMPT, System.currentTimeMillis()).commit()
                val activity = context as AppCompatActivity
                AppRating().show(activity.supportFragmentManager,"")
            } else {
                editor.commit()
            }
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, 0)
        }
    }
}

