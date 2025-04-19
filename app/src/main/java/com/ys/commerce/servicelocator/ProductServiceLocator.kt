package com.ys.commerce.servicelocator

import kotlin.reflect.KClass

object ProductServiceLocator {
    // 서비스 인스턴스를 저장할 맵 (Key: 클래스 타입, Value: 서비스 인스턴스)
    private val services = mutableMapOf<KClass<*>, Any>()

    // 서비스 등록 메소드
    fun <T : Any> register(serviceClass: KClass<T>, instance: T) {
        // 스레드 안전성을 위해 synchronized 사용 고려 가능
        services[serviceClass] = instance
    }

    // 서비스 가져오는 메소드
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(serviceClass: KClass<T>): T {
        return services[serviceClass] as? T
            ?: throw IllegalArgumentException("Service not found for class ${serviceClass.simpleName}. Did you register it?")
    }

    // Kotlin의 reified 타입 파라미터를 사용한 편의 메소드
    inline fun <reified T : Any> get(): T = get(T::class)

    // 테스트 등을 위한 클리어 함수 (선택 사항)
    fun clear() {
        services.clear()
    }
}
