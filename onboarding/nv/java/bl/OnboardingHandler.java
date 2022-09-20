package onboarding.nv.java.bl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Generated;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import onboarding.nv.java.ejb.info.DepartmentInfo;
import onboarding.nv.java.ejb.info.ObjectInfo;
import onboarding.nv.java.facade.RolesAndPrivilegesFacadeLocal;
import onboarding.nv.java.facade.UserFacadeLocal;
import onboarding.nv.java.util.ExceptionHandler;

/**
 * Session Bean implementation class OnboardingHandler
 */
@Stateless
public class OnboardingHandler implements OnboardingHandlerLocal {
	@EJB
	private RolesAndPrivilegesFacadeLocal rolesAndPrivilegesFacadeLocal;
	@EJB
	private UserFacadeLocal userFacadeLocal;

	public OnboardingHandler() {
	}

	@Override
	public void initialLoad(ObjectInfo user) throws Exception {

		userFacadeLocal.initialCreate(user);

		// ASSIGN ATTRIBUTES
		userFacadeLocal.addAttributes("email", user.getId(), user.getEmail(), "add");
		userFacadeLocal.addAttributes("uniqueIdentifier", user.getId(), user.getUniqueId(), "add");
		userFacadeLocal.addAttributes("name", user.getId(), user.getName(), "add");
		userFacadeLocal.addAttributes("lastName", user.getId(), user.getLastName(), "add");
		userFacadeLocal.addAttributes("title", user.getId(), user.getTitle(), "add");
		userFacadeLocal.addAttributes("dateOfBirth", user.getId(), user.getDateOfBirth(), "add");
		userFacadeLocal.addAttributes("department", user.getId(), user.getDepartment(), "add");
		userFacadeLocal.addAttributes("phoneNumber", user.getId(), user.getPhoneNumber(), "add");
		userFacadeLocal.addAttributes("language", user.getId(), user.getLanguage(), "add");
		userFacadeLocal.addAttributes("gender", user.getId(), user.getGender(), "add");
		userFacadeLocal.addAttributes("position", user.getId(), user.getPosition(), "add");
		userFacadeLocal.addAttributes("hireDate", user.getId(), user.getHireDate(), "add");
		userFacadeLocal.addAttributes("isManager", user.getId(), user.getIsManager(), "add");
		userFacadeLocal.addAttributes("managerId", user.getId(), user.getManagerId(), "add");
	}

