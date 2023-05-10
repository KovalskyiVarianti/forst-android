package com.example.forst_android.common.data.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

enum class RealtimeRoot(val value: String) {
    USERS("users"),
    CLUSTERS("clusters"),
    MEMBERS("members"),
    CHATS("chats"),
    MESSAGES("messages"),
    TOP_MESSAGE_ID("topMessageId"),
    LOCATIONS("locations"),
    NAME("name"),
    EVENTS("events"),
    GROUPS("groups"),
    GROUPS_MESSAGES("groups_messages")
}

fun FirebaseDatabase.getReference(root: RealtimeRoot) = getReference(root.value)

fun DatabaseReference.child(root: RealtimeRoot) = child(root.value)