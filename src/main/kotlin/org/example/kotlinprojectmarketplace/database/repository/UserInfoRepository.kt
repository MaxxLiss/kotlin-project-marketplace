package org.example.kotlinprojectmarketplace.database.repository

import org.example.kotlinprojectmarketplace.database.entity.UserInfo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserInfoRepository: CrudRepository<UserInfo, Int> {
    fun findByEmail(email: String): Optional<UserInfo>
    fun findByPhone(phone: String): Optional<UserInfo>
}