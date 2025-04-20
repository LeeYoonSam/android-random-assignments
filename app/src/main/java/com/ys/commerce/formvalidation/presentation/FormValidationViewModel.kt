package com.ys.commerce.formvalidation.presentation

import android.util.Patterns
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FormValidationViewModel : ViewModel() {
    // --- 입력 상태 관리 ---
    // UI의 TextField와 직접 바인딩될 StateFlow
    private val _emailText = MutableStateFlow(TextFieldValue())
    private val _passwordText = MutableStateFlow(TextFieldValue())

    // 폼 제출 상태 관리
    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)

    // --- Debounced 입력 Flow ---
    // 이메일 텍스트에 debounce 적용
    @OptIn(FlowPreview::class)
    private val debouncedEmail = _emailText
        .map { it.text } // TextFieldValue에서 text만 추출
        .debounce(300L) // 300ms 디바운스
        .distinctUntilChanged() // 값이 실제로 변경됐을 때만 진행

    // 비밀번호 텍스트에 debounce 적용
    @OptIn(FlowPreview::class)
    private val debouncedPassword = _passwordText
        .map { it.text } // TextFieldValue에서 text만 추출
        .debounce(300L) // 300ms 디바운스
        .distinctUntilChanged()


    // --- UI 상태 계산 및 노출 ---
    // 입력값 Flow들을 combine하여 유효성 검사 및 최종 UI 상태 계산
    val uiState: StateFlow<FormScreenUiState> = combine(
        _emailText,               // UI 표시용 원본 TextFieldValue
        _passwordText,            // UI 표시용 원본 TextFieldValue
        debouncedEmail,           // 유효성 검사용 Debounced 이메일 텍스트
        debouncedPassword,        // 유효성 검사용 Debounced 비밀번호 텍스트
        _submissionState
    ) { email, password, debouncedEmail, debouncedPassword, submission ->
        // 디바운싱된 텍스트 기준으로 유효성 검사 수행
        val isEmailValid = isIdValidation(debouncedEmail)
        val isPasswordValid = isPasswordValidation(debouncedPassword)

        // 에러 메시지 결정 (입력값이 있고 유효하지 않을 때만)
        val emailErrorMsg = if (!isEmailValid && debouncedEmail.isNotEmpty()) EmailError.InvalidFormat else null
        val passwordErrorMsg = if (!isPasswordValid && debouncedPassword.isNotEmpty()) PasswordError.TooShort else null

        // 최종 UI 상태 객체 생성
        FormScreenUiState(
            email = email, // UI에는 즉각적인 입력값 반영
            password = password, // UI에는 즉각적인 입력값 반영
            emailError = emailErrorMsg,
            passwordError = passwordErrorMsg,
            isSubmitEnabled = isEmailValid && isPasswordValid && submission !is SubmissionState.Submitting,
            submissionState = submission
        )
    }.stateIn(
        scope = viewModelScope,
        // WhileSubscribed: UI가 구독 중일 때만 활성 상태 유지 (리소스 효율적)
        // timeoutMillis: 화면 회전 등 잠시 구독이 끊겨도 5초간 상태 유지
        started = SharingStarted.WhileSubscribed(5000),
        // 초기 상태 객체 (모든 필드가 기본값)
        initialValue = FormScreenUiState()
    )
    // --- UI 이벤트 처리 ---

    fun updateEmail(email: TextFieldValue) {
        _emailText.value = email
        // 제출 시도 후 사용자가 다시 입력 시작하면 제출 상태 초기화
        resetSubmissionStateIfNeeded()
    }

    fun updatePassword(password: TextFieldValue) {
        _passwordText.value = password
        resetSubmissionStateIfNeeded()
    }

    // 폼 제출 액션 (버튼 클릭 시 호출)
    fun submitForm() {
        // 현재 계산된 상태에서 제출 가능한지 확인
        if (!uiState.value.isSubmitEnabled) return
        // 이미 제출 중이면 중복 실행 방지
        if (_submissionState.value is SubmissionState.Submitting) return

        viewModelScope.launch {
            _submissionState.value = SubmissionState.Submitting // 제출 중 상태로 변경
            try {
                // --- 실제 제출 로직 수행 (예: 네트워크 요청) ---
                delay(1500) // 예시: 1.5초 지연
                println("폼 제출 성공! Email: ${uiState.value.email.text}")
                // --- 제출 로직 끝 ---

                _submissionState.value = SubmissionState.Success // 성공 상태로 변경
                resetFormFields() // 성공 시 폼 필드 초기화

            } catch (e: Exception) {
                // 실패 시 에러 상태로 변경
                _submissionState.value = SubmissionState.Error(e)
            }
        }
    }

    // 폼 필드만 초기화하는 함수
    private fun resetFormFields() {
        _emailText.value = TextFieldValue()
        _passwordText.value = TextFieldValue()
    }

    // 제출 시도 후 다시 입력 시작 시 제출 상태 초기화
    private fun resetSubmissionStateIfNeeded() {
        if (_submissionState.value !is SubmissionState.Idle && _submissionState.value !is SubmissionState.Submitting) {
            _submissionState.value = SubmissionState.Idle
        }
    }

    // --- 유효성 검사 로직 (private 유지) ---
    private fun isIdValidation(email: String): Boolean {
        // Android에서 제공하는 표준 이메일 패턴 사용
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun isPasswordValidation(password: String): Boolean {
        // 비어있지 않으면서 8자 이상 (기존 로직 유지)
        return password.isNotBlank() && password.length >= 8
    }
}