package onboarding.nv.web.util;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateAdapter implements JsonSerializer<Date> {

	 public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getTime());
    }
}
