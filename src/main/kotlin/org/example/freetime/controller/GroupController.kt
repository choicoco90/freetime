package org.example.freetime.controller

import org.example.freetime.dto.CreateGroupRequest
import org.example.freetime.dto.GroupResponse
import org.example.freetime.dto.MyGroupResponse
import org.example.freetime.dto.UpdateGroupRequest
import org.example.freetime.service.GroupService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/group")
class GroupController(
    val groupService: GroupService
) {
    @GetMapping
    fun getMyGroups(
        @RequestAttribute("userId") requesterId: Long,
    ): List<MyGroupResponse> {
        return groupService.getMyGroups(requesterId)
    }

    @GetMapping("/{groupId}")
    fun getGroup(
        @PathVariable groupId: Long,
        @RequestAttribute("userId") requesterId: Long,
    ): GroupResponse {
        return groupService.getGroupInfo(groupId)
    }


    @PostMapping
    fun createGroup(
        @RequestBody request: CreateGroupRequest,
        @RequestAttribute("userId") requesterId: Long,
    ): GroupResponse {
        return groupService.createGroup(request, requesterId)
    }
    @PutMapping
    fun updateGroup(
        @RequestBody request: UpdateGroupRequest,
        @RequestAttribute("userId") requesterId: Long,
    ) {
        groupService.updateGroup(request, requesterId)
    }
    @DeleteMapping("/{groupId}")
    fun deleteGroup(
        @PathVariable groupId: Long,
        @RequestAttribute("userId") requesterId: Long,
    ) {
        groupService.deleteGroup(groupId, requesterId)
    }
    @DeleteMapping("/{groupId}/user/{userId}")
    fun deleteGroupUser(
        @PathVariable groupId: Long,
        @PathVariable userId: Long,
        @RequestAttribute("userId") requesterId: Long,
    ) {
        groupService.deleteMemberFromGroup(groupId, userId, requesterId)
    }

    @PostMapping("/{groupId}/user/{userId}")
    fun addGroupUser(
        @PathVariable groupId: Long,
        @PathVariable userId: Long,
        @RequestAttribute("userId") requesterId: Long,
    ) {
        groupService.addMemberToGroup(groupId, userId, requesterId)
    }
}
