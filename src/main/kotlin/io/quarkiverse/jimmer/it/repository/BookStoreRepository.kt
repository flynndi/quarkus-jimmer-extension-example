package io.quarkiverse.jimmer.it.repository

import io.quarkiverse.jimmer.it.entity.BookStore
import io.quarkiverse.jimmer.runtime.repository.KRepository
import io.quarkus.agroal.DataSource
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class BookStoreRepository: KRepository<BookStore, Long>