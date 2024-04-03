package io.quarkiverse.jimmer.it.config

import io.agroal.api.AgroalDataSource
import io.quarkus.agroal.DataSource
import io.quarkus.arc.Arc
import io.quarkus.runtime.LaunchMode
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import org.babyfish.jimmer.sql.ast.impl.query.ConfigurableRootQueryImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import java.util.Locale

@ApplicationScoped
class AppLifecycleBean {

    private val LOGGER: Logger = LoggerFactory.getLogger(AppLifecycleBean::class.java)

    @Inject
    @field:Default
    lateinit var agroalDataSource: AgroalDataSource

    @Inject
    @DataSource("DB2")
    lateinit var agroalDataSourceDB2: AgroalDataSource

    @Throws(Exception::class)
    fun onStart(@Observes ev: StartupEvent) {
        LOGGER.info("Default Charset = " + Charset.defaultCharset())
        LOGGER.info("file.encoding = " + Charset.defaultCharset().displayName())
        LOGGER.info("Default Charset in use = " + this.getDefaultCharset())
        LOGGER.info("The application is starting...")
        LOGGER.info("The application model is " + LaunchMode.current().defaultProfile)
        var kClass = Locale.getAvailableLocales()::class
        println(
            kClass
        )
        this.initH2DB1()
        this.initH2DB2()
    }

    private fun getDefaultCharset(): String {
        val outputStreamWriter = OutputStreamWriter(ByteArrayOutputStream())
        return outputStreamWriter.encoding
    }

    fun onStop(@Observes ev: ShutdownEvent) {
        LOGGER.info("The application is stopping...")
    }

    @Throws(Exception::class)
    private fun initH2DB1() {
        agroalDataSource.connection.use { con ->
            val inputStream = AppLifecycleBean::class.java
                .classLoader
                .getResourceAsStream("h2-database.sql")
            if (inputStream == null) {
                throw RuntimeException("no `h2-database.sql`")
            }
            InputStreamReader(inputStream).use { reader ->
                val buf = CharArray(1024)
                val builder = StringBuilder()
                while (true) {
                    val len = reader.read(buf)
                    if (len == -1) {
                        break
                    }
                    builder.append(buf, 0, len)
                }
                con.createStatement().execute(builder.toString())
            }
        }
    }

    @Throws(Exception::class)
    private fun initH2DB2() {
        agroalDataSourceDB2.connection.use { con ->
            val inputStream = AppLifecycleBean::class.java
                .classLoader
                .getResourceAsStream("h2-database2.sql")
            if (inputStream == null) {
                throw RuntimeException("no `h2-database2.sql`")
            }
            InputStreamReader(inputStream).use { reader ->
                val buf = CharArray(1024)
                val builder = StringBuilder()
                while (true) {
                    val len = reader.read(buf)
                    if (len == -1) {
                        break
                    }
                    builder.append(buf, 0, len)
                }
                con.createStatement().execute(builder.toString())
            }
        }
    }
}