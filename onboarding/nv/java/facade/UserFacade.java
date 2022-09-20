package onboarding.nv.java.facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.mail.search.IntegerComparisonTerm;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import onboarding.nv.java.ejb.info.DepartmentInfo;
import onboarding.nv.java.ejb.info.ObjectInfo;

/**
 * Session Bean implementation class OnboardingFacade
 */
@Stateless
public class UserFacade implements UserFacadeLocal {

	/**
	 * Default constructor.
	 */
	public UserFacade() {
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public void editUsersDepartment(int id, int departmentId) throws Exception {
//		Connection connection = getDBConnection();
//		PreparedStatement statement = connection.prepareStatement(
//				"INSERT INTO nv_relations (this_created_object, other_created_object) " + "VALUES ?, ?");
//		statement.setInt(1, id);
//		statement.setInt(2, departmentId);
//
//		statement.executeQuery();
//		statement.close();
//	}

//	@Override
//	public void deleteDepartmentFromUser(int id, int departmentId) throws Exception {
//		Connection connection = getDBConnection();
//
//		PreparedStatement statement = connection.prepareStatement(
//				"DELETE FROM nv_relations WHERE this_created_object = ? AND other_created_object = ?");
//
//		statement.setInt(1, id);
//		statement.setInt(1, departmentId);
//
//		statement.execute();
//		statement.close();
//
//	}

	@Override
	public List<ObjectInfo> findAllManagerUsers() throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT objects.id, attr.object_id, attr.value, attr.attribute_id, objects.is_deleted FROM  nv_created_objects AS objects "
						+ "INNER JOIN nv_assigned_attributes AS attr ON objects.id = attr.object_id "
						+ "WHERE attr.attribute_id = ? AND attr.value = ? AND objects.is_deleted = ?");

		statement.setInt(1, 16);
		statement.setString(2, "true");
		statement.setString(3, "false");

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> users = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo user = findObjectById(rSet.getInt("object_id"));

			users.add(user);
		}

		statement.close();

		return users;
	}

	@Override
	public ObjectInfo findManagerById(String managerId) throws Exception {
		Connection connection = getDBConnection();
		ObjectInfo manager = new ObjectInfo();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT objects.id, attr.object_id, attr.value, attr.attribute_id, objects.is_deleted FROM  nv_created_objects AS objects "
						+ "INNER JOIN nv_assigned_attributes AS attr ON objects.id = attr.object_id "
						+ "WHERE attr.attribute_id = ? AND attr.value = ? AND objects.name = ?");

		statement.setInt(1, 16);
		statement.setString(2, "true");
		statement.setString(3, managerId);

		ResultSet rSet = statement.executeQuery();

		while (rSet.next()) {
			manager = findObjectById(rSet.getInt("object_id"));
			manager.setIsDeleted(rSet.getString("is_deleted"));
		}

		statement.close();

		return manager;
	}

	@Override
	public List<ObjectInfo> findAllUsersByManagerId(String id) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT objects.id, attributes.object_id, attributes.value FROM  nv_created_objects AS objects "
						+ "INNER JOIN nv_assigned_attributes AS attributes ON objects.id = attributes.object_id "
						+ "WHERE attributes.attribute_id = ? AND attributes.value = ?");

		statement.setInt(1, 7);
		statement.setString(2, id);

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> users = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo user = findObjectById(rSet.getInt("object_id"));

			users.add(user);
		}

		statement.close();
		return users;
	}

