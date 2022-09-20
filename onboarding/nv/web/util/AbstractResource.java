package onboarding.nv.web.util;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AbstractResource {

	protected static final String EMPTY_JSON = "{}";
	protected Gson gson;
	protected JsonExceptionMapper exceptionMapper = new JsonExceptionMapper();
	
	protected AbstractResource() {
		GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(Date.class, new DateAdapter());
	    gson = gsonBuilder.create();
	}
}
