package io.quarkiverse.jimmer.it.entity

import org.babyfish.jimmer.sql.*

@Entity
interface BookStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    val name: String

    val website: String?

    @OneToMany(mappedBy = "store")
    val books: List<Book>
}