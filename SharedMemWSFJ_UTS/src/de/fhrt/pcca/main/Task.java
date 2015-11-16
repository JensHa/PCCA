package de.fhrt.pcca.main;

import java.util.concurrent.RecursiveTask;

import de.fhrt.pcca.sha1.Node;

public class Task extends RecursiveTask<Integer> {
	Node initNode;
	private int count = 0;

	public Task(Node node) {
		this.initNode = node;
	}

	@Override
	protected Integer compute() {
		if (initNode.getLevelkey().equals("0.")) {
			Task[] nodes = new Task[initNode.getParamRootChildren()];
			for (int i = 0; i < initNode.getParamRootChildren(); i++) {
				count++;
				nodes[i] = new Task(initNode.generateChild(i));
				nodes[i].fork();
			}
			for (int i = 0; i < nodes.length; i++) {
				count += nodes[i].join();
			}
		} else {
			Task[] nodes = new Task[initNode.getActualChildAmount()];
			for (int i = 0; i < initNode.getActualChildAmount(); i++) {
				count++;
				nodes[i] = new Task(initNode.generateChild(i));
				nodes[i].fork();
			}
			for (int i = 0; i < nodes.length; i++) {
				count += nodes[i].join();
			}
		}
		// TODO Auto-generated method stub
		return count;
	}

}
