package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.dto.UserInfoEnrichRequest
import org.example.kotlinprojectmarketplace.database.dto.UserInfoEnrichResponse
import org.example.kotlinprojectmarketplace.database.dto.UserInfoEnrichResponseMessage
import org.example.kotlinprojectmarketplace.database.entity.UserInfo
import org.example.kotlinprojectmarketplace.database.repository.UserInfoRepository
import org.example.kotlinprojectmarketplace.exception.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class UserInfoServiceImpl(
    @Autowired
    private val userInfoRepository: UserInfoRepository,
) : UserInfoService {
    override fun createUserInfo(userInfoEnrichRequest: UserInfoEnrichRequest, id: Int): UserInfoEnrichResponse {
        if (userInfoRepository.findById(id).isPresent || isConflicts(userInfoEnrichRequest, id)) {
            throw UserInfoEnrichException(UserInfoEnrichExceptionMessage.DATA_CONFLICT, HttpStatus.CONFLICT)
        }
        val userInfo = userInfoRepository.save(buildUserInfo(userInfoEnrichRequest, id))

        return UserInfoEnrichResponse(UserInfoEnrichResponseMessage.SUCCESS_CREATE, userInfo)
    }

    override fun updateUserInfo(userInfoEnrichRequest: UserInfoEnrichRequest, id: Int): UserInfoEnrichResponse {
        if (userInfoRepository.findById(id).isEmpty) throw UserNotFoundException()

        if (isConflicts(userInfoEnrichRequest, id)) {
            throw UserInfoEnrichException(UserInfoEnrichExceptionMessage.DATA_CONFLICT, HttpStatus.CONFLICT)
        }

        val userInfo = userInfoRepository.save(buildUserInfo(userInfoEnrichRequest, id))
        return UserInfoEnrichResponse(UserInfoEnrichResponseMessage.SUCCESS_UPDATE, userInfo)
    }

    override fun getUserInfo(id: Int): UserInfoEnrichResponse = UserInfoEnrichResponse(
        UserInfoEnrichResponseMessage.SUCCESS_GET,
        userInfoRepository.findById(id).orElseThrow { UserNotFoundException() }
    )

    override fun deleteUserInfo(id: Int): UserInfoEnrichResponse = UserInfoEnrichResponse(
        UserInfoEnrichResponseMessage.SUCCESS_DELETE,
        userInfoRepository.findById(id).orElseThrow { UserNotFoundException() }.also {
            userInfoRepository.delete(it)
        }
    )

    private fun isConflicts(
        userInfoEnrichRequest: UserInfoEnrichRequest,
        id: Int
    ): Boolean = userInfoEnrichRequest.email != null
            && userInfoRepository.findByEmail(userInfoEnrichRequest.email).run { !isEmpty && get().id != id }
            || userInfoEnrichRequest.phone != null
            && userInfoRepository.findByPhone(userInfoEnrichRequest.phone).run { !isEmpty && get().id != id }

    private fun buildUserInfo(
        userInfoEnrichRequest: UserInfoEnrichRequest,
        id: Int
    ): UserInfo = UserInfo(
        id,
        userInfoEnrichRequest.name,
        userInfoEnrichRequest.lastName,
        userInfoEnrichRequest.email,
        userInfoEnrichRequest.phone,
        userInfoEnrichRequest.cash
    )
}