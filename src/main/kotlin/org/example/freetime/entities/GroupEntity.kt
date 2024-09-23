package org.example.freetime.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.freetime.dto.request.UpdateGroupRequest

@Entity
@Table(name = "groups")
data class GroupEntity(
    @Column(name = "groupName", nullable = false)
    var groupName: String,
    @Column(name = "groupLeader", nullable = false)
    var groupLeader: Long,
    @Column(name = "description", nullable = false)
    var description: String = ""
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0


    fun update(request: UpdateGroupRequest) {
        this.groupName = request.groupName
        this.groupLeader = request.groupLeader
        this.description = request.description
    }
}
