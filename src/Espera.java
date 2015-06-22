import java.util.*;
public class Espera{
	private Dado dadoEspera;
	private Set<Transacao> transacoesEspera;
	public Espera(){
		dadoEspera = new Dado("");
		transacoesEspera = new HashSet<>();
	}

	public void setDadoEspera(Dado espera){
		dadoEspera = espera;
	}

	public void addTransacao(Transacao transacao){
		transacoesEspera.add(transacao);
	}

	public void removeTransacao(Transacao transacao){
		transacoesEspera.remove(transacao);
	}

	public Dado getDadoEspera(){
		return dadoEspera;
	}

	public Set<Transacao> getTransacoesEspera(){
		return transacoesEspera;
	}
}