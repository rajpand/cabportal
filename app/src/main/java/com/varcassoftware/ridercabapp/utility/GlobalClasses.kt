package com.varcassoftware.ridercabapp.utility



import java.util.Date
import android.content.Context
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

class GlobalClasses {
    companion object {
        fun getDateAndTime(condition: Boolean): Date? {
            return if (condition) {
                Date()
            } else {
                null
            }
        }

        fun getDeviceId(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun setToast(activity: FragmentActivity?, it: String?) {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        }

    }

}