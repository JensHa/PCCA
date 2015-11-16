package de.fhrt.pcca.main;

import java.rmi.RemoteException;

import de.fhrt.pcca.problem.Node;
import de.fhrt.pcca.server.Queue;

public class Worker {

	private Node node;
	private Queue localQueue;
	int count;

	public Worker(Node node) {
		this.node = node;
	}

	public Worker() {
		this.node = new Node(0, 0, 0, 0, 0, false);
	}

	public void run() {
		try {
			localQueue = new Queue();
			localQueue.start();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		init();
		doWork();
		System.out.println(count);
		localQueue.stop();
	}

	private void init() {
		if (this.node.getChildren().size() > 0) {
			// Root
			count++;
			for (int i = 0; i < node.getChildren().size(); i++) {
				count++;
				Node addNode = node.getChildren().get(i);
				try {
					localQueue.add(addNode);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void doWork() {
		try {
			if ((node = localQueue.get()) != null) {
				for (int i = 0; i < node.getChildren().size(); i++) {
					count++;
					Node addNode = node.getChildren().get(i);
					try {
						localQueue.add(addNode);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				doWork();
			} else {
				stealWork();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void stealWork() {
		// try {
		// QueueInterface remoteQueue = (QueueInterface)
		// Naming.lookup("//192.168.178.15/Server");
		// Node stolenNode = remoteQueue.get();
		// if (stolenNode == null) {
		// System.out.println("steal failed");
		// } else {
		// System.out.println("success");
		// }
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (RemoteException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (NotBoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

}
