package com.example.forst_android.common.data.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import timber.log.Timber

class DataEventListener(private val onChange: (snapshot: DataSnapshot) -> Unit) : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        onChange(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {
        Timber.d("New data ${error.message}")
    }
}