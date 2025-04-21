# 위치 권한 요청 및 현재 위치 표시

**과제 목표:** Android의 런타임 권한(Runtime Permission) 모델을 이해하고, Jetpack Compose 환경에서 위치 권한(`ACCESS_FINE_LOCATION`)을 사용자에게 요청합니다. 권한 상태에 따라 적절한 UI를 표시하고, 권한이 최종적으로 허용되면 Fused Location Provider API를 사용하여 사용자의 마지막 알려진 위치(위도/경도)를 가져와 화면에 표시하는 기능을 구현합니다.

**권장 학습 시간:** 1시간 30분 (권한 처리 및 위치 API 연동 포함)

**개발 환경:** Android Studio, Kotlin, Jetpack Compose

---

### 사전 준비 사항

1.  **`AndroidManifest.xml` 수정:**
  * 앱의 `AndroidManifest.xml` 파일에 위치 권한 사용 선언을 추가합니다.
      ```xml
      <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
      ```

2.  **라이브러리 의존성 추가 (`build.gradle(app)`):**
  * Fused Location Provider API 사용을 위해 Google Play Services Location 라이브러리를 추가합니다.
      ```gradle
      implementation 'com.google.android.gms:play-services-location:21.2.0' // 최신 버전 확인 권장
      ```
  * Compose에서 Activity Result API를 사용하기 위한 라이브러리를 추가합니다. (이미 포함되어 있을 수 있음)
      ```gradle
      implementation "androidx.activity:activity-compose:1.9.0" // 최신 버전 확인 권장
      ```

---

### 요구 사항

1.  **화면 구성 및 상태 관리 (Compose UI):**
  * 단일 화면을 `@Composable` 함수로 구현합니다.
  * `ViewModel` 사용은 선택 사항입니다. 이 과제에서는 Composable 내에서 `remember`와 상태 관리를 사용하여 권한 및 위치 상태를 관리해도 괜찮습니다.
  * **권한 상태 관리:** 현재 위치 권한 상태 (`Granted`, `Denied`, `Permanently Denied`/`Needs Rationale`)를 추적하는 상태 변수를 관리합니다.
  * **위치 정보 상태 관리:** 위도/경도 값을 저장하거나 로딩/오류 상태를 나타내는 상태 변수를 관리합니다.

2.  **권한 처리 로직:**
  * `ActivityResultContracts.RequestPermission()` 컨트랙트와 `rememberLauncherForActivityResult`를 사용하여 권한 요청 런처를 등록합니다.
  * Composable이 처음 시작될 때 현재 권한 상태를 확인합니다. (`LocalContext.current.checkSelfPermission`)
  * **UI 분기 처리:**
    * **권한이 이미 허용된 경우 (Granted):**
      * "위치 권한이 허용되었습니다." 와 같은 텍스트를 표시합니다.
      * 위도와 경도를 표시할 `Text` Composable 두 개를 배치합니다. (초기값: "위도: -", "경도: -")
      * "현재 위치 가져오기" `Button`을 표시합니다.
    * **권한이 거부되었거나 아직 요청하지 않은 경우 (Denied / Not Requested):**
      * "정확한 위치 정보를 표시하려면 위치 권한이 필요합니다." 와 같이 권한이 필요한 이유를 설명하는 텍스트를 표시합니다.
      * "권한 요청" `Button`을 표시합니다.
    * **권한이 영구적으로 거부된 경우 (Permanently Denied / ShouldShowRequestPermissionRationale = false بعد 거부):**
      * "위치 권한이 영구적으로 거부되었습니다. 앱 기능 사용을 원하시면 앱 설정에서 직접 권한을 허용해주세요." 와 같이 설정으로 유도하는 텍스트를 표시합니다. (권한 요청 버튼 비활성화 또는 미표시)
  * "권한 요청" 버튼 클릭 시, 등록된 런처를 사용하여 시스템 권한 요청 대화상자를 표시합니다 (`launcher.launch(permission)`).
  * 권한 요청 결과 콜백에서 반환된 결과를 바탕으로 관리 중인 권한 상태를 업데이트하여 UI가 적절히 변경되도록 합니다.

