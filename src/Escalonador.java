import java.util.*;
public class Escalonador{
	private List<Transacao> transacoes;
	private List<Operacao> operacoesEntrada;
	private List<Operacao> operacoesSaida;
	private boolean deadLock;
	private List<Transacao> transacoesAbortadas;
	private List<Transacao> transacoesEfetivadas;

	public Escalonador(List<Transacao> transacoes,List<Operacao> operacoesEntrada){
		this.transacoes = transacoes;
		this.operacoesEntrada = operacoesEntrada;
		deadLock = false;
		operacoesSaida = new ArrayList<>();
		transacoesAbortadas = new ArrayList<>();
		transacoesEfetivadas = new ArrayList<>();
	}

	public List<Operacao> getOperacoesEntrada(){
		return operacoesEntrada;
	}

	public List<Operacao> getOperacoesSaida(){
		return  operacoesSaida;
	}

	public List<Transacao> getTransacoesAbortadas(){
		return transacoesAbortadas;
	}

	public List<Transacao> getTransacoesEfetivadas(){
		return  transacoesEfetivadas;
	}

	public boolean getDeadLock(){
		return deadLock;
	}

	// private Operacao escolherOperacao(){
	// 	//TODO: Escolher qual operação executar
	// }
	// //Método para escalonar
	// public void escalonar(){
	// 	//Loop em tadas as transações
	// }

	//Dada a operação ele faz a operação do wound-wait
	public void operar(Operacao operacaoAtual){
		
		if(!operacaoAtual.isConflito()) // não há conflito entre operações
			operacaoAtual.operar();
		else{//há conflitos
			Transacao transacaoAtual = operacaoAtual.getTransacao();
			Transacao transacaoBloqueiaDado = null;
			Dado dadoAcessado = operacaoAtual.getDado();
			if(operacaoAtual instanceof Read){
				transacaoBloqueiaDado = dadoAcessado.getBloqueioEscrita();
				if(transacaoAtual.compareTo(transacaoBloqueiaDado) < 0){//Transação atual é mais antiga
					
					transacaoBloqueiaDado.abort();
					transacaoBloqueiaDado.start(transacaoBloqueiaDado.getTimestamp());//Recomeça transação parada com o mesmo timestamp
					
					operacaoAtual.operar();
				}else{
					transacaoAtual.waitFila(dadoAcessado);
					dadoAcessado.addFilaEspera(transacaoAtual);
				}
			}//Fim do conflito de Read

			else if(operacaoAtual instanceof Write){

				if(dadoAcessado.isBloqueadoEscrita()){//Dado está com bloqueio exclusivo para escrita

					transacaoBloqueiaDado = dadoAcessado.getBloqueioEscrita();
					if(transacaoAtual.compareTo(transacaoBloqueiaDado) < 0){//Transação atual é mais antiga
						
						transacaoBloqueiaDado.abort();
						transacaoBloqueiaDado.start(transacaoBloqueiaDado.getTimestamp());//Recomeça transação parada com o mesmo timestamp
						
						operacaoAtual.operar();
					}else{
						transacaoAtual.waitFila(dadoAcessado);
						dadoAcessado.addFilaEspera(transacaoAtual);
					}

				}else{//Dado está com bloqueio compartilhado para leitura
					Set<Transacao> listaTrasacoesBloqueioEscrita = dadoAcessado.getBloqueioLeitura();
					Set<Transacao> transacoesMaisNovas = new HashSet<>();
					for(Transacao t:listaTrasacoesBloqueioEscrita){//Percorre lista de transações que bloquearam o dado e seleciona as mais novas que a transação atual
						if(transacaoAtual.compareTo(t) < 0){//Trasação atual é mais antiga
							transacoesMaisNovas.add(t); //pode dar erro aqui pelo uso do for e não do iterator
						}
					}
					for(Transacao t:transacoesMaisNovas){//Mata transações mais novas
						listaTrasacoesBloqueioEscrita.remove(t);//pode dar erro pelo uso do for
						t.abort();
						t.start(t.getTimestamp());//Recomeça transação parada com o mesmo timestamp
					}
					if(operacaoAtual.isConflito()){//Verifica se ainda há conflito
						transacaoAtual.waitFila(dadoAcessado);
						dadoAcessado.addFilaEspera(transacaoAtual);
					}else{//Não há mais conflito
						operacaoAtual.operar();
					}
				}

			}//Fim conflito de Write
		}//Fim dos conflitos



	}//Fim do escalonamento	
}