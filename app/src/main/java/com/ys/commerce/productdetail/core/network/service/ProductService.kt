package com.ys.commerce.productdetail.core.network.service

import com.ys.commerce.productdetail.data.model.ProductInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("/products")
    suspend fun fetchProductDetailInfo(@Query("productId") productId: Int): ProductInfo
}