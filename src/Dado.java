public class Dado{
	private String identificador;
	private FilaTransacao filaEspera;
	private Transacao bloqueioLeitura;
	private Transacao bloqueioEscrita;
	public Dado(String identificador){
		this.identificador = identificador;
		filaEspera = new FilaTransacao();
		bloqueioEscrita = null;
		bloqueioLeitura = null;
	}
	public String getIdentificador(){
		return identificador;
	}
	public boolean isBloqueadaLeitura(){
		if(bloqueioLeitura == null)
			return false;
		return true;
	}
	public boolean isBloqueadaEscrita(){
		if(bloqueioEscrita == null)
			return false;
		return true;
	}
	public Transacao getBloqueioLeitura(){
		return bloqueioLeitura;
	}
	public Transacao getBloqueioEscrita(){
		return bloqueioEscrita;
	}
	public void setBloqueioLeitura(Transacao transacao){
		bloqueioLeitura = transacao;
	}
	public void setBloqueioEscrita(Transacao transacao){
		bloqueioEscrita = transacao;
	}

	public void addFilaEspera(Transacao transacao){
		// filaEspera.append(transacao);
	}
	public void removeFilaEspera(Transacao transacao){
		// filaEspera.removeTransacao(transacao);
	}
	public void removeFilaEspera(){
		// filaEspera.removeInicio();
	}
	public void notificarFilaEspera(){
		// filaEspera.getInicio().start(filaEspera.getInicio().getTimestamp());
	}
}