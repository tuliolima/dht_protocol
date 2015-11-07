package protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class DhtChord {
        
   /**
     * Atributos da classe DhtChord
     * nodeList: lista de nós do tipo Node
     * sortedNodeMap: 
     * sortedKeyArray:
     */
	List<Node> nodeList = new ArrayList<Node>();
	SortedMap<Key, Node> sortedNodeMap = new TreeMap<Key, Node>();
	Object[] sortedKeyArray;
        
        /**
         * Cria primeiro nó
         * @param nodeId url gerada pelo host
         * @throws DhtChordExcep classe de exceções do DhtChord
         */
	public void createNode(String nodeId) throws DhtChordExcep {
                //instancia primeiro nó
		Node node = new Node(nodeId);
		nodeList.add(node);
		
                //checa key duplicada
		if (sortedNodeMap.get(node.getNodeKey()) != null ) {
			throw new DhtChordExcep("Duplicated Key: " + node);
		}
		//insere no sortedmap chave e nó
		sortedNodeMap.put(node.getNodeKey(), node);
	}
        
        /**
         * Retorna nó i da lista
         * @param i índice do nó a ser retornado
         * @return nó i da lista
         */
	public Node getNode(int i) {
		return (Node) nodeList.get(i);
	}
        
        /**
         * 
         * @param i
         * @return 
         */
	public Node getSortedNode(int i) {
		if (sortedKeyArray == null) {
			sortedKeyArray = sortedNodeMap.keySet().toArray();
		}
		return (Node) sortedNodeMap.get(sortedKeyArray[i]);
	}
}