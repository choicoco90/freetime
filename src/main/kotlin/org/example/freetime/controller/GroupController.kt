package org.example.freetime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.freetime.dto.request.CreateGroupRequest
import org.example.freetime.dto.response.GroupResponse
import org.example.freetime.dto.response.MyGroupResponse
import org.example.freetime.dto.request.UpdateGroupRequest
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
@RequestMapping("/groups")
@Tag(name = "Group", description = "그룹 API")
class GroupController(
    val groupService: GroupService
) {
    @Operation(summary = "내 그룹 조회")
    @GetMapping
    fun getMyGroups(
        @RequestAttribute("userId") requesterId: Long,
    ): List<MyGroupResponse> {
        return groupService.getMyGroups(requesterId)
    }

    @Operation(summary = "그룹 생성")
    @PostMapping
    fun createGroup(
        @RequestBody request: CreateGroupRequest,
        @RequestAttribute("userId") requesterId: Long,
    ): GroupResponse {
        return groupService.createGroup(request, requesterId)
    }

    @Operation(summary = "그룹 조회(ID)")
    @GetMapping("/{groupId}")
    fun getGroup(
        @PathVariable groupId: Long,
        @RequestAttribute("userId") requesterId: Long,
    ): GroupResponse {
        return groupService.getGroupInfo(groupId)
    }

    @Operation(summary = "그룹 수정(ID)")
    @PutMapping("/{groupId}")
    fun updateGroup(
        @RequestBody request: UpdateGroupRequest,
        @RequestAttribute("userId") requesterId: Long,
        @PathVariable groupId: Long,
    ) {
        groupService.updateGroup(request, groupId, requesterId)
    }

    @Operation(summary = "그룹 삭제(ID)")
    @DeleteMapping("/{groupId}")
    fun deleteGroup(
        @PathVariable groupId: Long,
        @RequestAttribute("userId") requesterId: Long,
    ) {
        groupService.deleteGroup(groupId, requesterId)
    }

    @Operation(summary = "그룹 멤버 삭제")
    @DeleteMapping("/{groupId}/user/{userId}")
    fun deleteGroupUser(
        @PathVariable groupId: Long,
        @PathVariable userId: Long,
        @RequestAttribute("userId") requesterId: Long,
    ) {
        groupService.deleteMemberFromGroup(groupId, userId, requesterId)
    }

    @Operation(summary = "그룹 멤버 추가")
    @PostMapping("/{groupId}/user/{userId}")
    fun addGroupUser(
        @PathVariable groupId: Long,
        @PathVariable userId: Long,
        @RequestAttribute("userId") requesterId: Long,
    ) {
        groupService.addMemberToGroup(groupId, userId, requesterId)
    }
}
