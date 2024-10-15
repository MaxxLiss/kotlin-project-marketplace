package org.example.kotlinprojectmarketplace.database.repository

import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserDetailsRepository: CrudRepository<UserDetails, Int> {

    fun findByLogin(login: String): Optional<UserDetails>
}