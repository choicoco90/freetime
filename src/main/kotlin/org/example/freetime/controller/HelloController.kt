package org.example.freetime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
@Tag(name = "Hello API")
class HelloController {

    @Operation(summary = "인사 하기")
    @GetMapping("/hello")
    fun hello(): String {
        return "Hello, World!"
    }

    @Operation(summary = "테스트(인증 필요)")
    @GetMapping("/test")
    fun test(
        @RequestAttribute("userId") userId: String
    ): String {
        return "Test, $userId"
    }
}
