package org.example.kotlinprojectmarketplace.database.dto

import org.example.kotlinprojectmarketplace.database.entity.UserInfo

data class UserInfoEnrichResponse(
    val message: String,
    val userInfo: UserInfo
)

object UserInfoEnrichResponseMessage {
    const val SUCCESS_UPDATE: String = "Success update"
    const val SUCCESS_CREATE: String = "Success create"
    const val SUCCESS_GET: String = "Success get"
    const val SUCCESS_DELETE: String = "Success delete"
}