	// DONE
	@Override
	public String hireUser(ObjectInfo user) throws Exception {

		Pattern pattern = Pattern.compile("^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$");
		Pattern pattern2 = Pattern.compile("^[(]?[0-9]{3}(-)[)]?[-\\s\\.]?[0-9]{4}[-\\s\\.]?[0-9]{2}$");

		if (user.getName() == null || user.getName().replaceAll("\\s+", "").isEmpty()) {
			return "You can't create a user without name.";
		}
		if (user.getLastName() == null || user.getLastName().replaceAll("\\s+", "").isEmpty()) {
			return "You can't create a user without last name.";
		}

		Matcher matcher = pattern.matcher(user.getDateOfBirth());
		if (!matcher.find()) {
			return "Invalid date of birth format. Example:dd/mm/yyyy";
		}
		if (user.getDepartment() == null || user.getDepartment().replaceAll("\\s+", "").isEmpty()) {
			return "You can't create a user without department.";
		}
		if (user.getManagerId() == null || user.getManagerId().replaceAll("\\s+", "").isEmpty()) {
			return "You can't create a user without manager id.";
		}
		Matcher matcher1 = pattern.matcher(user.getHireDate());
		if (!matcher1.find()) {
			return "Invalid hire date format. Example:dd/mm/yyyy";
		}
		Matcher matcher2 = pattern2.matcher(user.getPhoneNumber());
		if (!matcher2.find()) {
			return "Invalid phone number forman. Example 000-0000-00.";
		}

		if (user.getIsManager() == null || user.getIsManager().replaceAll("\\s+", "").isEmpty()) {
			return "isManager can't be null. Example 'true' or 'false'";
		}

		if (!user.getIsManager().equals("true") && !user.getIsManager().equals("false")) {
			return "Invalid data. Accepts only 'true' or 'false'.";
		}

		List<ObjectInfo> managers = userFacadeLocal.findAllManagerUsers();

		// MAKE A MANAGER IF HE EXISTS, IF NOT CREATE ONE
		if (managers.size() == 0) {
			// CREATE OBJECT
			userFacadeLocal.createUser(user);
			user.setEmail(emailGenerator(user.getUniqueId()));

			user.setIsManager("true");
			user.setManagerId(user.getUniqueId());
		} else {
			ObjectInfo manager = userFacadeLocal.findManagerById(user.getManagerId());

			if (manager.getId() == 0 || manager.getIsDeleted().equals("true")) {
				return "Manager with that id does not exist";
			} else {
				// CREATE OBJECT
				userFacadeLocal.createUser(user);
				user.setEmail(emailGenerator(user.getUniqueId()));
			}
		}

		// ASSIGN ATTRIBUTES
		userFacadeLocal.addAttributes("email", user.getId(), user.getEmail(), "add");
		userFacadeLocal.addAttributes("uniqueIdentifier", user.getId(), user.getUniqueId(), "add");
		userFacadeLocal.addAttributes("name", user.getId(), user.getName(), "add");
		userFacadeLocal.addAttributes("lastName", user.getId(), user.getLastName(), "add");
		userFacadeLocal.addAttributes("title", user.getId(), user.getTitle(), "add");
		userFacadeLocal.addAttributes("dateOfBirth", user.getId(), user.getDateOfBirth(), "add");
		userFacadeLocal.addAttributes("department", user.getId(), user.getDepartment(), "add");
		userFacadeLocal.addAttributes("phoneNumber", user.getId(), user.getPhoneNumber(), "add");
		userFacadeLocal.addAttributes("language", user.getId(), user.getLanguage(), "add");
		userFacadeLocal.addAttributes("gender", user.getId(), user.getGender(), "add");
		userFacadeLocal.addAttributes("position", user.getId(), user.getPosition(), "add");
		userFacadeLocal.addAttributes("hireDate", user.getId(), user.getHireDate(), "add");
		userFacadeLocal.addAttributes("isManager", user.getId(), user.getIsManager(), "add");
		userFacadeLocal.addAttributes("managerId", user.getId(), user.getManagerId(), "add");

		return user.getUniqueId();
	}

	// DONE
	@Override
	public String addRole(ObjectInfo role) throws Exception {
		if (role.getUniqueName() == null || role.getUniqueName().replaceAll("\\s+", "").isEmpty()) {
			return "You can't create a role without unique name.";
		}
		if (role.getDisplayName() == null || role.getDisplayName().replaceAll("\\s+", "").isEmpty()) {
			return "You can't create a role without display name.";
		}

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("ROLE:");
		sBuilder.append(role.getUniqueName().replaceAll("\\s+", ""));
		role.setUniqueName(String.valueOf(sBuilder));

		if (rolesAndPrivilegesFacadeLocal.objectExists(role.getUniqueName()) == 0) {

			rolesAndPrivilegesFacadeLocal.addRole(role);

			userFacadeLocal.addAttributes("uniqueName", role.getId(), role.getUniqueName(), "add");
			userFacadeLocal.addAttributes("name", role.getId(), role.getDisplayName(), "add");
			userFacadeLocal.addAttributes("description", role.getId(), role.getDescription(), "add");
		} else {
			return "Role with that unique name already exists";
		}

		return role.getUniqueName();
	}

