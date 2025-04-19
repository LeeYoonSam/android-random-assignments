# 상품 리스트 검색
```md
# 안드로이드 과제 테스트 (Jetpack Compose 버전)
- 과제 목표: 주어진 Mock API를 통해 상품 목록 데이터를 가져와 화면에 표시하고, 검색 기능을 Jetpack Compose를 사용하여 구현합니다.
- 제한 시간: 1시간
- 개발 환경: Android Studio, Kotlin, Jetpack Compose

---

## 요구 사항
1. 화면 구성 (Compose UI):

- 화면 전체는 @Composable 함수로 구현합니다.
- 화면 상단에는 검색어를 입력할 수 있는 TextField (또는 OutlinedTextField) Composable이 위치합니다.
- TextField 아래에는 상품 목록을 표시할 LazyColumn Composable이 위치합니다.
- LazyColumn의 각 아이템은 별도의 @Composable 함수(예: ProductItem)로 분리하여 상품명과 상품 가격(Text Composable 사용)을 표시합니다.

2. 데이터 로딩 및 상태 관리:

- 앱 실행 시, 아래 제공된 Mock API 엔드포인트에서 상품 목록 데이터를 비동기적으로 가져옵니다. (실제 네트워크 호출 대신, 제공된 JSON 데이터를 파싱하여 사용해도 무방합니다.)
- ViewModel을 사용하여 상품 목록 데이터와 검색어 상태를 관리합니다.
- StateFlow 또는 LiveData를 사용하여 ViewModel의 상태를 Composable 함수에 전달하고, 상태 변경 시 UI가 자동으로 업데이트(recomposition)되도록 구현합니다. (collectAsStateWithLifecycle() 또는 collectAsState() 사용 권장).
- 데이터 로딩 중임을 나타내는 UI(예: CircularProgressIndicator)를 표시하는 것을 권장합니다. (선택 사항)

3. 상품 목록 표시:
- ViewModel로부터 관찰한 전체 상품 목록 상태를 LazyColumn에 표시합니다.

4. 검색 기능 구현:
- 사용자가 TextField에 검색어를 입력할 때마다 (onValueChange 콜백 활용), ViewModel의 검색어 상태를 업데이트합니다.
- ViewModel 내에서 검색어 상태 변경을 감지하여 상품 목록을 필터링하고, 필터링된 결과를 UI 상태로 노출합니다.
- 필터링 기준은 상품명이며, 대소문자를 구분하지 않고 검색어가 포함된 모든 상품을 보여줍니다.
- 검색어가 비어있는 경우, 필터링되지 않은 전체 상품 목록을 다시 표시합니다.

## 리소스

[
    { "id": 1, "name": "노트북 거치대", "price": 25000 },
    { "id": 2, "name": "무선 마우스", "price": 18000 },
    { "id": 3, "name": "기계식 키보드", "price": 120000 },
    { "id": 4, "name": "블루투스 이어폰", "price": 89000 },
    { "id": 5, "name": "웹캠", "price": 45000 },
    { "id": 6, "name": "USB 허브", "price": 22000 },
    { "id": 7, "name": "스마트폰 무선 충전기", "price": 30000 },
    { "id": 8, "name": "휴대용 모니터", "price": 250000 },
    { "id": 9, "name": "외장 SSD 1TB", "price": 150000 },
    { "id": 10, "name": "게이밍 마우스 패드", "price": 15000 }
]

## 참고 사항
- Jetpack Compose의 기본적인 사용법(Composable 함수, State 관리, LazyColumn, TextField)에 대한 이해가 필요합니다.
- 상태 호이스팅(State Hoisting) 및 단방향 데이터 흐름(Unidirectional Data Flow) 패턴을 적용하여 구현하는 것을 권장합니다.
- 제한 시간 내에 요구 사항을 최대한 만족시키는 것을 목표로 합니다.
- 코드의 가독성과 Compose UI 구조의 효율성을 고려하여 작성해주세요.
```

