package app;

import controller.Controller;
import networking.SecretaryServiceProxy;

public class Server {
	private static Thread clientListenerThread;

	public static void main(String[] args) {

		clientListenerThread = (new Thread() {
			public void run() {
				SecretaryServiceProxy server = new SecretaryServiceProxy();
				Controller.getInstance().setProxy(server);
				server.startListeningForClients();
			}
		});
		
		clientListenerThread.start();
		
		try {
			clientListenerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
