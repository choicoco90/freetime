package org.example.freetime.converter


import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.example.freetime.model.FreeTime
import org.example.freetime.utils.convertTo
import org.example.freetime.utils.createJsonNode
import org.example.freetime.utils.toJsonString

@Converter
class FreeTimeConverter : AttributeConverter<List<FreeTime>, String> {
    override fun convertToDatabaseColumn(attribute: List<FreeTime>): String {
        return attribute.toJsonString()
    }

    override fun convertToEntityAttribute(dbData: String): List<FreeTime> {
        return dbData.createJsonNode().map { it.convertTo(FreeTime::class.java) }
    }
}
