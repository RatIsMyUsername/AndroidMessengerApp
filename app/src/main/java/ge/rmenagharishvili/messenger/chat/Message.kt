package ge.rmenagharishvili.messenger.chat

class Message {
    var messageText: String? = null
    var senderId: String? = null
    var timeMillis: Long? = null

    constructor() {}

    constructor(messageText: String?, senderId: String?, timeMillis: Long?) {
        this.messageText = messageText
        this.senderId = senderId
        this.timeMillis = timeMillis
    }
}