//	@Override
//	public void addDepartment(DepartmentInfo department) throws Exception {
//		Connection connection = getDBConnection();
//
//		PreparedStatement statement = connection
//				.prepareStatement("INSERT INTO nv_created_objects (object_id, name, is_deleted) VALUES (?, ?, ?)");
//		statement.setInt(1, 4);
//		statement.setString(2, department.getName());
//		statement.setString(3, String.valueOf(department.isDeleted()));
//
//		statement.executeUpdate();
//		statement.close();
//	}

	@Override
	public void createUser(ObjectInfo user) throws Exception {
		Connection connection = getDBConnection();

		user.setUniqueId(uniqueIdentifierGenerator(user));
		user.setId(idSetter(user));

		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO nv_created_objects (object_id, name, is_deleted) VALUES (?, ?, ?)");
		statement.setInt(1, 1);
		statement.setString(2, user.getUniqueId());
		statement.setString(3, "false");
		statement.executeUpdate();

		statement.close();
	}

	@Override
	public void initialCreate(ObjectInfo user) throws Exception {
		Connection connection = getDBConnection();
		PreparedStatement statement = connection
				.prepareStatement("SELECT COUNT(object_id) FROM nv_created_objects " + "WHERE object_id = ?");
		statement.setInt(1, 1);
		ResultSet rSet = statement.executeQuery();
		rSet.next();

		if (rSet.getInt(1) == 0) {
			user.setId(1);
		} else {
			int id = rSet.getInt(1) + 1;
			user.setId(idSetter(user));
		}

		PreparedStatement statement2 = connection
				.prepareStatement("INSERT INTO nv_created_objects (object_id, name, is_deleted) VALUES (?, ?, ?)");
		statement2.setInt(1, 1);
		statement2.setString(2, user.getUniqueId());
		statement2.setString(3, "false");
		statement2.executeUpdate();

		statement2.close();
	}

	@Override
	public void addRole(ObjectInfo role) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO nv_created_objects (object_id, name, is_deleted) VALUES (?, ?, ?)");
		statement.setInt(1, 2);
		statement.setString(2, role.getUniqueName());
		statement.setString(3, "false");

		statement.executeUpdate();

		PreparedStatement statement2 = connection.prepareStatement(
				"SELECT id, object_id FROM nv_created_objects WHERE object_id = ?" + "ORDER BY id DESC");
		statement2.setInt(1, 2);
		ResultSet rSet = statement2.executeQuery();

		while (rSet.next()) {
			role.setId(rSet.getInt("id"));
			break;
		}

		rSet.close();

		statement.close();
	}

	@Override
	public void addPrivilege(ObjectInfo privilege) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO nv_created_objects (object_id, name, is_deleted) VALUES (?, ?, ?)");
		statement.setInt(1, 3);
		statement.setString(2, privilege.getUniqueName());
		statement.setString(3, "false");
		statement.executeUpdate();

		PreparedStatement statement2 = connection.prepareStatement(
				"SELECT id, object_id FROM nv_created_objects WHERE object_id = ?" + " ORDER BY id DESC");
		statement2.setInt(1, 3);
		ResultSet rSet = statement2.executeQuery();

		while (rSet.next()) {
			privilege.setId(rSet.getInt("id"));
			break;
		}

		rSet.close();
		statement.close();
	}

	@Override
	public void addAttributes(String attribute, int id, String value, String action) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO nv_assigned_attributes (value, object_id, attribute_id) VALUES (?, ?, ?)");
		statement.setString(1, value);
		statement.setInt(2, id);

		if (value != null && !value.replaceAll("\\s", "").isEmpty()) {
			if (action.equals("update")) {
				statement = connection.prepareStatement(
						"UPDATE nv_assigned_attributes SET value = ? " + "WHERE object_id = ? AND attribute_id = ?");
				statement.setString(1, value);
				statement.setInt(2, id);
			}
			switch (attribute) {
			case "name":
				statement.setInt(3, 1);

				break;
			case "lastName":
				statement.setInt(3, 2);

				break;
			case "title":
				statement.setInt(3, 3);

				break;
			case "dateOfBirth":
				statement.setInt(3, 4);

				break;
			case "department":
				statement.setInt(3, 5);
				break;
			case "phoneNumber":
				statement.setInt(3, 6);
				break;
			case "language":
				statement.setInt(3, 8);
				break;
			case "gender":
				statement.setInt(3, 14);

				break;
			case "position":
				statement.setInt(3, 15);

				break;
			case "hireDate":
				statement.setInt(3, 17);
				break;
			case "email":
				statement.setInt(3, 9);
				break;
			case "uniqueIdentifier":
				statement.setInt(3, 10);
				break;
			case "isManager":
				statement.setInt(3, 16);
				break;
			case "managerId":
				statement.setInt(3, 7);
				break;
			case "uniqueName":
				statement.setInt(3, 13);
				break;
			case "description":
				statement.setInt(3, 11);
				break;
			case "system":
				statement.setInt(3, 12);
				break;

			}

			statement.executeUpdate();

			statement.close();
		}
	}

	@Override
	public ObjectInfo findObjectByUniqueId(String uniqueId) throws Exception {
		Connection connection = getDBConnection();
		ObjectInfo user = new ObjectInfo();

		PreparedStatement statement = connection
				.prepareStatement("SELECT id, object_id, name, is_deleted FROM nv_created_objects " + "WHERE name = ?");

		statement.setString(1, uniqueId);
		ResultSet rSet = statement.executeQuery();

		while (rSet.next()) {
			user = findObjectById(rSet.getInt("id"));
			user.setIsDeleted(rSet.getString("is_deleted"));
		}
		statement.close();

		return user;
	}

