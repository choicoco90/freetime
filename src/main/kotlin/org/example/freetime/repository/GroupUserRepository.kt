package org.example.freetime.repository

import org.example.freetime.entities.GroupEntity
import org.example.freetime.entities.GroupUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GroupUserRepository: JpaRepository<GroupUserEntity, Long> {

    /**
     * 그룹에 속한 모든 유저를 찾는다.
     */
    fun findAllByGroupId(groupId: Long): List<GroupUserEntity>

    /**
     * 내가 속한 모든 그룹을 찾는다.
     */
    fun findAllByUserId(userId: Long): List<GroupUserEntity>

    fun deleteAllByGroupId(groupId: Long)

    fun deleteByGroupIdAndUserId(groupId: Long, userId: Long)

    fun existsByGroupIdAndUserId(groupId: Long, userId: Long): Boolean
}