	// DONE
	@Override
	public String addPrivilege(ObjectInfo privilege) throws Exception {

		if (privilege.getUniqueName() == null || privilege.getUniqueName().replaceAll("\\s+", "").isEmpty()) {
			return "You can't create a privilege without unique name.";
		}
		if (privilege.getDisplayName() == null || privilege.getDisplayName().replaceAll("\\s+", "").isEmpty()) {
			return "You can't create a privilege without display name.";
		}
		if (privilege.getSystem() == null || privilege.getSystem().replaceAll("\\s+", "").isEmpty()) {
			return "You can't create a privilege without system.";
		}

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("PRIV:");
		sBuilder.append(privilege.getSystem().replaceAll("\\s+", ""));
		sBuilder.append(":");
		sBuilder.append(privilege.getUniqueName().replaceAll("\\s+", ""));
		privilege.setUniqueName(String.valueOf(sBuilder));

		if (rolesAndPrivilegesFacadeLocal.objectExists(privilege.getUniqueName()) == 0) {

			rolesAndPrivilegesFacadeLocal.addPrivilege(privilege);

			userFacadeLocal.addAttributes("uniqueName", privilege.getId(), privilege.getUniqueName(), "add");
			userFacadeLocal.addAttributes("name", privilege.getId(), privilege.getDisplayName(), "add");
			userFacadeLocal.addAttributes("description", privilege.getId(), privilege.getDescription(), "add");
			userFacadeLocal.addAttributes("system", privilege.getId(), privilege.getSystem(), "add");
		} else {
			return "Privilege with that unique name already exists";
		}

		return privilege.getUniqueName();
	}

	// @Override
	// public void addDepartment(DepartmentInfo department) throws Exception {
	// userFacadeLocal.addDepartment(department);
	//
	// }
	//
	// @Override
	// public void changePosition(String uniqueId, String newDepartment) throws
	// Exception {
	// ObjectInfo user = userFacadeLocal.findObjectByUniqueId(uniqueId);
	// DepartmentInfo department =
	// userFacadeLocal.findDepartmentByName(newDepartment);
	//
	// if (!isUserLeft(user)) {
	// List<ObjectInfo> privilegesByDepartment = rolesAndPrivilegesFacadeLocal
	// .findPrivilegeByObjectId(department.getId());
	// boolean containsOnlyPrivilege = false;
	//
	// for (int i = 0; i < rolesByDepartment.size(); i++) {
	// List<ObjectInfo> privilegesByRole = rolesAndPrivilegesFacadeLocal
	// .findPrivilegeByObjectId(rolesByDepartment.get(i).getId());
	//
	// for (int j = 0; j < privilegesByRole.size(); j++) {
	// if (containsOnlyPrivilege(privilegesByRole.get(j))) {
	// containsOnlyPrivilege = true;
	// }
	// }
	//
	// }
	//
	// for (int i = 0; i < privilegesByDepartment.size(); i++) {
	// if (containsOnlyPrivilege(privilegesByDepartment.get(i))) {
	// containsOnlyPrivilege = true;
	// }
	// }
	//
	// if (containsOnlyPrivilege) {
	//
	// userFacadeLocal.deleteDepartmentFromUser(user.getId(), department.getId());
	// userFacadeLocal.editUsersDepartment(user.getId(), department.getId());
	// } else {
	// System.out.println("Your user does not have the required access, please
	// contact your manager");
	// }
	//
	// }
	//
	// }

	// DONE
	@Override
	public String leave(String uniqueId) throws Exception {
		ObjectInfo user = userFacadeLocal.findObjectByUniqueId(uniqueId);
		if (!isUserLeft(user)) {

			if (user.getIsManager() == "true") {
				List<ObjectInfo> usersToManagers = userFacadeLocal.findAllUsersByManagerId(user.getUniqueId());
				for (int i = 0; i < usersToManagers.size(); i++) {
					ObjectInfo currentUser = usersToManagers.get(i);

					currentUser.setManagerId(user.getManagerId());
					userFacadeLocal.setIsManager(user);
				}

			}

			// Removing the user from the privilege
			List<ObjectInfo> allOnlyPrivileges = rolesAndPrivilegesFacadeLocal
					.findAllOnlyPrivilegesForObject(user.getId());

			for (int i = 0; i < allOnlyPrivileges.size(); i++) {
				ObjectInfo currentPrivilege = allOnlyPrivileges.get(i);
				rolesAndPrivilegesFacadeLocal.removeAccessFromObject(user.getId(), currentPrivilege.getId());
			}

			rolesAndPrivilegesFacadeLocal.setDeleted(user.getId());

			return "User " + uniqueId + " left the company.";

		} else {
			return "This user has already left the company.";
		}
	}

