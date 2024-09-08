package org.example.freetime.utils

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.example.freetime.utils.Constants.DEFAULT_DATE_TIME_FORMAT
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.LocalDateTime
import java.util.Locale
import java.util.TimeZone

inline fun <reified T> T.logger(): Logger {
    if (T::class.isCompanion) {
        return LoggerFactory.getLogger(T::class.java.enclosingClass)
    }
    return LoggerFactory.getLogger(T::class.java)
}

fun Any.toJsonNode(): JsonNode {
    return DEFAULT_MAPPER.valueToTree(this)
}

fun <T> Any.convertTo(responseClass: Class<T>): T {
    return DEFAULT_MAPPER.convertValue(this, responseClass)
}

fun <T> Any.convertTo(clazz: TypeReference<T>): T {
    return DEFAULT_MAPPER.convertValue(this, clazz)
}

fun <T> String.convertTo(responseClass: Class<T>): T {
    return DEFAULT_MAPPER.readValue(this, responseClass)
}

fun <T> String.convertTo(clazz: TypeReference<T>): T {
    return DEFAULT_MAPPER.readValue(this, clazz)
}

fun String.createJsonNode(): JsonNode {
    return DEFAULT_MAPPER.readTree(this)
}

fun Any.toJsonString(): String {
    return DEFAULT_MAPPER.writeValueAsString(this)
}



val DEFAULT_MAPPER: ObjectMapper =
    Jackson2ObjectMapperBuilder.json().featuresToEnable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
        .featuresToEnable(
            DeserializationFeature.USE_LONG_FOR_INTS ,
            JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,  // 추가된 부분
            JsonParser.Feature.ALLOW_SINGLE_QUOTES         // 추가된 부분
        )
        .featuresToDisable(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
            SerializationFeature.FAIL_ON_EMPTY_BEANS,
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY,
        )
        .timeZone(TimeZone.getDefault())
        .modulesToInstall(
            JavaTimeModule()
                .apply { addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DEFAULT_DATE_TIME_FORMAT)) }
                .apply { addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DEFAULT_DATE_TIME_FORMAT)) }
        )
        .locale(Locale.getDefault())
        .simpleDateFormat("yyyy-MM-dd HH:mm:ss")
        .build<ObjectMapper>().apply {
            setVisibility(
                this.serializationConfig.defaultVisibilityChecker
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                    .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
            )
            setVisibility(
                this.deserializationConfig.defaultVisibilityChecker
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                    .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
            )
        }
