package onboarding.nv.web.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonExceptionMapper implements ExceptionMapper<Exception>{
	
	private Status errorStatus;
	private String errorMessage;
	private String stackTrace;
	
	public JsonExceptionMapper () {
		this.setErrorStatus(errorStatus);
		this.setErrorMessage(errorMessage);
		this.setStackTrace(stackTrace);
	}
	
	public JsonExceptionMapper (Status errorStatus, String errorMessage, StackTraceElement[] stackTrace) {
		this.setErrorStatus(errorStatus);
		this.setErrorMessage(errorMessage);
		String stackTraceString = "";
		String lineSeparator = System.getProperty("line.separator");
		if (stackTrace != null) {
			for (int i = 0; i <= 50; ++i) {
				stackTraceString += stackTrace[i]+lineSeparator;
			}
		}
		this.setStackTrace(stackTraceString);
	}
	
	
	@Override
	public Response toResponse(Exception e) {
		Gson gson = new GsonBuilder().create();
		e.printStackTrace();
		return Response.status(Status.INTERNAL_SERVER_ERROR).
			entity(gson.toJson(new JsonExceptionMapper(Status.INTERNAL_SERVER_ERROR, e.getMessage(),e.getStackTrace()))).build();
	}
	
	public void setErrorStatus(Status errorStatus) {
		this.errorStatus = errorStatus;
	}

	
	public Status getErrorStatus() {
		return errorStatus;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}


	


	




	


	




	
}