## Library
### androidx.lifecycle:lifecycle-viewmodel-compose 
필요 목적:
- Jetpack Compose 환경에서 ViewModel을 쉽고 생명주기를 고려하여 안전하게 사용할 수 있도록 지원하기 위해 필요합니다.
- Composable 함수 내에서 ViewModel 인스턴스를 가져오는 표준적이고 간결한 방법을 제공하며, Composable의 생명주기나 상위 컴포넌트(Activity, Fragment, Navigation BackStackEntry)의 생명주기에 맞게 ViewModel의 범위를 올바르게 지정해줍니다.

주요 기능/제공 요소:
- viewModel() Composable 함수: 현재 Composition 컨텍스트에 맞는 ViewModelStoreOwner를 찾아 ViewModelProvider를 통해 ViewModel 인스턴스를 가져오거나 생성합니다. Hilt와 함께 사용할 경우 Hilt가 주입하는 ViewModel을 가져오는 역할도 합니다 (hiltViewModel()은 이 라이브러리가 아닌 androidx.hilt:hilt-navigation-compose에 있음).
- viewModel(factory = ...): 커스텀 팩토리를 전달하여 파라미터가 있는 ViewModel을 생성할 수 있습니다.

해결하는 문제:
- Composable 함수 내에서 ViewModelProvider와 ViewModelStoreOwner를 수동으로 찾아 ViewModel을 생성하는 복잡하고 장황한 코드를 작성할 필요 없이, viewModel() 함수 호출만으로 간단하게 ViewModel 인스턴스를 얻고 생명주기 관리를 위임할 수 있게 해줍니다.

사용 예시:
```kotlin
// build.gradle.kts (:app 모듈 등)
dependencies {
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0") // 최신 버전 확인
}

// Composable 함수 내부
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MyScreen(
    // viewModel() 함수를 사용하여 ViewModel 인스턴스 획득
    // 기본적으로 가장 가까운 ViewModelStoreOwner(Fragment, Activity 등) 범위로 생성됨
    myViewModel: MyViewModel = viewModel()
) {
    val uiState by myViewModel.uiState.collectAsStateWithLifecycle()

    // myViewModel 인스턴스 사용...
    Button(onClick = { myViewModel.performAction() }) {
        Text("Do Action")
    }
}
```

### org.jetbrains.kotlin:kotlin-test
필요 목적:

- Kotlin 코드를 위한 단위 테스트(Unit Test) 를 작성하고 실행하는 데 필요한 기본적인 기능들을 제공합니다.
- 테스트 케이스를 정의하고, 코드의 동작이 예상과 같은지 검증(Assertion)하는 표준적인 방법을 제공합니다.

주요 기능/제공 요소:

- Assertion 함수: assertEquals(), assertTrue(), assertFalse(), assertNotNull(), assertNull(), assertSame(), assertFailsWith<ExceptionType> { ... } 등 다양한 검증 함수를 제공하여 테스트 결과를 확인합니다.
- 테스트 어노테이션: @Test, @BeforeTest, @AfterTest, @Ignore 등 테스트 구조를 정의하고 실행 방식을 제어하는 어노테이션을 제공합니다 (주로 JUnit과 함께 사용될 때 해당 어노테이션 활용).

해결하는 문제: 
- 개발자가 작성한 Kotlin 코드(함수, 클래스 등)의 정확성을 검증하고, 코드 변경 시 의도치 않은 오류(회귀, regression)가 발생하는 것을 방지하기 위한 표준화된 방법을 제공합니다. 테스트 프레임워크(JUnit 등)와 함께 사용되어 자동화된 테스트를 가능하게 합니다.

사용 예시:
```kotlin
// build.gradle.kts (:app 모듈 등 - testImplementation 사용)
dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:kotlin-version")
}

// src/test/kotlin/com/example/MyUtilTest.kt
import kotlin.test.* // kotlin.test의 함수들 import

class MyUtilTest {
    @Test
    fun `addition should return correct sum`() {
        // 검증하려는 코드 실행
        val result = MyUtils.add(2, 3)
        // 결과 검증
        assertEquals(5, result, "2 + 3 should be 5")
    }

    @Test
    fun `check for positive number`() {
        assertTrue(MyUtils.isPositive(10), "10 should be positive")
        assertFalse(MyUtils.isPositive(-5), "-5 should not be positive")
    }

    @Test
    fun `division by zero should throw exception`() {
        assertFailsWith<ArithmeticException>("Division by zero must throw ArithmeticException") {
            MyUtils.divide(10, 0)
        }
    }
}
```

