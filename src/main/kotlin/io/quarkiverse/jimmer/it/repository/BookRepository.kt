package io.quarkiverse.jimmer.it.repository

import io.quarkiverse.jimmer.it.entity.Book
import io.quarkiverse.jimmer.runtime.repository.KRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class BookRepository: KRepository<Book, Long> {

    fun selectBookById(id: Long): Book? {
        return sql().findById(Book::class, id)
    }
}