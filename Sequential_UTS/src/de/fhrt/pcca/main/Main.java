package de.fhrt.pcca.main;

import java.util.LinkedList;

import de.fhrt.pcca.sha1.Node;

public class Main {
	static LinkedList<Node> queue = new LinkedList<>();
	static int count = 0;
	static int[] statistics = new int[2000];

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(10000);

		long startTime = System.currentTimeMillis();

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		String parentID = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000101010";

		Node root = new Node(parentID, 0.124875, 8, 2000);

		for (int i = 0; i < root.getParamRootChildren(); i++) {
			count++;
			statistics[i]++;
			queue.add(root.generateChild(i));
		}

		while (!queue.isEmpty()) {
			// breadth first search
			// Node tempNode = queue.remove();

			// depth-first search
			Node tempNode = queue.removeLast();
			// System.out.println(tempNode.toString());
			int amountOfchildren = tempNode.getActualChildAmount();
			// System.out.println(tempNode.getLevelkey());
			for (int i = 0; i < amountOfchildren; i++) {
				count++;
				// System.out.println(count);

				statistics[tempNode.getSubtreeid()]++;
				queue.add(tempNode.generateChild(i));
			}
		}
		System.out.println(count);
		System.out.println(System.currentTimeMillis() - startTime);

		// for (int i = 0; i < 2000; i++) {
		// System.out.println(statistics[i]);
		// }
	}

}
