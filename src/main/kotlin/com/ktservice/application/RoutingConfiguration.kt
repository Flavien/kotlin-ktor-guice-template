package com.ktservice.application

import com.google.inject.Guice
import com.google.inject.Module
import io.ktor.server.application.Application
import io.ktor.server.routing.Routing
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

@Target(AnnotationTarget.CLASS)
annotation class ModuleConfiguration

interface ApiRouter {
    fun register(app: Application): Routing
}

fun configureRouting(
    application: Application,
    modules: Iterable<Module> = findModules(),
    resources: Iterable<Class<out ApiRouter>> = findResources()
) {
    val injector = Guice.createInjector(modules)

    for (type in resources) {
        val instance: ApiRouter = injector.getInstance(type)
        instance.register(application)
    }
}

fun findResources(): Iterable<Class<out ApiRouter>> = reflections.getSubTypesOf(ApiRouter::class.java)

fun findModules(): List<Module> {
    val modules: Set<Class<out Module>> = reflections.getSubTypesOf(Module::class.java)

    return modules
        .filter { it.getDeclaredAnnotation(ModuleConfiguration::class.java) != null }
        .map { it.getDeclaredConstructor().newInstance() }
}

private val reflections = Reflections(
    ConfigurationBuilder()
        .setUrls(ClasspathHelper.forJavaClassPath())
        .setScanners(Scanners.SubTypes)
)
