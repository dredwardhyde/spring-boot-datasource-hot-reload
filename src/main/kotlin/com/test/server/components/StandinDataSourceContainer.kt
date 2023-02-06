package com.test.server.components

import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.sql.Connection
import javax.annotation.PostConstruct

@Component
class StandinDataSourceContainer {

    @Autowired
    lateinit var meterRegistry: MeterRegistry

    @Autowired
    lateinit var env: Environment

    private var ds: HikariDataSource? = null

    @PostConstruct
    fun reloadDataSource() {
        ds?.close()
        val ds = HikariDataSource()
        ds.poolName = "standin " + Math.random()
        ds.driverClassName = env.getProperty("datasource.standin.class")
        ds.jdbcUrl = env.getProperty("datasource.standin.url")
        ds.username = env.getProperty("datasource.standin.user")
        ds.password = env.getProperty("datasource.standin.password")
        env.getProperty("datasource.standin.min-pool-size")?.let { ds.minimumIdle = it.toInt() }
        env.getProperty("datasource.standin.max-pool-size")?.let { ds.maximumPoolSize = it.toInt() }
        env.getProperty("datasource.standin.max-idle-time")?.let { ds.idleTimeout = it.toLong() }
        env.getProperty("datasource.standin.idle-connection-test-period")?.let { ds.validationTimeout = it.toLong() }
        ds.connectionTestQuery = env.getProperty("datasource.standin.preferred-test-query")
        ds.metricRegistry = meterRegistry
        this.ds?.close()
        this.ds = ds
    }

    fun getConnection(): Connection? {
        return ds?.connection
    }

    fun getPoolName(): String? {
        return ds?.poolName
    }
}