	// DONE
	@Override
	public String assignAccess(String uniqueId, String accessToAssign, String fromDate, String toDate)
			throws Exception {
		ObjectInfo toAssignAccess = userFacadeLocal.findObjectByUniqueId(uniqueId);
		ObjectInfo access = userFacadeLocal.findObjectByUniqueId(accessToAssign);

		if (toAssignAccess.getUniqueName() != null && toAssignAccess.getUniqueName().contains("PRIV:")) {
			return "Can't assign access to a privilege";
		}

		if (accessToAssign.contains("IDM")) {
			return "Access can't be of type USER.";
		}
		if (access.getId() == 0 || toAssignAccess.getId() == 0) {
			return "Object does not exist or is deleted.";
		}
		if (uniqueId.equals(accessToAssign)) {
			return "Can't assign access to it's own.";
		}

		if (!isUserLeft(toAssignAccess)) {
			try {
				SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
				Date date1 = sdformat.parse(fromDate);
				Date date2 = sdformat.parse(toDate);
				LocalDate localDate = LocalDate.now();
				Date date3 = convertToDateUsingInstant(localDate);

				if (date2.compareTo(date3) < 0) {
					return "toDate cannot be in the past.";
				}

				if (date1.compareTo(date2) < 0 ) {
					List<ObjectInfo> onlyPrivilegesForObject = rolesAndPrivilegesFacadeLocal
							.findAllOnlyPrivilegesForObject(toAssignAccess.getId());

					if (accessToAssign.contains("ROLE:")) {
						ObjectInfo role = rolesAndPrivilegesFacadeLocal.findRoleByName(accessToAssign);
						List<ObjectInfo> privilegesForRole = rolesAndPrivilegesFacadeLocal
								.findPrivilegeByObjectId(role.getId());

						if (toAssignAccess.getUniqueName() != null) {
							// in case its a role
							for (int i = 0; i < privilegesForRole.size(); i++) {
								ObjectInfo privilegeFromRole = privilegesForRole.get(i);

								userFacadeLocal.assignAccess(toAssignAccess.getId(), privilegeFromRole.getId(),
										fromDate, toDate, role.getUniqueName());
							}

							userFacadeLocal.assignAccess(toAssignAccess.getId(), role.getId(), fromDate, toDate,
									toAssignAccess.getUniqueName());
							return "The Role has been added to the objects access.";
						}

						// in case its a user
						if (!checkForOnlyPrivilege(onlyPrivilegesForObject, privilegesForRole)) {
							return "You do not have the required access, please contact your manager";
						} else {
							privilegesForRole = rolesAndPrivilegesFacadeLocal.findPrivilegeByObjectId(role.getId());

							for (int i = 0; i < privilegesForRole.size(); i++) {
								ObjectInfo privilegeFromRole = privilegesForRole.get(i);

								userFacadeLocal.assignAccess(toAssignAccess.getId(), privilegeFromRole.getId(),
										fromDate, toDate, role.getUniqueName());
							}

							userFacadeLocal.assignAccess(toAssignAccess.getId(), role.getId(), fromDate, toDate,
									toAssignAccess.getUniqueId());

							return "The Role has been added to the objects access.";
						}

					} else if (accessToAssign.contains("PRIV:")) {
						ObjectInfo privilege = rolesAndPrivilegesFacadeLocal.findPrivilegeByName(accessToAssign);

						if (toAssignAccess.getUniqueName() != null) {
							if (privilege.getUniqueName().contains(":ONLY")) {

								return "You can't assign an ONLY privilege to a role.";
							}

							userFacadeLocal.assignAccess(toAssignAccess.getId(), privilege.getId(), fromDate, toDate,
									toAssignAccess.getUniqueName());

							return "The Privilege has been added to the objects access.";
						}

						if (!checkForAccess(onlyPrivilegesForObject, privilege)
								&& !privilege.getUniqueName().contains(":ONLY")) {
							return "You do not have the required access, please contact your manager";
						} else {
							userFacadeLocal.assignAccess(toAssignAccess.getId(), privilege.getId(), fromDate, toDate,
									toAssignAccess.getUniqueId());

							return "The Privilege has been added to the objects access.";
						}
					}

				} else if (date1.compareTo(date2) > 0) {
					return "Overlapping validity";

				} else if (date1.compareTo(date2) == 0) {
					return "Both dates are equal";

				}
			} catch (Exception e) {
				return "Invalid date format";
			}
		} else {
			return "Object does not exist or is deleted.";
		}
		return "Invalid input";

	}

