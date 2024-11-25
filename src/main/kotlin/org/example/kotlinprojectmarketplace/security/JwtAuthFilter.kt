package org.example.kotlinprojectmarketplace.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.exception.UnauthorizedException
import org.example.kotlinprojectmarketplace.exception.UserNotFoundException
import org.example.kotlinprojectmarketplace.service.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val userDetailsRepository: UserDetailsRepository,
): OncePerRequestFilter() {
    private val headerPrefix = "Bearer "

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwtToken = request.getHeader("Authorization").let{
            if (it == null || !it.startsWith(headerPrefix)) {
                filterChain.doFilter(request, response)
                return
            }
            it.substringAfter(headerPrefix)
        }
        val id = jwtService.extractId(jwtToken).run {
            if (isEmpty) throw UserNotFoundException()
            get()
        }
        val foundUser = userDetailsRepository.findById(id).orElseThrow { UserNotFoundException() }
        if (jwtService.isValid(jwtToken, foundUser)) {
            updateContext(foundUser, request)
        } else {
            throw UnauthorizedException()
        }
        filterChain.doFilter(request, response)
    }

    private fun updateContext(foundUser: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(foundUser.id, null, foundUser.getAuthorities())
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
    }

}