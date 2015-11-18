package main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Protocolo usado pelo Rendezvous da rede DHT
 * @author tuliolima
 *
 */
public class ServerProtocol {

	private static final int serverPort = 9061; /**< Porta a ser usada pelo Rendezvous*/
	private static final int packetSize = 1024; /**< Tamanho do pacote usado pelo socket*/
	private static InetAddress iPAddress; /**< IP da mensagem recebida*/
	private static int port; /**< porta da mensagem recebida*/
	private static DatagramSocket serverSocket; /**< Socket UDP*/
	private static boolean hasRoot = false; /**< Indica se já existe um root*/
	private static int ID = 0; /**< contador de IDs*/
	private static List<Node> nodeList = new ArrayList<Node>(); /**< Lista de nós já registrados*/
	private static Node root; /**< Nó raiz*/
	private static int maxID = 20; /**< Número máximo de nós*/
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("Ouvindo");
		
		Message message = null;

		serverSocket = new DatagramSocket(serverPort);
		
		while(true) {

			message = receiveMessage();
			
			switch (message.opcao) {
			//Mensagem de Hello
			case 1 :
				System.out.println("1 - Recebi um Hello");
				sendMessage(2, String.valueOf(ID), iPAddress, port);
				System.out.println("Mandei o ID " + ID);
				ID++;
				break;
			//Mensagem de ACK
			case 3 :
				System.out.println("3 - Recebi um ACK");
				nodeList.add(ID-1, new Node(ID-1, iPAddress, port));
				if (hasRoot) {
					sendMessage(4, root.getIP().getHostName() + ":" + root.getPort(), iPAddress, port);
					System.out.println("Mandei o endereço do root");
				} else {
					sendMessage(4, null, iPAddress, port);
					System.out.println("Elegi um novo root");
					hasRoot = true;
					root = new Node(ID-1, iPAddress, port);
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Envia uma mensagem através de socket usando conexão UDP.
	 * @param opcao
	 * @param mess
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	private static void sendMessage (int opcao, String mess, InetAddress ip, int port) throws IOException {
		Message message = new Message(opcao, mess);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
      	ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(message);
        byte[] sendData = out.toByteArray();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
		serverSocket.send(sendPacket);
	}

	/**
	 * Recebe uma mensagem através de socket usando conexão UDP
	 * @return
	 * @throws IOException
	 */
	private static Message receiveMessage () throws IOException {
		byte[] receiveData = new byte[packetSize];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.receive(receivePacket);
		byte[] data = receivePacket.getData();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        Message message = null;
        try {
        	message = (Message) is.readObject();
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
        }
		iPAddress = receivePacket.getAddress();
		port = receivePacket.getPort();
		return message;
	}
}
