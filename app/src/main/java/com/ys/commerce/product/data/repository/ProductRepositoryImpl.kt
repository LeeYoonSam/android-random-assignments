package com.ys.commerce.product.data.repository

import com.ys.commerce.product.data.model.Product
import com.ys.commerce.product.data.model.productSamples
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl : ProductRepository {
    override fun getProducts(query: String?): Flow<List<Product>> {
        return flow {
            if (query.isNullOrBlank()) {
                emit(productSamples) // 검색어가 비어있으면 전체 리스트 반환
            } else {
                // 실제 필터링 로직 (대소문자 무시 등)
                val filteredProducts = productSamples.filter {
                    it.name.contains(query, ignoreCase = true)
                }
                emit(filteredProducts)
            }
        }
    }
}