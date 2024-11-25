package org.example.kotlinprojectmarketplace.controller

import org.example.kotlinprojectmarketplace.database.dto.UserInfoEnrichRequest
import org.example.kotlinprojectmarketplace.database.dto.UserInfoEnrichResponse
import org.example.kotlinprojectmarketplace.service.UserInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/user")
class UserInfoController(
    @Autowired
    private val userInfoService: UserInfoService,
) {
    @PostMapping("/enrich")
    fun createUserInfo(
        @RequestBody userInfoEnrichRequest: UserInfoEnrichRequest
    ): ResponseEntity<UserInfoEnrichResponse> = ResponseEntity
        .status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            userInfoService.createUserInfo(
                userInfoEnrichRequest,
                SecurityContextHolder.getContext().authentication.principal as Int
            )
        )

    @PutMapping("/enrich")
    fun updateUserInfo(
        @RequestBody userInfoEnrichRequest: UserInfoEnrichRequest
    ): ResponseEntity<UserInfoEnrichResponse> = ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            userInfoService.updateUserInfo(
                userInfoEnrichRequest,
                SecurityContextHolder.getContext().authentication.principal as Int
            )
        )

    @GetMapping("/enrich")
    fun getUserInfo(): ResponseEntity<UserInfoEnrichResponse> = ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            userInfoService.getUserInfo(
                SecurityContextHolder.getContext().authentication.principal as Int
            )
        )


    @DeleteMapping("/enrich")
    fun deleteUserInfo(): ResponseEntity<UserInfoEnrichResponse> = ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            userInfoService.deleteUserInfo(
                SecurityContextHolder.getContext().authentication.principal as Int
            )
        )
}