package io.quarkiverse.jimmer.it.config

import io.quarkus.arc.Unremovable
import jakarta.enterprise.context.ApplicationScoped
import org.babyfish.jimmer.sql.runtime.ScalarProvider
import java.util.*

@ApplicationScoped
@Unremovable
class UUIdScalarProvider : ScalarProvider<UUID, String> {

    override fun toScalar(sqlValue: String): UUID {
        return UUID.fromString(sqlValue)
    }

    override fun toSql(scalarValue: UUID): String {
        return scalarValue.toString()
    }
}