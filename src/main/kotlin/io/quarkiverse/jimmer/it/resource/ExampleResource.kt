package io.quarkiverse.jimmer.it.resource

import io.quarkiverse.jimmer.it.entity.BookStore
import io.quarkiverse.jimmer.it.entity.UserRole
import io.quarkiverse.jimmer.it.entity.fetchBy
import io.quarkiverse.jimmer.it.repository.BookStoreRepository
import io.quarkiverse.jimmer.it.repository.UserRoleRepository
import io.quarkiverse.jimmer.it.service.BookStoreService
import io.quarkiverse.jimmer.it.service.UserRoleService
import io.quarkus.agroal.DataSource
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.babyfish.jimmer.sql.kt.KSqlClient

@Path("/hello")
class ExampleResource {

    @Inject
    @field: Default
    lateinit var kSqlClient: KSqlClient

    @Inject
    @field:DataSource("DB2")
    lateinit var kSqlClientDB2: KSqlClient

    @Inject
    @field: Default
    lateinit var bookStoreRepository: BookStoreRepository

    @Inject
    @field:DataSource("DB2")
    lateinit var userRoleRepository: UserRoleRepository

    @Inject
    @field:Default
    lateinit var bookStoreService: BookStoreService

    @Inject
    @field:DataSource("DB2")
    lateinit var userRoleService: UserRoleService

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = "Hello from RESTEasy Reactive"

    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    fun test(): Response {
        val bookStores = kSqlClient
            .createQuery(BookStore::class) {
                select(
                    table.fetchBy {
                        name()
                        website()
                    }
                )
            }.execute()
        return Response.ok(bookStores).build()
    }

    @GET
    @Path("test2")
    @Produces(MediaType.APPLICATION_JSON)
    fun test2(): Response {
        val list = bookStoreRepository.findAll()
        return Response.ok(list).build()
    }

    @GET
    @Path("test3")
    @Produces(MediaType.APPLICATION_JSON)
    fun test3(): Response {
        return Response.ok(userRoleRepository.findAll()).build()
    }

    @POST
    @Path("test4")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    fun test4(userRole: UserRole) : Response {
        return Response.ok(userRoleRepository.save(userRole)).build()
    }

    @GET
    @Path("test5")
    @Produces(MediaType.APPLICATION_JSON)
    fun test5(): Response {
        return Response.ok(bookStoreService.findAll()).build()
    }

    @GET
    @Path("test6")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun test6(): Response {
        return Response.ok(userRoleService.findAll()).build()
    }
}