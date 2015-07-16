package org.springframework.data.cloudant.core;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

/**
 * Created by kevin on 7/14/15.
 */
public class DateTimeDataAdapter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    private final DateTimeFormatter fmt = ISODateTimeFormat.dateTime().withZoneUTC();

    @Override
    public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(fmt.print(src));
    }

    @Override
    public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        return fmt.parseDateTime(json.getAsString());
    }
}

