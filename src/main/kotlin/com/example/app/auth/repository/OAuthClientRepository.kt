package com.example.app.auth.repository

import com.example.app.auth.entity.OAuthClient
import org.springframework.data.jpa.repository.JpaRepository

interface OAuthClientRepository : JpaRepository<OAuthClient, Long> {
    fun findByEmail(email: String): OAuthClient?
}