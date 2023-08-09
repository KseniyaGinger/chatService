data class Message(val text: String, var deleted: Boolean = false, var red: Boolean = false)
data class Chat(val messages: MutableList<Message> = mutableListOf())
class NoChatException : RuntimeException()

object ChatService {

    private val chats: MutableMap<Int, Chat> = mutableMapOf()
    private val messages: MutableMap<Int, Message> = mutableMapOf()

  fun sendMessage(userId: Int, message: Message) {
        // chats.getOrPut(userId) { Chat() }.messages += message.copy()
        chats.getOrPut(userId, ::Chat).messages += message.copy()
    }


    /* fun lastMessages() = chats.values.map { chat -> chat.messages
    .lastOrNull { !it.deleted }?.text ?: "No messages" } */

    fun lastMessages(): List<String> {
        return chats.values
            .asSequence()
            .map(::getLastMessageText)
            .ifEmpty{throw NoChatException()}
            .toList()
    }

    fun getMessage(userId: Int, count: Int): List<Message> {
        val chat = chats[userId] ?: throw NoChatException()
        // return chat.messages.filter { !it.deleted }.takeLast(count).onEach { it.red = true }
                return chat.messages
            .asSequence()
            .filter(::isNotDeleted)
            .take(count)
            .onEach(::markAsRed)
            .toList()
    }


    fun deleteMessage(messageId: Int, userId: Int) {
        val chat = chats[userId] ?: throw NoChatException()
        val message = chat.messages.getOrNull(messageId)
        //message?.let { it.deleted = true }
        message?.let(::markAsDeleted)
    }


    fun getUnreadChatsCount() =
        chats.values.map { chat -> chat.messages.filter { !it.deleted && !it.red } }.count { it.isNotEmpty() }

    fun getChats() = chats

    fun deleteChat(userId: Int) {
        val chat = chats[userId] ?: throw NoChatException()
        chats.remove(userId)
    }

    fun printChats() = println(chats)



    // добавляю вспомогательные функции, чтобы ссылаться на них вместо написания лямбды
    fun isNotDeleted(message: Message): Boolean {
        return !message.deleted
    }
    fun markAsRed(message: Message) {
        message.red = true
    }
    fun markAsDeleted(message: Message) {
        message.deleted = true
    }
    fun isNotRed(message: Message): Boolean {
        return !message.red
    }
    fun getLastMessageText(chat: Chat): String {
        return chat.messages
            .asSequence()
            .lastOrNull(::isNotDeleted)
            ?.text ?: "No messages"
    }

}

fun main() {

    ChatService.sendMessage(1, Message("Hello", false))
    ChatService.sendMessage(2, Message("Hi", true))
    ChatService.sendMessage(1, Message("How are you?", false))
    ChatService.sendMessage(1, Message("Call me", false))

    ChatService.printChats()

    println(ChatService.lastMessages())

    println(ChatService.getMessage(1, 2))

    ChatService.printChats()

    println(ChatService.getUnreadChatsCount())

    println(ChatService.getChats())

    ChatService.sendMessage(2, Message("good morning", false, false))
    println(ChatService.getChats())

    ChatService.sendMessage(3, Message("new chat", false, false))
    println(ChatService.getChats())

    ChatService.deleteChat(1)
    println(ChatService.getChats())

    ChatService.deleteMessage(0, 3)
    println(ChatService.getChats())

}