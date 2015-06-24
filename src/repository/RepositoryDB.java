package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Student;

public class RepositoryDB implements IRepository<String, Student> {
	private String driver;
	private String connString;
	private String user;
	private String pass;
	private Statement stmt;
	private Connection con;

	public RepositoryDB() {
		this.driver = "com.mysql.jdbc.Driver";
		this.connString = "jdbc:mysql://localhost/sdi";// "jdbc:mysql://www.scs.ubbcluj.ro/gaie1245";
		this.user = "root";
		this.pass = "";
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
	public ArrayList<Student> getObjects() {
		ArrayList<Student> students = new ArrayList<Student>();

		try {
			connect();
			String query = "select * from students";
			ResultSet result = stmt.executeQuery(query);
			while (result.next()) {
				Student c = new Student();
				c.id = result.getString("id");
				c.name = result.getString("name");
				c.group = result.getString("gr");
				students.add(c);
			}

			disconnect();
		} catch (Exception e) {
			return null;
		}

		return students;
	}

	@Override
	public Student getObject(String key) {
		ArrayList<Student> students = new ArrayList<Student>();

		try {
			connect();
			String query = "select * from students";
			ResultSet result = stmt.executeQuery(query);
			while (result.next()) {
				Student c = new Student();
				c.id = result.getString("id");
				c.name = result.getString("name");
				c.group = result.getString("gr");
				students.add(c);
			}

			disconnect();
		} catch (Exception e) {
			return null;
		}

		if (students.size() == 0){
			return null;
		}
		
		return students.get(0);
	}

	@Override
	public void storeObject(String key, Student value) {
		try {
			System.out.println("Inserting student into database");

			connect();
			String query = "insert into students values(null, '" + value.name + "', " + value.group + ")";
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
	public void deleteObject(String key) {
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
	public void updateObject(String key, Student value) {
		try {
			connect();
			String query = "update students set name='" + value.name + "', gr='" + value.group + "' where id=" + key + ";";
			int i = stmt.executeUpdate(query);
			if (i < 1) {
				throw new Exception("Error at inserting new collection");
			}
			disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
