package io.quarkiverse.jimmer.it.service

import io.quarkiverse.jimmer.it.entity.BookStore
import io.quarkiverse.jimmer.it.repository.BookStoreRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject

@ApplicationScoped
class BookStoreService {

    @Inject
    @field:Default
    lateinit var bookStoreRepository: BookStoreRepository

    fun findAll() : List<BookStore> {
        return bookStoreRepository.findAll()
    }
}