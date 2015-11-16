package de.fhrt.pcca.main;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.fhrt.pcca.sha1.Node;

public class WorkerThread extends Thread {
	private WorkerThread[] otherThreads;
	private ConcurrentLinkedQueue<Node> queue = new ConcurrentLinkedQueue<>();
	private int stealindex = 0;
	private int count;

	public void setOtherThreads(WorkerThread[] otherThreads) {
		this.otherThreads = otherThreads;
	}

	public int getCount() {
		return this.count;
	}

	public Node removeNode() {
		try {
			return queue.remove();

		} catch (NoSuchElementException e) {
			// e.printStackTrace();
			return null;
		}
		// if (!queue.isEmpty()) {
		// return queue.remove();
		// }
		// return null;
	}

	public void insertNode(Node node) {
		queue.add(node);
	}

	public void run() {
		loop: while (true) {
			if (queue.isEmpty()) {
				Node stealNode = otherThreads[stealindex].removeNode();
				while (stealNode == null) {
					stealindex++;
					if (stealindex == otherThreads.length) {
						System.out.println(Thread.currentThread().getName() + " is out of work. Time: "
								+ System.currentTimeMillis());
						break loop;
					}
					stealNode = otherThreads[stealindex].removeNode();
				}
				this.insertNode(stealNode);
				// reset the counter when a steal was successful
				stealindex = 0;
				// queue.add(stealNode);
			} else {
				// System.out.println(count);
				// System.out.println("Thread " +
				// Thread.currentThread().getId()
				// + " is doing work");
				Node tempNode = removeNode();
				if (tempNode != null) {
					for (int i = 0; i < tempNode.getActualChildAmount(); i++) {
						count++;
						// System.out.println("Thread " +
						// Thread.currentThread().getId() + " count is " +
						// count);
						this.insertNode(tempNode.generateChild(i));
						// queue.add(tempNode.generateChild(i));
					}
				}
			}
		}

	}

}
