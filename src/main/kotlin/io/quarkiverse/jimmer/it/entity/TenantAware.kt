package io.quarkiverse.jimmer.it.entity

import org.babyfish.jimmer.sql.MappedSuperclass

@MappedSuperclass
interface TenantAware {

    val tenant: String
}