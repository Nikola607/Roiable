package onboarding.nv.java.facade;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import onboarding.nv.java.ejb.info.DepartmentInfo;
import onboarding.nv.java.ejb.info.ObjectInfo;

@Local
public interface UserFacadeLocal {

//	void editUsersDepartment(int id, int departmentId) throws Exception;

	void createUser(ObjectInfo user) throws Exception;

	ObjectInfo findObjectByUniqueId(String uniqueId) throws Exception;

	void addAttributes(String attribute, int id, String value, String action) throws Exception;

	void updateValidityAccess(int id1, int id2, String date1, String date2) throws Exception;

	void assignAccess(int firstId, int secondId, String validFrom, String validTo, String accessId) throws Exception;

	List<ObjectInfo> findAllUsersByObjectId(int id) throws Exception;

	List<ObjectInfo> findAllManagerUsers() throws Exception;

	List<ObjectInfo> findAllUsersByManagerId(String id) throws Exception;

//	DepartmentInfo findDepartmentByName(String newDepartment) throws Exception;

//	void deleteDepartmentFromUser(int id, int id2) throws Exception;

//	DepartmentInfo findDepartmentById(int parseInt) throws Exception;

	List<ObjectInfo> findAllUsers() throws SQLException, Exception;

//	void addDepartment(DepartmentInfo department) throws Exception;

	List<ObjectInfo> findAllAccessForObject(int id) throws Exception;

	ObjectInfo findManagerById(String managerId) throws Exception;

	void addRole(ObjectInfo role) throws Exception;

	void addPrivilege(ObjectInfo privilege) throws Exception;

	boolean checkAccess(int id, int id2) throws Exception;

	void initialCreate(ObjectInfo user) throws Exception;
	
	public List<ObjectInfo> findAllPrivilegeAccessForObject(int id) throws Exception;
	
	public List<ObjectInfo> findAllAvailableAccessForObject(int id) throws Exception;

	void setIsManager(ObjectInfo user) throws Exception;

}
