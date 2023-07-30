package ge.rmenagharishvili.messenger.user

data class Friend(
    val nickname: String? = null,
    var occupation: String? = null,
    val last_message: String? = null,
    val last_message_time: Long? = null,
    var uid: String? = null,
)