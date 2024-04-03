package io.quarkiverse.jimmer.it.repository

import io.quarkiverse.jimmer.it.entity.UserRole
import io.quarkiverse.jimmer.runtime.repository.KRepository
import io.quarkus.agroal.DataSource
import jakarta.enterprise.context.ApplicationScoped
import java.util.UUID

@ApplicationScoped
@DataSource("DB2")
class UserRoleRepository: KRepository<UserRole, UUID>