###  org.jetbrains.kotlinx:kotlinx-coroutines-test
필요 목적:

- Kotlin Coroutines를 사용하는 비동기 코드를 테스트하기 위한 특수한 도구와 환경을 제공합니다.
- 코루틴의 비동기성, 지연(delay), 디스패처(Dispatcher) 전환 등을 테스트 환경에서 제어 가능하고 예측 가능하게(deterministic) 만들어 테스트의 안정성과 속도를 높입니다.

주요 기능/제공 요소:

- runTest: 테스트 실행을 위한 기본 빌더. 가상 시간(virtual time)과 테스트 디스패처를 제공하여 delay를 건너뛰고 코루틴을 동기적으로 실행하는 것처럼 테스트할 수 있게 해줍니다.
- TestCoroutineScheduler: 가상 시간을 제어하는 스케줄러 (advanceTimeBy, runCurrent 등).
- TestDispatcher (StandardTestDispatcher, UnconfinedTestDispatcher): 테스트 환경에서 사용할 디스패처 구현체. viewModelScope 등에 주입하여 메인/IO 스레드 동작을 제어할 수 있습니다.
- TestScope: runTest 내부에서 사용되는 CoroutineScope. 스케줄러와 디스패처에 접근할 수 있습니다.
- Flow 테스트 지원 유틸리티 (Turbine 라이브러리와 함께 사용하면 더욱 편리).

해결하는 문제:

- 실제 시간을 기다리거나(Thread.sleep) 복잡한 콜백, 락(lock) 등을 사용하여 비동기 코드를 테스트할 때 발생하는 느린 테스트 속도, 불안정성(flakiness), 예측 불가능성 문제를 해결합니다.
- 가상 시간 제어와 테스트 디스패처를 통해 코루틴 코드를 빠르고 안정적이며 결정론적으로 테스트할 수 있는 환경을 제공합니다. delay, timeout, 동시성 로직 등을 효과적으로 검증할 수 있습니다.

사용 예시:
```kotlin
// build.gradle.kts (:app 모듈 등 - testImplementation 사용)
dependencies {
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0") // 최신 버전 확인
}

// src/test/kotlin/com/example/MyViewModelTest.kt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.* // 테스트 유틸리티 import
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class MyViewModelTest {

    @Test
    fun `WorkspaceData updates state after delay`() = runTest { // runTest 사용
        // TestDispatcher 생성 (StandardTestDispatcher가 일반적으로 권장됨)
        val testDispatcher = StandardTestDispatcher(this.testScheduler)
        // Dispatchers.Main을 테스트 디스패처로 교체 (예: MainCoroutineRule 또는 직접 설정)
        Dispatchers.setMain(testDispatcher)

        val viewModel = MyViewModel(testDispatcher) // ViewModel에 TestDispatcher 주입

        // 초기 상태 확인 (예시)
        assertEquals(UiState.Loading, viewModel.uiState.value)

        // 작업 트리거 (예: viewModel.fetchData() 호출)
        val job = launch { viewModel.fetchData(delayMs = 1000L) } // ViewModel 내부에서 delay(1000L) 사용 가정

        // 가상 시간 진행 없이 현재 작업만 실행 (필요시)
        // runCurrent()

        // 가상 시간을 1초 진행시킴 (실제로는 거의 시간 안 걸림)
        advanceTimeBy(1001L)
        // 또는 advanceUntilIdle() 로 모든 작업 완료까지 진행

        // 1초 후 상태 변경 확인
        assertEquals(UiState.Success("Data"), viewModel.uiState.value)

        job.cancel() // 테스트 종료 전 코루틴 정리
        Dispatchers.resetMain() // 테스트 후 메인 디스패처 복원
    }

    @Test
    fun `test flow emission`() = runTest {
        val flow = flow {
            emit(1)
            delay(100)
            emit(2)
        }

        val result = flow.toList() // Flow 결과를 리스트로 수집 (runTest 내에서 안전)
        assertEquals(listOf(1, 2), result)
    }
}
```
