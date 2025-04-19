```md
# 안드로이드 과제 테스트 (Compose - 상품 상세 화면)

- 과제 목표: 특정 상품 ID에 해당하는 상세 데이터를 Mock API를 통해 가져와, Jetpack Compose를 사용하여 상세 화면에 표시합니다.
- 제한 시간: 1시간
- 개발 환경: Android Studio, Kotlin, Jetpack Compose

## 요구 사항
1. 화면 구성 (Compose UI):

- 단일 화면을 @Composable 함수로 구현합니다.
- 미리 정의된 특정 상품 ID (예: productId = 3)에 대한 상세 정보를 표시하는 화면을 구현합니다.

2. 데이터 로딩 및 상태 관리:

- 앱 실행 시 또는 화면 진입 시, 지정된 productId를 사용하여 아래 제공된 Mock API 엔드포인트에서 상품 상세 데이터를 비동기적으로 가져옵니다. (실제 네트워크 호출 대신, 제공된 JSON 데이터를 파싱하여 사용해도 무방합니다.)
- ViewModel을 사용하여 상품 상세 데이터 상태(로딩 중, 성공, 실패)를 관리합니다.
- StateFlow 또는 LiveData를 사용하여 ViewModel의 상태를 Composable 함수에 전달하고, 상태 변경에 따라 UI가 자동으로 업데이트되도록 구현합니다. (collectAsStateWithLifecycle() 또는 collectAsState() 사용 권장).

3. UI 표시:

- 로딩 상태: 데이터 로딩 중에는 CircularProgressIndicator와 같은 로딩 UI를 표시합니다.
- 오류 상태: 데이터 로딩 실패 시, 사용자에게 오류가 발생했음을 알리는 Text 메시지를 표시합니다.
- 성공 상태: 데이터 로딩 성공 시, 다음 정보를 포함하여 상품 상세 내용을 화면에 표시합니다.
    - 상품 이미지 (Image Composable 사용): 상단에 표시하며, 제공된 imageUrl을 이용해 네트워크 이미지를 로드합니다. (Coil 또는 Glide 라이브러리 사용 권장)
    - 상품명 (Text Composable 사용): 이미지 아래에 상대적으로 큰 폰트로 표시합니다.
    - 상품 가격 (Text Composable 사용): 상품명 아래에 표시합니다. (가격 형식 적용은 선택 사항)
    - 상품 설명 (Text Composable 사용): 가격 아래에 상세 설명을 표시합니다. 내용이 길 경우 스크롤 가능하도록 처리하는 것을 권장합니다. (예: Column 내 verticalScroll modifier 사용)
- 각 UI 요소들은 Column Composable을 사용하여 수직으로 배치하고, 적절한 간격(Spacer Composable 또는 padding modifier 사용)을 둡니다.

## 리소스
{
    "id": 3,
    "name": "기계식 키보드",
    "price": 120000,
    "description": "타건감이 뛰어난 적축 기계식 키보드입니다. RGB 백라이트 기능과 커스텀 키 매핑을 지원하여 게이밍 및 작업 환경에 최적화되어 있습니다. USB-C 타입 연결을 지원합니다.",
    "imageUrl": "https://cdn.example.com/keyboard.jpg"
}

## 참고 사항
- 상태(로딩, 성공, 오류)에 따른 UI 분기 처리를 명확하게 구현해주세요.
- 네트워크 이미지 로딩 라이브러리를 올바르게 설정하고 사용해주세요.
- Compose의 상태 관리 및 UI 구성 요소 배치에 대한 이해를 바탕으로 코드를 작성해주세요.
- 제한 시간 내 요구 사항 완수에 집중하되, 코드 가독성과 구조를 고려해주세요.
```

## 라이브러리

