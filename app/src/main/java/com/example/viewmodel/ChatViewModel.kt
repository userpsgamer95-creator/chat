package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.model.ChatMessage
import com.example.model.Conversation
import com.example.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("secure_chat_prefs", Context.MODE_PRIVATE)

    private val _registeredName = MutableStateFlow<String?>(
        prefs.getString("user_name", null)
    )
    val registeredName: StateFlow<String?> = _registeredName.asStateFlow()

    private val _registeredPfp = MutableStateFlow<String?>(
        prefs.getString("user_pfp", null)
    )
    val registeredPfp: StateFlow<String?> = _registeredPfp.asStateFlow()

    private val _userStatusText = MutableStateFlow(
        prefs.getString("user_status", "Active & Encrypted") ?: "Active & Encrypted"
    )
    val userStatusText: StateFlow<String> = _userStatusText.asStateFlow()

    private val _chatWallpaper = MutableStateFlow(
        prefs.getInt("chat_wallpaper_id", 0) // 0: Classic, 1: Golden Sand, 2: Dark Space, 3: Cyber Mint
    )
    val chatWallpaper: StateFlow<Int> = _chatWallpaper.asStateFlow()

    // Current User representation
    val currentUserFlow: StateFlow<User> = MutableStateFlow(
        User(
            id = "me",
            name = _registeredName.value ?: "You",
            isOnline = true,
            statusText = _userStatusText.value,
            avatarUrl = _registeredPfp.value
        )
    ).asStateFlow()

    fun registerUser(name: String, status: String = "Active & Encrypted", pfpUri: String? = null) {
        prefs.edit().apply {
            putString("user_name", name)
            putString("user_status", status)
            putString("user_pfp", pfpUri)
            apply()
        }
        _registeredName.value = name
        _userStatusText.value = status
        _registeredPfp.value = pfpUri
    }

    fun setWallpaper(id: Int) {
        prefs.edit().putInt("chat_wallpaper_id", id).apply()
        _chatWallpaper.value = id
    }

    fun clearProfile() {
        prefs.edit().clear().apply()
        _registeredName.value = null
        _registeredPfp.value = null
    }

    private val _onlineUsers = MutableStateFlow<List<User>>(
        listOf(
            User(id = "u1", name = "Alice Core", isOnline = true, statusText = "Coding in Kotlin 🚀", avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=200&auto=format&fit=crop"),
            User(id = "u2", name = "Bob Builder", isOnline = true, statusText = "Design is where science meets art 🎨", avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=200&auto=format&fit=crop"),
            User(id = "u3", name = "Charlie Crypt", isOnline = true, statusText = "Securing nodes... 🔐", avatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?q=80&w=200&auto=format&fit=crop"),
            User(id = "u4", name = "Diana Photo", isOnline = false, statusText = "Taking aesthetic photos 📸", avatarUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=200&auto=format&fit=crop")
        )
    )
    val onlineUsers: StateFlow<List<User>> = _onlineUsers.asStateFlow()

    private val _privateChats = MutableStateFlow<List<Conversation>>(
        listOf(
            Conversation(
                user = User(id = "u1", name = "Alice Core", isOnline = true, statusText = "Coding in Kotlin 🚀", avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=200&auto=format&fit=crop"),
                messages = listOf(
                    ChatMessage(senderId = "u1", text = "Hey there! Love the end-to-end encryption setup. Everything compiles cleanly! ✨", timestamp = System.currentTimeMillis() - 360000)
                )
            ),
            Conversation(
                user = User(id = "u2", name = "Bob Builder", isOnline = true, statusText = "Design is where science meets art 🎨", avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=200&auto=format&fit=crop"),
                messages = listOf(
                    ChatMessage(senderId = "u2", text = "This WhatsApp design inspiration is super responsive. The animations are beautiful!", timestamp = System.currentTimeMillis() - 720000)
                )
            ),
            Conversation(
                user = User(id = "u3", name = "Charlie Crypt", isOnline = true, statusText = "Securing nodes... 🔐", avatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?q=80&w=200&auto=format&fit=crop"),
                messages = listOf(
                    ChatMessage(senderId = "u3", text = "Private keys generated successfully locally. Zero-knowledge authentication is awesome.", timestamp = System.currentTimeMillis() - 1080000)
                )
            )
        )
    )
    val privateChats: StateFlow<List<Conversation>> = _privateChats.asStateFlow()

    private val _publicMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(senderId = "u1", text = "Welcome to the Secured Public Room! Feel free to share messages and cool high-res photos securely! 🌟", timestamp = System.currentTimeMillis() - 1800000),
            ChatMessage(senderId = "u2", text = "Yes! Tap the Camera/Gallery icon to share beautiful photos from your camera roll or pick from our premium templates. 📸✨", timestamp = System.currentTimeMillis() - 1200000)
        )
    )
    val publicMessages: StateFlow<List<ChatMessage>> = _publicMessages.asStateFlow()

    fun sendPublicMessage(text: String, imageUri: String? = null) {
        val newMessage = ChatMessage(
            senderId = "me",
            text = text,
            imageUri = imageUri
        )
        _publicMessages.update { it + newMessage }

        // Simulated replies
        if (imageUri != null) {
            simulateReply(isPublic = true, query = "photo")
        } else {
            simulateReply(isPublic = true, query = text)
        }
    }

    fun sendPrivateMessage(conversationId: String, text: String, imageUri: String? = null) {
        val newMessage = ChatMessage(
            senderId = "me",
            text = text,
            imageUri = imageUri
        )
        _privateChats.update { chats ->
            chats.map { chat ->
                if (chat.id == conversationId) {
                    chat.copy(messages = chat.messages + newMessage)
                } else chat
            }
        }

        // Simulated reply
        if (imageUri != null) {
            simulateReply(isPublic = false, conversationId = conversationId, query = "photo")
        } else {
            simulateReply(isPublic = false, conversationId = conversationId, query = text)
        }
    }

    private fun simulateReply(isPublic: Boolean, conversationId: String? = null, query: String) {
        val replyText = when {
            query.lowercase().contains("photo") || query.lowercase().contains("image") -> {
                "Wow! That looks absolutely stunning. The high-resolution photo rendered perfectly under end-to-end encryption. 📸💎"
            }
            query.lowercase().contains("hello") || query.lowercase().contains("hey") -> {
                "Hello! How is everything? Nice to connect securely."
            }
            else -> {
                "Incredible! SecureChat and WhatsApp wallpapers look super aesthetic! Let's build something beautiful. 🚀💎"
            }
        }

        val responder = if (isPublic) {
            _onlineUsers.value.randomOrNull() ?: User(name = "Peer")
        } else {
            _privateChats.value.find { it.id == conversationId }?.user ?: User(name = "Partner")
        }

        val botMessage = ChatMessage(
            senderId = responder.id,
            text = replyText,
            timestamp = System.currentTimeMillis()
        )

        // Delay is simulated in UI or direct post
        if (isPublic) {
            _publicMessages.update { it + botMessage }
        } else if (conversationId != null) {
            _privateChats.update { chats ->
                chats.map { chat ->
                    if (chat.id == conversationId) {
                        chat.copy(messages = chat.messages + botMessage)
                    } else chat
                }
            }
        }
    }
}
