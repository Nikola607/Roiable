package onboarding.nv.java.bl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;


import javax.ejb.Local;

import onboarding.nv.java.ejb.info.DepartmentInfo;
import onboarding.nv.java.ejb.info.ObjectInfo;

@Local
public interface OnboardingHandlerLocal {
	
	String hireUser(ObjectInfo user) throws Exception;
	
	void initialLoad(ObjectInfo user) throws Exception;
	
	String addRole(ObjectInfo role) throws Exception;
	
	String addPrivilege(ObjectInfo privilege) throws Exception;
	
//	void addDepartment(DepartmentInfo department) throws Exception;
//	
//	public void changePosition(String name, String newDepartment) throws Exception;
	
	public String leave(String name) throws Exception;
	
	public String assignAccess(String name, String accessToAssign, String fromDate, String toDate) throws SQLException, ParseException, Exception;
	
	public String modifyAccess(String name, String uniqueName, String fromDate, String toDate) throws SQLException, ParseException, Exception;
	
	public String removeAccess(String name, String accessToRemove) throws Exception;
	
	public String emailGenerator(String username);
	
	public ObjectInfo displayUser(String uniqieId) throws Exception;
	
	public List<ObjectInfo> displayAllUsers() throws SQLException, Exception;
	
	public List<ObjectInfo> displayAllManagers() throws SQLException, Exception;
	
	public ObjectInfo displayPrivileges(String name) throws Exception;
	
	public ObjectInfo displayRoles(String name) throws Exception;
	
	public String modifyUser(ObjectInfo user) throws Exception;
	
	public String modifyRole(ObjectInfo role) throws Exception;
	
	public String modifyPrivilege(ObjectInfo privilege) throws Exception;
	
	public String deleteRoleOrPrivilege(String name) throws Exception;
	
	public List<ObjectInfo> displayAllAccessForUser(String name) throws Exception;

	List<ObjectInfo> displayAllRoles() throws SQLException, Exception;

	List<ObjectInfo> displayAllPrivileges() throws SQLException, Exception;
	
}
