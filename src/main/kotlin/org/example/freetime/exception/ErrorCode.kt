package org.example.freetime.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val httpStatus: HttpStatus,
    val message: String
) {
    /**
     * 인가 관련 에러 코드
     */
    TOKEN_VERIFICATION_ERROR("10000", HttpStatus.UNAUTHORIZED, "[인증] 토큰 인증 실패"),
    TOKEN_EXPIRED_ERROR("10001", HttpStatus.UNAUTHORIZED , "[인증] 토큰 만료"),
    TOKEN_MISSING("10002", HttpStatus.UNAUTHORIZED,"[인증] 토큰 없음"),

    /**
     * 유저 서비스 에러 코드
     */
    USER_NOT_FOUND("20000", HttpStatus.NOT_FOUND, "[서비스] 사용자를 찾을 수 없음"),
    USER_ALREADY_EXISTS("20001", HttpStatus.BAD_REQUEST, "[서비스] 사용자가 이미 존재함"),
    USER_PASSWORD_MISMATCH("20002", HttpStatus.BAD_REQUEST, "[서비스] 비밀번호 불일치"),
    CANNOT_USE_SMS("20003", HttpStatus.BAD_REQUEST, "[서비스] SMS 사용 불가 (핸드폰 번호가 없음)"),

    /**
     * 미팅 서비스 에러코드
     */
    MEETING_NOT_FOUND("30000", HttpStatus.NOT_FOUND, "[미팅] 미팅을 찾을 수 없음"),
    MEETING_IS_NOT_MINE("30001", HttpStatus.BAD_REQUEST, "[미팅] 미팅이 내 것이 아님"),
    MEETING_STATUS_IS_SAME("30002", HttpStatus.BAD_REQUEST, "[미팅] 미팅 상태가 같음"),

    /**
     * 미팅 제안 서비스 에러코드
     */
    PROPOSAL_NOT_FOUND("40000", HttpStatus.NOT_FOUND, "[미팅 제안] 미팅 제안을 찾을 수 없음"),
    PROPOSAL_SCHEDULE_NOT_FOUND("40001", HttpStatus.NOT_FOUND, "[미팅 제안] 미팅 제안 스케줄을 찾을 수 없음"),
    PROPOSAL_EXPIRED("40002", HttpStatus.BAD_REQUEST, "[미팅 제안] 미팅 제안이 만료되었음"),
    PROPOSAL_NOT_WAITING("40003", HttpStatus.BAD_REQUEST, "[미팅 제안] 미팅 제안이 대기 중이 아님"),
    PROPOSAL_IS_NOT_MINE("40004", HttpStatus.BAD_REQUEST, "[미팅 제안] 미팅 제안이 내 것이 아님"),


    /**
     * 빈 시간 서비스 에러코드
     */
    WEEKLY_FREE_TIME_NOT_FOUND("50000", HttpStatus.NOT_FOUND, "[빈 시간] 주간 빈 시간을 찾을 수 없음"),
    DAILY_FREE_TIME_NOT_FOUND("50001", HttpStatus.NOT_FOUND, "[빈 시간] 일일 빈 시간을 찾을 수 없음"),

    /**
     * 그룹 서비스 에러코드
     */
    GROUP_NOT_FOUND("60000", HttpStatus.NOT_FOUND, "[그룹] 그룹을 찾을 수 없음"),
    NOT_GROUP_LEADER("60001", HttpStatus.BAD_REQUEST, "[그룹] 그룹 리더가 아님"),
    CANNOT_DELETE_GROUP_LEADER("60002", HttpStatus.BAD_REQUEST, "[그룹] 그룹 리더는 삭제할 수 없음"),
    ALREADY_GROUP_MEMBER("60003", HttpStatus.BAD_REQUEST, "[그룹] 이미 그룹 멤버임"),
    NOT_GROUP_MEMBER("60004", HttpStatus.BAD_REQUEST, "[그룹] 그룹 멤버가 아님"),

    /**
     * 기타 에러 (80000 ~ 89999)
     */

    UNABLE_TO_ENCRYPT("80000", HttpStatus.INTERNAL_SERVER_ERROR, "[암호화] 암호화 실패"),
    UNABLE_TO_DECRYPT("80001", HttpStatus.INTERNAL_SERVER_ERROR, "[암호화] 복호화 실패"),


    UNEXPECTED("99999", HttpStatus.INTERNAL_SERVER_ERROR, "[운영] 예상치 못한 에러 발생")
}
