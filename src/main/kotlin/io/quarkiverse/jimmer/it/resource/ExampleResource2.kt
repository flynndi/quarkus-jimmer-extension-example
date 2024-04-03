package io.quarkiverse.jimmer.it.resource

import io.quarkiverse.jimmer.it.repository.BookStoreRepository
import io.quarkiverse.jimmer.it.repository.UserRoleRepository
import io.quarkiverse.jimmer.it.service.BookStoreService
import io.quarkiverse.jimmer.it.service.UserRoleService
import io.quarkus.agroal.DataSource
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.babyfish.jimmer.sql.kt.KSqlClient

@Path("/hello2")
class ExampleResource2(

    private val kSqlClient: KSqlClient,

    private val kSqlClientDB2: KSqlClient,

    private val bookStoreService: BookStoreService,

    @DataSource("DB2")
    private val userRoleService: UserRoleService,

    private val bookStoreRepository: BookStoreRepository,

    @DataSource("DB2")
    private val userRoleRepository: UserRoleRepository
) {

    @GET
    @Path("test5")
    @Produces(MediaType.APPLICATION_JSON)
    fun test5() : Response {
        return Response.ok(bookStoreRepository.findAll()).build()
    }

    @GET
    @Path("test6")
    @Produces(MediaType.APPLICATION_JSON)
    fun test6() : Response {
        return Response.ok(userRoleRepository.findAll()).build()
    }

}