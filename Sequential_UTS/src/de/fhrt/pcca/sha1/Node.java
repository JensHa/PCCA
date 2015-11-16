package de.fhrt.pcca.sha1;

public class Node {

	private static double paramProb;
	private static int paramChildren;
	private static int paramRootChildren;
	private final String id;
	private final int subtreeId;
	private final String levelKey;

	private Node(String id, int subtreeid, String levelKey) {
		this.id = id;
		this.subtreeId = subtreeid;
		this.levelKey = levelKey;
	}

	public Node(String parentID, double paramProb, int paramChildren, int paramRootChildren) {
		this(parentID, 0, "0.");
		Node.paramProb = paramProb;
		Node.paramChildren = paramChildren;
		Node.paramRootChildren = paramRootChildren;
	}

	public String getLevelkey() {
		return levelKey;
	}

	public String getId() {
		return id;
	}

	public double getParamProp() {
		return Node.paramProb;
	}

	public int getParamChildren() {
		return Node.paramChildren;
	}

	public int getParamRootChildren() {
		return Node.paramRootChildren;
	}

	public int getSubtreeid() {
		return subtreeId;
	}

	public int getActualChildAmount() {
		return Converter.noOfChildren(this.getId(), this.getParamProp(), this.getParamChildren());
	}

	public Node generateChild(int index) {
		int subTreeIndexforChild = subtreeId;
		if (subTreeIndexforChild == 0) {
			subTreeIndexforChild = index;
		}
		return new Node(Converter.hexStringToBinString(Converter.getChildId(this.getId(), index)), subTreeIndexforChild,
				String.valueOf(this.getLevelkey() + index + "."));

	}

	@Override
	public String toString() {
		return "{" + this.id + ", " + this.subtreeId + ", " + this.levelKey + "}\n";
	}

}
