package com.ktservice.application

import com.google.inject.Binder
import com.google.inject.Key
import com.google.inject.Module
import com.google.inject.name.Named
import io.ktor.server.config.ApplicationConfig

class EnvironmentModule(val config: ApplicationConfig) : Module {
    override fun configure(binder: Binder?) {
        requireNotNull(binder)
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