	// DONE
	@Override
	public String modifyAccess(String uniqueId, String accessToAssign, String fromDate, String toDate)
			throws Exception {

		ObjectInfo toAssignAccess = userFacadeLocal.findObjectByUniqueId(uniqueId);
		ObjectInfo access = userFacadeLocal.findObjectByUniqueId(accessToAssign);

		if (toAssignAccess.getUniqueName() != null && toAssignAccess.getUniqueName().contains("PRIV:")) {
			return "This field accepts only Roles or Users.";
		}

		if (accessToAssign.contains("USER:")) {
			return "Access can't be of type USER.";
		}
		if (access.getId() == 0 || toAssignAccess.getId() == 0) {
			return "Object does not exist or is deleted.";
		}

		if (!userFacadeLocal.checkAccess(toAssignAccess.getId(), access.getId())) {
			return "There is no such assigned access to this object.";
		}

		if (!isUserLeft(toAssignAccess)) {
			try {
				SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
				Date date1 = sdformat.parse(fromDate);
				Date date2 = sdformat.parse(toDate);

				if (date1.compareTo(date2) < 0) {

					if (accessToAssign.contains("ROLE:")) {
						ObjectInfo role = rolesAndPrivilegesFacadeLocal.findRoleByName(accessToAssign);
						List<ObjectInfo> privilegesForRole = rolesAndPrivilegesFacadeLocal
								.findPrivilegeByObjectId(role.getId());

						// in case its a role
						for (int i = 0; i < privilegesForRole.size(); i++) {
							ObjectInfo privilegeFromRole = privilegesForRole.get(i);

							userFacadeLocal.updateValidityAccess(toAssignAccess.getId(), privilegeFromRole.getId(),
									fromDate, toDate);
							userFacadeLocal.updateValidityAccess(role.getId(), privilegeFromRole.getId(), fromDate,
									toDate);
						}

						userFacadeLocal.updateValidityAccess(toAssignAccess.getId(), role.getId(), fromDate, toDate);
						return "Validity of the role was updated.";

					} else if (accessToAssign.contains("PRIV:")) {
						ObjectInfo privilege = rolesAndPrivilegesFacadeLocal.findPrivilegeByName(accessToAssign);

						userFacadeLocal.updateValidityAccess(toAssignAccess.getId(), privilege.getId(), fromDate,
								toDate);

						return "Validity of the privilege was updated";

					}

				} else if (date1.compareTo(date2) > 0) {
					return "Overlapping validity";

				} else if (date1.compareTo(date2) == 0) {
					return "Both dates are equal";

				}
			} catch (Exception e) {
				return "Invalid date format";
			}
		} else {
			return "Object does not exist or is deleted.";
		}
		return "Invalid input";
	}

	// DONE
	@Override
	public ObjectInfo displayUser(String uniqueId) throws Exception {

		return userFacadeLocal.findObjectByUniqueId(uniqueId);
	}

	// DONE
	@Override
	public List<ObjectInfo> displayAllUsers() throws SQLException, Exception {

		return userFacadeLocal.findAllUsers();
	}

