package com.ys.commerce.productdetail.data.repository

import com.ys.commerce.productdetail.data.model.ProductInfo
import kotlinx.coroutines.flow.Flow

interface ProductDetailRepository {
    fun getProductDetail(productId: Int) : Flow<Result<ProductInfo>>
}