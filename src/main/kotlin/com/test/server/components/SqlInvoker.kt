package com.test.server.components

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SqlInvoker {

    @Autowired
    lateinit var mainDataSource: MainDataSourceContainer

    @Autowired
    lateinit var standinDataSource: StandinDataSourceContainer

    @Scheduled(fixedRate = 200)
    fun testConnectionMain() {
        println(mainDataSource.getPoolName())
        val conn = mainDataSource.getConnection()
        val ps = conn?.prepareStatement("select 1")
        ps?.execute()
        ps?.close()
        conn?.close()
    }

    @Scheduled(fixedRate = 200)
    fun testConnectionStandin() {
        println(standinDataSource.getPoolName())
        val conn = standinDataSource.getConnection()
        val ps = conn?.prepareStatement("select 1")
        ps?.execute()
        ps?.close()
        conn?.close()
    }
}