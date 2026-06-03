package com.example.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isOnline: Boolean = true,
    val statusText: String = "Available",
    val avatarUrl: String? = null
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val senderId: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isEncrypted: Boolean = true,
    val imageUri: String? = null
)

data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val user: User, // For 1-on-1 private chats
    val messages: List<ChatMessage> = emptyList(),
    val unreadCount: Int = 0
)
