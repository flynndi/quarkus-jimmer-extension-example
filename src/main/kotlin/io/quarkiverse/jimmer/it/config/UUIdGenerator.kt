package io.quarkiverse.jimmer.it.config

import org.babyfish.jimmer.sql.meta.UserIdGenerator
import java.util.*

class UUIdGenerator : UserIdGenerator<UUID> {

    override fun generate(entityType: Class<*>?): UUID {
        return UUID.randomUUID()
    }
}