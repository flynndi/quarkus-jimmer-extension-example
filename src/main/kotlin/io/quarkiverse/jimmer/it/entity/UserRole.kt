package io.quarkiverse.jimmer.it.entity

import io.quarkiverse.jimmer.it.config.UUIdGenerator
import io.quarkiverse.jimmer.it.config.jsonmapping.AuthUser
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.LogicalDeleted
import java.util.*

@Entity
interface UserRole {

    @Id
    @GeneratedValue(generatorType = UUIdGenerator::class)
    val id: UUID

    val userId: String

    val roleId: String

    @LogicalDeleted("true")
    val deleteFlag: Boolean

    val authUser: AuthUser?
}