	// DONE
	@Override
	public ObjectInfo displayPrivileges(String name) throws Exception {

		return rolesAndPrivilegesFacadeLocal.findObjectByUniqueId(name);
	}

	// DONE
	@Override
	public List<ObjectInfo> displayAllPrivileges() throws SQLException, Exception {

		return rolesAndPrivilegesFacadeLocal.findAllPrivileges();
	}

	// DONE
	@Override
	public ObjectInfo displayRoles(String name) throws Exception {

		return rolesAndPrivilegesFacadeLocal.findObjectByUniqueId(name);
	}

	// DONE
	@Override
	public List<ObjectInfo> displayAllRoles() throws SQLException, Exception {

		return rolesAndPrivilegesFacadeLocal.findAllRoles();
	}

	// TODO: IMPLEMENT ADDITIONAL LOGIC
	@Override
	public String deleteRoleOrPrivilege(String name) throws Exception {
		ObjectInfo objectInfo = userFacadeLocal.findObjectByUniqueId(name);

		if (isUserLeft(objectInfo)) {
			return "Object is already deleted.";
		}
		if (name.contains("PRIV:")) {

			if (name.contains(":ONLY")) {
				return "Can't delete an ONLY Privilege.";
			}

			rolesAndPrivilegesFacadeLocal.setDeleted(objectInfo.getId());
			return "Privilege was deleted.";

		} else if (name.contains("ROLE:")) {

			rolesAndPrivilegesFacadeLocal.setDeleted(objectInfo.getId());
			return "Role was deleted.";

		} else {

			System.out.println("Invalid input.");
		}
		return "Invalid input.";

	}

	// DONE
	@Override
	public String removeAccess(String uniqueId, String access) throws Exception {
		ObjectInfo objectWithAccess = userFacadeLocal.findObjectByUniqueId(uniqueId);
		ObjectInfo accessToRemove = userFacadeLocal.findObjectByUniqueId(access);

		if (objectWithAccess.getUniqueName() != null && objectWithAccess.getUniqueName().contains("PRIV:")) {
			return "Can't remove access to a privilege";
		}

		if (objectWithAccess.getId() == 0 || accessToRemove.getId() == 0) {
			return "Object is invalid";
		}

		if (access.contains("IDM")) {
			return "Access can't be of type USER.";
		}

		if (!userFacadeLocal.checkAccess(objectWithAccess.getId(), accessToRemove.getId())) {
			return "There is no such assigned access to this object.";
		}

		if (!isUserLeft(objectWithAccess) && !isUserLeft(accessToRemove)) {

			if (access.contains("ROLE:")) {

				List<ObjectInfo> privilegesForRole = rolesAndPrivilegesFacadeLocal
						.findPrivilegeByObjectId(accessToRemove.getId());

				for (int i = 0; i < privilegesForRole.size(); i++) {
					ObjectInfo privilegeFromRole = privilegesForRole.get(i);

					rolesAndPrivilegesFacadeLocal.removeAccessFromObject(objectWithAccess.getId(),
							privilegeFromRole.getId());
				}

				rolesAndPrivilegesFacadeLocal.removeAccessFromObject(objectWithAccess.getId(), accessToRemove.getId());

				return "Role with its access removed from the objects access";
			} else if (access.contains("PRIV:")) {
				List<ObjectInfo> privilegesForObject = rolesAndPrivilegesFacadeLocal
						.findPrivilegeByObjectId(objectWithAccess.getId());
				List<ObjectInfo> rolesForObject = rolesAndPrivilegesFacadeLocal
						.findRolesByObjectId(objectWithAccess.getId());

				if (access.contains("ONLY")) {

					// CHECK FOR ROLES CONTAINING PRIVILEGES FROM THE SAME SYSTEM
					if (onlyPrivilegeCheck(rolesForObject, accessToRemove)) {
						return "There are privileges from the same system assigned to this object, access cannot be removed.";
					} else {

						// REMOVE AN ONLY PRIVILEGE WITH OTHER PRIVILEGES FROM THE SAME SYSTEM
						for (int i = 0; i < privilegesForObject.size(); i++) {
							ObjectInfo currentPrivilege = privilegesForObject.get(i);
							rolesAndPrivilegesFacadeLocal.removeAccessFromObject(objectWithAccess.getId(),
									currentPrivilege.getId());
						}

						return "ONLY privilege removed with other privileges from the same system from this object.";
					}

				} else {
					// REMOVE A SINGLE PRIVILEGE FROM USER
					rolesAndPrivilegesFacadeLocal.removeAccessFromObject(objectWithAccess.getId(),
							accessToRemove.getId());

					return "Privilege removed from objects access";
				}
			} else {
				return "Invalid input.";
			}
		}

		return "Object is not in the system.";

	}

