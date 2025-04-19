package com.ys.commerce.productdetail.data.di

import com.ys.commerce.productdetail.data.repository.ProductDetailRepository
import com.ys.commerce.productdetail.data.repository.ProductDetailRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Binds
    fun providesProductDetailRepository(productDetailRepositoryImpl: ProductDetailRepositoryImpl): ProductDetailRepository
}