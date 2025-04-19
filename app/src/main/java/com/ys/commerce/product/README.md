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
```