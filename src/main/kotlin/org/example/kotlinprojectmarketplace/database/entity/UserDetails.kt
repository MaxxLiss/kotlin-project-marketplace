package org.example.kotlinprojectmarketplace.database.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
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
) {
    fun getAuthorities(): MutableSet<GrantedAuthority> = mutableSetOf<GrantedAuthority>(Role.USER).apply {
        if (seller) add(Role.SELLER)
        if (admin) add(Role.ADMIN)
    }
}

enum class Role: GrantedAuthority {
    USER, SELLER, ADMIN;
    override fun getAuthority(): String = name
}
