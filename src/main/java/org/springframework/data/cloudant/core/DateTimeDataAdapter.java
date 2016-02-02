package org.springframework.data.cloudant.core;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by kevin on 7/14/15.
 */
public class DateTimeDataAdapter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    private final DateTimeFormatter fmt = ISODateTimeFormat.dateTime().withZoneUTC();
    private static final String[] DATE_FORMATS = new String[] {
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd HH:mm:ss Z",
            "yyyy-MM-dd HH:mm:ss z"
    };

    @Override
    public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(fmt.print(src));
    }

    @Override
    public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        for (String format : DATE_FORMATS) {
            try {
                return DateTime.parse(json.getAsString(), DateTimeFormat.forPattern(format));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        throw new JsonParseException("Unparseable date: \"" + json.getAsString()
                + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));

    }
}

