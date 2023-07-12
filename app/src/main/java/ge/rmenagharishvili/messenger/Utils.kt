package ge.rmenagharishvili.messenger

import android.content.Context
import android.widget.Toast

fun fastToast(context: Context, message: String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}