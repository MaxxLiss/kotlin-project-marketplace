package org.example.kotlinprojectmarketplace.database.entity

import jakarta.persistence.*

@Entity
@Table(name = "UserDetails")
data class UserDetails (
    @Id
    @GeneratedValue
    val id: Int? = null,

    @Column(name = "login", length = 30, nullable = false, unique = true)
    val login: String = "",

    @Column(name = "password", length = 255, nullable = false, unique = false)
    val password: String = "",

    @Column(name = "seller", nullable = false, unique = false)
    val seller: Boolean = false,

    @Column(name = "admin", nullable = false, unique = false)
    val admin: Boolean = false,
)