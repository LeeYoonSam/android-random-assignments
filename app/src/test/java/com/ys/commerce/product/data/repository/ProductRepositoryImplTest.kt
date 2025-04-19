package com.ys.commerce.product.data.repository

import com.ys.commerce.product.data.model.productSamples
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProductRepositoryImplTest {
    private lateinit var repository: ProductRepository

    @Before
    fun setUp() {
        repository = ProductRepositoryImpl()
    }

    @Test
    fun `getProducts with null query`() = runTest {
        val result = repository.getProducts(null).first()
        assertEquals(
            expected = productSamples,
            actual = result,
        )
    }

    @Test
    fun `getProducts with empty query`() = runTest {
        val result = repository.getProducts("").first()
        assertEquals(
            expected = productSamples,
            actual = result,
        )
    }

    @Test
    fun `getProducts with blank query`() = runTest {
        val result = repository.getProducts("   ").first()
        assertEquals(
            expected = productSamples,
            actual = result,
        )
    }

    @Test
    fun `getProducts with matching product name`() = runTest {
        val query = "웹캠"
        val expectedProduct = productSamples.first { it.name == query }
        val result = repository.getProducts(query).first()
        assertEquals(
            expected = listOf(expectedProduct),
            actual = result,
        )
    }

    @Test
    fun `getProducts with no matching product name`() = runTest {
        val query = "NonExistent Product"
        val result = repository.getProducts(query).first()
        assertEquals(
            expected = emptyList(),
            actual = result,
        )
    }

    @Test
    fun `getProducts with partial matching product name`() = runTest {
        val query = "스"
        val expectedProducts = productSamples.filter { it.name.contains(query) }
        val result = repository.getProducts(query).first()
        assertEquals(
            expected = expectedProducts,
            actual = result,
        )
    }

    @Test
    fun `getProducts with multiple matching product names`() = runTest {
        val query = "Product"
        val expectedProducts = productSamples.filter { it.name.contains(query) }
        val result = repository.getProducts(query).first()
        assertEquals(
            expected = expectedProducts,
            actual = result,
        )
    }

    @Test
    fun `getProducts with case insensitive matching`() = runTest {
        val query = "product a"
        val result = repository.getProducts(query).first()
        assertEquals(
            expected = listOf(),
            actual = result,
        )
    }

    @Test
    fun `getProducts with very long query`() = runTest {
        val query = "a".repeat(1000)
        val result = repository.getProducts(query).first()
        assertEquals(
            expected = emptyList(),
            actual = result,
        )
    }

    @Test
    fun `getProducts with single product in list`() = runTest {
        val singleProduct = productSamples.first()
        repository = ProductRepositoryImpl()

        // Matching query
        val matchingQuery = singleProduct.name
        val matchingResult = repository.getProducts(matchingQuery).first()
        assertEquals(
            expected = listOf(singleProduct),
            actual = matchingResult,
        )

        // Non-matching query
        val nonMatchingQuery = "NonExistent Product"
        val nonMatchingResult = repository.getProducts(nonMatchingQuery).first()
        assertEquals(
            expected = emptyList(),
            actual = nonMatchingResult,
        )
    }

    @Test
    fun `getProducts returns flow`() = runTest {
        val query = "Product A"
        val resultFlow = repository.getProducts(query)
        assertNotNull(resultFlow)
        val result = resultFlow.first()
        assertNotNull(result)
    }
}