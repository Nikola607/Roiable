package onboarding.nv.java.ejb.info;

import java.io.Serializable;
import java.util.List;

public class DepartmentInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private boolean isDeleted = false;
	private List<ObjectInfo> roles;
	private List<ObjectInfo> privileges;
	
	public DepartmentInfo() {
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ObjectInfo> getRoles() {
		return roles;
	}
	public void setRoles(List<ObjectInfo> roles) {
		this.roles = roles;
	}
	public List<ObjectInfo> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(List<ObjectInfo> privileges) {
		this.privileges = privileges;
	}
	
	

}
