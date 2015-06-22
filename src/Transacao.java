import java.util.*;
public class Transacao implements Comparable<Transacao>{
	private int identificador;
	private long timestamp;
	private List<Operacao> operacoes;
	private int operacaoAtual;
	private String estado;
	private Map<Dado,Espera> esperando;

	public String toString(){
		// return getIdentificador() + " : " + getTimestamp() + " : " + getEstado() + " : " + getOperacaoAtual();
		return "T"+getIdentificador();
	}

	public boolean isPrimeiraOperacao(){
		return (operacaoAtual == 0);
	}

	public Transacao(int identificador){
		this.identificador = identificador;
		operacoes = new ArrayList<>();
		operacaoAtual = 0;
		timestamp = 0;
		esperando = new HashMap<>();
		estado = "PROCESSANDO";
		start();
	}

	public int compareTo(Transacao t2){
		int r = (int)(this.getTimestamp() - t2.getTimestamp());
		if(r == 0 && !this.equals(t2))
			r = this.getIdentificador() - t2.getIdentificador();
		return r;
	}

	public boolean equals(Object t1){
		if(!(t1 instanceof Transacao))
			return false;
		if(this == t1)
			return true;
		Transacao t2 = (Transacao)t1;
		if((this.getIdentificador() == t2.getIdentificador()) && (this.operacoes.equals(t2.operacoes)))
			return true;
		return false;
	}

	public int getIdentificador(){
		return identificador;
	}

	public long getTimestamp(){
		return timestamp;
	}

	public void setTimestamp(long timestamp){
		this.timestamp = timestamp;
	}

	public void setTimestamp(){
		
		setTimestamp(Calendar.getInstance().getTime().getTime());
	}

	public void setEstado(String estado){
		this.estado = estado;
	}

	public String getEstado(){
		return estado;
	}

	public Operacao getOperacaoAtual(){
		if(operacaoAtual < operacoes.size())
			return operacoes.get(operacaoAtual);
		else
			return null;
	}

	public void passarOperacao(){
		if(operacaoAtual < operacoes.size())
			operacaoAtual++;
	}

	public void addOperacao(Operacao operacao){
		operacoes.add(operacao);
	}

	public boolean hasProximaOperacao(){
		return (operacaoAtual < operacoes.size());
	}

	public void abort(){
		if(!estado.equalsIgnoreCase("FINALIZADA")){
			estado = "ABORTADA";
			operacaoAtual = 0;
			liberarBloqueios();
		}
	} 

	public void start(long timestamp){
		if(!estado.equalsIgnoreCase("FINALIZADA")){
			this.timestamp = timestamp;
			estado = "PROCESSANDO";
		}
	}

	public void start(){
		start(Calendar.getInstance().getTime().getTime());
	}

	public void commit(){
		liberarBloqueios();
		estado = "FINALIZADA";
	}
	private void liberarBloqueios(){
		// System.out.println("LiberandoBloqueios");
		for(Operacao o : operacoes){
			if(o.getDado() != null){
				// System.out.println("LiberandoBloqueios1");
				Dado dadoOperacao = o.getDado();
				// System.out.println("LiberandoBloqueios2");
				dadoOperacao.removeFilaEspera(this);
				// System.out.println("LiberandoBloqueios3");
				dadoOperacao.removeListaLeitura(this);//Remove o bloqueio se tiver de leitura
				// System.out.println("LiberandoBloqueios4");
				if(dadoOperacao.getBloqueioEscrita() != null && dadoOperacao.getBloqueioEscrita().equals(this)){//Remove o bloqueio se tiver bloqueio de escrita 
					// System.out.println("LiberandoBloqueios5 ");
					dadoOperacao.setBloqueioEscrita(null);
				}

			}
		}
	}
	public void waitFila(Dado dado){
		if(!estado.equalsIgnoreCase("FINALIZADA") && !estado.equalsIgnoreCase("ABORTADA") && !estado.equalsIgnoreCase("ESPERANDO")){
			// esperando = dado;
			// System.out.println("Houve espera");
			estado = "ESPERANDO";
		}
	}

	public Map<Dado,Espera> getEsperando(){
		return esperando;
	}

}
