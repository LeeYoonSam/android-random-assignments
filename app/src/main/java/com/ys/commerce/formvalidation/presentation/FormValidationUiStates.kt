package com.ys.commerce.formvalidation.presentation

import androidx.compose.ui.text.input.TextFieldValue

// 이메일 유효성 에러 타입
sealed interface EmailError {
    data object InvalidFormat : EmailError // 형식 오류
}

// 비밀번호 유효성 에러 타입
sealed interface PasswordError {
    data object TooShort : PasswordError // 길이 오류
}

data class FormScreenUiState(
    val email: TextFieldValue = TextFieldValue(""), // 이메일 입력값
    val password: TextFieldValue = TextFieldValue(""), // 비밀번호 입력값
    val emailError: EmailError? = null, // 이메일 에러 메시지 (null이면 유효)
    val passwordError: PasswordError? = null, // 비밀번호 에러 메시지 (null이면 유효)
    val isSubmitEnabled: Boolean = false, // 제출 버튼 활성화 여부
    val submissionState: SubmissionState = SubmissionState.Idle // 폼 제출 결과 상태
)

sealed interface SubmissionState {
    data object Idle : SubmissionState // 초기 상태 또는 제출 시도 전
    data object Submitting : SubmissionState // 제출 진행 중 (예: 네트워크 요청)
    data object Success : SubmissionState // 제출 성공
    data class Error(val cause: Throwable) : SubmissionState // 제출 실패
}