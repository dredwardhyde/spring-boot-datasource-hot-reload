package com.test.server.components

import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.sql.Connection
import javax.annotation.PostConstruct

@Component
class MainDataSourceContainer {

    @Autowired
    lateinit var meterRegistry: MeterRegistry

    @Autowired
    lateinit var env: Environment

    private var ds: HikariDataSource? = null

    @PostConstruct
    fun reloadDataSource() {
        val ds = HikariDataSource()
        ds.poolName = "main " + Math.random()
        ds.driverClassName = env.getProperty("datasource.main.class")
        ds.jdbcUrl = env.getProperty("datasource.main.url")
        ds.username = env.getProperty("datasource.main.user")
        ds.password = env.getProperty("datasource.main.password")
        env.getProperty("datasource.main.min-pool-size")?.let { ds.minimumIdle = it.toInt() }
        env.getProperty("datasource.main.max-pool-size")?.let { ds.maximumPoolSize = it.toInt() }
        env.getProperty("datasource.main.max-idle-time")?.let { ds.idleTimeout = it.toLong() }
        env.getProperty("datasource.main.idle-connection-test-period")?.let { ds.validationTimeout = it.toLong() }
        ds.connectionTestQuery = env.getProperty("datasource.main.preferred-test-query")
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