package org.example.freetime.repository

import org.example.freetime.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?

    fun findByEmailAndName(email: String, name: String): UserEntity?
}
