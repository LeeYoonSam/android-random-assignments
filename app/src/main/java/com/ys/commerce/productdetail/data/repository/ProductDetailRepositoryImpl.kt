package com.ys.commerce.productdetail.data.repository

import com.ys.commerce.productdetail.core.network.service.ProductService
import com.ys.commerce.productdetail.data.model.ProductInfo
import com.ys.commerce.productdetail.data.model.sampleProductInfos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductDetailRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductDetailRepository {
    override fun getProductDetail(productId: Int): Flow<Result<ProductInfo>> = flow {
        try {
            val fakeInfo = productService.fetchProductDetailInfo(productId).runCatching {
                this
            }
            emit(fakeInfo)
        } catch (e: Exception) {
            val productInfo = sampleProductInfos.firstOrNull { it.id == productId }
            productInfo?.let {
                delay(1000)
                emit(Result.success(productInfo))
            } ?: emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}