	private boolean onlyPrivilegeCheck(List<ObjectInfo> rolesForObject, ObjectInfo accessToRemove) throws Exception {

		for (int i = 0; i < rolesForObject.size(); i++) {
			ObjectInfo currentRole = rolesForObject.get(i);
			List<ObjectInfo> privilegesForRole = rolesAndPrivilegesFacadeLocal
					.findPrivilegeByObjectId(currentRole.getId());

			for (int j = 0; j < privilegesForRole.size(); j++) {
				ObjectInfo currentPrivilege = privilegesForRole.get(i);
				if (currentPrivilege.getSystem().equals(accessToRemove.getSystem())) {
					return true;
				}
			}
		}

		return false;
	}

	// DONE
	@Override
	public String modifyUser(ObjectInfo user) throws Exception {

		if (!isUserLeft(user)) {
			Pattern pattern = Pattern.compile("^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$");
			Pattern pattern2 = Pattern.compile("^[(]?[0-9]{3}(-)[)]?[-\\s\\.]?[0-9]{4}[-\\s\\.]?[0-9]{2}$");

			if (user.getName() == null || user.getName().replaceAll("\\s+", "").isEmpty()) {
				return "Name can't be empty.";
			}
			if (user.getLastName() == null || user.getLastName().replaceAll("\\s+", "").isEmpty()) {
				return "Last name can't be empty.";
			}

			if (user.getTitle() == null || user.getTitle().replaceAll("\\s+", "").isEmpty()) {
				return "Title can't be empty.";
			}

			if (user.getLanguage() == null || user.getLanguage().replaceAll("\\s+", "").isEmpty()) {
				return "Language can't be empty";
			}

			if (user.getGender() == null || user.getGender().replaceAll("\\s+", "").isEmpty()) {
				return "Gender can't be empty";
			}

			if (user.getPosition() == null || user.getPosition().replaceAll("\\s+", "").isEmpty()) {
				return "Position can't be empty";
			}

			Matcher matcher = pattern.matcher(user.getDateOfBirth());
			if (!matcher.find()) {
				return "Invalid date of birth format. Example:dd/mm/yyyy";
			}
			if (user.getManagerId() == null || user.getManagerId().replaceAll("\\s+", "").isEmpty()) {
				return "You can't create a user without manager id.";
			}

			Matcher matcher2 = pattern2.matcher(user.getPhoneNumber());
			if (!matcher2.find()) {
				return "Invalid phone number forman. Example 000-0000-00.";
			}

			ObjectInfo manager = userFacadeLocal.findManagerById(user.getManagerId());

			if (manager.getId() == 0 || manager.getIsDeleted().equals("true")) {
				return "Manager with that id does not exist";
			}

			if (user.getManagerId().equals(user.getUniqueId())) {
				return "Can't assign your own id to the managerId field.";
			}

			userFacadeLocal.addAttributes("name", user.getId(), user.getName(), "update");
			userFacadeLocal.addAttributes("lastName", user.getId(), user.getLastName(), "update");
			userFacadeLocal.addAttributes("title", user.getId(), user.getTitle(), "update");
			userFacadeLocal.addAttributes("dateOfBirth", user.getId(), user.getDateOfBirth(), "update");
			userFacadeLocal.addAttributes("phoneNumber", user.getId(), user.getPhoneNumber(), "update");
			userFacadeLocal.addAttributes("language", user.getId(), user.getLanguage(), "update");
			userFacadeLocal.addAttributes("gender", user.getId(), user.getGender(), "update");
			userFacadeLocal.addAttributes("position", user.getId(), user.getPosition(), "update");
			userFacadeLocal.addAttributes("managerId", user.getId(), user.getManagerId(), "update");
		} else {
			return "This user is deleted.";
		}
		return "User modified.";
	}

