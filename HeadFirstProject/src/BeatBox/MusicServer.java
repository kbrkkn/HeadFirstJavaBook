package BeatBox;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

public class MusicServer {
	ArrayList clientOutputStreams;

	public static void main(String[] args) {
		new MusicServer().go();
	}

	public class ClientHandler implements Runnable {
		ObjectInputStream in;

		public ClientHandler(Socket socket) {
			try {
				in = new ObjectInputStream(socket.getInputStream());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void run() {
			Object o1 = null, o2 = null;
			try {
				while ((o1 = in.readObject()) != null) {
					o2 = in.readObject();
					System.out.println("read two objects");
					tellEveryone(o1, o2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void tellEveryone(Object one, Object two) {
		Iterator it = clientOutputStreams.iterator();
		while (it.hasNext()) {
			try {
				ObjectOutputStream out = (ObjectOutputStream) it.next();
				out.writeObject(one);
				out.writeObject(two);
				System.out.println(one.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void go() {
		clientOutputStreams = new ArrayList();
		try {
			ServerSocket serverSock = new ServerSocket(4242);
			while (true) {
				Socket clientSocket = serverSock.accept();
				ObjectOutputStream out = new ObjectOutputStream(
						clientSocket.getOutputStream());
				clientOutputStreams.add(out);
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
