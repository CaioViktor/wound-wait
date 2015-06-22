public abstract class Operacao{
	
	//atributos
	private Dado dado;
	private int identificadorTransacao;
	private Transacao transacao;
	private int id;
	public boolean equals(Object o){
		if(!(o instanceof Operacao))
			return false;
		Operacao operacao = (Operacao) o;
		if(this == operacao)
			return true;
		return false;
	}
	//Getters and Setters
	public Dado getDado() {
		return dado;
	}
	public void setDado(Dado dado) {
		this.dado = dado;
	}
	public int getIdentificadorTransacao() {
		return identificadorTransacao;
	}
	public void setIdentificadorTransacao(int identificadorTransacao) {
		this.identificadorTransacao = identificadorTransacao;
	}
	public void setTransacao(Transacao transacao){
		this.transacao = transacao;
	}
	public Transacao getTransacao(){
		return transacao;
	}
	
	public boolean isConflito(){
		return false;
	}

	//Metodos abstratos
	public abstract boolean operar();
	
}