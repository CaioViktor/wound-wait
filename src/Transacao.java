import java.util.*;
public class Transacao implements Comparator<Transacao>{
	private int identificador;
	private long timestamp;
	private List<Operacao> operacoes;
	private int operacaoAtual;
	private String estado;
	private Dado esperando;

	public Transacao(int identificador){
		this.identificador = identificador;
		timestamp = 0;
		operacoes = new ArrayList<>();
		operacaoAtual = 0;
		estado = "PRONTA";
	}
	public int compare(Transacao t1, Transacao t2){
		int r = (int)(t2.getTimestamp() - t1.getTimestamp());
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
			esperando.removeFilaEspera(this);
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
		estado = "FINALIZADA";
	}
	public void waitFila(Dado dado){
		if(!estado.equalsIgnoreCase("FINALIZADA") && !estado.equalsIgnoreCase("ABORTADA") && !estado.equalsIgnoreCase("ESPERANDO")){
			esperando = dado;
			estado = "ESPERANDO";
		}
	}

}