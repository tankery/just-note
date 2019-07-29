package me.tankery.justnote.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.util.*

/**
 * CalendarJsonDeserializer
 * Created by tankery on 2019-07-29.
 */
class CalendarJsonDeserializer : JsonDeserializer<Calendar> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Calendar =
        Calendar.getInstance().apply {
            timeInMillis = json?.asJsonPrimitive?.asLong ?: 0
        }
}