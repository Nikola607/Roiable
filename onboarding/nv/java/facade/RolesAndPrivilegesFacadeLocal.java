package onboarding.nv.java.facade;

import java.util.List;

import javax.ejb.Local;

import onboarding.nv.java.ejb.info.ObjectInfo;

@Local
public interface RolesAndPrivilegesFacadeLocal {

	void removeAccessFromObject(int object, int access) throws Exception;

	ObjectInfo findPrivilegeByName(String accessToRemove) throws Exception;

	ObjectInfo findRoleByName(String roleName) throws Exception;

	List<ObjectInfo> findRolesByObjectId(int id) throws Exception;

	List<ObjectInfo> findPrivilegeByObjectId(int id) throws Exception;

	void editPrivilege(ObjectInfo privilege) throws Exception;

	void editRole(ObjectInfo role) throws Exception;

	void setDeleted(int id) throws Exception;
	
	public List<ObjectInfo> findAllPrivileges() throws Exception;

	List<ObjectInfo> findAllRoles() throws Exception;

	void addRole(ObjectInfo role) throws Exception;

	void addPrivilege(ObjectInfo privilege) throws Exception;

	int objectExists(String uniqueName) throws Exception;

	List<ObjectInfo> findAllOnlyPrivilegesForObject(int id) throws Exception;

	ObjectInfo findObjectByUniqueId(String uniqueId) throws Exception;

	void setNotDeleted(int id) throws Exception;

}
