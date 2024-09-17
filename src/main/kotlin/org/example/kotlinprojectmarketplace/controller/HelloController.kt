package org.example.kotlinprojectmarketplace.controller

import org.example.kotlinprojectmarketplace.dto.UserDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping
    fun hello() = UserDto("John Doe", 16)
}