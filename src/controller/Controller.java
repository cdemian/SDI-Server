package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import networking.SecretaryServiceProxy;
import exception.InvalidStudentIdException;
import exception.StudentIdAlreadyInUseException;
import model.Student;
import repository.IRepository;
import repository.RepositoryDB;
import repository.RepositoryDBReflection;
import repository.RepositoryFile;
import repository.RepositoryXML;

public class Controller {
	private static Controller instance;
	
//	private IRepository<String, Student> studRepo;
	private RepositoryDB studRepo;
//	private RepositoryFile<String, String> secretaryRepo;
	private RepositoryXML secretaryRepo;
	private SecretaryServiceProxy proxy;

	private Controller() {
		//studRepo = new RepositoryFile<String, Student>("students.dat");
		studRepo = new RepositoryDB();
		

//		ArrayList<String> m = new ArrayList<String>();
//		m.add("id");
//		m.add("name");
//		m.add("gr");
//		studRepo = new RepositoryDBReflection<String, Student>(m, Student.class);
//		
//		secretaryRepo = new RepositoryFile<String, String>("secretaries.dat");
		secretaryRepo = new RepositoryXML("secretaries.xml");

		//secretaryRepo.storeObject("adam", "pass");
	}

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}

		return instance;
	}

	public boolean login(String username, String password) {
		Class<?>[] interfaces = studRepo.getClass().getInterfaces();
		
		for (Class<?> c: interfaces){
			if (c.getClass().equals(IRepository.class)){
				System.out.println("It's a repo");
			} else {
				System.out.println("It's not a repo???");
			}
		}
		
	
		String pass = instance.secretaryRepo.getObject(username);

		System.out.println("Password from repository: " + pass);
		
		if (pass != null && pass.equals(password)) {
			return true;
		}

		return false;
	}

	public boolean addStudent(String id, String name, String group) throws StudentIdAlreadyInUseException {
//		if (instance.studRepo.getObject(id) != null){
//			throw new StudentIdAlreadyInUseException();
//		}
		
		Student s = new Student();
		s.id = id;
		s.name = name;
		s.group = group;
		
		instance.studRepo.storeObject(s.id, s);
		instance.proxy.update(null, null);
		return true;
	}
	
	public boolean updateStudent(String id, String name, String group) throws InvalidStudentIdException{
		if (instance.studRepo.getObject(id) == null){
			System.out.println("No student with given id to update");
			throw new InvalidStudentIdException();
		}
		System.out.println("Student found with give id to update");
		Student s = new Student();
		s.id = id;
		s.name = name;
		s.group = group;
		
		instance.studRepo.updateObject(s.id, s);
		System.out.println("Student updated with id: " + s.id);
		instance.proxy.update(null, null);
		return true;
	}

	public ArrayList<Student> getAllStudents() {
		return instance.studRepo.getObjects();
	}

	public void setProxy(SecretaryServiceProxy server) {
		instance.proxy = server;
	}

	public ArrayList<Student> getFilteredStudents(String group) {
		ArrayList<Student> studs = instance.studRepo.getObjects();
		
		Iterator<Student> it = studs.iterator();
		
		while (it.hasNext()){
			Student s = it.next();
			
			if (!s.group.equals(group)){
				it.remove();
			}
		}
		
		return studs;
	}
}
