package org.example.freetime.repository

import org.example.freetime.entities.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository: JpaRepository<GroupEntity, Long> {

    fun findAllByIdIn(groupId: List<Long>): List<GroupEntity>
}
