package de.fhrt.pcca.main;

import java.util.concurrent.ForkJoinPool;

import de.fhrt.pcca.sha1.Node;

public class Main {

	private static Integer count = new Integer(0);

	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(10000);

		long startTime = System.currentTimeMillis();
		final ForkJoinPool fjPool = new ForkJoinPool();
		String parentID = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000101010";

		Node root = new Node(parentID, 0.124875, 8, 2000);

		count += fjPool.invoke(new Task(root));
		// for (int i = 0; i < root.getParamRootChildren(); i++) {
		// count += fjPool.invoke(new Task(root.generateChild(i)));
		// count++;
		// }

		System.out.println(count);
		System.out.println(System.currentTimeMillis() - startTime);

	}

}