//	@Override
//	public DepartmentInfo findDepartmentByName(String newDepartment) throws Exception {
//		Connection connection = getDBConnection();
//		DepartmentInfo department = new DepartmentInfo();
//
//		PreparedStatement statement = connection
//				.prepareStatement("SELECT object_id, name FROM nv_created_objects WHERE object_id = 4 AND name = ?");
//		statement.setString(1, newDepartment);
//
//		ResultSet rSet = statement.executeQuery();
//
//		while (rSet.next()) {
//			department.setId(rSet.getInt("id"));
//			department.setName(rSet.getString("name"));
//		}
//
//		statement.close();
//		return department;
//	}

	private ObjectInfo findObjectById(int id) throws Exception {
		Connection connection = getDBConnection();
		ObjectInfo object = new ObjectInfo();
		PreparedStatement statement = connection.prepareStatement(
				"SELECT object_id, attribute_id, value FROM nv_assigned_attributes " + "WHERE object_id = ?");
		statement.setInt(1, id);

		List<ObjectInfo> access = findAllAccessForObject(id);
		object.setAssignments(access);

		ResultSet rSet = statement.executeQuery();
		attachAttributuesToObject(rSet, object);

		statement.close();
		return object;
	}

	@Override
	public List<ObjectInfo> findAllUsersByObjectId(int id) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT r.this_created_object, r.other_created_object, c.object_id, c.id FROM nv_relations AS r "
						+ "JOIN nv_created_objects AS c ON r.other_created_object = c.id "
						+ "WHERE c.object_id = ? AND r.this_created_object = ?");
		statement.setInt(1, 1);
		statement.setInt(2, id);

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> users = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo user = findObjectById(rSet.getInt("other_created_object"));

			users.add(user);
		}

		statement.close();

		return users;
	}

	@Override
	public boolean checkAccess(int id, int id2) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection
				.prepareStatement("SELECT this_created_object, other_created_object FROM nv_relations "
						+ "WHERE this_created_object = ? AND other_created_object = ?");
		statement.setInt(1, id);
		statement.setInt(2, id2);

		ResultSet rSet = statement.executeQuery();

		while (rSet.next()) {
			return true;
		}

		rSet.close();

		return false;
	}

	public List<ObjectInfo> findAllAccessForObject(int id) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT DISTINCT r.this_created_object, r.other_created_object, c.object_id, c.id, r.valid_from, r.valid_to, r.parent, c.is_deleted FROM nv_relations AS r "
						+ "JOIN nv_created_objects AS c ON r.other_created_object = c.id "
						+ "WHERE r.this_created_object = ? AND c.is_deleted = ?");
		statement.setInt(1, id);
		statement.setString(2, "false");

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> users = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo object = findObjectById(rSet.getInt("other_created_object"));
			object.setValidFrom(rSet.getString("valid_from"));
			object.setValidTo(rSet.getString("valid_to"));
			object.setParentId(rSet.getString("parent"));

			users.add(object);
		}

		statement.close();

		return users;
	}

	public List<ObjectInfo> findAllAvailableAccessForObject(int id) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT DISTINCT r.this_created_object, r.other_created_object, c.object_id, c.id, r.valid_from, r.valid_to, r.parent, c.is_deleted FROM nv_relations AS r "
						+ "JOIN nv_created_objects AS c ON r.other_created_object = c.id "
						+ "WHERE r.this_created_object = ?");
		statement.setInt(1, id);

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> users = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo object = findObjectById(rSet.getInt("other_created_object"));
			object.setValidFrom(rSet.getString("valid_from"));
			object.setValidTo(rSet.getString("valid_to"));
			object.setParentId(rSet.getString("parent"));
			object.setIsDeleted(rSet.getString("is_deleted"));

			users.add(object);
		}

		statement.close();

		return users;
	}

	public List<ObjectInfo> findAllPrivilegeAccessForObject(int id) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT DISTINCT r.this_created_object, r.other_created_object, c.object_id, c.id, r.valid_from, r.valid_to, r.parent FROM nv_relations AS r "
						+ "JOIN nv_created_objects AS c ON r.other_created_object = c.id "
						+ "WHERE r.other_created_object = ?");
		statement.setInt(1, id);

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> users = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo object = findObjectById(rSet.getInt("other_created_object"));
			object.setValidFrom(rSet.getString("valid_from"));
			object.setValidTo(rSet.getString("valid_to"));
			object.setParentId(rSet.getString("parent"));

			users.add(object);
		}

		statement.close();

		return users;
	}

	@Override
	public void updateValidityAccess(int id1, int id2, String date1, String date2) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"UPDATE nv_relations SET valid_from = ?, valid_to = ? WHERE this_created_object = ? AND other_created_object = ?");
		statement.setString(1, date1);
		statement.setString(2, date2);
		statement.setInt(3, id1);
		statement.setInt(4, id2);

		statement.executeUpdate();
		statement.close();
	}

	@Override
	public void assignAccess(int firstId, int secondId, String validFrom, String validTo, String parentId)
			throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO nv_relations (this_created_object, other_created_object, valid_from, valid_to, parent) "
						+ "VALUES (?, ?, ?, ?, ?)");
		statement.setInt(1, firstId);
		statement.setInt(2, secondId);
		statement.setString(3, validFrom);
		statement.setString(4, validTo);
		statement.setString(5, parentId);

		statement.executeUpdate();
		statement.close();
	}

