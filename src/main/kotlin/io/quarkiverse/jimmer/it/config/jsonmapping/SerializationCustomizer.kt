package io.quarkiverse.jimmer.it.config.jsonmapping

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.arc.Unremovable
import jakarta.enterprise.context.ApplicationScoped
import org.babyfish.jimmer.sql.kt.cfg.KCustomizer
import org.babyfish.jimmer.sql.kt.cfg.KSqlClientDsl

@ApplicationScoped
@Unremovable
class SerializationCustomizer(
    private val objectMapper: ObjectMapper
): KCustomizer {

    override fun customize(dsl: KSqlClientDsl) {
        dsl
            .setSerializedTypeObjectMapper(
                AuthUser::class,
                objectMapper.addMixIn(AuthUser::class.java, AuthUserMixin::class.java)
                    .enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION)
            )
    }
}