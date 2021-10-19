package io.github.escapehonbab.jpa.objects

import lombok.Builder
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Table

@Builder
@Table(name = "chatroom")
@Entity
class FriendChatInfo(
    val img: ByteArray,
    val name: String,
    val userId: String,
    val sex: String,
    val age: Int,
    val time: String,
    val user: User
) : Serializable