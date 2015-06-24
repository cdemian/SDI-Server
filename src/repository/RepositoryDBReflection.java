package repository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

public class RepositoryDBReflection<K, V> implements IRepository<K, V> {
	private String driver;
	private String connString;
	private String user;
	private String pass;
	private Statement stmt;
	private Connection con;
	private ArrayList<String> fields;
	private Class<V> typeArgumentClass;

	/**
	 * Map holds as key the field's name, at as value the field's type (int,
	 * string, long...)
	 * 
	 * @param fields
	 */
	public RepositoryDBReflection(ArrayList<String> fields, Class<V> typeArgumentClass) {
		this.driver = "com.mysql.jdbc.Driver";
		this.connString = "jdbc:mysql://localhost/sdi";// "jdbc:mysql://www.scs.ubbcluj.ro/gaie1245";
		this.user = "root";
		this.pass = "";

		this.fields = fields;
		this.typeArgumentClass = typeArgumentClass;
	}

	public void connect() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(connString, user, pass);
			stmt = con.createStatement();
		} catch (Exception ex) {
			System.out.println("Error at connecting to database: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void disconnect() throws SQLException {
		con.close();
	}

	@Override
	public ArrayList<V> getObjects() {
		ArrayList<V> students = new ArrayList<V>();

		try {
			connect();
			String query = "select * from students";
			ResultSet result = stmt.executeQuery(query);
			while (result.next()) {
				V v = typeArgumentClass.newInstance();
				Field[] fs = v.getClass().getFields();
				for (int i = 0; i < fields.size(); i++) {
					Field fss = fs[i];
					System.out.println("Field name: " + fss.getName() + ", type: " + fss.getType());
					if (fss.getType().equals(String.class)) {
						fss.set(v, result.getString(fields.get(i)));
					}
				}
				
				students.add(v);

				// Student c = new Student();
				// c.id = result.getString("id");
				// c.name = result.getString("name");
				// c.group = result.getString("gr");
				// students.add(c);
			}

			disconnect();
		} catch (Exception e) {
			return null;
		}

		return students;
	}

	@Override
	public V getObject(K key) {
		ArrayList<V> students = new ArrayList<V>();

		try {
			connect();
			String query = "select * from students";
			ResultSet result = stmt.executeQuery(query);
			while (result.next()) {
				// Student c = new Student();
				// c.id = result.getString("id");
				// c.name = result.getString("name");
				// c.group = result.getString("gr");
				// students.add(c);
			}

			disconnect();
		} catch (Exception e) {
			return null;
		}

		if (students.size() == 0) {
			return null;
		}

		return students.get(0);
	}

	@Override
	public void storeObject(K key, V value) {
		try {
			System.out.println("Inserting student into database");

			connect();
			String query = "";// "insert into students values(null, '" +
								// value.name + "', " + value.group + ")";
			int i = stmt.executeUpdate(query);
			if (i < 1) {
				throw new Exception("Error at inserting new collection");
			}
			disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteObject(K key) {
		try {
			connect();
			String query = "delete from students where id=" + key + ";";
			int i = stmt.executeUpdate(query);
			if (i < 1) {
				throw new Exception("Error at inserting new collection");
			}
			disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateObject(K key, V value) {
		try {
			connect();
			String query = "";// "update students set name='" + value.name +
								// "', gr='" + value.group + "' where id=" + key
								// + ";";
			int i = stmt.executeUpdate(query);
			if (i < 1) {
				throw new Exception("Error at inserting new collection");
			}
			disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private V getInstanceOfV() {
		V v = null;

		try {
			v = (V) ((Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return v;
	}
}
