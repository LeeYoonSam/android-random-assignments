package com.ys.commerce.productdetail.presentation.product.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ys.commerce.productdetail.data.model.ProductInfo
import com.ys.commerce.productdetail.data.model.sampleProductInfos
import com.ys.commerce.productdetail.data.repository.ProductDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    repository: ProductDetailRepository
) : ViewModel() {
    private val productIdFlow = MutableStateFlow(3)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProductDetailUiState> = productIdFlow
        .flatMapLatest {
            repository.getProductDetail(it)
        }
        .mapLatest { result ->
            result.fold(
                onSuccess = { ProductDetailUiState.Success(it) },
                onFailure = { ProductDetailUiState.Error(it) }
            )
        }
        .catch {
            ProductDetailUiState.Error(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProductDetailUiState.Loading
        )

    // 상품 변경 테스트
    fun changeProduct() {
        productIdFlow.value = Random.nextInt(1, sampleProductInfos.size)
    }
}

sealed interface ProductDetailUiState {
    data object Loading : ProductDetailUiState
    data class Success(val productInfo: ProductInfo) : ProductDetailUiState
    data class Error(val throwable: Throwable) : ProductDetailUiState
}