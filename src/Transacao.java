import java.util.*;
public class Transacao{
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
		return operacoes.get(operacaoAtual);
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