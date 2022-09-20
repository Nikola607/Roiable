package onboarding.nv.java.ejb.info;

import java.io.Serializable;
import java.util.List;

public class ObjectInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String lastName;
	private String title;
	private String dateOfBirth;
	private String department;
	private String phoneNumber;
	private String managerId;
	private String language;
	private String uniqueId;
	private String email;
	private String gender;
	private String position;
	private String hireDate;
	private String description;
	private String isDeleted;
	private String isManager;
	private String uniqueName;
	private String displayName;
	private String system;
	private String validFrom;
	private String validTo;
	private String parentId;
	private List<ObjectInfo> assignedTo;
	private List<ObjectInfo> assignments;


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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsManager() {
		return isManager;
	}

	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String accessType) {
		this.parentId = accessType;
	}

	public List<ObjectInfo> getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(List<ObjectInfo> assignedTo) {
		this.assignedTo = assignedTo;
	}

	public List<ObjectInfo> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<ObjectInfo> assignments) {
		this.assignments = assignments;
	}

}
