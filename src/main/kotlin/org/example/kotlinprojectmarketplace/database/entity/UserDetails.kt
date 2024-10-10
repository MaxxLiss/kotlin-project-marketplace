package org.example.kotlinprojectmarketplace.database.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_details")
data class UserDetails (
    @Id
    @GeneratedValue
    val id: Int? = null,

    @Column(name = "login", length = 30, nullable = false, unique = true)
    val login: String = "",

    @Column(name = "password", length = 255, nullable = false)
    val password: String = "",

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "seller", nullable = false)
    val seller: Boolean = false,

    @Column(name = "admin", nullable = false)
    val admin: Boolean = false,
)