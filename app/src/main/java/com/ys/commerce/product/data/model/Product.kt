package com.ys.commerce.product.data.model

data class Product(
    val id: Long,
    val name: String,
    val price: Long
)

val productSamples = listOf(
    Product(1, "노트북 거치대", 25000),
    Product(2, "무선 마우스", 18000),
    Product(3, "기계식 키보드", 120000),
    Product(4, "블루투스 이어폰", 89000),
    Product(5, "웹캠", 45000),
    Product(6, "USB 허브", 22000),
    Product(7, "스마트폰 무선 충전기", 30000),
    Product(8, "휴대용 모니터", 250000),
    Product(9, "외장 SSD 1TB", 150000),
    Product(10, "게이밍 마우스 패드", 15000)
)
