package onboarding.nv.java.facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import onboarding.nv.java.ejb.info.ObjectInfo;

/**
 * Session Bean implementation class RolesAndPrivilegesFacade
 */
@Stateless
public class RolesAndPrivilegesFacade implements RolesAndPrivilegesFacadeLocal {

	/**
	 * Default constructor.
	 */
	public RolesAndPrivilegesFacade() {
		// TODO Auto-generated constructor stub
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
	public List<ObjectInfo> findAllPrivileges() throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT id, object_id, name, is_deleted FROM nv_created_objects " + "WHERE object_id = ?");
		statement.setInt(1, 3);

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> privileges = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo privilege = new ObjectInfo();
			privilege = findRoleById(rSet.getInt("id"));
			privilege.setIsDeleted(rSet.getString("is_deleted"));

			privileges.add(privilege);
		}

		statement.close();
		return privileges;
	}

	@Override
	public List<ObjectInfo> findAllRoles() throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT id, object_id, name, is_deleted FROM nv_created_objects " + "WHERE object_id = ?");

		statement.setInt(1, 2);

		ResultSet rSet = statement.executeQuery();

		List<ObjectInfo> roles = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo role = new ObjectInfo();
			role = findRoleById(rSet.getInt("id"));
			role.setIsDeleted(rSet.getString("is_deleted"));

			roles.add(role);
		}

		statement.close();
		return roles;
	}

	@Override
	public List<ObjectInfo> findAllOnlyPrivilegesForObject(int id) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT r.this_created_object, r.other_created_object, c.name, c.object_id, c.id FROM nv_relations AS r "
						+ "JOIN nv_created_objects AS c ON r.other_created_object = c.id "
						+ "WHERE c.object_id = ? AND r.this_created_object = ? AND c.name LIKE '%:ONLY'");
		statement.setInt(1, 3);
		statement.setInt(2, id);

		ResultSet rSet = statement.executeQuery();

		List<ObjectInfo> privileges = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo privilege = findPrivilegeById(rSet.getInt("other_created_object"));

			privileges.add(privilege);
		}

		return privileges;
	}

	@Override
	public ObjectInfo findPrivilegeByName(String uniqueName) throws Exception {
		Connection connection = getDBConnection();

		ObjectInfo privilege = new ObjectInfo();

		PreparedStatement statement = connection
				.prepareStatement("SELECT a.object_id, a.attribute_id, a.value FROM nv_assigned_attributes AS a "
						+ "INNER JOIN nv_created_objects AS o ON a.object_id = o.id " + "WHERE o.name = ?");

		statement.setString(1, uniqueName);
		ResultSet rSet = statement.executeQuery();

		attachAttributuesToObject(rSet, privilege);
		statement.close();

		return privilege;
	}

	@Override
	public ObjectInfo findRoleByName(String roleName) throws Exception {
		Connection connection = getDBConnection();

		ObjectInfo role = new ObjectInfo();

		PreparedStatement statement = connection
				.prepareStatement("SELECT a.object_id, a.attribute_id, a.value FROM nv_assigned_attributes AS a "
						+ "INNER JOIN nv_created_objects AS o ON a.object_id = o.id WHERE o.name = ?");

		statement.setString(1, roleName);
		ResultSet rSet = statement.executeQuery();

		attachAttributuesToObject(rSet, role);

		statement.close();
		return role;
	}

	@Override
	public void removeAccessFromObject(int object, int access) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"DELETE FROM nv_relations WHERE this_created_object = ? AND other_created_object = ?");

		statement.setInt(1, object);
		statement.setInt(2, access);

		statement.executeUpdate();
		statement.close();
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
			user = findRoleById(rSet.getInt("id"));
			user.setIsDeleted(rSet.getString("is_deleted"));
		}
		statement.close();

		return user;
	}

	private ObjectInfo findObjectById(int id) throws Exception {
		Connection connection = getDBConnection();
		ObjectInfo object = new ObjectInfo();
		PreparedStatement statement = connection.prepareStatement(
				"SELECT object_id, attribute_id, value FROM nv_assigned_attributes " + "WHERE object_id = ?");
		statement.setInt(1, id);

		ResultSet rSet = statement.executeQuery();
		attachAttributuesToObject(rSet, object);

		statement.close();
		return object;
	}

	private List<ObjectInfo> findAllAssignedToObjects(int id) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT DISTINCT r.this_created_object, r.other_created_object, c.object_id, c.id, r.valid_from, r.valid_to, r.parent, c.is_deleted FROM nv_relations AS r "
						+ "JOIN nv_created_objects AS c ON r.other_created_object = c.id "
						+ "WHERE r.other_created_object = ? AND c.is_deleted = ?");
		statement.setInt(1, id);
		statement.setString(2, "false");

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> users = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo object = findObjectById(rSet.getInt("this_created_object"));
			object.setValidFrom(rSet.getString("valid_from"));
			object.setValidTo(rSet.getString("valid_to"));
			object.setParentId(rSet.getString("parent"));

			users.add(object);
		}

		statement.close();

		return users;
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
			ObjectInfo object = findRoleById(rSet.getInt("other_created_object"));
			object.setValidFrom(rSet.getString("valid_from"));
			object.setValidTo(rSet.getString("valid_to"));
			object.setParentId(rSet.getString("parent"));

			users.add(object);
		}

		statement.close();

		return users;
	}

	@Override
	public void editPrivilege(ObjectInfo privilege) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection
				.prepareStatement("INSERT INTO nv_assigned_attributes (attrID, value) VALUES" + "(13,'?')," + "(1,'?'),"
						+ "(3,'?')," + "(12, '?')");

		statement.setString(1, privilege.getUniqueName());
		statement.setString(2, privilege.getDisplayName());
		statement.setString(3, privilege.getDescription());
		statement.setString(4, privilege.getSystem());

		statement.executeUpdate();
		statement.close();

	}

	@Override
	public void editRole(ObjectInfo role) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO nv_assigned_attributes (attrID, value) VALUES" + "(13,'?')," + "(1,'?')," + "(3,'?'),");

		statement.setString(1, role.getUniqueName());
		statement.setString(2, role.getDisplayName());
		statement.setString(3, role.getDescription());

		statement.executeUpdate();
		statement.close();
	}

	@Override
	public void setDeleted(int id) throws Exception {
		Connection connection = getDBConnection();
		PreparedStatement statement = connection
				.prepareStatement("UPDATE nv_created_objects SET is_deleted = ? WHERE id = ?");

		statement.setString(1, "true");
		statement.setInt(2, id);

		statement.executeUpdate();
		statement.close();
	}

	@Override
	public void setNotDeleted(int id) throws Exception {
		Connection connection = getDBConnection();
		PreparedStatement statement = connection
				.prepareStatement("UPDATE nv_created_objects SET is_deleted = ? WHERE id = ?");

		statement.setString(1, "false");
		statement.setInt(2, id);

		statement.executeUpdate();
		statement.close();
	}

	@Override
	public int objectExists(String uniqueName) throws Exception {
		Connection connection = getDBConnection();
		PreparedStatement statement = connection
				.prepareStatement("SELECT COUNT(name) FROM nv_created_objects " + "WHERE name = ?");
		statement.setString(1, uniqueName);
		ResultSet rSet = statement.executeQuery();
		rSet.next();

		return rSet.getInt(1);
	}

	private ObjectInfo findRoleById(int id) throws Exception {
		Connection connection = getDBConnection();
		ObjectInfo role = new ObjectInfo();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT object_id, attribute_id, value FROM nv_assigned_attributes " + "WHERE object_id = ?");
		statement.setInt(1, id);

		List<ObjectInfo> access = findAllAccessForObject(id);
		role.setAssignments(access);
		List<ObjectInfo> assignedTo = findAllAssignedToObjects(id);
		role.setAssignedTo(assignedTo);

		ResultSet rSet = statement.executeQuery();

		attachAttributuesToObject(rSet, role);

		statement.close();
		return role;
	}

	private ObjectInfo findPrivilegeById(int id) throws Exception {
		Connection connection = getDBConnection();
		ObjectInfo privilege = new ObjectInfo();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT object_id, attribute_id, value FROM nv_assigned_attributes " + "WHERE object_id = ?");
		statement.setInt(1, id);

		List<ObjectInfo> access = findAllAccessForObject(id);
		privilege.setAssignments(access);
		List<ObjectInfo> assignedTo = findAllAssignedToObjects(id);
		privilege.setAssignedTo(assignedTo);

		ResultSet rSet = statement.executeQuery();
		attachAttributuesToObject(rSet, privilege);

		statement.close();
		return privilege;
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
	public List<ObjectInfo> findRolesByObjectId(int id) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT r.this_created_object, r.other_created_object, c.object_id, c.id FROM nv_relations AS r "
						+ "JOIN nv_created_objects AS c ON r.other_created_object = c.id "
						+ "WHERE c.object_id = ? AND r.this_created_object = ?");
		statement.setInt(1, 2);
		statement.setInt(2, id);

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> roles = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo role = findObjectById(rSet.getInt("other_created_object"));

			roles.add(role);
		}

		statement.close();

		return roles;
	}

	@Override
	public List<ObjectInfo> findPrivilegeByObjectId(int id) throws Exception {
		Connection connection = getDBConnection();

		PreparedStatement statement = connection.prepareStatement(
				"SELECT r.this_created_object, r.other_created_object, c.object_id, c.id, c.is_deleted FROM nv_relations AS r "
						+ "JOIN nv_created_objects AS c ON r.other_created_object = c.id "
						+ "WHERE c.object_id = ? AND r.this_created_object = ? AND c.is_deleted = ?");
		statement.setInt(1, 3);
		statement.setInt(2, id);
		statement.setString(3, "false");

		ResultSet rSet = statement.executeQuery();
		List<ObjectInfo> privileges = new ArrayList<>();

		while (rSet.next()) {
			ObjectInfo privilege = findObjectById(rSet.getInt("other_created_object"));
			privilege.setIsDeleted(rSet.getString("is_deleted"));

			privileges.add(privilege);
		}

		statement.close();

		return privileges;
	}

}