3.  **위치 정보 가져오기:**
  * "현재 위치 가져오기" 버튼이 클릭되거나, 권한이 방금 허용되었을 때 위치 정보를 가져오는 로직을 실행합니다.
  * `LocalContext.current`를 사용하여 `Context`를 얻고, `LocationServices.getFusedLocationProviderClient(context)`를 통해 `FusedLocationProviderClient` 인스턴스를 가져옵니다.
  * `fusedLocationClient.lastLocation`을 호출하여 마지막 위치 정보를 비동기적으로 가져옵니다. ( **주의:** `lastLocation` 사용 시 반드시 권한이 허용되었는지 다시 확인하는 코드를 포함해야 `SecurityException`을 방지할 수 있습니다.)
  * `addOnSuccessListener`를 통해 위치 정보를 성공적으로 가져오면, 관리 중인 위치 정보 상태를 업데이트하여 위도/경도 `Text` Composable에 표시합니다.
  * `lastLocation` 결과가 `null`일 수 있습니다 (예: 기기 위치 설정 꺼짐, 새로운 기기). 이 경우 "위치를 찾을 수 없습니다." 와 같은 메시지를 표시합니다.
  * `addOnFailureListener`를 통해 위치 정보 가져오기 실패 시 오류 처리를 합니다 (예: 로그 출력, 간단한 오류 메시지 표시).

---

### 리소스 및 힌트

* **권한 확인:** `ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)`
* **런처 등록:** `rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> ... }`
* **영구 거부 확인:** 권한 요청 후 `isGranted`가 `false`일 때, `ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)` 를 확인하여 `false`이면 영구 거부 상태일 가능성이 높습니다. (Activity 컨텍스트 필요)
* **위치 클라이언트:** `LocationServices.getFusedLocationProviderClient(context)`
* **마지막 위치 가져오기:** `fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? -> ... }`
* **컨텍스트 얻기:** Compose 내부에서는 `LocalContext.current`를 사용합니다.
* **액티비티 얻기 (필요시):** `LocalContext.current as? Activity` (하지만 `shouldShowRequestPermissionRationale` 확인 등에만 제한적으로 사용)

---

### 학습 목표

* Android의 런타임 권한 요청 및 처리 흐름을 이해합니다.
* Jetpack Compose 환경에서 `rememberLauncherForActivityResult`를 사용하여 권한 요청을 처리하는 방법을 익힙니다.
* 권한 상태(허용, 거부, 영구 거부)에 따라 다른 UI를 표시하는 방법을 학습합니다.
* `FusedLocationProviderClient`를 사용하여 사용자의 위치 정보를 가져오는 방법을 이해합니다.
* 비동기 작업(위치 정보 가져오기)의 결과를 Compose 상태와 연동하여 UI를 업데이트하는 방법을 연습합니다.

---

## 작업
화면 구성
- 단일 화면
- 권한 상태 관리
- 위치 정보 상태 관리

권한 로직 처리
- 권한 이미 허용
  - 위치 권한이 허용 되었습니다. 텍스트 표시
  - 위도: -, 경도: - (초기값)
  - 현재 위치 가져오기 버튼 
- 권한 거부, 요청전
  - 정확한 위치 정보를 표시하러면 위치 권한이 필요합니다. 텍스트 표시
  - 권한 요청 버튼
    - 등록된 런처를 사용하여 시스템 권한 요청 대화상자 표시
    - 권한 요청 결과 콜백에서 반환된 결과를 바탕으로 관리 중인 권한 상태 업데이트
- 권한이 영구적으로 거부된 경우
  - 위치 권한이 영구적으로 거부되었습니다. 앱 기능 사용을 원하시면 앱 설정에서 직접 권한을 허용해주세요. 텍스트 표시

위치 정보 가져오기
- 마지막 위치 정보를 비동기적으로 가져옴
- 결과가 null(기기 위치 설정 꺼짐, 새로운 기기) 일 경우 "위치를 찾을 수 없습니다."
- 위치 정보 가져오기 실패시 오류 처리(로그, 간단한 오류 메시지 표시)