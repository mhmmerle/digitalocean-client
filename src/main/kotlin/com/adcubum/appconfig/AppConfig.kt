package com.adcubum.appconfig

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

class AppConfig {
    val sources = mutableListOf<AppConfigSource>()

    inline fun <reified T> get(): T {
        return get(T::class as KClass<*>)
    }

    fun <T> get(kClass: KClass<*>, prefix : String? = null): T {
        val defaultConstructor = kClass.constructors.toList()[0]
        val parametersWithValues = mutableMapOf<KParameter, Any?> ()

        defaultConstructor.parameters.forEach parameter@{ param ->
            if (param.kind != KParameter.Kind.VALUE) return@parameter

            when (param.type.classifier) {
                String::class -> addConstructorParamForString(prefix, param, parametersWithValues)
                else -> addContsructorParamForOther(prefix, param, parametersWithValues)
            }
        }
        return defaultConstructor.callBy(parametersWithValues) as T?:throw ErrorReadingConfig()
    }

    fun addConstructorParamForString(prefix: String?, param: KParameter, parametersWithValues: MutableMap<KParameter, Any?>) {
        val fullName = if (prefix != null) "$prefix.${param.name}" else "${param.name}"
        val value = sources.mapNotNull { source ->
            source.getValue(fullName) }.firstOrNull()
        if ( value == null && !param.isOptional && !param.type.isMarkedNullable) {
            throw RequiredParameterNotSet("${param.type.classifier} $fullName")
        } else if ( value != null || !param.isOptional){
            parametersWithValues.put(param, value)
        }
    }

    private fun addContsructorParamForOther(prefix: String?, param: KParameter, parametersWithValues: MutableMap<KParameter, Any?>) {
        val newPrefix = if (prefix != null) "$prefix.${param.name}" else param.name
        parametersWithValues.put(param, this.get(param.type.classifier as KClass<*>, newPrefix))
    }

    fun withJsonFile(filename: String): AppConfig {
        sources.add(AppConfigJsonFileSource(filename))
        return this
    }

    fun withSource(testConfigSource: AppConfigSource): AppConfig {
        sources.add(testConfigSource)
        return this
    }

    fun withEnvironment(): AppConfig {
        sources.add(AppConfigEnvironmentVariableSource())
        return this
    }

    fun withSystemProperties(): AppConfig {
        sources.add(AppConfigSystemPropertySource())
        return this
    }

}

class ErrorReadingConfig : Throwable()

class RequiredParameterNotSet(parameter: String) : Throwable(parameter)
