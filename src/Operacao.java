public abstract class Operacao{
	
	//atributos
	private Dado dado;
	private int identificadorTransacao;
	private Transacao transacao;
	
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
	
	//Metodos abstratos
	public boolean operar(){
		if(dado.isBloqueado())
			return false;
		dado.setBloqueio(transacao);
		return true;
	}
	
}