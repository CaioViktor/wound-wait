import java.util.*;
public class Dado{
	private String identificador;
	private TreeSet<Transacao> filaEspera;
	private Set<Transacao> bloqueioLeitura;
	private Transacao bloqueioEscrita;


	public String toString(){
		// return getIdentificador() + ":" + (isBloqueadoLeitura()||isBloqueadoEscrita()) +" : "+ bloqueioEscrita+" : "+ bloqueioLeitura;
		return getIdentificador();
	}

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
	public void removeListaLeitura(Transacao transacao){
		if(bloqueioLeitura.contains(transacao)){
			bloqueioLeitura.remove(transacao);
			if(bloqueioLeitura.isEmpty() || (bloqueioLeitura.size() == 1 && bloqueioLeitura.contains(filaEspera.first()))){ //não há bloqueio ou Sobrou apenas um elemento no bloqueio de leitura, mas ele estava para por querer de escrita
				notificarFilaEspera();
			}
		}
	}

	public void setBloqueioEscrita(Transacao transacao){
		bloqueioEscrita = transacao;
		if(transacao == null)
			notificarFilaEspera();
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
		boolean read = true;

		while(!filaEspera.isEmpty() && read){ // Notifica todos os de leitura até um de escrita ou o fim
			read = false;
			filaEspera.first().start(filaEspera.first().getTimestamp());
			if(filaEspera.first().getOperacaoAtual() instanceof Read)
				read = true;
			removeFilaEspera();
		}
	}
}