package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import model.Student;
import controller.Controller;
import exception.InvalidStudentIdException;
import exception.StudentIdAlreadyInUseException;

public class SecretaryWorker extends Thread implements Observer {
	private Socket socket;
	private Socket observer_socket;
	
	private DataOutputStream os;
	private DataInputStream is;

	private DataOutputStream obs_os;

	

	public SecretaryWorker(Socket s, Socket obs_socket) {
		socket = s;
		observer_socket = obs_socket;
	
		try {
			socket.setKeepAlive(true);
			observer_socket.setKeepAlive(true);
			
//			observer_socket.shutdownInput();
			os = new DataOutputStream(s.getOutputStream());
			is = new DataInputStream(s.getInputStream());
			obs_os = new DataOutputStream(observer_socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (true) {
			if (socket.isClosed()) {
				System.out.println("Secretary disconnected.");
				break;
			}
			
			try {
				int action = is.readInt();
				System.out.println("Action to be performed: " + action);

				if (action == -1) {
					// logout
					System.out.println("Secretary disconnected");
					socket.close();
					break;
				}

				if (action == 0) {
					// login
					loginAction();
				}

				if (action == 1) {
					// add new student
					addStudentAction();
				}
				
				if (action == 2){
					//update student
					updateStudent();
				}
				
				if (action == 3){
					//send filtered students
					filterStudents();
				}
				
				if (action == 4){
					//update request
					sendUpdate();
				}

			} catch (IOException e) {
				if (e.getMessage().equals("Connection reset")){
					System.out.println("Secretary disconnected");
					break;
				}
				
				System.out.println("Error at reading action from client (main)");
				e.printStackTrace();
			}

		}
	}

	private void filterStudents() {
		try {
			byte filter[] = new byte[1024];
			int len = is.readInt();
			is.readFully(filter, 0, len);
			String sFilter = new String(filter, "UTF-8");
			sFilter = sFilter.substring(0, len);
			
			ArrayList<Student> students = Controller.getInstance().getFilteredStudents(sFilter);
			System.out.println("Students nr: " + students.size());
			System.out.println("Filter: " + sFilter);
			
			
			os.writeInt(students.size());
			os.flush();
			System.out.println("Student's nr sent: " + students.size());
			for (Student s : students) {
				os.writeInt(s.id.length());
				os.flush();
				os.writeBytes(s.id);
				os.flush();
				
				os.writeInt(s.name.length());
				os.flush();
				os.writeBytes(s.name);
				os.flush();
				
				os.writeInt(s.group.length());
				os.flush();
				os.writeBytes(s.group);
				os.flush();
			}
			
			System.out.println("Students sent to client from server in filter");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private void updateStudent() {
		try {
			System.out.println("Performing updating new student action");
			byte[] id = new byte[255];
			byte[] name = new byte[255];
			byte[] group = new byte[255];

			int len = is.readInt();
			is.readFully(id, 0, len);
			String sId = new String(id, "UTF-8");
			sId = sId.substring(0, len);
			
			len = is.readInt();
			is.readFully(name, 0, len);
			String sName = new String(name, "UTF-8");
			sName = sName.substring(0, len);
			
			len = is.readInt();
			is.readFully(group, 0, len);
			String sGroup = new String(group, "UTF-8");
			sGroup = sGroup.substring(0, len);
			
			System.out.println("Id: " + sId);
			System.out.println("Name: " + sName);
			System.out.println("Group: " + sGroup);

			int res = 0;
			try {
				Controller.getInstance().updateStudent(sId, sName, sGroup);
				res = 1;
			} catch (InvalidStudentIdException e) {
				res = -1;
			} finally {
				os.writeInt(res);
				os.flush();
			}
		} catch (IOException e) {
			System.out.println("Error at reading action from client (updating student)");
			e.printStackTrace();
		}
	}

	private void addStudentAction() {
		try {
			System.out.println("Performing adding new student action");
			byte[] id = new byte[255];
			byte[] name = new byte[255];
			byte[] group = new byte[255];

			int len = is.readInt();
			is.readFully(id, 0, len);
			String sId = new String(id, "UTF-8");
			sId = sId.substring(0, len);
			
			len = is.readInt();
			is.readFully(name, 0, len);
			String sName = new String(name, "UTF-8");
			sName = sName.substring(0, len);
			
			len = is.readInt();
			is.readFully(group, 0, len);
			String sGroup = new String(group, "UTF-8");
			sGroup = sGroup.substring(0, len);
			
			System.out.println("Id: " + sId);
			System.out.println("Name: " + sName);
			System.out.println("Group: " + sGroup);

			int res = 0;

			try {
				Controller.getInstance().addStudent(sId, sName, sGroup);
				res = 0;
			} catch (StudentIdAlreadyInUseException e) {
				res = -1;
			} finally {
				os.writeInt(res);
				os.flush();
			}
		} catch (IOException e) {
			System.out.println("Error at reading action from client (add new student)");
			e.printStackTrace();
		}
	}

	private void loginAction() {
		try {
			System.out.println("Performing login action");
			byte[] username = new byte[1024];
			byte[] password = new byte[1024];

			int len = is.readInt();
			System.out.println("Username len: " + len);
			is.readFully(username, 0, len);
			String sUsername = new String(username, "UTF-8");
			sUsername = sUsername.substring(0, len);
			
			len = is.readInt();
			is.readFully(password, 0, len);
			String sPassword = new String(password, "UTF-8");
			sPassword = sPassword.substring(0, len);

			System.out.println("Username: " + sUsername);
			System.out.println("Password: " + sPassword + " + len: " + len);

			int res;
			if (Controller.getInstance().login(sUsername, sPassword)) {
				res = 1;
			} else {
				res = 0;
			}

			System.out.println("Result: " + res);
			os.writeInt(res);
			os.flush();
			
			Thread.currentThread().sleep(500);
			sendUpdate();
		} catch (IOException e) {
			System.out.println("Error at reading action from client");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("Updating worker");	
		sendUpdate();
	}

	private void sendUpdate() {
		ArrayList<Student> students = Controller.getInstance().getAllStudents();
		System.out.println("Students nr: " + students.size());
		try {
			obs_os.writeInt(students.size());
			obs_os.flush();
			for (Student s : students) {
				obs_os.writeInt(s.id.length());
				obs_os.flush();
				obs_os.writeBytes(s.id);
				obs_os.flush();
				obs_os.writeInt(s.name.length());
				obs_os.flush();
				obs_os.writeBytes(s.name);
				obs_os.flush();
				obs_os.writeInt(s.group.length());
				obs_os.flush();
				obs_os.writeBytes(s.group);
			}
			
			System.out.println("Students sent to client from server in update");
		} catch (Exception e) {

		}
	}

}
