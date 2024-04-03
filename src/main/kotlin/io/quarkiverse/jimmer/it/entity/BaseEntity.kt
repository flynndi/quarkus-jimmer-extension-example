package io.quarkiverse.jimmer.it.entity

import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
interface BaseEntity {

    val createdTime: LocalDateTime

    val modifiedTime: LocalDateTime
}