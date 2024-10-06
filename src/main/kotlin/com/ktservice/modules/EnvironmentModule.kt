package com.ktservice.modules

import com.ktservice.application.ModuleConfiguration
import com.google.inject.AbstractModule
import com.google.inject.Key
import com.google.inject.name.Named
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.yaml.YamlConfigLoader

@ModuleConfiguration
class EnvironmentModule : AbstractModule() {
    protected override fun configure() {
        val loader = YamlConfigLoader()
        val config: ApplicationConfig = loader.load(null) ?: return

        bindConfig("", config.toMap())
    }

    private fun bindConfig(prefix: String, config: Map<*, *>) {
        for ((key, value) in config) {
            val path = "$prefix$key"
            when (value) {
                is String -> binder().bind(Key.get(String::class.java, Named(path))).toInstance(value);
                is List<*> -> binder().bind(Key.get(List::class.java, Named(path))).toInstance(value);
                is Map<*, *> -> bindConfig("$path.", value)
            }
        }
    }
}
