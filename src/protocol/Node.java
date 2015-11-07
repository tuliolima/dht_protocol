package protocol;

import java.io.PrintStream;

public class Node {
        
    /**
     * Atributos da classe Node
     * nodeId: Id do nó
     * nodeKey: key do nó
     * predecessor: predecessor do nó
     * successor: successor do nó
     * fingerTable: fingerTable do nó
     * 
     */
	String nodeId;
	Key nodeKey;
	Node predecessor;
	Node successor;
	FingerTable fingerTable;
        
        /**
         * Construtor
         * @param nodeId ID a ser atribuido ao novo nó
         */
	public Node(String nodeId) {
		this.nodeId = nodeId;
		this.nodeKey = new Key(nodeId);
		this.fingerTable = new FingerTable(this);
		this.create();
	}

	/**
	 * Procura successor com id recebido
	 * 
	 * @param identifier
	 *            identificador a ser procurado
	 * @return successor com identificador recebido
	 */
	public Node findSuccessor(String identifier) {
		Key key = new Key(identifier);
		return findSuccessor(key);
	}

	/**
	 * Procura successor com key recebida
	 * 
	 * @param key
	 *           chave que será procurada 
	 * @return successor com key recebida
	 */
	public Node findSuccessor(Key key) {
		if (this == successor) {
			return this;
		}

		if (key.isBetween(this.getNodeKey(), successor.getNodeKey())
				|| key.compareTo(successor.getNodeKey()) == 0) {
			return successor;
		} else {
			Node node = closestPrecedingNode(key);
			if (node == this) {
				return successor.findSuccessor(key);
			}
			return node.findSuccessor(key);
		}
	}
        
        //TODO documentar closesPrecedingNode
        /**
         * 
         * @param key 
         * @return 
         */
	private Node closestPrecedingNode(Key key) {
		for (int i = Hash.KEY_LENGTH - 1; i >= 0; i--) {
			Finger finger = fingerTable.getFinger(i);
			Key fingerKey = finger.getNode().getNodeKey();
			if (fingerKey.isBetween(this.getNodeKey(), key)) {
				return finger.getNode();
			}
		}
		return this;
	}

	/**
	 * Aponta para successor e predecessor
	 */
	public void create() {
		predecessor = null;
		successor = this;
	}

	/**
	 * Insere um nó na lista
	 * 
	 * @param node
	 *            nó inicial
	 */
	public void join(Node node) {
		predecessor = null;
		successor = node.findSuccessor(this.getNodeId());
	}

	/**
	 * Verifica sucessor e notifica sobre objeto this. Implementa keep alive
         * Obs.: deve ser chamado de tempo em tempo
	 * 
	 */
	public void stabilize() {
		Node node = successor.getPredecessor();
		if (node != null) {
			Key key = node.getNodeKey();
			if ((this == successor)
					|| key.isBetween(this.getNodeKey(), successor.getNodeKey())) {
				successor = node;
			}
		}
		successor.notifyPredecessor(this);
	}
        
        /**
         * Notifica predecessor sobre nó recebido
         * @param node 
         *              nó a ser atribuido a predecessor
         */
	private void notifyPredecessor(Node node) {
		Key key = node.getNodeKey();
		if (predecessor == null
				|| key.isBetween(predecessor.getNodeKey(), this.getNodeKey())) {
			predecessor = node;
		}
	}

	/**
	 * Atualiza fingerTable do nó
	 */
	public void fixFingers() {
		for (int i = 0; i < Hash.KEY_LENGTH; i++) {
			Finger finger = fingerTable.getFinger(i);
			Key key = finger.getStart();
			finger.setNode(findSuccessor(key));
		}
	}
        
        /**
         * Converte nó em string
         * @return string contendo id e key do nó
         */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nChordNode[");
		sb.append("ID=" + nodeId);
		sb.append(",KEY=" + nodeKey);
		sb.append("]");
		return sb.toString();
	}
        
        /**
         * Imprime fingerTable do nó em out
         * @param out 
         *          PrintStream a ser utilizado para imprimir fingerTable
         */
	public void printFingerTable(PrintStream out) {
		out.println("=======================================================");
		out.println("FingerTable: " + this);
		out.println("-------------------------------------------------------");
		out.println("Predecessor: " + predecessor);
		out.println("Successor: " + successor);
		out.println("-------------------------------------------------------");
		for (int i = 0; i < Hash.KEY_LENGTH; i++) {
			Finger finger = fingerTable.getFinger(i);
			out.println(finger.getStart() + "\t" + finger.getNode());
		}
		out.println("=======================================================");
	}

        //getters and setters
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Key getNodeKey() {
		return nodeKey;
	}

	public void setNodeKey(Key nodeKey) {
		this.nodeKey = nodeKey;
	}

	public Node getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Node predecessor) {
		this.predecessor = predecessor;
	}

	public Node getSuccessor() {
		return successor;
	}

	public void setSuccessor(Node successor) {
		this.successor = successor;
	}

	public FingerTable getFingerTable() {
		return fingerTable;
	}

	public void setFingerTable(FingerTable fingerTable) {
		this.fingerTable = fingerTable;
	}

}