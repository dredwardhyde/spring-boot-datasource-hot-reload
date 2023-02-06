package com.test.server.configurations

import com.test.server.components.MainDataSourceContainer
import com.test.server.components.StandinDataSourceContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.EnableScheduling
import java.nio.file.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Configuration
@EnableScheduling
class RootConfiguration {

    @Autowired
    lateinit var env: Environment

    @EventListener
    fun onApplicationReadyEvent(applicationReadyEvent: ApplicationReadyEvent) {
        env.getProperty("datasource.secret.folder")?.let {
            val executor: Executor = Executors.newSingleThreadExecutor()
            executor.execute {
                val watchService = FileSystems.getDefault().newWatchService()
                Paths.get(it).register(
                        watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY)
                var key: WatchKey
                while (watchService.take().also { key = it } != null) {
                    for (event: WatchEvent<*> in key.pollEvents()) {
                        println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".")
                        (applicationReadyEvent.applicationContext.getBean(MainDataSourceContainer::class.java) as MainDataSourceContainer).reloadDataSource()
                        (applicationReadyEvent.applicationContext.getBean(StandinDataSourceContainer::class.java) as StandinDataSourceContainer).reloadDataSource()
                    }
                    key.reset()
                }
            }
        }

    }
}