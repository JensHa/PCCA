package de.fhrt.pcca.main;

import java.util.LinkedList;

import de.fhrt.pcca.sha1.Node;

public class Main {
	static LinkedList<Node> queue = new LinkedList<>();
	static int count = 0;
	static int[] statistics = new int[2000];

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(10000);

		// Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		long startTime = System.currentTimeMillis();
		String parentID = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000101010";

		Node root = new Node(parentID, 0.124875, 8, 2000);

		int cores = Runtime.getRuntime().availableProcessors();
		int overhead = root.getParamRootChildren() % cores;
		int nodesPerPackage = (root.getParamRootChildren() - overhead) / cores;
		Thread[] worker = new Thread[16];

		for (int i = 0; i < cores; i++) {
			LinkedList<Node> nodesForThread = new LinkedList<>();

			for (int i1 = 0; i1 < nodesPerPackage; i1++) {
				count++;
				nodesForThread.add(root.generateChild(i * nodesPerPackage + i1));
			}

			if (i == cores - 1) {
				for (int i1 = 0; i1 < overhead; i1++) {
					count++;
					nodesForThread.add(root.generateChild(cores * nodesPerPackage + i1));
				}
			}

			worker[i] = new Thread(new Task(nodesForThread));
		}

		for (int i = 0; i < worker.length; i++) {
			worker[i].start();
		}

		for (int i = 0; i < worker.length; i++) {
			worker[i].join();
		}

		System.out.println(count);
		System.out.println(System.currentTimeMillis() - startTime);
	}

	static synchronized void addValue(int value) {
		count += value;
	}

}
