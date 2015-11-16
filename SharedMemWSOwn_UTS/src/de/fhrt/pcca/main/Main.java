package de.fhrt.pcca.main;

import de.fhrt.pcca.sha1.Node;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(10000);
		long startTime = System.currentTimeMillis();

		String parentID = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000101010";

		Node root = new Node(parentID, 0.124875, 8, 2000);
		int count = 0;
		int cores = Runtime.getRuntime().availableProcessors();
		WorkerThread[] workers = new WorkerThread[cores];

		for (int i = 0; i < cores; i++) {
			workers[i] = new WorkerThread();
		}

		// this list is used by each thread so that each thread knows all other
		// threads
		for (int i = 0; i < cores; i++) {
			workers[i].setOtherThreads(workers);
		}

		// push all nodes of level 1 to the first thread
		for (int i = 0; i < root.getParamRootChildren(); i++) {
			count++;
			System.out.println("Push work to  " + workers[0].getName());
			workers[0].insertNode(root.generateChild(i));
		}

		for (int i = 0; i < cores; i++) {
			workers[i].start();
		}

		for (int i = 0; i < cores; i++) {
			workers[i].join();
			count += workers[i].getCount();
		}
		System.out.println(count);
		System.out.println(System.currentTimeMillis() - startTime);

	}

}
