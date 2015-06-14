public abstract class Operacao{
	
	//atributos
	protected Dado dado;
	protected int identificadorTransacao;
	
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
	
	//Metodos abstratos
	public abstract boolean operar();
	
}