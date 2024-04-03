package io.quarkiverse.jimmer.it.config

import io.quarkiverse.jimmer.it.entity.TenantAware
import io.quarkiverse.jimmer.it.entity.tenant
import io.quarkus.arc.Unremovable
import jakarta.enterprise.context.ApplicationScoped
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.filter.KFilter
import org.babyfish.jimmer.sql.kt.filter.KFilterArgs

@ApplicationScoped
@Unremovable
class TenantFilter(
    private val tenantProvider: TenantProvider
): KFilter<TenantAware> {

    override fun filter(args: KFilterArgs<TenantAware>) {
        tenantProvider.tenant.let {
            args.apply {
                where(table.tenant.eq(it))
            }
        }
    }
}