package io.quarkiverse.jimmer.it.service

import io.quarkiverse.jimmer.it.entity.UserRole
import io.quarkiverse.jimmer.it.repository.UserRoleRepository
import io.quarkus.agroal.DataSource
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
@DataSource("DB2")
class UserRoleService {

    @Inject
    @field:DataSource("DB2")
    lateinit var userRoleRepository: UserRoleRepository

    fun findAll() : List<UserRole> {
        return userRoleRepository.findAll()
    }
}