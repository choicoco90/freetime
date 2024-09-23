package org.example.freetime.repository

import org.example.freetime.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean

    fun findByEmailAndName(email: String, name: String): UserEntity?

    fun findAllByIdIn(userIds: List<Long>): List<UserEntity>

    fun findAllByNameStartsWith(name: String): List<UserEntity>
    fun findAllByEmailStartsWith(email: String): List<UserEntity>
}
