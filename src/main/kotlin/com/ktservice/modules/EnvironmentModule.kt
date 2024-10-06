package com.ktservice.modules

import com.google.inject.Binder
import com.google.inject.Key
import com.google.inject.Module
import com.google.inject.name.Named
import com.ktservice.application.ModuleConfiguration
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.yaml.YamlConfigLoader

@ModuleConfiguration
class EnvironmentModule : Module {
    override fun configure(binder: Binder?) {
        requireNotNull(binder)

        val loader = YamlConfigLoader()
        val config: ApplicationConfig = loader.load(null) ?: return

        bindConfig(binder, "", config.toMap())
    }

    private fun bindConfig(binder: Binder, prefix: String, config: Map<*, *>) {
        for ((key, value) in config) {
            val path = "$prefix$key"
            when (value) {
                is String -> binder.bind(Key.get(String::class.java, Named(path))).toInstance(value);
                is List<*> -> binder.bind(Key.get(List::class.java, Named(path))).toInstance(value);
                is Map<*, *> -> bindConfig(binder, "$path.", value)
            }
        }
    }
}
