package com.ys.commerce.productdetail.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductInfo(
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    val imageUrl: String
)

val sampleProductInfos = listOf(
    ProductInfo(
        id = 1,
        name = "마우스",
        price = 80000,
        description = "초경량 마우스. 단순하지만 단순하지 않습니다. 우리는 X2 구조를 설계 할 때 항상 이것을 염두에 두었습니다. 구조를 최대한 단순하게 설계했지만 내구성을 유지했습니다. 그 결과 손에 든 계란보다 가벼운 고성능 게이밍 마우스를 만들게 되었습니다.",
        imageUrl = "https://cdn.pixabay.com/photo/2013/07/12/17/41/computer-mouse-152249_960_720.png",
    ),
    ProductInfo(
        id = 2,
        name = "24인치 모니터",
        price = 220000,
        description = "더 선명하고 넓은 화면, 몰입감 높은 넓은 시야, 초고속 반응, 165Hz 주사율과 1ms 응답 속도",
        imageUrl = "https://cdn.pixabay.com/photo/2015/06/20/09/02/monitor-815614_960_720.png",
    ),
    ProductInfo(
        id = 3,
        name = "기계식 키보드",
        price = 120000,
        description = "타건감이 뛰어난 적축 기계식 키보드입니다. RGB 백라이트 기능과 커스텀 키 매핑을 지원하여 게이밍 및 작업 환경에 최적화되어 있습니다. USB-C 타입 연결을 지원합니다.",
        imageUrl = "https://cdn.pixabay.com/photo/2013/07/13/14/06/keyboard-162135_960_720.png",
    ),
)