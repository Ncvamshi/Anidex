//package com.example.anidex.data
//
//import android.content.Context
//
//class SessionManager(context: Context) {
//    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
//
//    companion object {
//        private const val KEY_CURRENT_USER = "current_user_id"
//        private const val KEY_ALL_USERS = "all_logged_in_users"
//    }
//
//    fun saveUserId(userId: String) {
//        val users = getAllUsers().toMutableSet()
//        users.add(userId)
//
//        prefs.edit()
//            .putString(KEY_CURRENT_USER, userId)
//            .putStringSet(KEY_ALL_USERS, users)
//            .putLong("LOGIN_TIME_$userId", System.currentTimeMillis())
//            .apply()
//    }
//
//    fun getCurrentUserId(): String? = prefs.getString(KEY_CURRENT_USER, null)
//
//    fun getAllUsers(): Set<String> = prefs.getStringSet(KEY_ALL_USERS, emptySet()) ?: emptySet()
//
//    fun switchUser(userId: String) {
//        if (getAllUsers().contains(userId)) {
//            prefs.edit().putString(KEY_CURRENT_USER, userId).apply()
//        }
//    }
//
//    fun logoutCurrent() {
//        val current = getCurrentUserId()
//        val users = getAllUsers().toMutableSet()
//
//        if (current != null) {
//            users.remove(current)
//        }
//
//        val editor = prefs.edit()
//        editor.remove(KEY_CURRENT_USER)
//        editor.putStringSet(KEY_ALL_USERS, users)
//
//        // If there are other users left, set the first one as current
//        if (users.isNotEmpty()) {
//            editor.putString(KEY_CURRENT_USER, users.first())
//        }
//
//        editor.apply()
//    }
//}

package com.example.anidex.data

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CURRENT_USER = "current_user_id"
        private const val KEY_ALL_USERS = "all_logged_in_users"
        private const val PREFIX_NAME = "user_name_"
    }

    fun saveUserId(userId: String, username: String) {
        val users = getAllUsers().toMutableSet()
        users.add(userId)

        prefs.edit()
            .putString(KEY_CURRENT_USER, userId)
            .putStringSet(KEY_ALL_USERS, users)
            .putString("$PREFIX_NAME$userId", username)
            .apply()
    }

    fun getCurrentUserId(): String? = prefs.getString(KEY_CURRENT_USER, null)

    fun getUsername(userId: String): String = prefs.getString("$PREFIX_NAME$userId", "User") ?: "User"

    fun getAllUsers(): Set<String> = prefs.getStringSet(KEY_ALL_USERS, emptySet()) ?: emptySet()

    fun switchUser(userId: String) {
        if (getAllUsers().contains(userId)) {
            prefs.edit().putString(KEY_CURRENT_USER, userId).apply()
        }
    }

    fun logoutCurrent() {
        val current = getCurrentUserId()
        val users = getAllUsers().toMutableSet()

        if (current != null) {
            users.remove(current)
            prefs.edit().remove("$PREFIX_NAME$current").apply()
        }

        val editor = prefs.edit().remove(KEY_CURRENT_USER).putStringSet(KEY_ALL_USERS, users)
        if (users.isNotEmpty()) {
            editor.putString(KEY_CURRENT_USER, users.first())
        }
        editor.apply()
    }
}