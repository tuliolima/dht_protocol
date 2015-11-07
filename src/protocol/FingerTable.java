package protocol;

public class FingerTable {

	Finger[] fingers;
        
        /**
         * Construtor de FingerTable
         * @param node nó a ser inserido na tabela
         */
	public FingerTable(Node node) {
		this.fingers = new Finger[Hash.KEY_LENGTH];
		for (int i = 0; i < fingers.length; i++) {
			Key start = node.getNodeKey().createStartKey(i);
			fingers[i] = new Finger(start, node);
		}
	}
        
        /**
         * Retorna um Finger da tabela
         * @param i índice do Finger
         * @return finger de índice i
         */
	public Finger getFinger(int i) {
		return fingers[i];
	}

}