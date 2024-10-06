package com.ktservice.modules

import com.ktservice.application.ModuleConfiguration
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import java.time.Clock
import java.time.ZoneId

@ModuleConfiguration
class TimeModule : AbstractModule() {
    @Provides
    @Singleton
    fun zoneId(
        @Named("server.location") location: String
    ): ZoneId = ZoneId.of(location)

    @Provides
    @Singleton
    fun clock(timezone: ZoneId): Clock = Clock.tickMinutes(timezone)
}