	// DONE
	@Override
	public String modifyRole(ObjectInfo role) throws Exception {

		if (!isUserLeft(role)) {
			if (role.getDisplayName() == null || role.getDisplayName().replaceAll("\\s+", "").isEmpty()) {
				return "Name can't be empty";
			}

			if (role.getDescription() == null || role.getDescription().replaceAll("\\s+", "").isEmpty()) {
				return "Description can't be empty";
			}
			userFacadeLocal.addAttributes("name", role.getId(), role.getDisplayName(), "update");
			userFacadeLocal.addAttributes("description", role.getId(), role.getDescription(), "update");
		} else {
			return "This role is deleted.";
		}

		return "Role modified.";
	}

	// DONE
	@Override
	public String modifyPrivilege(ObjectInfo privilege) throws Exception {

		if (!isUserLeft(privilege)) {
			if (privilege.getDisplayName() == null || privilege.getDisplayName().replaceAll("\\s+", "").isEmpty()) {
				return "Name can't be empty";
			}

			if (privilege.getDescription() == null || privilege.getDescription().replaceAll("\\s+", "").isEmpty()) {
				return "Description can't be empty";
			}

			userFacadeLocal.addAttributes("name", privilege.getId(), privilege.getDisplayName(), "update");
			userFacadeLocal.addAttributes("description", privilege.getId(), privilege.getDescription(), "update");
		} else {
			return "This Privilege is deleted.";
		}

		return "Privilege modified.";
	}

	@Override
	public String emailGenerator(String username) {

		String domain = "ROIABLE.com";
		String email = String.format("%s@%s", username, domain);

		return email;
	}

	private boolean isUserLeft(ObjectInfo object) {
		if (object.getIsDeleted().equals("true")) {
			return true;
		}

		return false;
	}

	private boolean checkForOnlyPrivilege(List<ObjectInfo> onlyPrivilegesForObject,
			List<ObjectInfo> privilegesForObject) {

		for (int i = 0; i < onlyPrivilegesForObject.size(); i++) {
			ObjectInfo currentOnlyPrivilege = onlyPrivilegesForObject.get(i);

			for (int j = 0; j < privilegesForObject.size(); j++) {
				ObjectInfo privilegeFromRole = privilegesForObject.get(j);

				if (currentOnlyPrivilege.getSystem().equals(privilegeFromRole.getSystem())) {
					privilegesForObject.remove(j);
					j--;
					if (privilegesForObject.size() == 0) {
						return true;
					}
				}
			}
		}

		if (privilegesForObject.size() == 0) {
			return true;
		}

		return false;
	}

	private boolean checkForAccess(List<ObjectInfo> onlyPrivilegesForObject, ObjectInfo privilege) {
		for (int i = 0; i < onlyPrivilegesForObject.size(); i++) {
			ObjectInfo currentOnlyPrivilege = onlyPrivilegesForObject.get(i);
			if (currentOnlyPrivilege.getSystem().equals(privilege.getSystem())) {
				return true;

			}
		}
		return false;
	}

	public static Date convertToDateUsingInstant(LocalDate date) {
		return java.util.Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	@Override
	public List<ObjectInfo> displayAllAccessForUser(String name) throws Exception {
		ObjectInfo user = userFacadeLocal.findObjectByUniqueId(name);

		return userFacadeLocal.findAllAvailableAccessForObject(user.getId());
	}

	@Override
	public List<ObjectInfo> displayAllManagers() throws Exception {
		List<ObjectInfo> managers = userFacadeLocal.findAllManagerUsers();

		return managers;
	}

}
