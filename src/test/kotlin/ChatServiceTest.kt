import NoChatException
import org.junit.Assert.*
import org.junit.Before
import NoChatException as NoChatException1

class ChatServiceTest {

    @Before
    fun setUp() {
        ChatService.sendMessage(1, Message("Hello, world!"))
        ChatService.sendMessage(1, Message("How are you?"))
        ChatService.sendMessage(2, Message("Hi!"))
    }

    @org.junit.Test
    fun sendMessage() {
        ChatService.sendMessage(2, Message("Goodbye!"))

        val messages = ChatService.lastMessages()

        assertEquals("Goodbye!", messages[1])
    }

    @org.junit.Test
    fun lastMessages() {
        val message = ChatService.lastMessages()
        assertEquals(message, message)
    }


    @org.junit.Test
    fun getMessage() {
        val message = ChatService.getMessage(2, 1)
        assertEquals(
            Message(text = "good morning", deleted = false, red = true),
            Message(text = "good morning", deleted = false, red = true)
        )
    }


    @org.junit.Test
    fun getUnreadChatsCount() {
        val unreadChatsCount = ChatService.getUnreadChatsCount()

        assertEquals(2, unreadChatsCount)
    }

    @org.junit.Test
    fun getChats() {
        val chats = ChatService.getChats()
        assertEquals(2, chats.size)
    }

    @org.junit.Test
    fun deleteChat() {
        ChatService.deleteChat(1)

        val chats = ChatService.getChats()

        assertEquals(1, chats.size)
        assertFalse(chats.contains(1))
    }
}