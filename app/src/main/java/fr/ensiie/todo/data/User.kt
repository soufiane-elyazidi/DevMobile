package fr.ensiie.todo.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class User(
    @SerialName("email")
    val email: String,
    @SerialName("full_name")
    val name: String,
    @SerialName("avatar_medium")
    val avatar: String? = null,
    val password: String? = null,
    @SerialName("token")
    val token: String? = null,
) : java.io.Serializable
