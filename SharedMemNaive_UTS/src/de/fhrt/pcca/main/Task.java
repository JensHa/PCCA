package de.fhrt.pcca.main;

import java.util.LinkedList;

import de.fhrt.pcca.sha1.Node;

public class Task implements Runnable {
	LinkedList<Node> queue = new LinkedList<>();
	int count = 0;

	public Task(LinkedList<Node> nodes) {
		queue.addAll(nodes);
	}

	@Override
	public void run() {
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
				queue.add(tempNode.generateChild(i));
			}

		}

		Main.addValue(count);
	}
}