package ge.rmenagharishvili.messenger

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Toast
import java.util.*

private const val PASS_MIN_LENGTH = 6
private const val MESSAGE_INVALID_PASS = "input at least " + PASS_MIN_LENGTH + "chars for password"
private const val MESSAGE_INVALID_NICKNAME = "you must have a nickname"
private const val MESSAGE_INVALID_OCCUPATION = "you must have a job"

private lateinit var loadingDialog: Dialog

fun fastToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    println(message)
}

fun getMail(nickname: String): String {
    return "$nickname@coldmail.com".replace(" ", "+")
}

fun validFields(context: Context, nickname: String, pass: String, occupation: String?): Boolean {
    if (nickname.isEmpty()) {
        fastToast(context, MESSAGE_INVALID_NICKNAME)
        return false
    }
    if (pass.length < PASS_MIN_LENGTH) {
        fastToast(context, MESSAGE_INVALID_PASS)
        return false
    }
    if (occupation != null && occupation.isEmpty()) {
        fastToast(context, MESSAGE_INVALID_OCCUPATION)
        return false
    }
    return true
}

fun showLoadingProgressBar(context: Context) {
    loadingDialog = Dialog(context)
    loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    loadingDialog.setContentView(R.layout.dialog_wait)
    loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    loadingDialog.setCanceledOnTouchOutside(false)
    loadingDialog.show()
}

fun hideLoadingProgressBar() {
    loadingDialog.dismiss()
}

fun timeInTimezoneMillis(): Long {
    return Calendar.getInstance(TimeZone.getDefault()).timeInMillis
}

fun hourAndMinuteFromMillis(timeInMillis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    return "${
        calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
    }:${calendar.get(Calendar.MINUTE).toString().padStart(2, '0')}"
}
