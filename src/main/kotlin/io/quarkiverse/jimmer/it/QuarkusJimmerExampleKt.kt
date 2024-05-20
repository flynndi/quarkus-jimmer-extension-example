package io.quarkiverse.jimmer.it

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path

@ApplicationScoped
@QuarkusMain
@Path("QuarkusJimmerExampleKt")
class QuarkusJimmerExampleKt: QuarkusApplication {

    // java -agentlib:native-image-agent=config-output-dir=/Users/brucewayne/Code/quarkus-jimmer-example-kt/src -jar /Users/brucewayne/Code/quarkus-jimmer-example-kt/build/quarkus-jimmer-example-kt-unspecified-runner.jar

    companion object {
        @JvmStatic
        fun main(array: Array<String>) {
            Quarkus.run(QuarkusJimmerExampleKt::class.java)
        }
    }

    override fun run(vararg args: String?): Int {
        Quarkus.waitForExit()
        return 0
    }

    @POST
    @Path("shutdown")
    fun shutdownTaigaQuarkus() {
        Quarkus.asyncExit()
    }
}