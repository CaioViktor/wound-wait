import java.util.*;
public class Transacao{
	private int identificador;
	private long timestamp;
	private List<Operacao> operacoes;
	private int operacaoAtual;
	private String estado;
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
		//TODO: abortar operaçao
	} 
	public void start(long timestamp){
		//TODO: começar operaçao
	}
	public void start(){
		//TODO: começar operaçao com timestamp atual
	}
	public void despertar(){
		//TODO: despertar
	}
}