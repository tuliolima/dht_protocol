package protocol;

import java.security.MessageDigest;

public class Hash {
        
        //constantes com função hash e tamanho das chaves
	public static final String function = "SHA-1";
	public static final int KEY_LENGTH = 160;
        
        /**
         * Algoritmo para gerar chave SHA-1
         * @param identifier identificador 
         *                          a ser usado para gerar o hash
         * @return value para hash gerado com sucesso ou null para chaves nao geradas
         */
	public static byte[] hash(String identifier) {
                try {
                        MessageDigest md = MessageDigest.getInstance(function);
                        md.reset();
                        byte[] code = md.digest(identifier.getBytes());
                        byte[] value = new byte[KEY_LENGTH / 8];
                        int shrink = code.length / value.length;
                        int bitCount = 1;
                        for (int j = 0; j < code.length * 8; j++) {
                                int currBit = ((code[j / 8] & (1 << (j % 8))) >> j % 8);
                                if (currBit == 1)
                                        bitCount++;
                                if (((j + 1) % shrink) == 0) {
                                        int shrinkBit = (bitCount % 2 == 0) ? 0 : 1;
                                        value[j / shrink / 8] |= (shrinkBit << ((j / shrink) % 8));
                                        bitCount = 1;
                                }
                        }
                        return value;
                } catch (Exception e) {
                        e.printStackTrace();
                }		
		return null;
	}
        
        /**
         * Retorna função hash utilizada (para testes)
         * @return 
         */
	public static String getFunction() {
               return function;
	}
        
        /**
         * Retorna tamanho da chave utilizada (para testes)
         * @return 
         */
	public static int getKeyLength() {
                return KEY_LENGTH;
	}
}