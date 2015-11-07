package protocol;

public class Key implements Comparable {
        
        /**
        * Atributos da classe Key
        * identifier:
        * key:
        */
	String identifier;
	byte[] key;
        
        /**
         * Construtor
         * @param key a ser atribuida em array de bytes
         */
	public Key(byte[] key) {
		this.key = key;
	}
        
        /**
         * Construtor
         * Atribui identifier a identifier e hasheia identifier e atribui a key
         * @param identifier a ser hasheado
         */
	public Key(String identifier) {
		this.identifier = identifier;
		this.key = Hash.hash(identifier);
	}
        
        /** 
         * Checa se nó está entre dois nós dados
         * @param fromKey nó inicial da comparação
         * @param toKey nó final da comparação
         * @return true caso nó esteja entre nós dados ou false caso contrário
         */
	public boolean isBetween(Key fromKey, Key toKey) {
                //
		if (fromKey.compareTo(toKey) < 0) {
			if (this.compareTo(fromKey) > 0 && this.compareTo(toKey) < 0) {
				return true;
			}
		} else if (fromKey.compareTo(toKey) > 0) {
			if (this.compareTo(toKey) < 0 || this.compareTo(fromKey) > 0) {
				return true;
			}
		}
		return false;
	}
        //TODO documentar createStartKey
        /**
         * 
         * @param index
         * @return 
         */
	public Key createStartKey(int index) {
                //instancia novo byte com tamanho key.length
		byte[] newKey = new byte[key.length];
		System.arraycopy(key, 0, newKey, 0, key.length);
		int carry = 0;
		for (int i = (Hash.KEY_LENGTH - 1) / 8; i >= 0; i--) {
                        //pega byte de key em int                        
			int value = key[i] & 0xff;
                        //acrescenta a value 1 deslocado (index mod 8) bits a esquerda com carry
			value += (1 << (index % 8)) + carry;
                        //atribui byte gerado a newKey
			newKey[i] = (byte) value;                        
			if (value <= 0xff) {
				break;
			}
                        //atribui carry a (value deslocado 8 bits a direita) AND 0xff
			carry = (value >> 8) & 0xff;
		}
		return new Key(newKey);
	}
        
        /**
         * Compara dois objetos
         * Converte bytes de key e targetkey para int e faz comparação
         * @param obj a ser comparado
         * @return resultado da diferença de loperand e roperand
         */
	public int compareTo(Object obj) {
		Key targetKey = (Key) obj;
		for (int i = 0; i < key.length; i++) {
			int loperand = (this.key[i] & 0xff);
			int roperand = (targetKey.getKey()[i] & 0xff);
			if (loperand != roperand) {
				return (loperand - roperand);
			}
		}
		return 0;
	}
        
        /**
         * Algoritmo para converter array de bytes para string
         * @return resultado da conversão de key de array de bytes para string
         */
        @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
                //checa se key tem mais de 4 bytes
		if (key.length > 4) {
			for (int i = 0; i < key.length; i++) {                                
                                //key[i] AND 0xff, com cast de key[i] como int
                                //coloca na string o resultado da operação AND
                                //com um . concatenado a fim de separar os bytes
                                //na string resultante
				sb.append(Integer.toString(((int) key[i]) & 0xff) + ".");
			}
                //caso tenha 4 bytes ou menos
		} else {
			long n = 0;
			for (int i = key.length-1 , j=0; i >= 0 ; i--, j++) {
                                //faz a seguinte operação: desloca key[i] 8*j bits a esquerda
                                //faz  op. AND com 0xff deslocado 8*j bits a esquerda
                                //atribui a n o valor de n OR o resultado do AND acima
				n |= ((key[i]<<(8*j)) & (0xffL<<(8*j)));
			}
			sb.append(Long.toString(n));
		}
                //retorna substring (retira "." do último byte)
		return sb.substring(0, sb.length() - 1).toString();
	}
        
        /**
         * Retorna identifier
         * @return string identifier
         */
	public String getIdentifier() {
		return identifier;
	}
        
        /**
         * Seta identifier
         * @param identifier a ser atribuido
         */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
        
        /**
         * Retorna key
         * @return array de bytes da key
         */
	public byte[] getKey() {
		return key;
	}
        
        /**
         * Seta key
         * @param key 
         */
	public void setKey(byte[] key) {
		this.key = key;
	}

}