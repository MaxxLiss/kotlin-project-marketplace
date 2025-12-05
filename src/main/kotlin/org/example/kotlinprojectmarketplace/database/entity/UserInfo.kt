package org.example.kotlinprojectmarketplace.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "user_info")
data class UserInfo(
    @Id
    @Column(name = "id", nullable = false, unique = true)
    val id: Int = 0,

    @Column(name = "name", length = 30, nullable = false)
    val name: String = "",

    @Column(name = "last_name", length = 30, nullable = false)
    val lastName: String = "",

    @Column(name = "email", length = 60, unique = true)
    val email: String? = null,

    @Column(name = "phone", length = 30, unique = true)
    val phone: String? = null,

    @Column(name = "cash", nullable = false)
    val cash: Int = 0,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)