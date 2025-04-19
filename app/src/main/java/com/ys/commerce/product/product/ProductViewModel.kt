package com.ys.commerce.product.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ys.commerce.product.data.model.Product
import com.ys.commerce.product.data.repository.ProductRepository
import com.ys.commerce.servicelocator.ProductServiceLocator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModel : ViewModel() {
    private val productRepository = ProductServiceLocator.get<ProductRepository>()

    // UI 입력을 즉시 반영할 검색어 StateFlow
    private val _searchQuery = MutableStateFlow("")

    // Debounce 적용된 검색어 Flow (중복 방출 방지 추가)
    @OptIn(FlowPreview::class)
    private val debouncedQuery: Flow<String> = _searchQuery
        .debounce(500L) // 500ms 디바운스
        .distinctUntilChanged() // 실제 검색어가 변경되었을 때만 진행

    val uiState: StateFlow<ProductUiState> = debouncedQuery
        .flatMapLatest { query ->
            // 검색어가 비어있으면 null 또는 빈 문자열 전달 (Repository 구현에 따라 결정)
            val effectiveQuery = query.takeIf { it.isNotBlank() }
            productRepository.getProducts(effectiveQuery) // Flow<List<Product>> 반환
                .map<List<Product>, ProductUiState> { products ->
                    // Repository에서 성공적으로 리스트를 받으면 Success 상태로 변환
                    ProductUiState.Success(products)
                }
                .catch { throwable ->
                    // Repository Flow 처리 중 에러 발생 시 Error 상태 방출
                    emit(ProductUiState.Error(throwable))
                }
        }.catch {
            ProductUiState.Error(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProductUiState.Loading
        )

    fun searchWord(word: String) {
        _searchQuery.value = word
    }
}

sealed interface ProductUiState {
    data object Loading : ProductUiState
    data class Success(val products: List<Product>) : ProductUiState
    data class Error(val throwable: Throwable) : ProductUiState
}