package protocol;

public class Finger {
                
	Key start;
	Node node;
        
        /**
         * Construtor de Finger
         * 
         * @param start
         *          nó inicial do DHT
         * @param node
         *          instância de nó
         */
	public Finger(Key start, Node node) {
		this.node = node;
		this.start = start;
	}
        
        /**
         * Retorna nó de start
         * @return nó inicial 
         */
	public Key getStart() {
		return start;
	}
        
        /**
         * Atribui nó inicial 
         * @param start seta nó
         */
	public void setStart(Key start) {
		this.start = start;
	}
        
        /**
         * Retorna instância de nó
         * @return instância de nó
         */
	public Node getNode() {
		return node;
	}
        
        /**
         * Atribui instância de nó
         * @param node seta instância de nó
         */
	public void setNode(Node node) {
		this.node = node;
	}

}