package main;

import java.net.InetAddress;
import java.net.URL;

import protocol.*;

/**
 * Classe Server executa Main Thread
 * @author luigi
 */
public class Server {                
        
        /**
         * Define número de nós
         */	
	public static final int NUM_OF_NODES = 50;
        
        /**
         * Define número da porta
         */
        public static final int port = 10000;        
        
        public static Logger log;
        
	public static void main(String[] args) throws Exception {		
                
                //inicia logger, contador e seta host
                log = new Logger();                                
		long start = System.currentTimeMillis();                
		String host = InetAddress.getLocalHost().getHostAddress();	
                
                //instancia chord
		DhtChord chord = new DhtChord();
		for (int i = 0; i < NUM_OF_NODES; i++) {
                        //seta url dos nós
			URL url = new URL("http", host, port + i, "");
			try {
				chord.createNode(url.toString());
			} catch (DhtChordExcep e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
                
    		log.out.println(NUM_OF_NODES + " nodes are created.");
                
                //ordena chords para printar no log
                //ordenação é feita pelo primeiro byte da key do node
		for (int i = 0; i < NUM_OF_NODES; i++) {
			Node node = chord.getSortedNode(i);
                        //ChordNode node = chord.getNode(i);
			log.out.println(node);
		}
                
		for (int i = 1; i < NUM_OF_NODES; i++) {
			Node node = chord.getNode(i);
			node.join(chord.getNode(0));
			Node preceding = node.getSuccessor().getPredecessor();
			node.stabilize();
			if (preceding == null) {
				node.getSuccessor().stabilize();
			} else {
				preceding.stabilize();
			}
		}
		log.out.println("Chord ring is established.");

		for (int i = 0; i < NUM_OF_NODES; i++) {
			Node node = chord.getNode(i);
			node.fixFingers();
		}
		log.out.println("Finger Tables are fixed.");

		for (int i = 0; i < NUM_OF_NODES; i++) {
			Node node = chord.getSortedNode(i);
			node.printFingerTable(log.out);
		}

		long end = System.currentTimeMillis();

		int interval = (int) (end - start);
		System.out.printf("Elapsed Time : %d.%d\n", interval / 1000,
				interval % 1000);
                //System.out.println(Hash.getFunction());
                //System.out.println(Hash.getKeyLength());
	}
}