### Hilt (의존성 주입)
* **의존성:** `implementation(libs.hilt.android)`, `ksp(libs.hilt.compiler)` (또는 `kapt`)
* **설명:** 
  * Hilt는 Dagger를 기반으로 하여 Android 앱에서 **의존성 주입(Dependency Injection, DI)** 을 더 쉽고 표준화된 방식으로 사용할 수 있도록 Google에서 제공하는 라이브러리입니다. 
  * `@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, `@Inject`, `@Module`, `@Provides` 등의 어노테이션을 사용하여 객체 생성 및 의존성 연결을 자동화합니다. 
  * 이를 통해 코드의 결합도를 낮추고, 테스트 용이성을 높이며, 보일러플레이트 코드를 크게 줄여줍니다. 
  * `hilt-android`는 런타임 및 핵심 어노테이션을 제공하고, `hilt-compiler`는 컴파일 시점에 필요한 코드를 생성하는 어노테이션 프로세서입니다 (`ksp` 또는 `kapt` 설정 필요).

### Hilt-navigation-compose (hiltViewModel)
* **의존성:** `implementation(libs.androidx.hilt.navigation.compose)`
* **설명:** 
  * Jetpack Navigation Compose 환경에서 **Hilt로 관리되는 ViewModel을 쉽게 주입**받을 수 있도록 도와주는 통합 라이브러리입니다. 
  * `@Composable` 함수 내에서 `hiltViewModel()` 함수를 호출하면, 현재 네비게이션 대상(NavBackStackEntry) 또는 네비게이션 그래프 범위에 맞는 ViewModel 인스턴스를 Hilt가 자동으로 생성하고 필요한 의존성을 주입하여 제공해줍니다. 
  * 이를 통해 각 Composable 화면이 자체 생명주기에 맞는 ViewModel을 간편하게 사용할 수 있습니다.

### Retrofit (Network 라이브러리)
* **의존성:** `implementation(libs.retrofit)`
* **설명:** 
  * Square에서 개발한 **타입 안전(Type-safe)한 HTTP 클라이언트 라이브러리**입니다. 
  * REST API 인터페이스를 Kotlin/Java 인터페이스로 정의하고 어노테이션을 사용하여 네트워크 요청(GET, POST 등)을 쉽게 구현할 수 있게 해줍니다. 
  * 내부적으로는 주로 OkHttp를 사용하여 실제 네트워크 통신을 수행하며, Converter 및 Call Adapter와 함께 사용하여 요청/응답 데이터 처리 및 비동기 처리를 간편하게 만듭니다. 
  * 네트워크 통신 관련 보일러플레이트 코드를 크게 줄여줍니다.

### okhttp-logging (Network 로깅)
* **의존성:** `com.squareup.okhttp3:logging-interceptor`
* **설명:** 
  * OkHttp 클라이언트(Retrofit이 내부적으로 사용하는 HTTP 클라이언트)에 **인터셉터(Interceptor)** 형태로 추가되어, 네트워크 요청 및 응답의 상세 정보(헤더, 본문 등)를 Logcat 등에 출력해주는 라이브러리입니다. 
  * 개발 및 디버깅 과정에서 **실제 네트워크 통신 내용을 확인**하는 데 매우 유용합니다. 보통 디버그 빌드에서만 상세 로그(Level.BODY)를 출력하도록 설정합니다.

### Serialization (Kotlinx Serialization)
* **의존성:** `implementation(libs.retrofit.kotlinx.serialization)`, `implementation(libs.kotlinx.serialization.json)`
* **설명:** Kotlin에서 공식적으로 지원하는 **직렬화(Serialization)/역직렬화(Deserialization) 라이브러리**입니다. Kotlin 객체(특히 data class)를 JSON, Protobuf 등 다른 데이터 포맷으로 변환하거나 그 반대로 변환하는 기능을 제공합니다.
    * `kotlinx-serialization-json`: JSON 포맷 처리를 위한 런타임 라이브러리입니다. `@Serializable` 어노테이션과 함께 사용됩니다.
    * `retrofit-kotlinx-serialization`: Retrofit에서 Kotlinx Serialization을 **ConverterFactory**로 사용할 수 있게 해주는 어댑터 라이브러리입니다. (Jake Wharton이 만듦) 이를 통해 Retrofit이 네트워크 응답(JSON)을 `@Serializable` Kotlin 객체로 자동 변환하거나, 요청 시 객체를 JSON으로 변환할 수 있습니다. (Gradle 플러그인 `org.jetbrains.kotlin.plugin.serialization` 설정도 필요)

### Coil (이미지로더)
* **의존성:** `implementation(libs.coil.compose)`
* **설명:** Coil(Coroutine Image Loader)은 Kotlin Coroutines 기반으로 만들어진 **현대적인 Android 이미지 로딩 및 캐싱 라이브러리**입니다. Jetpack Compose 환경에서 이미지를 쉽게 로드하고 표시하기 위한 확장 라이브러리(`coil-compose`)를 제공합니다.
    * `coil-compose`는 `AsyncImage`와 같은 Composable 함수나 `rememberAsyncImagePainter`와 같은 Painter를 제공하여 네트워크, 로컬 파일, 리소스 등 다양한 소스로부터 이미지를 비동기적으로 로드하고, 캐싱(메모리/디스크), 이미지 변환, 플레이스홀더/에러 처리 등을 Compose UI 내에서 간편하게 처리할 수 있도록 지원합니다. Glide와 함께 Compose 환경에서 널리 사용되는 이미지 로딩 라이브러리입니다.


## 주요 ContentScale 타입 및 동작 방식:

### ContentScale.Crop (자르기):

- 동작: 원본 이미지의 가로세로 비율을 유지하면서, 이미지의 너비와 높이가 모두 Composable 영역의 너비와 높이보다 크거나 같아지도록 이미지를 확대 또는 축소합니다.
- 결과: Composable 영역 전체가 이미지로 채워집니다. 하지만 원본 이미지와 Composable 영역의 비율이 다르면, 영역을 벗어나는 이미지의 일부(상하 또는 좌우)가 잘려 보이지 않게 됩니다. 기본적으로 중앙을 기준으로 자릅니다.
- 사용 예: 배경 이미지, 프로필 사진 등 영역을 빈틈없이 채우는 것이 중요하고 일부 잘려도 괜찮을 때 주로 사용됩니다. (사용자 코드 예시에서 사용됨)

### ContentScale.Fit (맞추기):

- 동작: 원본 이미지의 가로세로 비율을 유지하면서, 이미지의 너비와 높이가 모두 Composable 영역의 너비와 높이보다 작거나 같아지도록 이미지를 확대 또는 축소합니다.
- 결과: 이미지 전체가 Composable 영역 안에 보이게 됩니다 (잘리는 부분 없음). 하지만 원본 이미지와 Composable 영역의 비율이 다르면, 영역 내부에 빈 공간(레터박스 또는 필러박스)이 생깁니다.
- 사용 예: 이미지의 전체 내용을 잘림 없이 보여주는 것이 중요할 때 사용됩니다. (예: 사진 뷰어의 기본 보기)

### ContentScale.FillBounds (경계 채우기):

- 동작: 원본 이미지의 가로세로 비율을 무시하고, 이미지의 너비가 Composable 영역의 너비와 정확히 같아지고, 높이도 영역의 높이와 정확히 같아지도록 이미지를 강제로 늘리거나 줄입니다.
- 결과: 이미지가 Composable 영역을 정확히 채웁니다. 하지만 원본과 영역의 비율이 다르면 이미지가 찌그러지거나 늘어나 보일 수 있습니다.
- 사용 예: 비율 왜곡이 상관없거나 의도적으로 이미지를 영역에 꽉 채워야 할 때 사용될 수 있으나, 일반적인 이미지 표시에선 잘 사용되지 않습니다.

### ContentScale.FillWidth (너비 채우기):

- 동작: 원본 이미지의 가로세로 비율을 유지하면서, 이미지의 너비가 Composable 영역의 너비와 정확히 같아지도록 이미지를 확대 또는 축소합니다. 높이는 비율에 따라 자동으로 조절됩니다.
- 결과: 이미지가 Composable 영역의 전체 너비를 채웁니다. 만약 조정된 높이가 영역의 높이보다 크면 이미지의 상하 일부가 잘립니다. 조정된 높이가 영역 높이보다 작으면 위아래에 빈 공간이 생길 수 있습니다(Alignment에 따라).
- 사용 예: 가로 스크롤 배너 이미지 등 너비를 기준으로 이미지를 채우고 싶을 때 사용됩니다.

### ContentScale.FillHeight (높이 채우기):

- 동작: 원본 이미지의 가로세로 비율을 유지하면서, 이미지의 높이가 Composable 영역의 높이와 정확히 같아지도록 이미지를 확대 또는 축소합니다. 너비는 비율에 따라 자동으로 조절됩니다.
- 결과: 이미지가 Composable 영역의 전체 높이를 채웁니다. 만약 조정된 너비가 영역의 너비보다 크면 이미지의 좌우 일부가 잘립니다. 조정된 너비가 영역 너비보다 작으면 좌우에 빈 공간이 생길 수 있습니다(Alignment에 따라).
- 사용 예: 세로 스크롤 리스트의 아이템 이미지 등 높이를 기준으로 이미지를 채우고 싶을 때 사용됩니다.

### ContentScale.Inside (내부에 맞추기):

- 동작: 원본 이미지의 가로세로 비율을 유지합니다.
- 만약 원본 이미지가 Composable 영역보다 크면, 이미지 전체가 영역 안에 들어오도록 축소합니다 (Fit과 동일).
- 만약 원본 이미지가 Composable 영역보다 작으면, 이미지를 확대하지 않고 원본 크기 그대로 표시합니다 (기본적으로 중앙 정렬).
- 결과: 이미지가 영역 경계를 넘지 않도록 보장하며, 작은 이미지가 불필요하게 확대되어 깨져 보이는 것을 방지합니다. 빈 공간이 생길 수 있습니다.
- 사용 예: 원본 이미지 크기를 존중하면서 영역 안에 표시해야 할 때 유용합니다. (예: 아이콘 표시)

### ContentScale.None (스케일링 없음):

- 동작: 이미지 크기를 전혀 조절하지 않고 원본 크기 그대로 Composable 영역의 중앙(기본값)에 배치합니다.
- 결과: 원본 이미지가 영역보다 크면 잘리고, 작으면 주변에 빈 공간이 생깁니다.
- 사용 예: 이미지의 특정 부분만 타일처럼 표시하거나, 1:1 픽셀 매칭이 중요한 경우 등에 사용될 수 있습니다.