package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.dto.UserInfoEnrichRequest
import org.example.kotlinprojectmarketplace.database.dto.UserInfoEnrichResponse

interface UserInfoService {
    fun createUserInfo(userInfoEnrichRequest: UserInfoEnrichRequest, id: Int): UserInfoEnrichResponse

    fun updateUserInfo(userInfoEnrichRequest: UserInfoEnrichRequest, id: Int): UserInfoEnrichResponse

    fun getUserInfo(id: Int): UserInfoEnrichResponse

    fun deleteUserInfo(id: Int): UserInfoEnrichResponse
}