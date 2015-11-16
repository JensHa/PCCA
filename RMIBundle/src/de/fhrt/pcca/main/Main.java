package de.fhrt.pcca.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.fhrt.pcca.problem.Node;

public class Main {

	public static void main(String[] args) {
		// try {
		// Thread.sleep(15000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		loadConfig();

		startWorker(new Node(0, 0, 15, 5, 2, true));

		// startResponder();

		/*
		 * try { ServerInterface server =
		 * (ServerInterface)Naming.lookup("//134.103.195.117/Server");
		 * server.method(); server.printInt(10); } catch (Exception ex) {
		 * ex.printStackTrace(); }
		 */
	}

	private static void startWorker(Node node) {
		System.out.println("here");
		// try {
		// Thread.sleep(15000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		Worker worker = new Worker(node);
		worker.run();

	}

	private static String[] loadConfig() {
		Properties prop = new Properties();
		InputStream input = null;
		String[] instances = null;
		try {

			input = new FileInputStream("config.properties");

			// load a properties file
			prop.load(input);

			instances = prop.getProperty("nodes").split(",");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return instances;
	}

}
