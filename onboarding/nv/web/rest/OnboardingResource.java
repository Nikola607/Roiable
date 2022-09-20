package onboarding.nv.web.rest;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.registry.infomodel.User;

import com.google.gson.reflect.TypeToken;
import com.roiable.onboarding.java.ejb.rest.ResourceUtils;

import onboarding.nv.java.bl.OnboardingHandlerLocal;
import onboarding.nv.java.ejb.info.DepartmentInfo;
import onboarding.nv.java.ejb.info.LinkInfo;
import onboarding.nv.java.ejb.info.ObjectInfo;
import onboarding.nv.web.util.AbstractResource;

@Path(value = "rest")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OnboardingResource extends AbstractResource {

	@Path(value = "/initialLoad")
	@POST
	public Response initialLoad(String input) {
		try {
			Type loadType = new TypeToken<Collection<ObjectInfo>>() {
			}.getType();
			Collection<ObjectInfo> initialLoadInfo = gson.fromJson(input, loadType);
			for (ObjectInfo load : initialLoadInfo) {
				getOnboardingHandlerLocal().initialLoad(load);
			}

			return createOKResponse("Ready");
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/showUser/{name}")
	@GET
	public Response showUser(@PathParam(value = "name") String name) throws NamingException, Exception {
		try {
			ObjectInfo user = getOnboardingHandlerLocal().displayUser(name);
			if (user.getId() == 0) {
				return createNotFoundResponse(EMPTY_JSON);
			}
			return createOKResponse(user);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/showAccess/{name}")
	@GET
	public Response showAccess(@PathParam(value = "name") String name) throws NamingException, Exception {
		try {
			ObjectInfo object = new ObjectInfo();
			List<ObjectInfo> objects = new ArrayList<>();
			if (name.contains("IDM")) {
				
				object = getOnboardingHandlerLocal().displayUser(name);
				objects = getOnboardingHandlerLocal().displayAllAccessForUser(name);
			} else if (name.contains("ROLE:")) {
				
				object = getOnboardingHandlerLocal().displayRoles(name);
				objects = getOnboardingHandlerLocal().displayAllAccessForUser(name);
			} else if (name.contains("PRIV:")) {
				
				object = getOnboardingHandlerLocal().displayPrivileges(name);
				objects = getOnboardingHandlerLocal().displayAllAccessForUser(name);
			} else {
				return createBadRequestResponse("Invalid input.");
			}
			if (object.getId() == 0) {
				return createNotFoundResponse(EMPTY_JSON);
			}
			return createOKResponse(objects);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/showUsers")
	@GET
	public Response showUsers() throws SQLException, NamingException, Exception {
		try {
			List<ObjectInfo> users = getOnboardingHandlerLocal().displayAllUsers();

			return createOKResponse(users);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}
	
	@Path(value = "/showManagers")
	@GET
	public Response showManagers() throws SQLException, NamingException, Exception {
		try {
			List<ObjectInfo> users = getOnboardingHandlerLocal().displayAllManagers();

			return createOKResponse(users);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/showRoles")
	@GET
	public Response showRoles() throws SQLException, NamingException, Exception {
		try {
			List<ObjectInfo> roles = getOnboardingHandlerLocal().displayAllRoles();

			return createOKResponse(roles);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}

	}

	@Path(value = "/showPrivileges")
	@GET
	public Response showPrivileges() throws SQLException, NamingException, Exception {
		try {
			List<ObjectInfo> privileges = getOnboardingHandlerLocal().displayAllPrivileges();

			return createOKResponse(privileges);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/showAssignmentObject/{name}")
	@GET
	public Response showAssignmentObject(@PathParam(value = "name") String name) throws NamingException, Exception {

		try {
			if (name.contains("ROLE:")) {
				ObjectInfo role = getOnboardingHandlerLocal().displayRoles(name);
				if (role.getId() == 0) {
					return createNotFoundResponse(EMPTY_JSON);
				}

				return createOKResponse(role);
			} else if (name.contains("PRIV:")) {
				ObjectInfo privilege = getOnboardingHandlerLocal().displayPrivileges(name);
				if (privilege.getId() == 0) {
					return createNotFoundResponse(EMPTY_JSON);
				}

				return createOKResponse(privilege);
			}
			return createBadRequestResponse(EMPTY_JSON);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/addUser")
	@POST
	public Response hireUser(String input) throws NamingException, Exception {
		try {
			ObjectInfo user = gson.fromJson(input, ObjectInfo.class);
			String hireUser = getOnboardingHandlerLocal().hireUser(user);

			return createCreatedResponse(hireUser);
		} catch (Exception e) {

			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/addRole")
	@POST
	public Response addRole(String input) throws Exception {
		try {
			ObjectInfo role = gson.fromJson(input, ObjectInfo.class);
			String roleBody = getOnboardingHandlerLocal().addRole(role);

			return createCreatedResponse(roleBody);
		} catch (Exception e) {

			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/addPrivilege")
	@POST
	public Response addPrivilege(String input) throws Exception {
		try {
			ObjectInfo privilege = gson.fromJson(input, ObjectInfo.class);
			String privilegeBody = getOnboardingHandlerLocal().addPrivilege(privilege);

			return createCreatedResponse(privilegeBody);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}
//
//	@Path(value = "/addDepartment")
//	@POST
//	public Response addDepartment(String input) throws Exception {
//		try {
//			DepartmentInfo department = gson.fromJson(input, DepartmentInfo.class);
//			getOnboardingHandlerLocal().addDepartment(department);
//
//			return createCreatedResponse(EMPTY_JSON);
//		} catch (Exception e) {
//			return exceptionMapper.toResponse(e);
//		}
//	}

	@Path(value = "/modifyUser/{name}")
	@PUT
	public Response modifyUser(@PathParam(value = "name") String name, String input) throws NamingException, Exception {
		try {
			ObjectInfo user = getOnboardingHandlerLocal().displayUser(name);
			ObjectInfo userToModify = gson.fromJson(input, ObjectInfo.class);

			if (user.getId() == 0) {
				return createNotFoundResponse(name);

			}
			userToModify.setId(user.getId());
			userToModify.setIsDeleted(user.getIsDeleted());
			userToModify.setUniqueId(name);
			String modifyUser = getOnboardingHandlerLocal().modifyUser(userToModify);

			return createOKResponse(modifyUser);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/modifyRole/{name}")
	@PUT
	public Response modifyRole(@PathParam(value = "name") String name, String input) throws NamingException, Exception {
		try {
			ObjectInfo role = getOnboardingHandlerLocal().displayRoles(name);
			ObjectInfo roleToModify = gson.fromJson(input, ObjectInfo.class);

			if (role.getId() == 0) {
				return createNotFoundResponse(name);
			}
			roleToModify.setId(role.getId());
			roleToModify.setIsDeleted(role.getIsDeleted());
			String modifyRole = getOnboardingHandlerLocal().modifyRole(roleToModify);

			return createOKResponse(modifyRole);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/modifyPrivilege/{name}")
	@PUT
	public Response modifyPrivilege(@PathParam(value = "name") String name, String input)
			throws NamingException, Exception {
		try {
			ObjectInfo privilege = getOnboardingHandlerLocal().displayPrivileges(name);
			ObjectInfo privilegeToModify = gson.fromJson(input, ObjectInfo.class);

			if (privilege.getId() == 0) {
				return createNotFoundResponse(name);
			}
			privilegeToModify.setId(privilege.getId());
			privilegeToModify.setIsDeleted(privilege.getIsDeleted());
			String modifyPrivilege = getOnboardingHandlerLocal().modifyPrivilege(privilegeToModify);

			return createOKResponse(modifyPrivilege);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/deleteUser/{id}")
	@PUT
	public Response deleteUser(@PathParam(value = "id") String uniqueId) throws NamingException, Exception {
		try {
			ObjectInfo user = getOnboardingHandlerLocal().displayUser(uniqueId);

			if (user.getId() == 0) {
				return createNotFoundResponse(EMPTY_JSON);
			}
			String leave = getOnboardingHandlerLocal().leave(uniqueId);

			return createACCEPTEDResponse(leave);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/deleteAssignmentObject/{uniqueName}")
	@PUT
	public Response deleteAssignmentObject(@PathParam(value = "uniqueName") String uniqueName)
			throws NamingException, Exception {
		try {
			ObjectInfo objectInfo = new ObjectInfo();
			if (uniqueName.contains("ROLE:")) {
				objectInfo = getOnboardingHandlerLocal().displayRoles(uniqueName);
			} else {
				objectInfo = getOnboardingHandlerLocal().displayPrivileges(uniqueName);
			}

			if (objectInfo.getId() == 0) {
				return createNotFoundResponse(EMPTY_JSON);
			}
			String delete = getOnboardingHandlerLocal().deleteRoleOrPrivilege(uniqueName);

			return createOKResponse(delete);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

//	@Path(value = "/changeDepartment/{uniqueId}/{newDepartment}")
//	@PUT
//	public Response changeDepartment(@PathParam(value = "uniqueId") String uniqueId,
//			@PathParam(value = "newDepartment") String newDepartment) throws NamingException, Exception {
//
//		try {
//			getOnboardingHandlerLocal().changePosition(uniqueId, newDepartment);
//
//			return createOKResponse(EMPTY_JSON);
//		} catch (Exception e) {
//			return exceptionMapper.toResponse(e);
//		}
//	}

	@Path(value = "/assignAccess")
	@POST
	public Response assignAccess(String input) throws SQLException, ParseException, NamingException, Exception {

		try {
			LinkInfo linkInfo = gson.fromJson(input, LinkInfo.class);

			String assignAccess = getOnboardingHandlerLocal().assignAccess(linkInfo.getUniqueId(), linkInfo.getAccess(),
					linkInfo.getFromDate(), linkInfo.getToDate());

			return createOKResponse(assignAccess);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	// merge

	@Path(value = "/modifyAccess")
	@PUT
	public Response modifyAccess(String input) throws SQLException, ParseException, NamingException, Exception {

		try {
			LinkInfo linkInfo = gson.fromJson(input, LinkInfo.class);

			String modifyAccess = getOnboardingHandlerLocal().modifyAccess(linkInfo.getUniqueId(), linkInfo.getAccess(),
					linkInfo.getFromDate(), linkInfo.getToDate());

			return createOKResponse(modifyAccess);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	@Path(value = "/removeAccess/{uniqueId}/{accessToRemove}")
	@PUT
	public Response removeAccess(@PathParam(value = "uniqueId") String uniqueId,
			@PathParam(value = "accessToRemove") String accessToRemove) throws NamingException, Exception {

		try {
			String access = getOnboardingHandlerLocal().removeAccess(uniqueId, accessToRemove);

			return createACCEPTEDResponse(access);
		} catch (Exception e) {
			return exceptionMapper.toResponse(e);
		}
	}

	private Response createOKResponse(Object objectForJson) {
		String responseBody = gson.toJson(objectForJson);
		Response response = Response.status(Response.Status.OK).entity(responseBody).build();
		return response;
	}

	private Response createNotFoundResponse(Object objectForJson) {
		String responseBody = gson.toJson(objectForJson);
		Response response = Response.status(Response.Status.NOT_FOUND).entity(responseBody).build();
		return response;
	}

	private Response createACCEPTEDResponse(Object objectForJson) {
		String responseBody = gson.toJson(objectForJson);
		Response response = Response.status(Response.Status.ACCEPTED).entity(responseBody).build();
		return response;
	}

	private Response createBadRequestResponse(Object objectForJson) {
		String responseBody = gson.toJson(objectForJson);
		Response response = Response.status(Response.Status.BAD_REQUEST).entity(responseBody).build();
		return response;
	}

	private Response createCreatedResponse(Object objectForJson) {
		String responseBody = gson.toJson(objectForJson);
		Response response = Response.status(Response.Status.CREATED).entity(responseBody).build();
		return response;
	}

	private OnboardingHandlerLocal getOnboardingHandlerLocal() throws NamingException {
		return (OnboardingHandlerLocal) ResourceUtils.getHandler(OnboardingHandlerLocal.class);
	}
}