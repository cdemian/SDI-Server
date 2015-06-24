package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class SecretaryServiceProxy implements Observer {
	private ServerSocket socket;
	private ServerSocket observer_socket;
	private final static Integer PORT = 8000;
	private final static Integer OBSERVER_PORT = 8001;
	private ArrayList<SecretaryWorker> clients = new ArrayList<SecretaryWorker>();

	public SecretaryServiceProxy() {
		try {
			socket = new ServerSocket(PORT);
			observer_socket = new ServerSocket(OBSERVER_PORT);
		} catch (IOException e) {
			System.out.println("Error at setting up server socket");
			e.printStackTrace();
		}
	}

	public void startListeningForClients() {
		while (true) {
			try {
				Socket s = socket.accept();
				Socket os = observer_socket.accept();
				SecretaryWorker w = new SecretaryWorker(s, os);
				w.start();

				clients.add(w);

				System.out.println("New secretary connected, " + s.getPort());
			} catch (IOException e) {
				System.out.println("Error at new client connetion");
				e.printStackTrace();
			}
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Updateing workers in Proxy");

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				for (SecretaryWorker w : clients) {
					w.update(null, null);
				}
			}
		});

		t.start();
	}
}
