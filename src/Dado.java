import java.util.*;
public class Dado{
	private String identificador;
	private TreeSet<Transacao> filaEspera;
	private Set<Transacao> bloqueioLeitura;
	private Transacao bloqueioEscrita;

	public Dado(String identificador){
		this.identificador = identificador;
		filaEspera = new TreeSet<>();
		bloqueioEscrita = null;
		bloqueioLeitura = new HashSet<>();

	}
	public String getIdentificador(){
		return identificador;
	}
	public boolean isBloqueadoLeitura(){
		if(bloqueioLeitura.isEmpty())
			return false;
		return true;
	}
	public boolean isBloqueadoEscrita(){
		if(bloqueioEscrita == null)
			return false;
		return true;
	}

	public Set<Transacao> getBloqueioLeitura(){
		return bloqueioLeitura;
	}
	public Transacao getBloqueioEscrita(){
		return bloqueioEscrita;
	}


	public void setBloqueioLeitura(Transacao transacao){
		bloqueioLeitura.add(transacao);
	}
	public void setBloqueioEscrita(Transacao transacao){
		bloqueioEscrita = transacao;
	}

	public void addFilaEspera(Transacao transacao){
		filaEspera.add(transacao);
	}
	public void removeFilaEspera(Transacao transacao){
		filaEspera.remove(transacao);
	}
	public void removeFilaEspera(){
		filaEspera.remove(filaEspera.first());
	}
	public void notificarFilaEspera(){
		// filaEspera.getInicio().start(filaEspera.getInicio().getTimestamp());
		filaEspera.first().start(filaEspera.first().getTimestamp());
		removeFilaEspera();
	}
}