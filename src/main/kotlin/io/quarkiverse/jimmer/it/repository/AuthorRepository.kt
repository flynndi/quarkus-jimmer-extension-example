package io.quarkiverse.jimmer.it.repository

import io.quarkiverse.jimmer.it.entity.Author
import io.quarkiverse.jimmer.runtime.repository.KRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AuthorRepository: KRepository<Author, Long>