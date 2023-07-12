package ge.rmenagharishvili.messenger

import android.content.Context
import android.widget.Toast

private const val PASS_MIN_LENGTH = 6
private const val MESSAGE_INVALID_PASS = "input at least "+ PASS_MIN_LENGTH + "chars for password"
private const val MESSAGE_INVALID_NICKNAME = "you must have a nickname"
private const val MESSAGE_INVALID_OCCUPATION = "you must have a job"

fun fastToast(context: Context, message: String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    println(message)
}

fun getMail(nickname: String): String {
    return "$nickname@coldmail.com"
}

fun validFields(context: Context, nickname: String, pass: String, occupation: String?): Boolean{
    if(nickname.isEmpty()){
        fastToast(context, MESSAGE_INVALID_NICKNAME)
        return false
    }
    if(pass.length < PASS_MIN_LENGTH){
        fastToast(context,MESSAGE_INVALID_PASS)
        return false
    }
    if(occupation != null && occupation.isEmpty()){
        fastToast(context, MESSAGE_INVALID_OCCUPATION)
        return false
    }
    return true
}
