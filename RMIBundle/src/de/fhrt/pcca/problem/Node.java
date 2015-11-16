package de.fhrt.pcca.problem;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {

	private ArrayList<Node> children;
	private int level;
	private int no;

	public Node(int level, int no, int threshold, int amountOfChildren, int density, boolean hasChildren) {

		this.level = level;
		this.no = no;
		children = new ArrayList<Node>();

		if (level == threshold || !hasChildren) {
		} else {
			level++;
			for (int i = 1; i < amountOfChildren + 1; i++) {
				if (i % density == 0) {
					children.add(new Node(level, i, threshold, amountOfChildren, density, true));
				} else {
					children.add(new Node(level, i, threshold, amountOfChildren, density, false));
				}
			}
		}
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.level + "-" + this.no;
	}
}
