package org.example.freetime.converter



import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.example.freetime.model.Schedule
import org.example.freetime.utils.convertTo
import org.example.freetime.utils.createJsonNode
import org.example.freetime.utils.toJsonString

@Converter
class SchedulesConverter : AttributeConverter<List<Schedule>, String> {
    override fun convertToDatabaseColumn(attribute: List<Schedule>): String {
        return attribute.toJsonString()
    }

    override fun convertToEntityAttribute(dbData: String): List<Schedule> {
        return dbData.createJsonNode().map { it.convertTo(Schedule::class.java) }
    }
}