//	@Override
//	public DepartmentInfo findDepartmentById(int id) throws Exception {
//		Connection connection = getDBConnection();
//
//		DepartmentInfo department = new DepartmentInfo();
//
//		PreparedStatement statement = connection
//				.prepareStatement("SELECT id, object_id, name FROM nv_created_objects WHERE id = ? AND object_id = 4");
//
//		statement.setInt(1, id);
//		ResultSet rSet = statement.executeQuery();
//		department.setName(rSet.getString("name"));
//
//		statement.close();
//
//		return department;
//	}

	@Override
	public List<ObjectInfo> findAllUsers() throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection
				.prepareStatement("SELECT id, object_id, name, is_deleted FROM nv_created_objects " + "WHERE object_id = ?");
		statement.setInt(1, 1);

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> users = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo user = findObjectById(rSet.getInt("id"));
			user.setIsDeleted(rSet.getString("is_deleted"));

			users.add(user);
		}

		statement.close();

		return users;
	}

	public void attachAttributuesToObject(ResultSet rSet, ObjectInfo object) throws SQLException {

		while (rSet.next()) {
			object.setId(rSet.getInt("object_id"));
			int attributeId = rSet.getInt("attribute_id");
			switch (attributeId) {
			case 1:
				object.setName(rSet.getString("value"));

				break;
			case 2:
				object.setLastName(rSet.getString("value"));

				break;
			case 3:
				object.setTitle(rSet.getString("value"));

				break;
			case 4:
				object.setDateOfBirth(rSet.getString("value"));

				break;
			case 6:
				object.setPhoneNumber(rSet.getString("value"));

				break;
			case 7:
				object.setManagerId(rSet.getString("value"));

				break;
			case 8:
				object.setLanguage(rSet.getString("value"));

				break;
			case 10:
				object.setUniqueId(rSet.getString("value"));

				break;
			case 9:
				object.setEmail(rSet.getString("value"));

				break;
			case 5:
				object.setDepartment(rSet.getString("value"));
				break;
			case 16:
				object.setIsManager(rSet.getString("value"));

				break;
			case 15:
				object.setPosition(rSet.getString("value"));

				break;
			case 17:
				object.setHireDate(rSet.getString("value"));

				break;
			case 14:
				object.setGender(rSet.getString("value"));

				break;
			case 13:
				object.setUniqueName(rSet.getString("value"));
				break;

			case 11:
				object.setDescription(rSet.getString("value"));
				break;

			case 12:
				object.setSystem(rSet.getString("value"));
				break;
			}
		}

	}

	private String uniqueIdentifierGenerator(ObjectInfo user) throws Exception {
		Connection connection = getDBConnection();
		int id = 0;

		PreparedStatement statement = connection
				.prepareStatement("SELECT COUNT(object_id) FROM nv_created_objects " + "WHERE object_id = ?");
		statement.setInt(1, 1);
		ResultSet rSet = statement.executeQuery();
		rSet.next();

		if (rSet.getInt(1) == 0) {
			id = 1;
			user.setId(1);
		} else {
			id = rSet.getInt(1) + 1;
			user.setId(idSetter(user));
		}

		String var = String.format("IDM%04d", id);

		return var;
	}

	private int idSetter(ObjectInfo user) throws Exception {
		Connection connection = getDBConnection();
		PreparedStatement statement2 = connection
				.prepareStatement("SELECT id, object_id FROM nv_created_objects " + "ORDER BY id DESC");
		ResultSet rSet2 = statement2.executeQuery();

		while (rSet2.next()) {
			user.setId(rSet2.getInt("id") + 1);
			break;
		}
		return user.getId();
	}

	private Connection getDBConnection() throws Exception {
		String jtaDataSourcePath = "jdbc/ONBOARDING_DATASOURCE";

		try {

			InitialContext ctxt = new InitialContext();
			DataSource ds = (DataSource) ctxt.lookup(jtaDataSourcePath);

			Connection connection = ds.getConnection();
			return connection;
		} catch (Exception e) {
			throw new Exception("Could not connect to '" + jtaDataSourcePath + "' . Exception : " + e.getMessage());
		}
	}

	@Override
	public void setIsManager(ObjectInfo user) throws Exception {
		Connection connection = getDBConnection();
		PreparedStatement statement = connection.prepareStatement(
				"UPDATE nv_assigned_attributes SET value = ? WHERE attribute_id = ? AND object_id = ?");

		statement.setString(1, user.getManagerId());
		statement.setInt(2, 7);
		statement.setInt(3, user.getId());

		statement.executeUpdate();
		statement.close();

	}

}
