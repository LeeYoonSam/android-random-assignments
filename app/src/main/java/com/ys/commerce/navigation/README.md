# Compose Navigation 화면 간 이동

**과제 목표:** Jetpack Compose Navigation 라이브러리를 사용하여 두 개의 화면(리스트 화면, 상세 화면) 사이를 이동하고, 화면 간에 간단한 데이터를 전달하는 기능을 구현하며 내비게이션의 기본 개념을 학습합니다.

**권장 학습 시간:** 1시간 ~ 1시간 30분 (Compose Navigation 설정 및 개념 학습 포함)

**개발 환경:** Android Studio, Kotlin, Jetpack Compose

---

### 요구 사항

1.  **내비게이션 설정:**
  * `androidx.navigation:navigation-compose` 라이브러리 의존성을 추가합니다.
  * 앱의 메인 Composable 구조 내에서 `rememberNavController()`를 사용하여 `NavHostController`를 생성합니다.
  * `NavHost` Composable을 사용하여 내비게이션 그래프를 설정합니다. 시작 목적지(startDestination)는 `ListScreen`으로 지정합니다.

2.  **화면 정의:**
  * **`ListScreen` Composable:**
    * 내비게이션 그래프 내에서 고유한 라우트(route) 문자열 (예: `"list"`)을 가집니다.
    * 미리 정의된 간단한 문자열 리스트(예: `listOf("사과", "바나나", "체리", "딸기", "포도")`)를 `LazyColumn`을 사용하여 화면에 표시합니다. (데이터는 Composable 내부에 하드코딩해도 무방합니다.)
    * 각 리스트 아이템은 클릭 가능해야 하며 (`clickable` modifier 사용), 클릭 시 `DetailScreen`으로 이동해야 합니다.
    * 아이템을 클릭하면, 해당 아이템의 **문자열 값**(예: "사과")을 `DetailScreen`으로 전달하는 내비게이션 액션을 수행합니다 (`navController.navigate(...)` 사용).
  * **`DetailScreen` Composable:**
    * 내비게이션 그래프 내에서 고유한 라우트 문자열을 가지며, 리스트에서 전달받을 문자열 인자를 포함해야 합니다 (예: `"detail/{itemName}"`). `{itemName}` 부분이 인자를 받는 placeholder입니다.
    * Composable 함수의 파라미터 또는 `NavBackStackEntry`의 `arguments`를 통해 전달받은 `itemName` 값을 추출합니다. (예: `arguments?.getString("itemName")`)
    * 화면에 "상세 정보: [itemName]"과 같이 전달받은 아이템 이름을 `Text` Composable을 사용하여 표시합니다.
    * 이 화면에서는 별도의 '뒤로 가기' UI 요소를 만들 필요 없이, 시스템에서 제공하는 뒤로 가기 제스처나 버튼으로 `ListScreen`으로 돌아갈 수 있어야 합니다.

3.  **내비게이션 로직:**
  * `ListScreen`에서 아이템 클릭 시, `DetailScreen`의 라우트를 동적으로 구성하여(예: `"detail/사과"`) `navController.navigate()` 함수를 호출합니다.
  * `DetailScreen`에서는 전달된 인자(`itemName`)를 안전하게 추출하여 UI에 표시합니다.

---

### 리소스 및 힌트

* **핵심 의존성:** `androidx.navigation:navigation-compose`
* **주요 Composable:** `NavHost`, `rememberNavController`
* **핵심 함수:** `navController.navigate(route: String)`
* **인자 전달:** 라우트 문자열에 `{argumentName}` 형태로 placeholder를 정의하고, `Maps` 함수 호출 시 실제 값을 포함한 문자열(예: `"detail/바나나"`)을 사용합니다.
* **인자 수신:** `NavHost`의 `composable` 함수 정의 시 `arguments` 파라미터를 통해 `navArgument`를 정의하고, `NavBackStackEntry` 객체의 `arguments` 프로퍼티에서 값을 추출합니다.
* **데이터:** 이 과제에서는 복잡한 상태 관리나 외부 데이터 로딩 없이, 간단한 하드코딩된 리스트를 사용해도 충분합니다. `ViewModel` 사용은 필수는 아닙니다.

---

### 학습 목표

* Compose Navigation의 기본 설정 방법을 이해합니다.
* `NavHost`와 `NavGraph`를 구성하여 여러 화면(Composable)을 정의하는 방법을 배웁니다.
* `NavController`를 사용하여 화면 간 이동을 구현하는 방법을 익힙니다.
* 내비게이션 시 화면 간에 간단한 데이터를 인자(argument)로 전달하고 받는 방법을 학습합니다.

---

## 작업 계획
### 화면 구현
- 리스트
  - 데이터는 하드코딩 무방
  - 클릭시 상세 이동
  - 네비게이션 사용
- 상세
  - NavBackStackEntry의 arguments 로 아이템 전달 받음
  - 화면에 전달받은 아이템 이름 표시
  - 별도 뒤로가기 아이콘 필요없음, 시스템 백버튼 사용

### 요구사항
- 네비게이션
  - detail/사과
  - 전달된 인자를 안전하게 추출하여 UI 표시

### 작업 순서
- 리스트 화면 구현
- 상세 화면 구현
- 네비게이션 의존성 추가
- 네비게이션 컴포즈 구현
  - NavHost
  - NavGraph
  - NavController
  - navArgument
- 상세 이동 연결