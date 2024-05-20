package io.quarkiverse.jimmer.it.resource

import io.quarkiverse.jimmer.it.repository.UserRoleRepository
import io.quarkus.agroal.DataSource
import io.smallrye.common.annotation.RunOnVirtualThread
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.babyfish.jimmer.client.meta.Api
import org.jboss.resteasy.reactive.RestQuery
import java.util.*

@Path("/userRoleResources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api("userRoleResource")
@RunOnVirtualThread
class UserRoleResource(

    @DataSource("DB2")
    private val userRoleRepository: UserRoleRepository
) {

    @GET
    @Path("/userRoleFindById")
    @Api
    fun userRoleFindById(@RestQuery id: UUID): Response {
        return Response.ok(userRoleRepository.findById(id)).build()
    }
}