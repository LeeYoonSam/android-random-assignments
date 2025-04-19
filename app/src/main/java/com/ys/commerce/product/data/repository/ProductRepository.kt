package com.ys.commerce.product.data.repository

import com.ys.commerce.product.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(query: String?): Flow<List<Product>>
}