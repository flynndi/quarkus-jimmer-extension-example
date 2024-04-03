package io.quarkiverse.jimmer.it.config

import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class TenantProvider {

    val tenant: String
        get() {
            return "a"
        }
}