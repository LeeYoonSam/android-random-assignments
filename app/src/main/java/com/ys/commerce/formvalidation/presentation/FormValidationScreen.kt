package com.ys.commerce.formvalidation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ys.commerce.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormValidationScreen(
    modifier: Modifier = Modifier,
    viewModel: FormValidationViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // --- UI 컨트롤러 가져오기 ---
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val snackbarHostState = remember { SnackbarHostState() }

    // LaunchedEffect는 제출 상태(submissionState) 변경 감지
    LaunchedEffect(uiState.submissionState) {
        when (val submissionState = uiState.submissionState) {
            is SubmissionState.Success -> {
                // --- 키보드 숨기기 및 포커스 제거 ---
                focusManager.clearFocus() // 현재 포커스 해제
                keyboardController?.hide()  // 키보드 숨기기

                snackbarHostState.showSnackbar(context.getString(R.string.success_valid)) // 성공 메시지
            }
            is SubmissionState.Error -> {
                val message = context.getString(R.string.error_submission_failed, submissionState.cause.localizedMessage ?: "Unknown error")
                snackbarHostState.showSnackbar(message) // 에러 메시지
            }
            else -> { /* Idle, Submitting 상태에서는 스낵바 표시 안 함 */ }
        }
    }

    Box (modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "입력 폼 유효성 검사",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent,
                ),
            )
            // 3. FormValidationContent에 단일 uiState 객체와 필요한 콜백 전달
            FormValidationContent(
                formState = uiState, // 전체 UI 상태 전달
                updateEmail = viewModel::updateEmail, // :: 함수 레퍼런스 사용 가능
                updatePassword = viewModel::updatePassword,
                onSubmitClick = viewModel::submitForm // 제출 버튼 클릭 시 호출될 콜백
            )
        }

        // 스낵바 위치 및 패딩 조정 (이전 질문 내용 반영)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter) // 하단 중앙 배치 (예시)
                .navigationBarsPadding() // 시스템 네비게이션 바 영역 피하기
                .padding(bottom = 16.dp) // 추가적인 하단 여백
        )
    }
}

@Composable
fun FormValidationContent(
    formState: FormScreenUiState, // 단일 상태 객체 받기
    updateEmail: (TextFieldValue) -> Unit, // 콜백 타입 유지
    updatePassword: (TextFieldValue) -> Unit, // 콜백 타입 유지
    onSubmitClick: () -> Unit // 파라미터 없는 콜백으로 변경
) {
    val scrollState = rememberScrollState() // 컨텐츠가 길어질 경우 대비

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // 스크롤 가능하게
    ) {
        // 이메일 입력 필드
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = formState.email, // 상태 객체에서 값 사용
            onValueChange = updateEmail, // 콜백 연결
            label = { Text(stringResource(R.string.placeholder_email)) }, // label 사용 권장
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = formState.emailError != null, // 에러 상태 반영
            singleLine = true
        )
        // 이메일 에러 메시지 표시
        formState.emailError?.let { error ->
            val errorMessage = when (error) { // 에러 타입에 따라 리소스 선택
                is EmailError.InvalidFormat -> stringResource(id = R.string.error_invalid_email_format)
                // 다른 이메일 에러 타입 추가 가능
            }
            Text(
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 4.dp),
                text = errorMessage,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error // 테마 에러 색상 사용 권장
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 비밀번호 입력 필드
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = formState.password, // 상태 객체에서 값 사용
            onValueChange = updatePassword, // 콜백 연결
            label = { Text(stringResource(R.string.placeholder_password)) }, // label 사용 권장
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(), // 비밀번호 가리기
            isError = formState.passwordError != null, // 에러 상태 반영
            singleLine = true
        )
        // 비밀번호 에러 메시지 표시
        formState.passwordError?.let { error ->
            val errorMessage = when (error) { // 에러 타입에 따라 리소스 선택
                is PasswordError.TooShort -> stringResource(id = R.string.error_password_too_short)
                // 다른 비밀번호 에러 타입 추가 가능
            }
            Text(
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 4.dp),
                text = errorMessage,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 제출 버튼
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSubmitClick, // 파라미터 없는 콜백 호출
            enabled = formState.isSubmitEnabled // 상태 객체에서 활성화 여부 사용
        ) {
            // 제출 중 상태일 때 로딩 인디케이터 표시 (선택적)
            if (formState.submissionState is SubmissionState.Submitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = stringResource(R.string.login))
            }
        }
    }
}