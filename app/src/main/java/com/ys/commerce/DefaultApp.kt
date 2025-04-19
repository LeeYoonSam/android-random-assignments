package com.ys.commerce

import android.app.Application
import com.ys.commerce.product.data.repository.ProductRepository
import com.ys.commerce.product.data.repository.ProductRepositoryImpl
import com.ys.commerce.servicelocator.ProductServiceLocator

class DefaultApp : Application() {
    override fun onCreate() {
        super.onCreate()
        registerProductServiceLocator()
    }

    private fun registerProductServiceLocator() {
        val productRepository = ProductRepositoryImpl()
        ProductServiceLocator.register(ProductRepository::class, productRepository)
    }
}